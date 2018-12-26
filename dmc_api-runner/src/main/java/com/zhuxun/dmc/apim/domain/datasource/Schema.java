package com.zhuxun.dmc.apim.domain.datasource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.repository.model.DcDataSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Schema extends Modifiable {
  String name;

  SchemaType type;

  Datasource dataSource;

  Collection<Table> tableList;

  Collection<String> authUsers;

  public static Schema of(DcDataSchema s) {
    Schema schema =
        new Schema()
            .setName(s.getDatabaseName())
            .setDataSource(new Datasource().setId(s.getDataSourceId()));
    try {
      schema.setType(Schema.SchemaType.valueOf(s.getDatabaseSort()));
    } catch (Exception e) {
      schema.setType(null);
    }
    schema.setCreateDatetime(s.getCreateDatetime());
    schema.setCreateUser(s.getCreateUserid());
    return schema;
  }

  public enum SchemaType {
    mysql,
    sqlserver,
    oracle,
    presto
  }
}
