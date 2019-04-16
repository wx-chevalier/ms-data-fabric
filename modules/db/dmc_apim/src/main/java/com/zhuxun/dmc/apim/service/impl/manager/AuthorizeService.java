package com.zhuxun.dmc.apim.service.impl.manager;

import com.google.common.collect.Sets;
import com.zhuxun.dmc.apim.repository.model.DcUserApi;
import com.zhuxun.dmc.apim.repository.model.DcUserApiExample;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.AuthorizationService;
import com.zhuxun.dmc.apim.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
public class AuthorizeService extends AbstractService implements AuthorizationService {
  /**
   * TODO: 检查 userId 用户是否对 apiId 接口有授权权限
   *
   * @param apiId 服务中不做接口存在性检查，注意调用前检测
   */
  @Transactional
  @Override
  public void authorizeAPI(String authorizerUserId, String apiId, Set<String> userIds) {
    if (userIds.isEmpty()) {
      return;
    }
    DcUserApiExample userApiExample = new DcUserApiExample();
    userApiExample.createCriteria()
        .andApiIdEqualTo(apiId)
        .andUserIdIn(new ArrayList<>(userIds));
    List<DcUserApi> authed = dcUserApiMapper.selectByExample(userApiExample);

    Sets.SetView<String> toAuth = Sets.difference(
        userIds,
        authed.stream().map(DcUserApi::getUserId).collect(toSet()));
    if (toAuth.isEmpty()) {
      return;
    }

    for (String i : toAuth) {
      dcUserApiMapper.insert(new DcUserApi()
          .withId(UUIDUtils.getUUID())
          .withUserId(i)
          .withApiId(apiId)
          .withCreateDatetime(new Date())
          .withCreateUserid(authorizerUserId));
    }
  }

  @Override
  public void deauthAPI(String authorizerUserId, String apiId, Set<String> userIds) {
    if (userIds.isEmpty()) {
      return;
    }
    DcUserApiExample userApiExample = new DcUserApiExample();
    userApiExample.createCriteria()
        .andApiIdEqualTo(apiId)
        .andUserIdIn(new ArrayList<>(userIds));
    dcUserApiMapper.deleteByExample(userApiExample);
  }

  @Override
  public Boolean isAPIAuthorizedToUser(String apiId, String userId) {
    DcUserApiExample userApiExample = new DcUserApiExample();
    userApiExample.createCriteria()
        .andApiIdEqualTo(apiId)
        .andUserIdEqualTo(userId);
    return dcUserApiMapper.countByExample(userApiExample) != 0;
  }
}
