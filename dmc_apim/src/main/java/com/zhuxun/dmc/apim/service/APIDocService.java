package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.api.APIDoc;
import com.zhuxun.dmc.apim.dto.api.APIDocTO;

import java.util.List;

public interface APIDocService {

  APIDoc createDoc(String createUserId, APIDocTO apiDocTO);

  APIDoc getDocById(String docId);

  List<APIDoc> getDocsByAPIId(String apiId);

  APIDoc updateDocById(String modifyUserId, String apiDocId, APIDocTO apiDocTO);

  void logicDeleteById(String docId);
}
