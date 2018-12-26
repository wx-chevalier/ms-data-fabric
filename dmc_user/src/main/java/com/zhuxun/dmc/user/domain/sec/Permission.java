package com.zhuxun.dmc.user.domain.sec;

import com.zhuxun.dmc.user.domain.common.Identifiable;
import com.zhuxun.dmc.user.repository.model.SecPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Optional;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class Permission extends Identifiable<Permission> {
  String name;

  public static Permission of(SecPermission secPermission) {
    return Optional.ofNullable(secPermission)
        .map(p -> new Permission()
            .setId(secPermission.getId())
            .setName(secPermission.getPermission()))
        .orElse(null);
  }
}
