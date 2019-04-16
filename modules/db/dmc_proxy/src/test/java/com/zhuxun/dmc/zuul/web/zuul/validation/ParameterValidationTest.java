package com.zhuxun.dmc.zuul.web.zuul.validation;

import com.zhuxun.dmc.zuul.utils.HttpParseUtils;
import com.zhuxun.dmc.zuul.utils.JacksonUtil;
import io.swagger.models.parameters.QueryParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 作用： 参数校验单元测试
 *
 * <p>时间：18-7-9 上午9:25
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.zuul.validation
 *
 * @author Yan - tao
 */

@Slf4j
public class ParameterValidationTest {

    @Test
    public void testQueryString() throws IOException {
        // 构建校验模型
        String schema = OpenApiValidationImplTest.file2String("src/test/resources/json/parameter/schema.json");
        List<QueryParameter> parameter = JacksonUtil.readListValue(schema, QueryParameter.class);
        Assert.assertTrue(parameter!=null);
        Assert.assertTrue(parameter.size() > 0);

        // 构建测试数据
        String data = OpenApiValidationImplTest.file2String("src/test/resources/json/parameter/data.txt");
        Map<String, String> parser = HttpParseUtils.parserString(data);
        Assert.assertTrue(parser.size() > 0);

        // 开始校验
        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_PARAMETER).process(parameter,parser);
    }


}
