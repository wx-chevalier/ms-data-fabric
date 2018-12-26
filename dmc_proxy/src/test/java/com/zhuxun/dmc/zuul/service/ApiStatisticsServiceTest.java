package com.zhuxun.dmc.zuul.service;

import com.zhuxun.dmc.zuul.Application;
import com.zhuxun.dmc.zuul.repository.model.DcApiStatistics;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


/**
 * 作用：
 *
 * <p>时间：2018/6/27 17:02
 *
 * <p>位置：com.zhuxun.dmc.zuul.service
 *
 * @author Yan - tao
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ApiStatisticsServiceTest {

  @Autowired
  private ApiStatisticsService apiStatisticsService;

  @Test
  @Transactional
  public void addApiStatisticsRecord() {
    DcApiStatistics apiStatistics = new DcApiStatistics();
    apiStatistics.setId(UUID.randomUUID().toString());
    apiStatistics.setAccessTime(new Date());
    apiStatistics.setApiId("1234567890");
    apiStatistics.setApiPath("http://localhost");
    apiStatistics.setProxyPath("http://www.baidu.com/?s=123&wd=123");
    apiStatistics.setConsumeTime(1200);
    apiStatistics.setProxyMethod("POST");
    apiStatistics.setStatusCode(200);
    apiStatistics.setUserId(UUID.randomUUID().toString());
    Integer integer = apiStatisticsService.addApiStatisticsRecord(apiStatistics);
    Assert.assertTrue(integer == 1);
  }
}
