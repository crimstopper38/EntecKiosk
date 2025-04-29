package com.avatarmind.enteckiosk;

import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class Justice extends SpeechListeningActivity {
    private Robot myRobot;
    private WebView webView;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justice);

        // Get shared robot instance
        myRobot = Robot.getInstance(this);

        // Initialize WebView
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.mdc.edu/justice/");

        // Initialize buttons
        buttonBack = (Button) findViewById(R.id.justice_back_button);

        setupButtonListeners();

        try {
            // Robot introduction
            if (myRobot != null) {
                String introText = "You are viewing the School of Justice information. " +
                        "Use the back button when you're finished.";
                myRobot.speak(introText);
            }

        } catch (Exception e) {
            Log.e("JusticeWebActivity", "Robot error: " + e.getMessage());
        }

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startSpeechListening(Config.START_STT_URL, Config.STT_RESULT_URL);
    }

    private void setupButtonListeners() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                finish();
            }
        });
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.toLowerCase().contains("back")) {
            finish();
        } else {
            myRobot.speak("Sorry, I didn't understand. Please tap or speak an option");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startSpeechListening(Config.START_STT_URL, Config.STT_RESULT_URL);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myRobot != null) {
            myRobot.stopSpeaking();
        }
    }
}