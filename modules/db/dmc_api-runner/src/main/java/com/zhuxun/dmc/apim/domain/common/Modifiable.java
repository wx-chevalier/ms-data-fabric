package com.zhuxun.dmc.apim.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhuxun.dmc.apim.domain.sec.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class Modifiable<T extends Modifiable<T>> extends Identifiable<T> {
  @JsonProperty
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  protected Date createDatetime;

  @JsonProperty
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  protected Date modifyDatetime;

  @JsonProperty protected User createUser;

  @JsonProperty protected User modifyUser;

  @SuppressWarnings("unchecked")
  public T setCreateDatetime(Date createDatetime) {
    this.createDatetime = createDatetime;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setModifyDatetime(Date modifyDatetime) {
    this.modifyDatetime = modifyDatetime;
    return (T) this;
  }

  @JsonIgnore
  public T setCreateUser(String userId) {
    return setCreateUser(new User().setId(userId));
  }

  @SuppressWarnings("unchecked")
  public T setCreateUser(User createUser) {
    this.createUser = createUser;
    return (T) this;
  }

  @JsonIgnore
  public T setModifyUser(String userId) {
    return setModifyUser(new User().setId(userId));
  }

  @SuppressWarnings("unchecked")
  public T setModifyUser(User modifyUser) {
    this.modifyUser = modifyUser;
    return (T) this;
  }
}
