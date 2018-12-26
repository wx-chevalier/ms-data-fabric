package com.zhuxun.dmc.zuul.web.zuul.validation;

/**
 * 作用： OpenAPI - Specification 校验工厂Bean
 *
 * <p>时间：2018/7/4 16:06
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.zuul.validation
 *
 * @author Yan - tao
 */
public class OASValidateFactory {

    private static OASValidate validateSchemaInstance;

    private static OASValidate validateParameterInstance;

    private static OASValidate validateHeaderInstance;

    public static OASValidate getValidationInstance(ValidationType type) {
        switch (type) {
            case VALIDATION_SCHEMA:
                getSchemaValidateInstance();
                return validateSchemaInstance;
            case VALIDATION_PARAMETER:
                getParameterValidateInstance();
                return validateParameterInstance;
            case VALIDATION_HEADER:
                return getHeaderValidateInstance();
            default:
                throw new ValidateFailException("获取校验实例失败,暂不支持的校验类型:" + type.name());
        }
    }

    /**
     * 获取请求头校验实例
     */
    private static OASValidate getHeaderValidateInstance() {
        if (validateHeaderInstance == null) {
            synchronized (OASValidate.class) {
                if (validateHeaderInstance == null) {
                    validateHeaderInstance = new HeaderValidateInstance();
                }
            }
        }
        return validateHeaderInstance;
    }

    /**
     * 获取请求参数和请求头的校验实例
     */
    private static void getParameterValidateInstance() {
        if (validateParameterInstance == null) {
            synchronized (OASValidate.class) {
                if (validateParameterInstance == null) {
                    validateParameterInstance = new ParameterValidateInstance();
                }
            }
        }
    }

    /**
     * 获取校验Schema实例
     */
    private static void getSchemaValidateInstance() {
        if (validateSchemaInstance == null) {
            synchronized (OASValidate.class) {
                if (validateSchemaInstance == null) {
                    validateSchemaInstance = new RequestBodyValidateInstance();
                }
            }
        }
    }
}
