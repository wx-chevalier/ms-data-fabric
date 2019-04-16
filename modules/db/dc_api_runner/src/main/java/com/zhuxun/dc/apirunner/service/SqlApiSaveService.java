package com.zhuxun.dc.apirunner.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zhuxun.dc.apirunner.dao.entity.*;
import com.zhuxun.dc.apirunner.entity.dto.SqlApiCreation;
import com.zhuxun.dc.apirunner.entity.vo.api.VApiImpl;
import com.zhuxun.dc.apirunner.entity.vo.api.VParam;
import com.zhuxun.dc.apirunner.entity.vo.api.VResponse;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import com.zhuxun.dc.apirunner.sqlapi.impl.SQLAPIAnalyzerImpl;
import com.zhuxun.dc.apirunner.utils.UUIDUtils;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * 新增SqlAPI接口
 */
@Service
public class SqlApiSaveService extends BaseService {
  private ManagedDatasourceService managedDatasourceService;

  public SqlApiSaveService(@Autowired ManagedDatasourceService managedDatasourceService) {
    this.managedDatasourceService = managedDatasourceService;
  }

  /**
   * 向数据库增加API接口
   *
   * @param dto
   * @throws IOException
   * @throws SQLException
   * @throws SQLAPIException
   */
  @Transactional
  public String createSqlApi(SqlApiCreation dto)
      throws IOException, SQLException, SQLAPIException, ManagedDatasourceException {
    String datasourceName = dto.getDatasourceName();
    String dialect = dto.getDialect();
    String sql = dto.getSql();
    ObjectSchema parameterSchema = dto.getParameterSchema();

    checkArgument(!isNullOrEmpty(dialect), "数据源类型为空");
    if (!"presto".equals(dialect)) {
      checkArgument(!isNullOrEmpty(datasourceName), "数据源未指定");
    }
    checkArgument(!isNullOrEmpty(sql), "SQL 语句为空");
    checkArgument(!isNullOrEmpty(dto.getProjectId()), "项目 ID 为空");
    checkArgument(!isNullOrEmpty(dto.getName()), "接口名称不能为空");
    checkArgument(parameterSchema != null, "参数 Schema 不可为空");

    Schema responseSchema;
    try (Connection connection = managedDatasourceService.getConnection(datasourceName)) {
      SQLAPIAnalyzerImpl sqlapiAnalyzer = new SQLAPIAnalyzerImpl().cnx(connection);
      // 获取响应模型
      responseSchema = sqlapiAnalyzer
          .parameter(parameterSchema)
          .analyzeResponse(sql, dialect);
    }

    // 保存参数信息
    // 保存到数据库中
    DcApi api = dto.toDcApi(UUIDUtils.getUUID(), getUserId());
    dcApiMapper.insert(api);

    // 保存参数模型
    DcApiParam param = dto.toDcApiParam(UUIDUtils.getUUID(), api.getId(), getUserId());
    dcApiParamMapper.insert(param);

    // 保存实现模型
    DcApiImpl apiImpl = dto.toDcApiImpl(UUIDUtils.getUUID(), api.getId(), getUserId());
    dcApiImplMapper.insert(apiImpl);

    // 保存响应模型
    DcApiResponse response = dto.toDcApiResponse(
        UUIDUtils.getUUID(), api.getId(), getUserId(), responseSchema);
    dcApiResponseMapper.insert(response);
    return api.getId();
  }

  /**
   * 通过apiId获取接口实例
   *
   * @param apiId
   * @return
   */
  public DcApi getApiById(String apiId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(apiId), "获取接口列表失败,接口ID不能为空");
    DcApiKey key = new DcApiKey();
    key.setId(apiId);
    return dcApiMapper.selectByPrimaryKey(key);
  }

  /**
   * 通过apiId 获取参数列表
   *
   * @param apiId
   * @return
   */
  public List<VParam> getApiParamByapiId(String apiId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(apiId), "获取接口列表失败,接口ID不能为空");
    DcApiParamExample exampleResponse = new DcApiParamExample();
    exampleResponse.createCriteria().andApiIdEqualTo(apiId);
    List<DcApiParam> apiResponseList = dcApiParamMapper.selectByExample(exampleResponse);
    return apiResponseList.stream().map(param -> {
      VParam vParam = VParam.of(param);
      return vParam;
    }).collect(Collectors.toList());
  }

  /**
   * 通过apiID获取API响应实例
   *
   * @param apiId
   * @return
   */
  public VResponse getApiResponseByapiId(String apiId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(apiId), "获取接口列表失败,接口ID不能为空");
    DcApiResponseExample exampleResponse = new DcApiResponseExample();
    exampleResponse.createCriteria().andApiIdEqualTo(apiId);
    List<DcApiResponse> apiResponseList = dcApiResponseMapper.selectByExample(exampleResponse);
    if (apiResponseList == null || apiResponseList.size() == 0) {
      return null;
    }
    return VResponse.of(apiResponseList.get(0));
  }

  /**
   * 通过APIID获取接口实现
   *
   * @param apiId
   * @return
   */
  public VApiImpl getApiImplByapiId(String apiId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(apiId), "获取接口列表失败,接口ID不能为空");
    DcApiImplExample exampleImpl = new DcApiImplExample();
    exampleImpl.createCriteria().andApiIdEqualTo(apiId);
    List<DcApiImpl> apiImpleList = dcApiImplMapper.selectByExample(exampleImpl);
    if (apiImpleList == null || apiImpleList.size() == 0) {
      return null;
    }
    return VApiImpl.of(apiImpleList.get(0));
  }
}
