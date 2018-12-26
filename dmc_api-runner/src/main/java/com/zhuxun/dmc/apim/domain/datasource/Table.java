package com.zhuxun.dmc.apim.domain.datasource;

import com.zhuxun.dmc.apim.repository.model.DcDataTable;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Table {
  String name;

  List<Column> columnList;

  Schema schema;

  public static Table of(DcDataTable t) {
    return new Table().setName(t.getTableName());
  }
}
