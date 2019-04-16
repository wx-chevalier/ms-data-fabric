package com.zhuxun.dmc.apim.domain.sec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends Modifiable<User> {
  String name;

  private List<Permission> permissions;

  private List<Role> roles;

  private Boolean status;
}
