package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.spring.service.errors.ServiceException;

public interface APIService {

  API getAPIById(String apiId, String userId) throws ServiceException;

  API getAPIById(String apiId) throws ServiceException;
}
