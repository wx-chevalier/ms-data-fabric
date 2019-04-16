package com.zhuxun.dmc.apim.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.repository.model.*;
import com.zhuxun.dmc.apim.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientService extends AbstractService {
  /**
   * 获取Client用户下的授权API接口
   *
   * @return
   */
  @Transactional(readOnly = true)
  public List<API> getUserApiList(String userId, String projectId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(projectId), "查询接口列表");

    DcUserApiExample userApiExample = new DcUserApiExample();
    userApiExample.createCriteria().andUserIdEqualTo(userId);
    List<DcUserApi> userApiList = dcUserApiMapper.selectByExample(userApiExample);
    List<String> apiIds = userApiList.stream()
        .map(DcUserApi::getApiId)
        .collect(Collectors.toList());

    if (apiIds.size() == 0) {
      return new ArrayList<>();
    } else {
      DcApiExample apiExample = new DcApiExample();
      apiExample.createCriteria()
          .andIdIn(apiIds)
          .andProjectIdEqualTo(projectId);
      return dcApiMapper.selectByExample(apiExample).stream()
          .map(API::of)
          .collect(Collectors.toList());
    }
  }

  /**
   * 获取Clien用户下指定项目的授权API接口
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Map<String, List<Map>> getUserApiListWithProjectId(Set<String> projectIdSet, Map projectNameMap) {
    return null;
  }


  /**
   * 获取当前企业用户可用的项目列表信息
   *
   * @return
   */
  public List<Project> getUserProjectList(String userId) {
    return null;
  }
}
