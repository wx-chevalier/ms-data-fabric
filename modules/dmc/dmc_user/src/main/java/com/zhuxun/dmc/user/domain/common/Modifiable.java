package com.zhuxun.dmc.user.domain.common;

import java.util.Date;

public abstract class Modifiable<T extends Modifiable<T>> extends Identifiable<T> {
  protected Date createDatetime;

  protected Date modifyDatetime;

  public Date getCreateDatetime() {
    return createDatetime;
  }

  @SuppressWarnings("unchecked")
  public T setCreateDatetime(Date createDatetime) {
    this.createDatetime = createDatetime;
    return (T) this;
  }

  public Date getModifyDatetime() {
    return modifyDatetime;
  }

  @SuppressWarnings("unchecked")
  public T setModifyDatetime(Date modifyDatetime) {
    this.modifyDatetime = modifyDatetime;
    return (T) this;
  }
}
