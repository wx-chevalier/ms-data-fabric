package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class Tuple<X, Y> {
  private X x;
  private Y y;

  public Tuple(X x, Y y) {
    this.x = x;
    this.y = y;
  }
}
