package com.bazaarvoice.zookeeper.dropwizard;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ZooKeeperConfigurationTest {
    private static final ObjectMapper JSON = new MappingJsonFactory()
            .getCodec()
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    @Test
    public void testJsonDefaults() throws IOException {
        ZooKeeperConfiguration config = fromJson("{'connectString':'prod-zk-1:12345'}");

        assertEquals("prod-zk-1:12345", config.getConnectString());
        assertTrue(config.getRetryPolicy() instanceof com.netflix.curator.retry.BoundedExponentialBackoffRetry);
    }

    @Test
    public void testJsonConnectString() throws IOException {
        ZooKeeperConfiguration config = fromJson("{'connectString':'prod-zk-1:12345'}");
        assertEquals("prod-zk-1:12345", config.getConnectString());
    }

    @Test
    public void testJsonBoundedExponentialBackoffRetry() throws IOException {
        ZooKeeperConfiguration config = fromJson("{'retry':{'baseSleepTimeMs':1,'maxSleepTimeMs':10,'maxAttempts':3}}");
        assertTrue(config.getRetryPolicy() instanceof com.netflix.curator.retry.BoundedExponentialBackoffRetry);
    }

    @Test
    public void testNamespace() throws IOException {
        ZooKeeperConfiguration config = fromJson("{'namespace':'/global'}");
        assertEquals("/global", config.getNamespace());
    }

    private ZooKeeperConfiguration fromJson(String json) throws IOException {
        return JSON.readValue(json, ZooKeeperConfiguration.class);
    }
}
