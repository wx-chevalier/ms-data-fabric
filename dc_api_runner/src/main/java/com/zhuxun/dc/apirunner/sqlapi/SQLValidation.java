package com.zhuxun.dc.apirunner.sqlapi;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.Violation;
import com.alibaba.druid.wall.WallCheckResult;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.wall.spi.MySqlWallProvider;
import com.alibaba.druid.wall.spi.OracleWallProvider;
import com.alibaba.druid.wall.spi.SQLServerWallProvider;

public class SQLValidation {
  private final static WallConfig QUERY_ONLY_CONFIG = new WallConfig();
  private final static WallConfig RESTRICTED_CONFIG = new WallConfig();

  static {
    QUERY_ONLY_CONFIG.setSelectAllColumnAllow(false);
    QUERY_ONLY_CONFIG.setSelectIntoAllow(false);
    QUERY_ONLY_CONFIG.setDeleteAllow(false);
    QUERY_ONLY_CONFIG.setUpdateAllow(false);
    QUERY_ONLY_CONFIG.setInsertAllow(false);
    QUERY_ONLY_CONFIG.setReplaceAllow(false);
    QUERY_ONLY_CONFIG.setMergeAllow(false);
    QUERY_ONLY_CONFIG.setCallAllow(false);
    QUERY_ONLY_CONFIG.setCreateTableAllow(false);
    QUERY_ONLY_CONFIG.setAlterTableAllow(false);
    QUERY_ONLY_CONFIG.setDropTableAllow(false);
    QUERY_ONLY_CONFIG.setUseAllow(false);
    QUERY_ONLY_CONFIG.setDescribeAllow(false);
    QUERY_ONLY_CONFIG.setShowAllow(false);

    RESTRICTED_CONFIG.setSelectAllColumnAllow(false);
    RESTRICTED_CONFIG.setMergeAllow(false);
    RESTRICTED_CONFIG.setCallAllow(false);
    RESTRICTED_CONFIG.setCreateTableAllow(false);
    RESTRICTED_CONFIG.setAlterTableAllow(false);
    RESTRICTED_CONFIG.setDropTableAllow(false);
    RESTRICTED_CONFIG.setUseAllow(false);
    RESTRICTED_CONFIG.setDescribeAllow(false);
    RESTRICTED_CONFIG.setShowAllow(false);

    // 不允许多条语句
    QUERY_ONLY_CONFIG.setMultiStatementAllow(false);
    RESTRICTED_CONFIG.setMultiStatementAllow(false);
  }

  public void validate(String sql, String dialect) throws SQLValidationError {
    switch (dialect) {
      case JdbcConstants.MYSQL:
      case JdbcConstants.ORACLE:
      case JdbcConstants.SQL_SERVER:
        validateGeneralSQLDB(sql, dialect);
        break;
      case "presto":
        validatePresto(sql);
        break;
      default:
        throw new SQLValidationError("未知数据源类型: " + dialect);
    }
  }

  public void validateGeneralSQLDB(String sql, String dialect) throws SQLValidationError {
    WallProvider provider;
    switch (dialect) {
      case JdbcConstants.MYSQL:
        provider = new MySqlWallProvider(RESTRICTED_CONFIG);
        break;
      case JdbcConstants.ORACLE:
        provider = new OracleWallProvider(RESTRICTED_CONFIG);
        break;
      case JdbcConstants.SQL_SERVER:
        provider = new SQLServerWallProvider(RESTRICTED_CONFIG);
        break;
      default:
        throw new SQLValidationError("未知数据源类型: " + dialect);
    }
    checkWithWallProvider(provider, sql);
  }

  public void validatePresto(String sql) throws SQLValidationError {
    checkWithWallProvider(new MySqlWallProvider(RESTRICTED_CONFIG), sql);
  }

  private void checkWithWallProvider(WallProvider provider, String sql) throws SQLValidationError {
    WallCheckResult result = provider.check(sql);
    if (result.getViolations().size() != 0) {
      StringBuilder errBuilder = new StringBuilder();
      for (Violation violation : result.getViolations()) {
        errBuilder.append(violation.getMessage());
        errBuilder.append(';');
      }
      throw new SQLValidationError("不受允许的 SQL [" + sql + "]: " + errBuilder.toString());
    }
  }
}
