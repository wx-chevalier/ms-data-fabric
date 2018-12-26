package com.zhuxun.dmc.user.dto.user;

import com.zhuxun.dmc.user.repository.model.SecUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data
public class UserUpdate extends UserCreation {
  public SecUser toEntity(String id) {
    Date now = new Date();
    // TODO 密码的处理，后台不应存储明文密码
    SecUser secUser = new SecUser()
        .withId(id)
        .withModifyDatetime(now)
        .withUsername(name);

    if (password == null) {
      return secUser;
    } else {
      return secUser
          .withPassword(password)
          .withPasssalt(password);
    }
  }
}
