package com.zhuxun.dmc.zuul.web.zuul.validation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhuxun.dmc.zuul.utils.JacksonUtil;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * 作用：
 *
 * <p>时间：2018/7/3 14:23
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.zuul.validation
 *
 * @author Yan - tao
 */
public class OpenApiValidationImplTest {

    /**
     * 校验 ObjectSchema 数据
     *
     * @throws IOException 文件读取的是否抛出的异常
     */
    @Test
    public void validationSchema() throws IOException {

        String schemaStr = file2String("src/test/resources/json/object/schema.json");
        String data = file2String("src/test/resources/json/object/data.json");

        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, jsonObject);
    }

    /**
     * 校验 StingSchema 数据
     *
     * @throws IOException 文件读取的是否抛出的异常
     */
    @Test
    public void validationStringSchema() throws IOException {
        String schemaStr = file2String("src/test/resources/json/string/schema.json");
        String data = file2String("src/test/resources/json/string/data.json");
        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonElement element = new JsonParser().parse(data);
        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, element);
    }

    /**
     * 校验 ArraySchema 数据
     *
     * @throws IOException 文件读取的是否抛出的异常
     */
    @Test
    public void validationArraySchema() throws IOException {
        String schemaStr = file2String("src/test/resources/json/array/schema.json");
        String data = file2String("src/test/resources/json/array/data.json");
        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonElement jsonObject = new JsonParser().parse(data);
        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, jsonObject);
    }

    /**
     * 校验Object内部包含ArraySchema的形式
     *
     * @throws IOException
     */
    @Test
    public void validationObjectAndArraySchema() throws IOException {
        String schemaStr = file2String("src/test/resources/json/object&array/schema.json");
        String data = file2String("src/test/resources/json/object&array/data.json");
        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonElement jsonObject = new JsonParser().parse(data);
        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, jsonObject);
    }


    /**
     * 测试错误是否抛出
     *
     * @throws IOException
     */
    @Test
    public void testValidateError() throws IOException {
        String schemaStr = file2String("src/test/resources/json/error/schema.json");
        String data = file2String("src/test/resources/json/error/data.json");
        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonElement jsonObject = new JsonParser().parse(data);
        try {
            OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, jsonObject);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ValidateFailException);
        }
    }


    /**
     * <p>综合测试布尔、数字、数组、类、字符串等
     *
     * @throws IOException
     */
    @Test
    public void testMixValidateSchema() throws IOException {
        String schemaStr = file2String("src/test/resources/json/mix/schema.json");
        String data = file2String("src/test/resources/json/mix/data.json");
        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonElement jsonObject = new JsonParser().parse(data);

        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, jsonObject);
    }

    @Test
    public void testFLoatAndIntegerSchema() throws IOException {
        String schemaStr = file2String("src/test/resources/json/float/schema.json");
        String data = file2String("src/test/resources/json/float/data.json");
        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonElement jsonObject = new JsonParser().parse(data);
        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, jsonObject);

    }

    /**
     * 测试空允许检测
     */
    @Test
    public void testNullable() throws IOException {
        String schemaStr = file2String("src/test/resources/json/null/schema.json");
        String data = file2String("src/test/resources/json/null/data.json");
        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonElement jsonObject = new JsonParser().parse(data);
        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, jsonObject);
    }

    /**
     * 测试数字格式问题
     *
     * @throws IOException
     */
    @Test
    public void testNumber() throws IOException {
        String schemaStr = file2String("src/test/resources/json/number/schema.json");
        String data = file2String("src/test/resources/json/number/data.json");
        ObjectSchema schema = JacksonUtil.readValue(schemaStr, ObjectSchema.class);
        JsonElement jsonObject = new JsonParser().parse(data);
        OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, jsonObject);
    }

    /**
     * 通过文件路径读取一个文件，返回该文件的内容
     *
     * @param filePath 文件路径
     * @return 返回文件的内容以字符串的形式
     * @throws IOException 文件读取的是否抛出的异常
     */
    public static String file2String(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        // 将file文件内容转成字符串
        StringBuilder stringBuilder;
        try (BufferedReader bf = new BufferedReader(isr);) {
            String content = "";
            stringBuilder = new StringBuilder();
            while (content != null) {
                content = bf.readLine();
                if (content == null) {
                    break;
                }
                stringBuilder.append(content.trim());
            }
        }
        return stringBuilder.toString();
    }
}
