package com.avatarmind.enteckiosk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CampusDirectory extends SpeechListeningActivity {
    private Robot myRobot;
    private RobotMotion rMotion;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_directory);

        // Get shared robot instance
        myRobot = Robot.getInstance(this);
        rMotion = new RobotMotion();

        // Initialize buttons
        buttonBack = (Button) findViewById(R.id.directory_back_button);

        setupButtonListeners();

        try {
            // Speech performed by robot when view is opened
            if (myRobot != null) {
                String introText = "Welcome to the Miami Dade College Campus Directory. " +
                        "Here you can find contact information for various departments. " +
                        "Tap on any department to visit its webpage for more details.";
                myRobot.speak(introText);
            }

        } catch (Exception e) {
            Log.e("CampusDirectory", "Error during initial actions: " + e.getMessage());
        }

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("Robot", "Sending speech signal to server...");
        startSpeechListening(
                Config.START_STT_URL,
                Config.STT_RESULT_URL
        );
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
        if (recognizedText.toLowerCase().contains("back") || recognizedText.toLowerCase().contains("return")) {
            finish();
        } else {
            myRobot.speak("Sorry, I didn't understand. Please tap or speak a department name");
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