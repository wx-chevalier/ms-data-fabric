package com.zhuxun.dc.apirunner.shiro;

import org.apache.shiro.subject.Subject;

/**
 * Created by lotuc on 14/04/2017.
 */
public class SessionStorageEvaluator implements org.apache.shiro.mgt.SessionStorageEvaluator {
  @Override
  public boolean isSessionStorageEnabled(Subject subject) {
    return false;
  }
}
