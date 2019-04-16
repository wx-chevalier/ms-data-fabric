package com.zhuxun.dmc.apim.service.impl.manager.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.config.ApplicationConstants;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.APIDoc;
import com.zhuxun.dmc.apim.domain.api.APIImpl;
import com.zhuxun.dmc.apim.domain.api.APIResponse;
import com.zhuxun.dmc.apim.domain.api.param.APIParam;
import com.zhuxun.dmc.apim.domain.sec.User;
import com.zhuxun.dmc.apim.dto.api.creation.APIModification;
import com.zhuxun.dmc.apim.dto.api.creation.GeneratedAPIModification;
import com.zhuxun.dmc.apim.repository.model.*;
import com.zhuxun.dmc.apim.service.APIService;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.errors.ServiceException;
import com.zhuxun.dmc.apim.utils.UUIDUtils;
import com.zhuxun.dmc.sqlapi.datasource.ManagedDatasource;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Iterables.getOnlyElement;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class APIServiceImpl extends AbstractService implements APIService {
  private ObjectMapper objectMapper;

  private ManagedDatasource managedDatasource;

  @Autowired
  public APIServiceImpl(ObjectMapper objectMapper,
                        ManagedDatasource managedDatasource) {
    this.objectMapper = objectMapper;
    this.managedDatasource = managedDatasource;
  }

  @Override
  public Boolean exists(String apiId) {
    DcApiExample apiExample = new DcApiExample();
    apiExample.createCriteria().andIdEqualTo(apiId);
    return dcApiMapper.countByExample(apiExample) != 0;
  }

  @Override
  public API getAPIById(String apiId) throws ServiceException {
    DcApiExample apiExample = new DcApiExample();
    apiExample.createCriteria()
        .andStatusEqualTo(ApplicationConstants.STATUS_NORMAL);
    DcApi dcApi = dcApiMapper.selectByPrimaryKey(apiId);

    if (dcApi == null) {
      return null;
    }

    return API.of(dcApi)
        .setImpl(getAPIImpl(apiId))
        .setParam(getAPIParam(apiId))
        .setResponse(getOnlyElement(getAPIResponse(apiId), null))
        .setDoc(getAPIDoc(apiId));
  }

  @Transactional
  @Override
  @SuppressWarnings("unchecked")
  public API createAPI(String createUserId, APIModification apiModification)
      throws ServiceException {

    if (apiModification instanceof GeneratedAPIModification) {
      // 如果创建生成的接口，设定接口实现的解析器
      ((GeneratedAPIModification) apiModification).setImplParser(new APIImplDevToolImpl(managedDatasource));
    }

    String apiId = UUIDUtils.getUUID();
    Date now = new Date();
    // 创建接口
    dcApiMapper.insert(apiModification.toEntity()
        .withCreateUserid(createUserId)
        .withModifyUserid(createUserId)
        .withCreateDatetime(now)
        .withModifyDatetime(now)
        .withId(apiId));

    try {
      // 创建接口参数模型
      Optional.ofNullable(apiModification.toDcApiParamList(objectMapper))
          .ifPresent(params -> this.createAPIParam(apiId, createUserId, params));

      // 创建接口响应模型
      Optional.ofNullable(apiModification.toDcApiResponse(objectMapper))
          .ifPresent(resp -> this.createAPIResponse(apiId, createUserId, resp));

      if (apiModification instanceof GeneratedAPIModification) {
        // 创建接口实现信息
        dcApiImplMapper.insert(((GeneratedAPIModification) apiModification).getImpl()
            .toEntity(objectMapper)
            .withId(UUIDUtils.getUUID())
            .withApiId(apiId)
            .withCreateDatetime(now)
            .withModifyDatetime(now)
            .withCreateUserid(createUserId)
            .withModifyUserid(createUserId)
            .withStatus(ApplicationConstants.STATUS_NORMAL));
      }

    } catch (JsonProcessingException e) {
      // Schema 应该来自网络层反序列化的结果，这里正常必然能够进行序列化
      throw new IllegalStateException(e);
    } catch (SQLAPIException e) {
      throw new ServiceException("接口定义存在问题", e);
    } catch (SQLException e) {
      throw new ServiceException("数据源连接错误", e);
    } catch (ManagedDatasourceException e) {
      throw new ServiceException("数据源存在问题", e);
    }

    return getAPIById(apiId);
  }

  @Override
  public List<API> getAPIListByProjectId(String projectId) {
    DcApiExample apiExample = new DcApiExample();
    apiExample.createCriteria()
        .andProjectIdEqualTo(projectId)
        .andStatusEqualTo(ApplicationConstants.STATUS_NORMAL);
    return dcApiMapper.selectByExample(apiExample).stream()
        .map(API::of)
        .collect(toList());
  }

  @Override
  public void logicDeleteAPI(String apiId) {
    dcApiMapper.updateByPrimaryKeySelective(new DcApi()
        .withId(apiId)
        .withStatus(ApplicationConstants.STATUS_DISABLE));
  }

  @Transactional
  @Override
  @SuppressWarnings("unchecked")
  public API updateAPI(
      String modifyUserId, String apiId, APIModification apiModification)
      throws ServiceException {

    DcApiExample apiExample = new DcApiExample();
    apiExample.createCriteria()
        .andStatusEqualTo(ApplicationConstants.STATUS_NORMAL)
        .andIdEqualTo(apiId)
        .andCreateUseridEqualTo(modifyUserId);
    if (dcApiMapper.countByExample(apiExample) == 0) {
      return null;
    }

    Date now = new Date();

    // 更新基本信息
    dcApiMapper.updateByPrimaryKeySelective(apiModification.toEntity()
        .withId(apiId)
        .withModifyUserid(modifyUserId)
        .withModifyDatetime(now));

    try {
      if (apiModification instanceof GeneratedAPIModification) {
        GeneratedAPIModification m = ((GeneratedAPIModification) apiModification);
        if (m.getImpl() != null) {
          m.setImplParser(new APIImplDevToolImpl(managedDatasource));
          // 更新接口实现
          updateApiImpl(apiId, m.getImpl().toEntity(objectMapper));
        }
      }

      // 更新参数模型
      Optional.ofNullable(apiModification.toDcApiParamList(objectMapper))
          .ifPresent(params -> this.replaceAPIParam(apiId, modifyUserId, params));

      // 更新响应模型
      Optional.ofNullable(apiModification.toDcApiResponse(objectMapper))
          .ifPresent(resp -> this.replaceAPIResponse(apiId, modifyUserId, resp));
    } catch (JsonProcessingException e) {
      // Schema 应该来自网络层反序列化的结果，这里正常必然能够进行序列化
      throw new IllegalStateException(e);
    } catch (SQLAPIException e) {
      throw new ServiceException("接口定义存在问题", e);
    } catch (SQLException e) {
      throw new ServiceException("数据源连接错误", e);
    } catch (ManagedDatasourceException e) {
      throw new ServiceException("数据源存在问题", e);
    }

    return getAPIById(apiId);
  }

  @Override
  public List<User> getAPIAuthUserList(String apiId) {
    DcUserApiExample userApiExample = new DcUserApiExample();
    userApiExample.createCriteria().andApiIdEqualTo(apiId);

    return dcUserApiMapper.selectByExample(userApiExample).stream()
        .map(ua -> new User().setId(ua.getUserId()))
        .collect(toList());
  }

  // region 接口相关资源处理

  /**
   * 获取指定接口的参数模型列表
   */
  private List<APIParam> getAPIParam(String apiId) throws ServiceException {
    DcApiParamExample paramExample = new DcApiParamExample();
    paramExample.createCriteria().andApiIdEqualTo(apiId);

    List<APIParam> params = new ArrayList<>();
    for (DcApiParam dcApiParam : dcApiParamMapper.selectByExampleWithBLOBs(paramExample)) {
      try {
        params.add(APIParam.of(dcApiParam, objectMapper));
      } catch (IOException e) {
        throw new ServiceException("接口参数定义错误: " + apiId, e);
      }
    }
    return params;
  }

  /**
   * 创建接口的参数模型列表
   */
  private void createAPIParam(String apiId, String userId, List<DcApiParam> params) {
    for (DcApiParam param : params) {
      dcApiParamMapper.insert(param
          .withId(UUIDUtils.getUUID())
          .withCreateUserid(userId)
          .withCreateDatetime(new Date())
          .withApiId(apiId));
    }
  }

  /**
   * 替换接口的参数模型列表
   */
  private void replaceAPIParam(String apiId, String userId, List<DcApiParam> params) {
    DcApiParamExample paramExample = new DcApiParamExample();
    paramExample.createCriteria().andApiIdEqualTo(apiId);
    dcApiParamMapper.deleteByExample(paramExample);

    createAPIParam(apiId, userId, params);
  }

  /**
   * 获取接口的响应模型列表
   */
  private List<APIResponse> getAPIResponse(String apiId) {
    DcApiResponseExample responseExample = new DcApiResponseExample();
    responseExample.createCriteria().andApiIdEqualTo(apiId);
    List<DcApiResponse> dcApiResponses =
        dcApiResponseMapper.selectByExampleWithBLOBs(responseExample);
    List<APIResponse> responses = new ArrayList<>();
    for (DcApiResponse resp : dcApiResponses) {
      try {
        responses.add(APIResponse.of(objectMapper, resp));
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
    return responses;
  }


  /**
   * 创建接口的响应模型
   */
  private void createAPIResponse(String apiId, String userId, DcApiResponse response) {
    dcApiResponseMapper.insert(response
        .withId(UUIDUtils.getUUID())
        .withCreateUserid(userId)
        .withCreateDatetime(new Date())
        .withApiId(apiId));
  }

  /**
   * 替换接口的响应模型
   */
  private void replaceAPIResponse(String apiId, String userId, DcApiResponse response) {
    DcApiResponseExample responseExample = new DcApiResponseExample();
    responseExample.createCriteria().andApiIdEqualTo(apiId);
    dcApiResponseMapper.deleteByExample(responseExample);

    createAPIResponse(apiId, userId, response);
  }

  /**
   * 获取接口的文档列表
   */
  private List<APIDoc> getAPIDoc(String apiId) {
    DcApiDocExample docExample = new DcApiDocExample();
    docExample.createCriteria().andApiIdEqualTo(apiId);

    return dcApiDocMapper.selectByExample(docExample).stream()
        .map(APIDoc::of)
        .collect(toList());
  }

  private void updateApiImpl(String apiId, DcApiImpl dcApiImpl) {
    DcApiImplExample example = new DcApiImplExample();
    example.createCriteria().andApiIdEqualTo(apiId);
    dcApiImplMapper.updateByExampleSelective(dcApiImpl, example);
  }

  private APIImpl getAPIImpl(String apiId) {
    DcApiImplExample example = new DcApiImplExample();
    example.createCriteria().andApiIdEqualTo(apiId);
    return getOnlyElement(dcApiImplMapper.selectByExampleWithBLOBs(example).stream()
            .map(APIImpl::of)
            .collect(toList()),
        null);
  }
  // endregion
}
