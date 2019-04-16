package com.zhuxun.dc.apirunner.entity.vo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dc.apirunner.dao.entity.DcApiResponse;
import com.zhuxun.dc.apirunner.entity.VCreteAndModify;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Optional;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VResponse extends VCreteAndModify {

  private String id;

  private String responseModel;

  private String apiId;

  public static VResponse of(DcApiResponse dcApiResponse) {
    return Optional.of(dcApiResponse)
        .map(
            response -> {
              VResponse vResponse = new VResponse();
              vResponse
                  .setCreteDatetime(response.getCreateDatetime())
                  .setCreteUserId(response.getCreateUserid())
                  .setModifyDatetime(response.getModifyDatetime())
                  .setModifyUserId(response.getModifyUserid());
              vResponse
                  .setId(response.getId())
                  .setApiId(response.getApiId())
                  .setResponseModel(response.getResponseModel());
              return vResponse;
            })
        .orElse(null);
  }
}
