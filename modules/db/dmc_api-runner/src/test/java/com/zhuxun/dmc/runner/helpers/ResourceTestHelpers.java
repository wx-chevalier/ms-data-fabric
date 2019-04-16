package com.zhuxun.dmc.runner.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.spring.security.jwt.JWTConfigurer;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * 测试访问 REST 资源的辅助函数
 *
 * <p>注意，执行资源访问前需要手工设定 objectMapper 和 mockMvc 值
 */
@Accessors(chain = true)
@Slf4j
public class ResourceTestHelpers {
  @Setter static ObjectMapper objectMapper;

  @Setter static MockMvc mockMvc;

  public static MockHttpServletRequestBuilder withToken(
      MockHttpServletRequestBuilder builder, String token) {
    if (token == null) {
      return builder;
    } else {
      return builder.header(JWTConfigurer.TRUELORE_KEY, token);
    }
  }

  public static <T> T deserialize(ResultActions resultActions, Class<T> clazz) throws IOException {
    String resultStr = resultActions.andReturn().getResponse().getContentAsString();
    log.debug("deserialize {}\n{}", clazz, resultStr);
    return objectMapper.readValue(resultStr, clazz);
  }

  public static <T> T deserialize(ResultActions resultActions, TypeReference<T> t)
      throws IOException {
    String resultStr = resultActions.andReturn().getResponse().getContentAsString();
    log.debug("deserialize {}\n{}", t, resultStr);
    return objectMapper.readValue(resultStr, t);
  }

  // region api impl
  public static ResultActions post_api(String token, String apiId, Map<String, Object> params)
      throws Exception {
    return mockMvc.perform(
        withToken(post("/api/{apiId}", apiId), token)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(params)));
  }
  // endregion
}
