package com.zhuxun.dmc.apim.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zhuxun.dmc.apim.domain.common.Affected;
import com.zhuxun.dmc.apim.domain.environment.Environment;
import com.zhuxun.dmc.apim.dto.environment.EnvironmentDTO;
import com.zhuxun.dmc.apim.repository.model.DcEnvironmentExample;
import com.zhuxun.dmc.apim.repository.model.DcEnvironmentWithBLOBs;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.EnvironmentService;
import com.zhuxun.dmc.apim.utils.UUIDUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnvironmentServiceImpl extends AbstractService implements EnvironmentService {

  /**
   * 通过环境的ID获取环境的数据信息
   *
   * @param id 环境的主键ID
   * @return
   */
  @Transactional(readOnly = true)
  public Environment getEnvironmentById(String id) {
    return Optional.ofNullable(dcEnvironmentMapper.selectByPrimaryKey(id))
        .map(Environment::of)
        .orElse(null);
  }

  /**
   * 新增环境信息 要求角色 管理员
   *
   * @param environmentDTO
   * @return
   */
  @Transactional
  public Environment createEnvironment(String createUserId, EnvironmentDTO environmentDTO) {
    Preconditions.checkArgument(
        !Strings.isNullOrEmpty(environmentDTO.getEnvName()), "新增环境失败,环境名称不能为空");
    Preconditions.checkArgument(
        !Strings.isNullOrEmpty(environmentDTO.getEnvValue()), "新增环境失败,环境值不能为空");
    Preconditions.checkArgument(
        !Strings.isNullOrEmpty(environmentDTO.getProjectId()), "新增环境失败,项目ID不能为空");


    String envId = UUIDUtils.getUUID();
    dcEnvironmentMapper.insertSelective((DcEnvironmentWithBLOBs) environmentDTO.toEntity()
        .withId(envId)
        .withCreateUserid(createUserId));
    return getEnvironmentById(envId);
  }

  /**
   * 修改
   *
   * @param environmentDTO
   * @return
   */
  @Transactional
  public Environment updateEnvironmentById(String modifyUserId, String envId, EnvironmentDTO environmentDTO) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(envId), "修改环境失败,环境ID不能为空");

    dcEnvironmentMapper.updateByPrimaryKeySelective((DcEnvironmentWithBLOBs) environmentDTO.toEntity()
        .withId(envId)
        .withModifyUserid(modifyUserId)
        .withModifyDatetime(new Date()));
    return getEnvironmentById(envId);
  }

  /**
   * 获取当前用户的创建的环境信息
   *
   * @return
   */
  @Transactional(readOnly = true)
  public List<Environment> getEnvironmentList(String userId) {
    DcEnvironmentExample example = new DcEnvironmentExample();
    example.createCriteria().andCreateUseridEqualTo(userId);
    return dcEnvironmentMapper.selectByExample(example).stream()
        .map(Environment::of)
        .collect(Collectors.toList());
  }

  /**
   * 批量删除环境
   *
   * @param environmentIdList
   * @return
   */
  public Affected logDeleteEnvironment(List<String> environmentIdList) {
    if (environmentIdList == null) {
      throw new IllegalArgumentException("批量删除环境失败,参数不能为空");
    }
    if (environmentIdList.size() == 0) {
      return new Affected(0);
    }
    DcEnvironmentExample example = new DcEnvironmentExample();
    example.createCriteria().andIdIn(environmentIdList);
    int affect = dcEnvironmentMapper.deleteByExample(example);
    return new Affected(affect);
  }

  /**
   * 获取指定项目的环境值
   *
   * @param projectId
   * @return
   */
  @Transactional(readOnly = true)
  public List<Environment> getProjectEnvByProjectId(String projectId) {
    DcEnvironmentExample example = new DcEnvironmentExample();
    example.createCriteria().andProjectIdEqualTo(projectId);
    return dcEnvironmentMapper.selectByExampleWithBLOBs(example).stream()
        .map(Environment::of)
        .collect(Collectors.toList());
  }
}
