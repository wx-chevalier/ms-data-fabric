package com.zhuxun.dmc.user.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UserLogin {
  String username;
  String password;
  Boolean rememberMe;
}
