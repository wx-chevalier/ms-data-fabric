package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.common.Affected;
import com.zhuxun.dmc.apim.domain.environment.Environment;
import com.zhuxun.dmc.apim.dto.environment.EnvironmentDTO;

import java.util.List;

public interface EnvironmentService {
  Environment getEnvironmentById(String id);

  Environment createEnvironment(String createUserId, EnvironmentDTO environmentDTO);

  Environment updateEnvironmentById(String modifyUserId, String envId, EnvironmentDTO environmentDTO);

  /**
   * 获取项目下所有环境列表，列表中环境值应该一并取出
   */
  List<Environment> getProjectEnvByProjectId(String projectId);

  List<Environment> getEnvironmentList(String userId);

  Affected logDeleteEnvironment(List<String> environmentIdList);
}
