package com.example.junit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AssertTest {

    @Test
    void testNotEquals() {
        int result = 1 + 1;

        assertNotEquals(3, result);
    }
}
