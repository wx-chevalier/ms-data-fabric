package com.zhuxun.dmc.zuul.domain.token;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class VTokenTest {

    @Test
    public void of() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2N2U0NDUxYy1kYzNhLTQ3OGQtOTkxMi0yZmVjNmVkYjU1YjMiLCJBUElfSUQiOiI1NTU5YzljNC05MWMwLTRiNTUtOTViNi1iNTk3NWFhNWNlOTgiLCJFTlZfS0VZIjoiNDQ5OWY2YjMtZmExNC00OWEyLTk2NDAtYzU5OGMxNzcyNWFhIiwiZXhwIjoxNTMzMzAxNTk0fQ.rHh-ooVLskdAdRPMbWk8HSpDlGDgeyNWFpUwBY0sZUHTmKam94j4nWGs-zCC8sP1TiM-iaAQUOd0fGgdl2Wd-Q";
        try {
            VToken.of(token);
        } catch (ExpiredJwtException e) {
            log.info("token解析成功,但是已经过期");
        }
    }
}