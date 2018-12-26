package com.zhuxun.dmc.apim.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.config.ApplicationConstants;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.APIResponse;
import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImpl;
import com.zhuxun.dmc.apim.domain.api.param.APIParam;
import com.zhuxun.dmc.apim.repository.model.*;
import com.zhuxun.spring.service.errors.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;

@Service
@Slf4j
public class APIServiceImpl extends AbstractService implements APIService {
  private ObjectMapper objectMapper;

  @Autowired
  public APIServiceImpl(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public API getAPIById(String apiId, String userId) throws ServiceException {
    DcUserApiExample userApiExample = new DcUserApiExample();
    userApiExample.createCriteria().andApiIdEqualTo(apiId).andUserIdEqualTo(userId);
    if (dcUserApiMapper.countByExample(userApiExample) == 0) {
      return null;
    }

    return getAPIById(apiId);
  }

  @Override
  public API getAPIById(String apiId) throws ServiceException {
    DcApiExample apiExample = new DcApiExample();
    apiExample.createCriteria().andStatusEqualTo(ApplicationConstants.STATUS_NORMAL);
    DcApi dcApi = dcApiMapper.selectByPrimaryKey(apiId);

    if (dcApi == null) {
      return null;
    }

    return API.of(dcApi)
            .setImpl(getAPIImpl(apiId))
            .setParam(getAPIParam(apiId))
            .setResponse(getOnlyElement(getAPIResponse(apiId), null));
  }

  // region 接口相关资源处理

  /** 获取指定接口的参数模型列表 */
  private List<APIParam> getAPIParam(String apiId) throws ServiceException {
    DcApiParamExample paramExample = new DcApiParamExample();
    paramExample.createCriteria().andApiIdEqualTo(apiId);

    List<APIParam> params = new ArrayList<>();
    for (DcApiParam dcApiParam : dcApiParamMapper.selectByExampleWithBLOBs(paramExample)) {
      try {
        log.debug(
            "接口参数定义 {}: {} - {}", apiId, dcApiParam.getParamType(), dcApiParam.getParamModel());
        params.add(APIParam.of(dcApiParam, objectMapper));
      } catch (IOException e) {
        throw new ServiceException("接口参数定义错误: " + apiId, e);
      }
    }
    return params;
  }

  /** 获取接口的响应模型列表 */
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

  private APIImpl getAPIImpl(String apiId) {
    DcApiImplExample example = new DcApiImplExample();
    example.createCriteria().andApiIdEqualTo(apiId);
    DcApiImpl impl = getOnlyElement(dcApiImplMapper.selectByExampleWithBLOBs(example), null);
    if (impl == null) {
      return null;
    } else {
      try {
        log.debug("接口实现 {}: {}", apiId, impl.getApiImpl());
        return APIImpl.of(objectMapper, impl);
      } catch (IOException e) {
        log.warn("Invalid api impl for API{id={}}\n{}", apiId, impl.getApiImpl());
        return null;
      }
    }
  }
  // endregion
}
