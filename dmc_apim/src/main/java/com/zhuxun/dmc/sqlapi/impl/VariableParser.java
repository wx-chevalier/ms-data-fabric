package com.zhuxun.dmc.sqlapi.impl;

import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static java.lang.Character.isWhitespace;
import static java.lang.String.format;

@Slf4j
@Accessors(chain = true)
@NoArgsConstructor
public class VariableParser {
  private static final String VARIABLE_PREFIX = "lc__";
  private Set<String> SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
      "string", "integer", "number", "date", "datetime"));

  /**
   * End of Input
   */
  static final int EOI = 0x1A;

  /**
   * 变量出现位置 -> 对应位置变量名
   */
  @Getter
  List<Var> positionedVariables;

  /**
   * 当前解析的 SQL 文本
   */
  @Getter
  @Setter
  private String sql;

  /**
   * 当前字符位置
   */
  private int pos;

  /**
   * 对应 {@link #pos} 位置的字符
   */
  private char ch;

  private StringBuilder questionMarkedSqlBuilder;

  private StringBuilder parsedSqlBuilder;

  /**
   * 替换括号定义的变量为 ? 的语句，用于构造 PreparedStatement
   */
  public String getQuestionMarkedSql() {
    return questionMarkedSqlBuilder.toString();
  }

  /**
   * 替换括号定义的变量替换为 {@link #VARIABLE_PREFIX} 后的语句
   * <p>
   * 方便语法解析使用
   */
  public String getVarReplacedSql() {
    return parsedSqlBuilder.toString();
  }

  public VariableParser(String sql) {
    this.sql = sql;
  }

  public VariableParser parse() throws SQLAPIException {
    this.reset();
    log.debug("Start parsing {}", this.sql);

    while (this.ch != EOI) {
      switch (this.ch) {
        case '{':
          Var var = this.parseVariable();
          this.positionedVariables.add(var);
          this.questionMarkedSqlBuilder.append('?');

          this.parsedSqlBuilder.append('@');
          this.parsedSqlBuilder.append(VARIABLE_PREFIX);
          this.parsedSqlBuilder.append(var.getName());
          continue;
        case '}':
          throw new SQLAPIException("Unbalanced brace");
        default:
          this.questionMarkedSqlBuilder.append(this.ch);
          this.parsedSqlBuilder.append(this.ch);
          this.scanChar();
      }
    }

    log.debug("Parsed: {}", this.getVarReplacedSql());
    log.debug("Prepared: {}", this.getQuestionMarkedSql());
    return this;
  }

  private Var parseVariable() throws SQLAPIException {
    scanChar();
    StringBuilder builder = new StringBuilder();

    skipWhitespace();
    if (ch == '}' || ch == ':') {
      throw new SQLAPIException("Variable type not specified");
    }
    while (ch != '}' && ch != ':' && !isWhitespace(ch)) {
      builder.append(this.ch);
      scanChar();
    }
    skipWhitespace();

    // 分析类型
    VarType type = null;
    if (this.ch == ':') {
      type = this.parseType();
    }

    if (this.ch != '}') {
      throw new SQLAPIException("Spaces is not allowed in variable type");
    }
    scanChar();

    return new Var().setType(type).setName(builder.toString());
  }

  private VarType parseType() throws SQLAPIException {
    scanChar();

    StringBuilder builder = new StringBuilder();

    skipWhitespace();
    if (ch == '}') {
      throw new SQLAPIException("No type specified");
    }
    while (ch != '}' && !isWhitespace(ch)) {
      builder.append(ch);
      scanChar();
    }
    skipWhitespace();

    return VarType.fromString(builder.toString());
  }

  /**
   * 重置/初始化解析所需状态变量
   */
  private void reset() {
    this.pos = 0;
    this.ch = charAt(this.pos);
    this.questionMarkedSqlBuilder = new StringBuilder();
    this.parsedSqlBuilder = new StringBuilder();
    this.positionedVariables = new ArrayList<>();
  }

  private char charAt(int index) {
    if (index >= sql.length()) {
      return EOI;
    }

    return sql.charAt(index);
  }

  private void scanChar() {
    ch = charAt(++pos);
  }

  private void skipWhitespace() {
    while (isWhitespace(ch)) {
      scanChar();
    }
  }

  @Accessors(chain = true)
  @Data
  public static class Var {
    String name;
    VarType type;
  }

  public enum VarType {
    bool,
    string,
    integer,
    number,
    date,
    datetime;

    public static VarType fromTypeFormat(String type, String format) throws SQLAPIException {
      switch (type) {
        case "number":
          return number;
        case "integer":
          return integer;
        case "bool":
          return bool;
        case "string":
          if (format == null) {
            return string;
          }
          switch (format) {
            case "date":
              return date;
            case "date-time":
              return datetime;
            default:
              throw new SQLAPIException(format(
                  "Type not supported {type=%s, format=%s}", type, format));
          }
        default:
          throw new SQLAPIException(format(
              "Type not supported {type=%s, format=%s}", type, format));
      }
    }

    public static VarType fromString(String s) throws SQLAPIException {
      switch (s) {
        case "bool":
          return bool;
        case "string":
          return string;
        case "integer":
          return integer;
        case "number":
          return number;
        case "date":
          return date;
        case "datetime":
          return datetime;
        default:
          throw new SQLAPIException("Type not supported");
      }
    }
  }
}
