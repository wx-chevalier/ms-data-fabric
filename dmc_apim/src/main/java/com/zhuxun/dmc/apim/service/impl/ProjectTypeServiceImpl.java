package com.zhuxun.dmc.apim.service.impl;

import com.zhuxun.dmc.apim.config.ApplicationConstants;
import com.zhuxun.dmc.apim.domain.project.ProjectType;
import com.zhuxun.dmc.apim.repository.model.DcProjectTypeExample;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.ProjectTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.collect.Iterators.getOnlyElement;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ProjectTypeServiceImpl extends AbstractService implements ProjectTypeService {
  @Transactional(readOnly = true)
  public List<ProjectType> getAllProjectType() {
    log.info("获取全部项目信息列表");
    DcProjectTypeExample example = new DcProjectTypeExample();
    example.createCriteria().andStatusEqualTo(ApplicationConstants.STATUS_NORMAL);
    return dcProjectTypeMapper.selectByExample(example).stream()
        .map(ProjectType::of)
        .collect(toList());
  }

  @Transactional(readOnly = true)
  public ProjectType getProjectTypeById(String projectTypeId) {
    return ofNullable(dcProjectTypeMapper.selectByPrimaryKey(projectTypeId))
        .map(ProjectType::of)
        .orElse(null);
  }

  @Transactional(readOnly = true)
  public ProjectType getProjectTypeByName(String name) {
    DcProjectTypeExample example = new DcProjectTypeExample();
    example.createCriteria().andTypeNameEqualTo(name);

    return ofNullable(
        getOnlyElement(
            dcProjectTypeMapper.selectByExample(example).iterator()))
        .map(ProjectType::of)
        .orElse(null);
  }

  public String getIdByName(String typeName) {
    return ofNullable(getProjectTypeByName(typeName))
        .map(ProjectType::getId)
        .orElse(null);
  }

  public String getNameById(String typeId) {
    return ofNullable(getProjectTypeById(typeId))
        .map(ProjectType::getName)
        .orElse(null);
  }
}
