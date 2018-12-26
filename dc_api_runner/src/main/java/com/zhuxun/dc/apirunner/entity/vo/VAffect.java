package com.zhuxun.dc.apirunner.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VAffect {

  private int count;

  private static VAffect vCount = new VAffect();

  private VAffect() {
    this.count = 0;
  }

  public static VAffect of(int count) {
    synchronized (VAffect.class) {
      vCount.setCount(count);
      return vCount;
    }
  }
}
