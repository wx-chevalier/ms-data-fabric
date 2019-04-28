package com.zhuxun.dc.apirunner.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.media.ObjectSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true, chain = true)
@NoArgsConstructor
public class SqlApiAnalyzerDTO extends SqlModelDTO {
  private ObjectSchema schema;

  private String projectId;

  private String folderId;

  private String apiName;

  private String apiDescribe;

  public SqlApiAnalyzerDTO(
      @JsonProperty("typ") String typ,
      @JsonProperty("dialect") String dialect,
      @JsonProperty("sql") String sql,
      @JsonProperty("datasourceName") String datasourceName,
      @JsonProperty("schema") ObjectSchema schema,
      @JsonProperty("projectId") String projectId,
      @JsonProperty("folderId") String folderId,
      @JsonProperty("apiName") String apiName,
      @JsonProperty("apiDescribe") String apiDescribe) {
    super(typ, dialect, sql, datasourceName);
    this.schema = schema;
    this.projectId = projectId;
    this.folderId = folderId;
    this.apiName = apiName;
    this.apiDescribe = apiDescribe;
  }
}
