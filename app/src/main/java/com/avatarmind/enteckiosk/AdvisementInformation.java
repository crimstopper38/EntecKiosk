package com.avatarmind.enteckiosk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AdvisementInformation extends SpeechListeningActivity {

    private Robot myRobot;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisement_information);

        //get shared robot instance
        myRobot = Robot.getInstance(this);

        //back button functionality
        buttonBack = (Button) findViewById(R.id.advisement_back_button);

        setupButtonListeners();

        try {

            //speech performed by robot when view is opened
            if (myRobot != null) {
                //string that holds intro speech
                String introText = getString(R.string.advisement_info_message);
                myRobot.speak(introText);
            }

        } catch (Exception e) {
            Log.e("Learning Opportunities", "Error during initial actions: " + e.getMessage());
        }

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
        if (recognizedText.equalsIgnoreCase("Back")) {
            finish();
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
}
