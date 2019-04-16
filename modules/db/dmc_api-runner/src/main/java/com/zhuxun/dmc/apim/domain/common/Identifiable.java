package com.zhuxun.dmc.apim.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public abstract class Identifiable<T extends Identifiable<T>> {
  @JsonProperty @Getter protected String id;

  @SuppressWarnings("unchecked")
  public T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
