package com.zhuxun.dmc.zuul.domain.common;

public abstract class Identifiable<T extends Identifiable<T>> {
  protected String id;

  public String getId() {
    return id;
  }

  @SuppressWarnings("unchecked")
  public T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
