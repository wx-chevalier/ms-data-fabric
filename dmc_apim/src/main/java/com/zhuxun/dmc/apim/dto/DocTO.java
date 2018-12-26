package com.zhuxun.dmc.apim.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DocTO {

  protected String content;

  protected String title;

  protected String contentType;
}
