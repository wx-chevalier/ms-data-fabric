package com.zhuxun.dmc.apim.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ApiMapper {
  @Update("UPDATE dc_api SET folder_id=null WHERE folder_id=#{folderId}")
  int clearFolder(@Param("folderId") String folderId);
}
