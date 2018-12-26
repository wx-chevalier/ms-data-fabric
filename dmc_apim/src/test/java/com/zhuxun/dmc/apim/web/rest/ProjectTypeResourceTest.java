package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.helpers.AbstractIntgrationTest;
import org.junit.Test;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ProjectTypeResourceTest extends AbstractIntgrationTest {

  @Test
  public void getAllProjectType() throws Exception {
    this.mockMvc.perform(get("/project/type"))
        .andExpect(jsonPath("$", hasSize(5)));
  }
}