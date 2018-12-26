package com.zhuxun.dmc.zuul.utils;

import java.util.UUID;

/**
 * 作用：UUID生成工具类
 *
 * <p>时间：2018/6/27 17:32
 *
 * <p>位置：com.zhuxun.dmc.zuul.utils
 *
 * @author Yan - tao
 */
public class UUIDUtils {

  /**
   * 生成UUID
   *
   * @return 36位长的字符串
   */
  public static String getUUID() {
    return UUID.randomUUID().toString();
  }
}
