package com.zhuxun.dmc.apim.domain.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.page.PageInfo;
import com.zhuxun.dmc.apim.repository.model.DcProjectDynamic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 作用：携带分页信息的
 *
 * <p>时间：18-7-12 上午10:35
 *
 * <p>位置：com.zhuxun.dmc.apim.domain.project
 *
 * @author Yan - tao
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDynamicsWithPageInfo extends PageInfo {

  List<ProjectDynamic> projectDynamics;

  public static ProjectDynamicsWithPageInfo of(
      com.github.pagehelper.PageInfo<DcProjectDynamic> pageInfo) {
    ProjectDynamicsWithPageInfo dynamics = new ProjectDynamicsWithPageInfo();
    dynamics
        .setEndRow(pageInfo.getEndRow())
        .setStartRow(pageInfo.getStartRow())
        .setHasNextPage(pageInfo.isHasNextPage())
        .setHasPreviousPage(pageInfo.isHasPreviousPage())
        .setFristPage(pageInfo.isIsFirstPage())
        .setLastPage(pageInfo.isIsLastPage())
        .setNextPage(pageInfo.getNextPage())
        .setPrePage(pageInfo.getPrePage())
        .setPageNum(pageInfo.getPageNum())
        .setPageSize(pageInfo.getPageSize())
        .setTotal(pageInfo.getTotal())
        .setPages(pageInfo.getPages());
    dynamics.setProjectDynamics(
        pageInfo.getList().stream().map(ProjectDynamic::of).collect(Collectors.toList()));
    return dynamics;
  }
}
