package com.zhuxun.dmc.user.domain.sec;

import com.zhuxun.dmc.user.domain.common.Identifiable;
import com.zhuxun.dmc.user.repository.model.SecRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Optional;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends Identifiable<Role> {
  String name;

  public static Role of(SecRole secRole) {
    return Optional.ofNullable(secRole)
        .map(r -> new Role()
            .setId(secRole.getId())
            .setName(secRole.getRole()))
        .orElse(null);
  }
}
