package com.zhuxun.dmc.sqlapi;

import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Various unsorted utility function */
public class Utils {
  public static final String VARIABLE_DEFINITION_REGEXP =
      "^\\s*\\{\\s*(?<name>[a-zA-Z_]+\\w*)\\s*(:\\s*(?<type>\\w+)\\s*(=\\s*(?<default>\\S+.*\\S+))?)?\\s*\\}\\s*$";
  public static final Pattern VARIABLE_DEFINITION_PATTERN =
      Pattern.compile(VARIABLE_DEFINITION_REGEXP);
  public static final String VARIABLE_INTERNAL_PREFIX = "lc__";

  public static String decorateVar(String s) {
    if (s == null) {
      return null;
    } else {
      return String.format("%s%s", VARIABLE_INTERNAL_PREFIX, s);
    }
  }

  public static String deDecorateVar(String s) {
    if (s == null || !s.startsWith(VARIABLE_INTERNAL_PREFIX)) {
      return s;
    } else {
      return s.substring(VARIABLE_INTERNAL_PREFIX.length());
    }
  }

  public static Set<String> findDecoratedVars(String sql) {
    Set<String> results = new HashSet<>();
    Pattern pattern = Pattern.compile("lc__\\w+\\w*");
    Matcher matcher = pattern.matcher(sql);
    while (matcher.find()) {
      results.add(deDecorateVar(matcher.group()));
    }
    return results;
  }

  public static Variable parseVar(String s) {
    Matcher matcher = VARIABLE_DEFINITION_PATTERN.matcher(s);
    if (matcher.find()) {
      return new Variable()
          .setName(matcher.group("name"))
          .setType(matcher.group("type"))
          .setDefaultValue(matcher.group("default"));
    } else {
      return null;
    }
  }

  public static String prestoQuoteIdentifier(String id) {
    return String.format("\"%s\"", id);
  }

  public static @Nullable SQLBinaryOperator toSQLBinaryOperator(String s) {
    if (s == null) {
      return null;
    }
    for (SQLBinaryOperator op : SQLBinaryOperator.values()) {
      if (op.name.toLowerCase().equals(s.toLowerCase())) {
        return op;
      }
    }
    return null;
  }

  @Accessors(chain = true)
  @Data
  public static class Variable {
    String name;
    String type;
    String defaultValue;

    public boolean isSame(Variable var) {
      if (this == var) return true;
      return Objects.equal(name, var.name) && Objects.equal(type, var.type);
    }

    public String repr() {
      if (type == null) {
        return String.format("{%s}", name);
      } else if (defaultValue == null) {
        return String.format("{%s:%s}", name, type);
      } else {
        return String.format("{%s:%s=%s}", name, type, defaultValue);
      }
    }
  }
}
