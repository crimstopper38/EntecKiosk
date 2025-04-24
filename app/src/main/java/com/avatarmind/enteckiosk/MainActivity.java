package com.avatarmind.enteckiosk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends SpeechListeningActivity {

    private Robot myRobot;
    private Button generalQuestionsButton;
    private Button stemProgramsButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRobot = Robot.getInstance(this);

        // Initialize buttons
        generalQuestionsButton = (Button) findViewById(R.id.general_questions_button);
        stemProgramsButton = (Button) findViewById(R.id.STEM_programs_button);
        exitButton = (Button) findViewById(R.id.exit_button);

        // Set up button listeners
        setupButtonListeners();
        deliverWelcomeMessage();

        // Needed to give time for SpeechManager to properly show up as being used. If removed, speechtriggermanager
        // thread will activate because robot.isSpeaking() hasn't had time to register as being true
        // should be in a handler for final version
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("Robot", "Sending speech signal to server...");
        startSpeechListening(
                "http://192.168.0.115:8080/startSTT", // Replace with servers actual ip
                "http://192.168.0.115:8080/sttResult" // Same here
        );
    }

    private void setupButtonListeners() {
        // General Questions Button
        generalQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                openActivity(GeneralQuestions.class);
            }
        });

        // STEM Programs Button
        stemProgramsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                openActivity(StemPrograms.class);
            }
        });

        // exit button
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                finishAffinity();
            }
        });
    }

    private void deliverWelcomeMessage() {
        String welcomeMessage = getString(R.string.main_message);
        if (myRobot != null) {
            myRobot.speak(welcomeMessage);
        } else {
            Log.e("MainActivity", "Robot instance is null, cannot deliver welcome message.");
        }
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.equalsIgnoreCase("General Questions")) {
            openActivity(GeneralQuestions.class);
        } else if (recognizedText.equalsIgnoreCase("Programs")) {
            openActivity(StemPrograms.class);
        } else {
            Log.d("MainActivity", "Unrecognized speech: " + recognizedText);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myRobot != null) {
            myRobot.stopSpeaking();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String rePropmt = "Please select or speak an option";
        if (myRobot != null) {
            myRobot.speak(rePropmt);
        } else {
            Log.e("MainActivity", "Robot instance is null, cannot deliver welcome message.");
        }

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startSpeechListening(
                "http://192.168.0.115:8080/startSTT", // Replace with servers actual ip
                "http://192.168.0.115:8080/sttResult" // Same here
        );
    }
}
