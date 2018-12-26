package com.zhuxun.dmc.sqlapi;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.v3.oas.models.media.Schema;

public interface OASSchemaValidation {
  void validate(Schema schema, JSONPObject obj);
}
