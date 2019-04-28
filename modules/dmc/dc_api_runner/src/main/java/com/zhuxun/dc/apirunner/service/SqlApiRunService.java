package com.zhuxun.dc.apirunner.service;

import com.google.common.base.Strings;
import com.zhuxun.dc.apirunner.dao.entity.DcApiImpl;
import com.zhuxun.dc.apirunner.dao.entity.DcApiImplExample;
import com.zhuxun.dc.apirunner.dao.entity.DcApiParam;
import com.zhuxun.dc.apirunner.dao.entity.DcApiParamExample;
import com.zhuxun.dc.apirunner.entity.vo.VToken;
import com.zhuxun.dc.apirunner.entity.vo.api.VApiImplModel;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import com.zhuxun.dc.apirunner.sqlapi.impl.SQLAPIAnalyzerImpl;
import com.zhuxun.dc.apirunner.sqlapi.impl.SQLAPIExecutorImpl;
import com.zhuxun.dc.apirunner.utils.JacksonUtil;
import com.zhuxun.dc.apirunner.utils.JwtUtils;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.inject.internal.util.$Preconditions.checkArgument;
import static com.zhuxun.dc.apirunner.config.SecretConstant.JWT_SECRET;

/**
 * 新增SqlAPI接口
 */
@Service
@Slf4j
public class SqlApiRunService extends BaseService {
  private ManagedDatasourceService managedDatasourceService;

  public SqlApiRunService(@Autowired ManagedDatasourceService managedDatasourceService) {
    this.managedDatasourceService = managedDatasourceService;
  }

  /**
   * 运行SQL脚本语句
   *
   * @param apiId
   * @param parameter
   * @return
   * @throws SQLException
   * @throws IOException
   * @throws SQLAPIException
   */
  @Transactional
  public Object runSqlImpl(String apiId, Map<String, Object> parameter, String token)
      throws SQLException, SQLAPIException, ManagedDatasourceException {
    checkTokenArgument(apiId, token);
    DcApiParamExample paramExample = new DcApiParamExample();
    paramExample.createCriteria().andApiIdEqualTo(apiId);
    DcApiParam apiParam = getOnlyElement(dcApiParamMapper.selectByExample(paramExample));
    checkState(apiParam != null, "接口定义不完整，缺少参数模型定义");

    DcApiImplExample apiImplExample = new DcApiImplExample();
    apiImplExample.createCriteria().andApiIdEqualTo(apiId);
    DcApiImpl apiImpl = getOnlyElement(dcApiImplMapper.selectByExample(apiImplExample));
    checkState(apiImpl != null, "接口定义不完整，缺少接口实现定义");

    // 参数模型
    ObjectSchema paramSchema = JacksonUtil.readValue(
        apiParam.getParamModel(), ObjectSchema.class);

    // 接口定义
    VApiImplModel implModel = JacksonUtil.readValue(
        apiImpl.getApiImpl(), VApiImplModel.class);

    checkState(implModel != null, "接口定义错误");

    try (Connection connection =
             managedDatasourceService.getConnection(implModel.getDatasourceName())) {
      SQLAPIAnalyzerImpl analyzer = new SQLAPIAnalyzerImpl().cnx(connection);
      SQLAPIExecutorImpl executor = new SQLAPIExecutorImpl().cnx(connection);
      log.debug("SqlApi[sql={}, dialect={}, ds={}]", implModel.getSql(), implModel.getDialect(), implModel.getDatasourceName());

      // 分析语句成分
      Schema analyzeResponse = analyzer
          .parameter(paramSchema)
          .analyzeResponse(implModel.getSql(), implModel.getDialect());
      // 判断是是否是查询操作
      if (analyzeResponse == SQLAPIAnalyzerImpl.UPDATE_RESULT_SCHEMA) {
        return executor.execute(
            implModel.getSql(), implModel.getDialect(), parameter, paramSchema);

      } else {
        return executor.executeQuery(
            implModel.getSql(), implModel.getDialect(), parameter, paramSchema);
      }
    }
  }

  public VToken checkTokenArgument(String apiId, String token) {
    Map<String, Object> parseTestToken = JwtUtils.parseTestToken(JWT_SECRET, token);
    String apiIdInToken = (String) parseTestToken.get("apiId");
    String userIdInToken = (String) parseTestToken.get("userId");
    checkArgument(!Strings.isNullOrEmpty(apiIdInToken), "Token解析失败");
    checkArgument(!Strings.isNullOrEmpty(userIdInToken), "Token解析失败");
    checkArgument(Objects.equals(apiId, apiIdInToken), "Token验证失败,请检查Token是否正确");
    checkArgument(Objects.equals(userIdInToken, getUserId()), "Token验证失败,请检查Token是否正确");
    return new VToken().setUserId(userIdInToken).setApiId(apiIdInToken);
  }
}
