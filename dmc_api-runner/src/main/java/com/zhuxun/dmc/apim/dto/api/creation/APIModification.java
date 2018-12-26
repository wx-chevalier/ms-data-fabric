package com.zhuxun.dmc.apim.dto.api.creation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.zhuxun.dmc.apim.config.ApplicationConstants;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.repository.model.DcApi;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @Type(value = ProxyAPIModification.class, name = "PROXY"),
  @Type(value = GeneratedAPIModification.class, name = "GENERATED")
})
@ApiModel(
    value = "APIModification",
    description =
        "API 创建/编辑对象\n"
            + "- [GeneratedAPIModification](#model-GeneratedAPIModification)\n"
            + "- [ProxyAPIModification](#model-ProxyAPIModification)",
    subTypes = {GeneratedAPIModification.class, ProxyAPIModification.class},
    discriminator = "type")
@JsonIgnoreProperties(
    value = {"type"},
    ignoreUnknown = true)
public abstract class APIModification<T extends APIModification<T>> {

  private String name;

  private String summary;

  private String projectId;

  private String folderId;

  public abstract API.APIType getType();

  @SuppressWarnings("unchecked")
  public T setName(String name) {
    this.name = name;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setSummary(String summary) {
    this.summary = summary;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setProjectId(String projectId) {
    this.projectId = projectId;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setFolderId(String folderId) {
    this.folderId = folderId;
    return (T) this;
  }

  public DcApi toEntity() {
    return new DcApi()
        .withApiType(getType().toString())
        .withApiName(name)
        .withStatus(ApplicationConstants.STATUS_NORMAL)
        .withApiSummary(summary)
        .withFolderId(folderId)
        .withProjectId(projectId);
  }
}
