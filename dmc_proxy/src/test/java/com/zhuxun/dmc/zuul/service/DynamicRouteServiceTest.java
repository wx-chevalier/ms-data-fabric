package com.zhuxun.dmc.zuul.service;

import com.zhuxun.dmc.zuul.Application;
import com.zhuxun.dmc.zuul.repository.model.DcRoute;
import com.zhuxun.dmc.zuul.web.rest.AbstractIntgretionTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 作用：
 *
 * <p>时间：2018/6/27 17:19
 *
 * <p>位置：com.zhuxun.dmc.zuul.service
 *
 * @author Yan - tao
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Sql(value = "classpath:db/test/cleanup_test_proxy.sql")
@Sql(value = "classpath:db/test/test_users.sql")
public class DynamicRouteServiceTest  extends AbstractIntgretionTest{

    @Autowired
    private DynamicRouteService dynamicRouteService;


    //检测到环境信息发生改变之后，开始将数据映射到DcRoute表中
    @Autowired
    private EnvironmentMonitorService environmentMonitorService;


    @Autowired
    private RefreshRouteService refreshRouteService;

    @Test
    @Transactional
    public void addRouteRule() {
        DcRoute dcRoute = new DcRoute();
        dcRoute.setId(UUID.randomUUID().toString());
        dcRoute.setEnvId(UUID.randomUUID().toString());
        dcRoute.setPath("http://localhsot:8080/apim/test.do");
        dcRoute.setProjectId(UUID.randomUUID().toString());
        dcRoute.setRetryable(false);
        dcRoute.setUrl("http://localhost:8080/api/some/" + UUID.randomUUID().toString());
        List<DcRoute> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dcRoute.setId(UUID.randomUUID().toString());
            list.add(dcRoute);
        }

        Integer integer = dynamicRouteService.addRouteRule(list);
        assertTrue(integer == list.size());
    }

    @Test
    public void getAllRouteRules() {
        List<DcRoute> routeList = dynamicRouteService.getAllRouteRules();
        assertNotNull(routeList);
    }

    @Test
    public void testRoute() throws Exception {
        List<DcRoute> dcRoutes = environmentMonitorService.addRouteRules();
        Long environmentCount = environmentMonitorService.getEnvironmentCount();
        Assert.assertNotNull(dcRoutes);
        Assert.assertEquals(dcRoutes.size(), environmentCount.intValue());
        refreshRouteService.refreshRoute();
        this.mockMvc.perform(get("/api/4a84d3dbe2a8e8a71f80368ea386ddac/s"))
                .andExpect(status().is5xxServerError());
    }

}
