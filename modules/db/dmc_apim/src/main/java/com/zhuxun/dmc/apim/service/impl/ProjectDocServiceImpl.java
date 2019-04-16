package com.zhuxun.dmc.apim.service.impl;

import com.zhuxun.dmc.apim.domain.project.ProjectDoc;
import com.zhuxun.dmc.apim.dto.project.ProjectDocTO;
import com.zhuxun.dmc.apim.repository.model.DcProjectDoc;
import com.zhuxun.dmc.apim.repository.model.DcProjectDocExample;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.ProjectDocService;
import com.zhuxun.dmc.apim.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.zhuxun.dmc.apim.config.ApplicationConstants.STATUS_DISABLE;
import static com.zhuxun.dmc.apim.config.ApplicationConstants.STATUS_NORMAL;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ProjectDocServiceImpl extends AbstractService implements ProjectDocService {

  public ProjectDoc createProjectDoc(String createUserId, ProjectDocTO projectDocTO) {
    String id = UUIDUtils.getUUID();
    Date now = new Date();
    dcProjectDocMapper.insert(projectDocTO.toEntity()
        .withId(id)
        .withCreateUserid(createUserId)
        .withCreateDatetime(now)
        .withModifyUserid(createUserId)
        .withModifyDatetime(now)
        .withStatus(STATUS_NORMAL));
    return getProjectDocById(id);
  }

  /**
   * 通过ProjectDocId获取文档
   *
   * @param projectDocId
   * @return
   */
  @Transactional(readOnly = true)
  public ProjectDoc getProjectDocById(String projectDocId) {
    DcProjectDocExample docExample = new DcProjectDocExample();
    docExample.createCriteria()
        .andIdEqualTo(projectDocId)
        .andStatusEqualTo(STATUS_NORMAL);
    return Optional.ofNullable(
        getOnlyElement(
            dcProjectDocMapper.selectByExampleWithBLOBs(docExample), null))
        .map(ProjectDoc::of)
        .orElse(null);
  }

  @Override
  public List<ProjectDoc> getProjectDocListByProjectId(String projectId) {
    DcProjectDocExample docExample = new DcProjectDocExample();
    docExample.createCriteria()
        .andStatusEqualTo(STATUS_NORMAL)
        .andProjectIdEqualTo(projectId);

    return dcProjectDocMapper.selectByExample(docExample).stream()
        .map(ProjectDoc::of)
        .collect(toList());
  }

  /**
   * 通过projectId获取该接口的一系列文档
   *
   * @param projectId 项目ID
   * @return
   */
  @Transactional(readOnly = true)
  public List<ProjectDoc> getProjectDocByProjectId(String projectId) {
    DcProjectDocExample docExample = new DcProjectDocExample();
    docExample.createCriteria()
        .andStatusEqualTo(STATUS_NORMAL)
        .andProjectIdEqualTo(projectId);

    return dcProjectDocMapper.selectByExample(docExample).stream()
        .map(ProjectDoc::of)
        .collect(toList());
  }

  /**
   * 更新指定项目文档
   */
  @Transactional
  public ProjectDoc updateProjectDocById(String modifyUserId, String docId, ProjectDocTO projectDocTO) {
    dcProjectDocMapper.updateByPrimaryKeySelective(projectDocTO.toEntity()
        .withStatus(STATUS_NORMAL)
        .withId(docId)
        .withModifyUserid(modifyUserId)
        .withModifyDatetime(new Date()));
    return getProjectDocById(docId);
  }

  @Override
  public void logicDeleteProjectDoc(String modifyUserId, String docId) {
    dcProjectDocMapper.updateByPrimaryKeySelective(new DcProjectDoc()
        .withId(docId)
        .withStatus(STATUS_DISABLE)
        .withModifyUserid(modifyUserId)
        .withModifyDatetime(new Date()));
  }
}
