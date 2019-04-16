package com.zhuxun.dmc.apim.domain.project;

/**
 * 作用：项目动态以及其操作对象
 *
 * <p>时间：18-7-12 上午10:54
 *
 * <p>位置：com.zhuxun.dmc.apim.domain.project
 *
 * @author Yan - tao
 */
public enum ProjectDynamicType {
  ADD_ENV,
  ADD_PROJECT,
  ADD_PROJECT_DOC,
  ADD_API_DOC,
  ADD_SELF_API,
  ADD_PROXY_API,
  ADD_MODEL,
  ADD_FOLDER,

  DELETE_ENV,
  DELETE_PROJECT,
  DELETE_PROJECT_DOC,
  DELETE_API_DOC,
  DELETE_SELF_API,
  DELETE_PROXY_API,
  DELETE_MODEL,
  DELETE_FOLDER,

  UPDATE_ENV,
  UPDATE_PROJECT,
  UPDATE_PROJECT_DOC,
  UPDATE_API_DOC,
  UPDATE_SELF_API,
  UPDATE_PROXY_API,
  UPDATE_MODEL,
  UPDATE_FOLDER;
}
