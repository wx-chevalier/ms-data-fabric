package com.zhuxun.dmc.apim.domain.datasource;

import com.zhuxun.dmc.apim.repository.model.DcDataColumn;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Column {
  String name;

  String type;

  Table table;

  public static Column of (DcDataColumn c) {
    return new Column().setName(c.getColumnName()).setType(c.getColumnType());
  }
}
