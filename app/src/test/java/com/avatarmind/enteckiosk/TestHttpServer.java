package com.avatarmind.enteckiosk;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class TestHttpServer {

    private final MockWebServer server;

    public TestHttpServer() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    public void enqueueStartSTT() {
        server.enqueue(new MockResponse().setResponseCode(200));
    }

    public void enqueueResult(String json) {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(json));
    }

    public String getStartSTTUrl() {
        return server.url("/startSTT").toString();
    }

    public String getResultUrl() {
        return server.url("/sttResult").toString();
    }

    public void shutdown() throws Exception {
        server.shutdown();
    }
}
