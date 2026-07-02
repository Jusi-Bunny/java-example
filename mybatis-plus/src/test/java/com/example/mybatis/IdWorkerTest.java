package com.example.mybatis;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.junit.jupiter.api.Test;

public class IdWorkerTest {

    @Test
    public void testIdWorker() throws Exception {
        for (int i = 0; i < 1; i++) {
            System.out.println(IdWorker.getId());
            Thread.sleep(2000);
            // System.out.println(IdWorker.get32UUID());
            Thread.sleep(2000);
        }
    }
}
