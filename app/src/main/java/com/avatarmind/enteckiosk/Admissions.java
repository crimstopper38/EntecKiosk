package com.avatarmind.enteckiosk;

import android.content.Intent;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Admissions extends SpeechListeningActivity {

    private Robot myRobot;
    private RobotMotion rMotion;
    private Button buttonBack;
    private Button buttonFindLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admissions);

        // Get shared robot instance
        myRobot = Robot.getInstance(this);
        rMotion = new RobotMotion();

        // Initialize buttons
        buttonBack = (Button) findViewById(R.id.admissions_back_button);
        buttonFindLocation = (Button) findViewById(R.id.find_admissions_button);

        setupButtonListeners();

        try {
            // Speech performed by robot when view is opened
            if (myRobot != null) {
                String introText = "Welcome to Admissions information. How can I help you today?";
                myRobot.speak(introText);
            }

            // Action performed by robot when view is opened
            if (rMotion != null) {
                myRobot.doAction(Robot.WAVE);
                rMotion.emoji(RobotMotion.Emoji.SMILE);
            }
        } catch (Exception e) {
            Log.e("AdmissionsActivity", "Error during initial actions: " + e.getMessage());
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

        buttonFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(Admissions.this, AdmissionsLocation.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.toLowerCase().contains("back") || recognizedText.toLowerCase().contains("return")) {
            finish();
        } else if (recognizedText.toLowerCase().contains("find") || recognizedText.toLowerCase().contains("location")) {
            openActivity(AdmissionsLocation.class);
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