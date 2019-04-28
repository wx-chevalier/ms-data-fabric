package com.zhuxun.dmc.zuul.service;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.zhuxun.dmc.zuul.config.validation.ParamTypeConstant;
import com.zhuxun.dmc.zuul.repository.model.DcApiParam;
import com.zhuxun.dmc.zuul.repository.model.DcApiParamExample;
import com.zhuxun.dmc.zuul.repository.model.DcApiResponse;
import com.zhuxun.dmc.zuul.repository.model.DcApiResponseExample;
import com.zhuxun.dmc.zuul.utils.HttpParseUtils;
import com.zhuxun.dmc.zuul.utils.JacksonUtil;
import com.zhuxun.dmc.zuul.web.zuul.validation.OASValidateFactory;
import com.zhuxun.dmc.zuul.web.zuul.validation.ValidateFailException;
import com.zhuxun.dmc.zuul.web.zuul.validation.ValidationType;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.media.ArraySchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 用于校验模型,主要用于Proxy代理的过滤器中
 *
 * @author tao
 */
@Slf4j
@Service
public class ValidationSchemaService extends AbstractService {

    /**
     * 通过 apiid 获取API参数数据
     *
     * @param apiId
     * @return
     */
    public List<DcApiParam> getAPiParamByApiId(String apiId) {
        DcApiParamExample apiParamExample = new DcApiParamExample();
        apiParamExample.createCriteria().andApiIdEqualTo(apiId);
        List<DcApiParam> apiParams = apiParamMapper.selectByExample(apiParamExample);
        return apiParams;
    }


    /**
     * 通过 apiId 获取该API的响应参数模型
     *
     * @param apiId
     * @return
     */
    public List<DcApiResponse> getApiResponseByApiId(String apiId) {
        DcApiResponseExample apiResponseExample = new DcApiResponseExample();
        apiResponseExample.createCriteria().andApiIdEqualTo(apiId);
        List<DcApiResponse> responses = apiResponseMapper.selectByExample(apiResponseExample);
        return responses;
    }

    /**
     * 校验请求参数
     *
     * @param request
     * @param apiId
     * @throws IOException
     */
    public void validateRequestSchema(HttpServletRequest request, String apiId) throws IOException, JsonSyntaxException, ValidateFailException {

        List<DcApiParam> apiParams = getAPiParamByApiId(apiId);


        //校验查询参数和请求头参数
        List<DcApiParam> paramList = apiParams.stream().filter(dcApiParam -> ParamTypeConstant.PARAMETER.equals(dcApiParam.getParamType())).collect(Collectors.toList());
        for (DcApiParam dcApiParam : paramList) {
            List<QueryParameter> parameter = JacksonUtil.readListValue(dcApiParam.getParamModel(), QueryParameter.class);
            Map<String, String> parameters = HttpParseUtils.parserQueryString(request);
            // 校验查询参数
            OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_PARAMETER).process(parameter, parameters);
            // 校验请求头参数
            Map<String, String> headers = HttpParseUtils.parseHeaders(request);
            OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_HEADER).process(parameter,headers);
        }


        // 目前仅仅校验 content-type = "application/json"类型的数据
        if (request.getContentType() == null || !MediaType.APPLICATION_JSON_UTF8_VALUE.contains(request.getContentType())) {
            log.warn("暂不支持校验非APPLICATION/JSON的媒体类型");
            return;
        }

        if (!request.getMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
            String requestBody = HttpParseUtils.parseRequestBody(request);
            // 校验请求体
            List<DcApiParam> requestBodyParam = apiParams.stream().filter(dcApiParam -> ParamTypeConstant.REQUEST_BODY.equals(dcApiParam.getParamType())).collect(Collectors.toList());
            for (DcApiParam dcApiParam : requestBodyParam) {
                ArraySchema schema = JacksonUtil.readValue(dcApiParam.getParamModel(), ArraySchema.class);
                JsonElement element = new JsonParser().parse(requestBody);
                OASValidateFactory.getValidationInstance(ValidationType.VALIDATION_SCHEMA).process(schema, element);
            }
        }
    }

}
