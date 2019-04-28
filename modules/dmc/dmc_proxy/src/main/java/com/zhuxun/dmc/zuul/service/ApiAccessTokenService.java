package com.zhuxun.dmc.zuul.service;

import com.zhuxun.dmc.zuul.domain.exception.TokenException;
import com.zhuxun.dmc.zuul.repository.model.DcAccessToken;
import com.zhuxun.dmc.zuul.repository.model.DcAccessTokenExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * ApiAccessToken 用于校验代理的token中是否存在
 */
@Service
@Slf4j
public class ApiAccessTokenService extends AbstractService {

    @Transactional(readOnly = true)
    public boolean tokenExist(String token) {
        DcAccessTokenExample example = new DcAccessTokenExample();
        example.createCriteria().andAccessTokenEqualTo(token);
        List<DcAccessToken> tokens = accessTokenMapper.selectByExample(example);
        return tokens.size() != 0;
    }

}
