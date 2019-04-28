package com.zhuxun.dmc.user.domain.sec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.user.domain.common.Modifiable;
import com.zhuxun.dmc.user.repository.model.SecUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends Modifiable<User> {
  String name;

  private List<Permission> permissions;

  private List<Role> roles;

  private Boolean status;

  public static User of(SecUser secUser) {
    return Optional.ofNullable(secUser)
        .map(u -> new User()
            .setId(u.getId())
            .setName(u.getUsername())
            .setStatus(u.getStatus())
            .setCreateDatetime(u.getCreateDatetime())
            .setModifyDatetime(u.getModifyDatetime()))
        .orElse(null);
  }
}
