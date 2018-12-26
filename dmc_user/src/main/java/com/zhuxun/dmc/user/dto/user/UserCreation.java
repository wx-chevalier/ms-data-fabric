package com.zhuxun.dmc.user.dto.user;

import com.zhuxun.dmc.user.repository.model.SecUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
public class UserCreation {
  protected String name;

  protected String password;

  public SecUser toEntity(String id) {
    Date now = new Date();
    // TODO 密码的处理，后台不应存储明文密码
    return new SecUser()
        .withId(id)
        .withCreateDatetime(now)
        .withModifyDatetime(now)
        .withUsername(name)
        .withPassword(password)
        .withPasssalt(password)
        .withStatus(true);
  }
}
