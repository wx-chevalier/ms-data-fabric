package com.zhuxun.dmc.apim.service.impl.client;

import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.repository.model.*;
import com.zhuxun.dmc.apim.service.ProjectService;
import com.zhuxun.dmc.apim.service.impl.AbstractProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("clientProjectService")
@Slf4j
public class ProjectServiceImpl extends AbstractProjectService implements ProjectService {
  @Override
  public List<Project> getProjectListByUserId(String userId) {
    DcUserApiExample userApiExample = new DcUserApiExample();
    userApiExample.createCriteria().andUserIdEqualTo(userId);
    List<DcUserApi> userApis = dcUserApiMapper.selectByExample(userApiExample);
    if (userApis.isEmpty()) {
      return new ArrayList<>();
    }

    DcApiExample apiExample = new DcApiExample();
    apiExample.createCriteria()
        .andIdIn(userApis.stream()
            .map(DcUserApi::getApiId)
            .collect(toList()));
    List<DcApi> apis = dcApiMapper.selectByExample(apiExample);

    if (apis.isEmpty()) {
      return new ArrayList<>();
    }

    DcProjectExample projectExample = new DcProjectExample();
    projectExample.createCriteria()
        .andIdIn(apis.stream().map(DcApi::getProjectId).collect(toList()));

    return dcProjectMapper.selectByExample(projectExample).stream()
        .map(Project::of)
        .collect(toList());
  }
}
