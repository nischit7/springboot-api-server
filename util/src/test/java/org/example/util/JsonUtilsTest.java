package org.example.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class JsonUtilsTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void convertToJson() {
        final Map<String, String> obj = new HashMap<>();
        obj.put("key", "value");
        assertThat(JsonUtils.convertToJson(obj), notNullValue());
    }

    @Test
    void convertFileToJsonString() {
        assertThat(JsonUtils.convertFileToJsonString("teamDet.json"), notNullValue());
    }
}
