package com.zhuxun.dmc.apim.domain.datasource;

import com.zhuxun.dmc.apim.domain.common.Identifiable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
public class Datasource extends Identifiable<Datasource> {
  String name;

  List<Schema> schemaList;
}
