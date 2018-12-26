package com.zhuxun.dmc.apim.domain.sec;

import com.zhuxun.dmc.apim.domain.common.Identifiable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends Identifiable<Role> {
  String name;
}
