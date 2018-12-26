package com.zhuxun.dmc.zuul.service;

import com.zhuxun.dmc.zuul.repository.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tao
 */
public abstract class AbstractService {

    @Autowired
    protected DcRouteMapper routeMapper;

    @Autowired
    protected DcEnvironmentMapper environmentMapper;

    @Autowired
    protected DcApiStatisticsMapper apiStatisticsMapper;

    @Autowired
    protected DcApiMapper apiMapper;

    @Autowired
    protected DcApiParamMapper apiParamMapper;

    @Autowired
    protected DcApiResponseMapper apiResponseMapper;

    @Autowired
    protected DcAccessTokenMapper accessTokenMapper;
}
