package com.zhuxun.dc.apirunner.sqlapi;

import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Slf4j
public class VariableParser {
  public static final String VARIABLE_PREFIX = "___";

  /**
   * End of Input
   */
  static final int EOI = 0x1A;
  /**
   * 变量出现位置 -> 对应位置变量名
   */
  @Getter
  List<String> positionedVariables;
  /**
   * 当前解析的 SQL 文本
   */
  private String text;
  /**
   * 当前字符位置
   */
  private int pos;
  /**
   * 对应 {@link #pos} 位置的字符
   */
  private char ch;
  private StringBuilder preparedTextBuilder;
  private StringBuilder parsedSqlBuilder;

  public VariableParser(String text) {
    this.text = text;
  }

  /**
   * 替换括号定义的变量为 ? 的语句，用于构造 PreparedStatement
   */
  public String questionMarkedSql() {
    return preparedTextBuilder.toString();
  }

  /**
   * 替换括号定义的变量替换为 {@link #VARIABLE_PREFIX} 后的语句
   * <p>
   * 方便语法解析使用
   */
  public String parsedSql() {
    return parsedSqlBuilder.toString();
  }

  public VariableParser parse() throws SQLAPIException {
    this.reset();
    log.debug("Start parsing {}", this.text);

    while (this.ch != EOI) {
      switch (this.ch) {
        case '{':
          String var = this.parseVariable();
          this.positionedVariables.add(var);
          this.preparedTextBuilder.append('?');

          this.parsedSqlBuilder.append('@');
          this.parsedSqlBuilder.append(VARIABLE_PREFIX);
          this.parsedSqlBuilder.append(var);
          continue;
        case '}':
          throw new SQLAPIException("Unbalanced brace");
        default:
          this.preparedTextBuilder.append(this.ch);
          this.parsedSqlBuilder.append(this.ch);
          this.scanChar();
      }
    }

    log.debug("Parsed: {}", this.parsedSql());
    log.debug("Prepared: {}", this.questionMarkedSql());
    return this;
  }

  private String parseVariable() throws SQLAPIException {
    scanChar();
    StringBuilder builder = new StringBuilder();
    // 前置空白符
    while (Character.isWhitespace(ch)) {
      scanChar();
    }
    while (this.ch != '}' && !Character.isWhitespace(ch)) {
      builder.append(this.ch);
      scanChar();
    }
    // 后置空白符
    while (Character.isWhitespace(ch)) {
      scanChar();
    }
    if (this.ch != '}') {
      throw new SQLAPIException("Spaces is not allowed in variable name");
    }
    scanChar();
    return builder.toString();
  }

  /**
   * 重置/初始化解析所需状态变量
   */
  private final void reset() {
    this.pos = 0;
    this.ch = charAt(this.pos);
    this.preparedTextBuilder = new StringBuilder();
    this.parsedSqlBuilder = new StringBuilder();
    this.positionedVariables = new ArrayList<>();
  }

  private char charAt(int index) {
    if (index >= text.length()) {
      return EOI;
    }

    return text.charAt(index);
  }

  private void scanChar() {
    ch = charAt(++pos);
  }
}
