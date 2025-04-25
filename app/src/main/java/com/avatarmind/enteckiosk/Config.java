package com.avatarmind.enteckiosk;

public class Config {
    // Change this one value only when IP changes
    public static final String SERVER_IP = "192.168.1.218";
    public static final String SERVER_PORT = "8080";

    // Full endpoint URLs
    public static final String START_STT_URL = "http://" + SERVER_IP + ":" + SERVER_PORT + "/startSTT";
    public static final String STT_RESULT_URL = "http://" + SERVER_IP + ":" + SERVER_PORT + "/sttResult";
}
