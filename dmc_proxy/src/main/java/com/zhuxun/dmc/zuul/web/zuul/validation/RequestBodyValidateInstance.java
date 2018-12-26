package com.zhuxun.dmc.zuul.web.zuul.validation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;

import static com.zhuxun.dmc.zuul.web.zuul.validation.RequestBodyValidateInstance.SchemaType.*;

/**
 * 作用：
 *
 * <p>时间：2018/7/3 15:08
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.zuul.validation
 *
 * @author Yan - tao
 */
@Slf4j
public class RequestBodyValidateInstance implements OASValidate<Schema,JsonElement> {

  /** 跳过不支持类型检查 */
  private boolean skipNotSupportSchemaType = true;

  /** 校验 ArrayScheme 的item */
  private boolean validateArrayItem = false;

  @Override
  public void process(Schema schema, JsonElement element) throws ValidateFailException {
    // 如果 Schema 不存在或者 nullable 允许为 true，那么不在继续检查，直接通过
    if (schema == null) {
      return;
    }
    // 如果schema不允许为空，那么element为空的话，则抛出异常
    if (!schema.getNullable() && element == null) {
      throw new ValidateFailException("校验失败,元素不允许为NULL");
    }
    // 如果scheme允许位空，且element是null，那么至直接返回，否则继续检查element的类型
    if(schema.getNullable() && element == null){
        return;
    }
    switch (schema.getType()) {
      case OBJECT:
        validateObjectSchema(schema, element);
        break;
      case ARRAY:
        validateArraySchema(schema, element);
        break;
      case INTEGER:
        schema.setType(NUMBER);
      case STRING:
      case BOOLEAN:
      case NUMBER:
        validateBaseSchema(schema, element);
        break;
      default:
        log.info(" {} 类型暂不支持校验", schema.getType());
        if (!skipNotSupportSchemaType) {
          throw new ValidateFailException(String.format("类型 %s 暂不支持校验", schema.getType()));
        }
    }
  }


  private void validateBaseSchema(Schema schema, JsonElement element) throws ValidateFailException {
    if (!(element instanceof JsonPrimitive)) {
      throw new ValidateFailException("校验失败,元素不是一个JsonPrimitive对象");
    }
    // 校验数据类型
    String type = schema.getType();
    JsonPrimitive primitive = (JsonPrimitive) element;

    if (STRING.equals(type) && !primitive.isString()) {
      throw new ValidateFailException("校验失败,元素不是一个String对象");
    }

    if (NUMBER.equals(type) && !primitive.isNumber()) {
      throw new ValidateFailException("校验失败,元素不是一个Number对象");
    }

    if (BOOLEAN.equals(type) && !primitive.isBoolean()) {
      throw new ValidateFailException("校验失败,元素不是一个Boolean对象");
    }
  }

  private void validateObjectSchema(Schema schema, JsonElement element) throws ValidateFailException {
    Map<String, Schema> schemaProperties = schema.getProperties();
    if (schemaProperties == null) {
      if (element == null) {
        throw new ValidateFailException("校验失败,元素不允许为NULL");
      }
      return;
    }
    Iterator<String> iterator = schemaProperties.keySet().iterator();
    while (iterator.hasNext()) {
      String iteratorKey = iterator.next();
      Schema subSchema = schemaProperties.get(iteratorKey);
      if (element instanceof JsonObject) {
        JsonElement subElement = ((JsonObject) element).get(iteratorKey);
        process(subSchema, subElement);
      } else {
        throw new ValidateFailException("校验失败,元素不是一个JsonObject对象");
      }
    }
  }

  private void validateArraySchema(Schema schema, JsonElement element) throws ValidateFailException {
    if (!(element instanceof JsonArray)) {
      throw new ValidateFailException("校验失败,元素不是一个JsonArray对象");
    }
    JsonArray elements = (JsonArray) element;

    if(elements.size() ==0 ){
      return;
    }

    // FIXME 子Array无法校验
    if(schema instanceof ArraySchema){
      ArraySchema arraySchema = (ArraySchema) schema;
      elements.iterator().forEachRemaining(jsonElement ->  process(arraySchema.getItems(),jsonElement));
    }
  }



  /** Schema类型 名称常量信息 */
  class SchemaType {
    public static final String STRING = "string";
    public static final String OBJECT = "object";
    public static final String ARRAY = "array";
    public static final String NUMBER = "number";
    public static final String BOOLEAN = "boolean";
    public static final String INTEGER = "integer";
  }
}
