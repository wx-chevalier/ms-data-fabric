package com.zhuxun.dmc.apim.domain.datasource;

import com.zhuxun.dmc.apim.repository.model.DcDataSchema;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class Schema {
  String name;

  SchemaType type;

  Datasource dataSource;

  List<Table> tableList;

  public enum SchemaType {
    mysql,
    sqlserver,
    oracle,
    presto
  }

  public static Schema of(DcDataSchema s) {
    Schema schema = new Schema()
        .setName(s.getDatabaseName())
        .setDataSource(new Datasource().setId(s.getDataSourceId()));
    try {
      schema.setType(Schema.SchemaType.valueOf(s.getDatabaseSort()));
    } catch (Exception e) {
      schema.setType(null);
    }
    return schema;
  }
}
