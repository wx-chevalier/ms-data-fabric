package com.zhuxun.dmc.sqlapi.impl;

import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VariableParserTest {
  @Test
  public void test_parse() throws SQLAPIException {
    VariableParser parser = new VariableParser();
    List<String> sqls = Arrays.asList(
        "SELECT a FROM t WHERE id={ id}",
        "SELECT a FROM t WHERE id={id }",
        "SELECT a FROM t WHERE id={ id }",
        "SELECT a FROM t WHERE id={  id  }");
    for (String sql : sqls) {
      parser.setSql(sql).parse();
      assertEquals(1, parser.getPositionedVariables().size());
      assertEquals("id", parser.getPositionedVariables().get(0).getName());
      assertNull(parser.getPositionedVariables().get(0).getType());
      System.out.println(parser.getPositionedVariables());
    }

    sqls = Arrays.asList(
        "SELECT a FROM t WHERE id={id: string}",
        "SELECT a FROM t WHERE id={id: string }",
        "SELECT a FROM t WHERE id={id : string}",
        "SELECT a FROM t WHERE id={id  : string }");
    for (String sql : sqls) {
      parser.setSql(sql).parse();
      assertEquals(1, parser.getPositionedVariables().size());
      assertEquals("id", parser.getPositionedVariables().get(0).getName());
      assertEquals(VariableParser.VarType.string, parser.getPositionedVariables().get(0).getType());
    }
  }
}