package com.zhuxun.dc.apirunner.entity.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SqlModelDTO {
  /**
   * 接口实现方式，这里固定为 <code>"sql"</code>
   */
  @JsonProperty("typ")
  protected String typ;

  /**
   * 实现 SQL 语句的方言，这里可以为：
   * <li>presto</li>
   * <li>mysql</li>
   * <li>sqlserver</li>
   * <li>oracle</li>
   */
  @JsonProperty("dialect")
  protected String dialect;

  @JsonProperty("sql")
  protected String sql;

  /**
   * SQL 对应数据源名称，如果 {@link #dialect} 为 presto，该值可以为空
   */
  @JsonProperty("datasourceName")
  protected String datasourceName;
}
