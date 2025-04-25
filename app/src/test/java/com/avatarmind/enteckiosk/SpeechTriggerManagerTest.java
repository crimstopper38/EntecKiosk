package com.avatarmind.enteckiosk;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.json.JSONObject;

public class SpeechTriggerManagerTest {

    private Robot mockRobot;
    private SpeechTriggerManager.SpeechResultListener mockListener;

    @Before
    public void setUp() {
        mockRobot = mock(Robot.class);
        mockListener = mock(SpeechTriggerManager.SpeechResultListener.class);
    }

    @Test
    public void testRobotIsSpeakingDelaysStart() throws Exception {
        when(mockRobot.isSpeaking()).thenReturn(true, true, false);

        TestHttpServer server = new TestHttpServer();
        server.enqueueStartSTT(); // mock /startSTT
        server.enqueueResult("{\"status\":\"ready\",\"text\":\"General Questions\"}");

        final CountDownLatch latch = new CountDownLatch(1);

        SpeechTriggerManager.triggerSTTAndPoll(
                mockRobot,
                server.getStartSTTUrl(),
                server.getResultUrl(),
                new SpeechTriggerManager.SpeechResultListener() {
                    @Override
                    public void onResultReceived(String recognizedText) {
                        assertEquals("General Questions", recognizedText);
                        latch.countDown();
                    }

                    @Override
                    public void onError(Exception e) {
                        fail("Should not hit error path");
                    }
                }
        );

        assertTrue("Timed out waiting for result", latch.await(5, TimeUnit.SECONDS));
        server.shutdown();
    }

    @Test
    public void testErrorOnServerFailure() throws Exception {
        when(mockRobot.isSpeaking()).thenReturn(false);

        final CountDownLatch latch = new CountDownLatch(1);

        SpeechTriggerManager.triggerSTTAndPoll(
                mockRobot,
                "http://localhost:9999/fail",  // unreachable port
                "http://localhost:9999/fail",
                new SpeechTriggerManager.SpeechResultListener() {
                    @Override
                    public void onResultReceived(String recognizedText) {
                        fail("Should not succeed when connection fails");
                    }

                    @Override
                    public void onError(Exception e) {
                        assertNotNull(e);
                        latch.countDown();
                    }
                }
        );

        assertTrue("Error not triggered", latch.await(3, TimeUnit.SECONDS));
    }
}
