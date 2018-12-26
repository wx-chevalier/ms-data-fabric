package com.zhuxun.dmc.zuul.web.zuul.validation;

import com.google.common.base.Strings;
import io.swagger.models.parameters.QueryParameter;

import java.util.List;
import java.util.Map;

/**
 * 作用：请求头参数校验
 *
 * <p>时间：18-7-9 上午9:07
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.zuul.validation
 *
 * @author Yan - tao
 */
public class HeaderValidateInstance implements OASValidate<List<QueryParameter>, Map<String,String>> {

    @Override
    public void process(List<QueryParameter> queryParameters, Map<String,String> map) throws ValidateFailException {
        if (queryParameters == null || queryParameters.size() == 0) {
            return;
        }

        queryParameters.stream().filter(parameter -> parameter.getRequired() && "header".equals(parameter.getIn())).forEach(parameter -> {
            String name = parameter.getName();
            if (!map.containsKey(name)){
                throw new ValidateFailException("请求头中,缺少参数:" + name);
            }

            // 不允许位空,则检查参数是否为空字符串
            // 在 OAS 中 allowEmptyValue 默认是false,所以当其为 null 的时候,也一并做不允许empty处理
            if (parameter.getAllowEmptyValue()== null || !parameter.getAllowEmptyValue()){
                // 为空,则抛出校验失败的异常
                
                if (Strings.isNullOrEmpty(map.get(name))){
                    throw new ValidateFailException("请求头参数:"+ name + "的值不允许为空");
                }
            }
        });


    }
}
