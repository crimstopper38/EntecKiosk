package com.avatarmind.enteckiosk;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpeechTriggerManager {

    public interface SpeechResultListener {
        void onResultReceived(String recognizedText);
        void onError(Exception e);
    }

    public static void triggerSTTAndPoll(final Robot robot, final String triggerUrl, final String resultUrl, final SpeechResultListener listener) {

        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Wait for robot to finish speaking
                    while (robot.isSpeaking()) {
                        Thread.sleep(1000);
                    }

                    // Trigger server to start STT
                    URL url = new URL(triggerUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.getResponseCode();  // Make the call
                    conn.disconnect();

                    // Poll the server for the STT result
                    boolean sttResultReceived = false;
                    String recognizedText = "";

                    while (!sttResultReceived) {
                        Thread.sleep(3000);
                        URL pollUrl = new URL(resultUrl);
                        HttpURLConnection pollConn = (HttpURLConnection) pollUrl.openConnection();
                        pollConn.setRequestMethod("GET");
                        pollConn.setConnectTimeout(5000);
                        pollConn.setReadTimeout(5000);

                        if (pollConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(pollConn.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            JSONObject json = new JSONObject(response.toString());
                            if (json.getString("status").equals("ready")) {
                                recognizedText = json.getString("text");
                                sttResultReceived = true;
                            }
                        }
                        pollConn.disconnect();
                    }

                    listener.onResultReceived(recognizedText);

                } catch (Exception e) {
                    listener.onError(e);
                }
            }
        });
        workerThread.start();
    }
}