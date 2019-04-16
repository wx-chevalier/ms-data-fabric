package com.zhuxun.dmc.zuul.domain.zuul;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 作用：封装错误信息
 *
 * <p>时间：18-7-11 上午9:54
 *
 * <p>位置：com.zhuxun.dmc.zuul.domain.zuul
 *
 * @author Yan - tao
 */

@Data
@Accessors(chain = true)
public class VErrorBean {

    private String message;

    private int code;

}
