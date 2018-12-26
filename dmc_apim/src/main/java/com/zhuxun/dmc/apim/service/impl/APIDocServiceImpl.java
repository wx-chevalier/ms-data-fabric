package com.zhuxun.dmc.apim.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zhuxun.dmc.apim.config.ApplicationConstants;
import com.zhuxun.dmc.apim.domain.api.APIDoc;
import com.zhuxun.dmc.apim.dto.api.APIDocTO;
import com.zhuxun.dmc.apim.repository.model.DcApiDoc;
import com.zhuxun.dmc.apim.repository.model.DcApiDocExample;
import com.zhuxun.dmc.apim.service.APIDocService;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

@Service
@Slf4j
public class APIDocServiceImpl extends AbstractService implements APIDocService {
  @Transactional
  public APIDoc createDoc(String createUserId, APIDocTO apiDocTO) {
    checkArgument(!isNullOrEmpty(apiDocTO.getContent()), "新增接口文档失败,文档内容不能为空");
    Date now = new Date();
    String docId = UUIDUtils.getUUID();
    dcApiDocMapper.insert(apiDocTO.toEntity()
        .withId(docId)
        .withCreateUserid(createUserId)
        .withCreateDatetime(now)
        .withModifyDatetime(now)
        .withStatus(ApplicationConstants.STATUS_NORMAL));
    return getDocById(docId);
  }

  @Transactional(readOnly = true)
  public APIDoc getDocById(String apiDocId) {
    checkArgument(!isNullOrEmpty(apiDocId), "获取接口文档失败,文档ID不能为空");

    return Optional.ofNullable(dcApiDocMapper.selectByPrimaryKey(apiDocId))
        .map(APIDoc::of)
        .orElse(null);
  }

  @Transactional(readOnly = true)
  public List<APIDoc> getDocsByAPIId(String apiId) {
    checkArgument(!isNullOrEmpty(apiId), "获取接口文档失败,接口ID不能为空");
    DcApiDocExample example = new DcApiDocExample();
    example.createCriteria()
        .andApiIdEqualTo(apiId)
        .andStatusEqualTo(ApplicationConstants.STATUS_NORMAL);

    return dcApiDocMapper.selectByExample(example).stream()
        .map(APIDoc::of)
        .collect(Collectors.toList());
  }

  @Transactional
  public APIDoc updateDocById(String modifyUserId, String apiDocId, APIDocTO apiDocTO) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(apiDocId), "更新接口文档失败,文档ID不能为空");

    dcApiDocMapper.updateByPrimaryKeySelective(apiDocTO.toEntity()
        .withId(apiDocId)
        .withModifyUserid(modifyUserId)
        .withModifyDatetime(new Date()));

    return getDocById(apiDocId);
  }

  @Transactional
  public void logicDeleteById(String apiDocId) {
    checkArgument(!isNullOrEmpty(apiDocId), "删除接口文档失败,文档ID不能为空");
    dcApiDocMapper.updateByPrimaryKeySelective(new DcApiDoc()
        .withId(apiDocId)
        .withStatus(ApplicationConstants.STATUS_DISABLE));
  }
}
