package com.zhuxun.dc.apirunner.entity.vo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dc.apirunner.dao.entity.DcApiParam;
import com.zhuxun.dc.apirunner.entity.VCreteAndModify;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Optional;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VParam extends VCreteAndModify {

  private String id;

  private String paramModel;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String paramType;

  private String apiId;


  public static VParam of(DcApiParam dcApiParam) {
    return Optional.of(dcApiParam)
        .map(
            param -> {
              VParam vParam = new VParam();
              vParam
                  .setCreteDatetime(param.getCreateDatetime())
                  .setCreteUserId(param.getCreateUserid())
                  .setModifyDatetime(param.getModifyDatetime())
                  .setModifyUserId(param.getModifyUserid());
              vParam
                  .setId(param.getId())
                  .setApiId(param.getApiId())
                  .setParamModel(param.getParamModel())
                  .setParamType(param.getParamType());
              return vParam;
            })
        .orElse(null);
  }
}
