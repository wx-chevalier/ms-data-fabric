package com.zhuxun.dmc.apim.domain.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProjectDocTest {
  @Test
  public void serialization_ProjectDoc() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    ProjectDoc doc0 = new ProjectDoc()
        .setId("doc-id-0")
        .setTitle("title 0")
        .setType("typ")
        .setContent("content 0")
        .setCreateUser("user 0")
        .setModifyUser("user 1");

    String s = objectMapper.writeValueAsString(doc0);
    System.out.println(s);
    ProjectDoc doc0Recreated = objectMapper.readValue(s, ProjectDoc.class);

    assertNotNull(doc0Recreated.getCreateUser());
    assertEquals(
        doc0.getCreateUser().getId(),
        doc0Recreated.getCreateUser().getId());
  }
}