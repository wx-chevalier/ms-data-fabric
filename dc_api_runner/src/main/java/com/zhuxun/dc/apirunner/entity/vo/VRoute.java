package com.zhuxun.dc.apirunner.entity.vo;

import com.zhuxun.dc.apirunner.dao.entity.DcRoute;
import com.zhuxun.dc.apirunner.entity.VCreteAndModify;
import com.zhuxun.dc.apirunner.utils.JacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VRoute extends VCreteAndModify {

  private String id;

  private String projectId;

  private String envId;

  private String protocol;

  private String operation;

  private String path;

  private String envName;

  private String envValue;

  public DcRoute of() {
    Map<String, String> envMap = null;
    if (this.getEnvValue() != null) {
      envMap = JacksonUtil.readValue(this.getEnvValue(), Map.class);
    } else {
      envMap = new HashMap<String, String>();
    }
    DcRoute dcRoute = new DcRoute();
    dcRoute.setId(this.getId());
    dcRoute.setEnvId(this.getEnvId());
    dcRoute.setPath("/apis/" + this.getId() + "/**");
    dcRoute.setProjectId(this.getProjectId());
    dcRoute.setRetryable(false);
    dcRoute.setStripPrefix(true);
    String url = this.getPath();
    Iterator<String> iterator = envMap.keySet().iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      String replaceStr = String.format("{{%s}}", key);
      url = url.replace(replaceStr, envMap.get(key));
    }
    dcRoute.setUrl(this.getProtocol() + "://" + url);
    return dcRoute;
  }
}
