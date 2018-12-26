package com.zhuxun.dmc.zuul.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * The class JacksonUtil
 *
 * <p>
 *
 * <p>json字符与对像转换
 *
 * @version: $Revision$ $Date$ $LastChangedBy$
 */
public final class JacksonUtil {

    public static ObjectMapper objectMapper;

    /**
     * 获取泛型的集合类型
     *
     * @param collectionClass
     * @param elementClasses
     * @return
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。 (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List,如List<Student>,将第二个参数传递为Student [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     *
     * @param jsonStr
     * @param valueType
     * @return
     */
    public static <T> T readValue(String jsonStr, Class<T> valueType) throws IOException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        return objectMapper.readValue(jsonStr, valueType);
    }

    /**
     * 反序列化List<T>
     *
     * @param jsonStr 数据源
     * @param valueType 集合元素类型
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> readListValue(String jsonStr, Class<T> valueType) throws IOException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        List<T> list = objectMapper.readValue(jsonStr, getCollectionType(List.class, valueType));
        return list;
    }

    /**
     * 把JavaBean转换为json字符串
     *
     * @param object
     * @return
     */
    public static String toJSon(Object object) throws JsonProcessingException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper.writeValueAsString(object);
    }
}
