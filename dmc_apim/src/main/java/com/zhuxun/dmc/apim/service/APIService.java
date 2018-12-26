package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.sec.User;
import com.zhuxun.dmc.apim.dto.api.creation.APIModification;
import com.zhuxun.dmc.apim.service.errors.ServiceException;

import java.util.List;

public interface APIService {

  Boolean exists(String apiId);

  API getAPIById(String apiId) throws ServiceException;

  API createAPI(String createUserId, APIModification apiModification) throws ServiceException;

  List<API> getAPIListByProjectId(String projectId);

  void logicDeleteAPI(String apiId);

  API updateAPI(String modifyUserId, String apiId, APIModification apiModification) throws ServiceException;

  List<User> getAPIAuthUserList(String apiId);
}

