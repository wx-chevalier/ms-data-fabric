package com.zhuxun.dmc.apim.domain.api.apiimpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.domain.api.apiimpl.graph.RelGraphImplDef;
import com.zhuxun.dmc.apim.repository.model.DcApiImpl;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/** 接口实现定义 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = SQLImplDef.class, name = "SQL"),
  @JsonSubTypes.Type(value = RelGraphImplDef.class, name = "REL_GRAPH")
})
@ApiModel(
    description =
        "API 实现定义\n"
            + "- type=SQL - SQL 语句实现的接口 [SQLImplDef](#model-SQLImplDef)\n"
            + "- type=REL_GRAPH - 关系图实现的接口 [RelGraphImplDef](#model-RelGraphImplDef)",
    subTypes = {SQLImplDef.class, RelGraphImplDef.class},
    discriminator = "type")
@JsonIgnoreProperties(
    value = {"type"},
    ignoreUnknown = true)
public abstract class APIImplDef {
  public abstract APIImplType getType();

  public DcApiImpl toEntity(ObjectMapper objectMapper) throws JsonProcessingException {

    return new DcApiImpl().withApiImpl(objectMapper.writeValueAsString(this));
  }
}
