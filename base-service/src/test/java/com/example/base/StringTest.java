package com.example.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class StringTest {

    @Test
    public void test() {
        String str = "刘治港 370283199706292014.wav";
        log.info(str.substring(0, str.lastIndexOf(".")));
    }

    @Test
    public void getException() {
        try {
            throw new Exception("异常");
        } catch (Exception e) {
            log.error("出异常啦！");
            log.error("异常信息:{}", e.getMessage());
        }
    }
}
