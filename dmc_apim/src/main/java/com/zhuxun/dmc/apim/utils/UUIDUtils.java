package com.zhuxun.dmc.apim.utils;


import java.util.UUID;


public class UUIDUtils {

  /**
   * 生成32为UUID
   *
   * @return
   */
  public static String getUUID() {
    return UUID.randomUUID().toString();
  }

}
