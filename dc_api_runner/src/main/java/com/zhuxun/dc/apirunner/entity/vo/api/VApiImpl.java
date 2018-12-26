package com.zhuxun.dc.apirunner.entity.vo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dc.apirunner.dao.entity.DcApiImpl;
import com.zhuxun.dc.apirunner.entity.VCreteAndModify;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Optional;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VApiImpl extends VCreteAndModify {

  private String id;

  private String apiImpl;

  private String apiId;

  public static VApiImpl of(DcApiImpl dcApiResponse) {
    return Optional.of(dcApiResponse)
        .map(
            apiImpl -> {
              VApiImpl vApiImpl = new VApiImpl();
              vApiImpl
                  .setCreteDatetime(apiImpl.getCreateDatetime())
                  .setCreteUserId(apiImpl.getCreateUserid())
                  .setModifyDatetime(apiImpl.getModifyDatetime())
                  .setModifyUserId(apiImpl.getModifyUserid());
              vApiImpl
                  .setId(apiImpl.getId())
                  .setApiId(apiImpl.getApiId())
                  .setApiImpl(apiImpl.getApiImpl());
              return vApiImpl;
            })
        .orElse(null);
  }
}
