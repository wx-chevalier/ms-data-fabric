package com.zhuxun.dmc.apim.dto.api.apiimpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.repository.model.DcApiImpl;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SQLImpl.class, name = "SQL"),
    @JsonSubTypes.Type(value = RelGraphImpl.class, name = "REL_GRAPH")})
@ApiModel(
    description = "API 实现定义\n" +
        "- type=SQL - SQL 语句实现的接口 [SQLImpl](#model-SQLImpl)\n" +
        "- type=REL_GRAPH - 关系图实现的接口 [RelGraphImpl](#model-RelGraphImpl)",
    subTypes = {SQLImpl.class, RelGraphImpl.class},
    discriminator = "type")
@JsonIgnoreProperties(value = {"type"}, ignoreUnknown = true)
public abstract class APIImplTO {

  abstract APIImplType getType();

  public DcApiImpl toEntity(ObjectMapper objectMapper)
      throws JsonProcessingException {

    return new DcApiImpl()
        .withApiImpl(objectMapper.writeValueAsString(this));
  }

  public enum APIImplType {
    SQL,
    REL_GRAPH
  }
}
