package com.zhuxun.dc.apirunner.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VCreteAndModify {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
  private Date creteDatetime;

  private String creteUserId;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
  private Date modifyDatetime;

  private String modifyUserId;

}
