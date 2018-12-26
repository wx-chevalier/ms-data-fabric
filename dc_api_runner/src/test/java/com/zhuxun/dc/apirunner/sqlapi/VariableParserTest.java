package com.zhuxun.dc.apirunner.sqlapi;

import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import org.junit.Test;

import static org.junit.Assert.*;

public class VariableParserTest {
  @Test
  public void testParser() throws SQLAPIException {
    VariableParser parser = new VariableParser("SELECT * FROM A LIMIT {limit}");
    parser.parse();
  }
}