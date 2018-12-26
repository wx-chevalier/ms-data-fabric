package com.zhuxun.dc.apirunner.entity.vo.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class VApiImplModel {
  /**
   * 接口实现方式，这里固定为 <code>"sql"</code>
   */
  protected String typ;

  /**
   * 实现 SQL 语句的方言，这里可以为：
   * <li>presto</li>
   * <li>mysql</li>
   * <li>sqlserver</li>
   * <li>oracle</li>
   */
  protected String dialect;

  protected String sql;

  /**
   * SQL 对应数据源名称，如果 {@link #dialect} 为 presto，该值可以为空
   */
  protected String datasourceName;
}
