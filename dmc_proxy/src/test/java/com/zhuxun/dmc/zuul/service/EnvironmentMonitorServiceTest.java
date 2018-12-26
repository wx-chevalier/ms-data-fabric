package com.zhuxun.dmc.zuul.service;

import com.zhuxun.dmc.zuul.Application;
import com.zhuxun.dmc.zuul.repository.model.DcEnvironment;
import com.zhuxun.dmc.zuul.repository.model.DcRoute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * 作用：
 *
 * <p>时间：2018/6/27 17:12
 *
 * <p>位置：com.zhuxun.dmc.zuul.service
 *
 * @author Yan - tao
 */

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class EnvironmentMonitorServiceTest {

  @Autowired
  private EnvironmentMonitorService environmentMonitorService;

  @Test
  public void testGetEnvironmentList() {
    List<DcEnvironment> list = environmentMonitorService.getEnvironmentList();
    assertTrue(list != null);
  }

  @Test
  public void testGetEnvironmentCount() {
    List<DcEnvironment> list = environmentMonitorService.getEnvironmentList();
    Long count = environmentMonitorService.getEnvironmentCount();
    assertTrue(list.size() == count);
  }

  @Test
  public void testGetRouteRoules() throws IOException {
    List<DcRoute> routeRoules = environmentMonitorService.addRouteRules();
  }
}
