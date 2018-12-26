package com.zhuxun.dmc.apim.domain.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 作用：暂未说明
 *
 * <p>时间：18-7-12 上午11:46
 *
 * <p>位置：com.zhuxun.dmc.apim.domain.project
 *
 * @author Yan - tao
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VProjectDynamicType {

  private Set<String> operateObject;

  private Set<String> typeName;
}
