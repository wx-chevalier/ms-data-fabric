package com.zhuxun.dmc.zuul.web.zuul.validation;

import javax.xml.bind.ValidationException;

/**
 * 作用：
 *
 * <p>时间：2018/7/3 14:18
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.zuul.validation
 *
 * @author Yan - tao
 */
public interface OASValidate<T,D> {

  /**
   * 校验Schema执行
   *
   * @param t
   * @param d 需要校验的元素
   * @throws ValidationException
   */
  void process(T t, D d) throws ValidateFailException;

}
