package com.zhuxun.dc.apirunner.zuulapi;

import com.zhuxun.dc.apirunner.controller.BaseResource;
import com.zhuxun.dc.apirunner.entity.vo.VResult;
import com.zhuxun.dc.apirunner.entity.vo.VZuulRoute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 创建接口,用于测试更新路由表
 *
 * @author tao
 */
@RestController
@Api(description = "此接口由开发者调用不作为接口使用，正式部署前请删除此接口")
public class ZuulRouteController  extends BaseResource {

  @Autowired RefreshRouteService refreshRouteService;

  @ApiOperation(value = "更新接口路由信息",notes = "**此接口由开发者调用不作为接口使用，正式部署前请删除此接口**")
  @GetMapping("/refreshRoute")
  public VResult refreshRoute() {
    //refreshRouteService.refreshRoute();
    return new VResult("成功刷新映射关系");
  }

  @Autowired
  CustomZuulHandleMapper handleMapper;

  @GetMapping("/watchNowRoute")
  @ApiOperation(value = "查看当前路由表的数据信息",notes = "**此接口由开发者调用不作为接口使用，正式部署前请删除此接口**")
  public List<VZuulRoute> watchNowRoute() {
    List<VZuulRoute> allRoute = sqlApiAnalyzerService.getAllRoute();
    Map<String, Object> handlerMap = handleMapper.getHandlerMap();
    return allRoute;
  }
}
