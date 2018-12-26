package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.datasource.Schema;
import com.zhuxun.dmc.apim.domain.datasource.Table;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.service.impl.DatasourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/datasource")
@Api(description = "数据源、库、表、字段相关管理接口")
public class DataSourceResource extends AbstractResource {
  private DatasourceService datasourceService;

  public DataSourceResource(TokenProvider tokenProvider,
                            DatasourceService datasourceService) {
    super(tokenProvider);
    this.datasourceService = datasourceService;
  }

  @ApiOperation(value = "返回用户可用的数据库列表")
  @GetMapping(value = "/database", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Collection<Schema> getSchemaList() {
    return datasourceService.getSchemaList(currentUserOrThrow().getId());
  }

  @ApiOperation(value = "返回指定数据库的表信息")
  @GetMapping(value = "/{datasourceName}/database/{databaseName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Collection<Table> getTableList(
      @PathVariable("datasourceName") String datasourceName,
      @PathVariable("databaseName") String databaseName) {
    return datasourceService.getTableList(databaseName, datasourceName);
  }
}
