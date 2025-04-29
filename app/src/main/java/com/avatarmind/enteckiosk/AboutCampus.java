package com.avatarmind.enteckiosk;

import android.content.Intent;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AboutCampus extends SpeechListeningActivity {

    private Robot myRobot;
    private RobotMotion rMotion;

    private Button buttonBack;
    private Button buttonCampusAdmin;
    private Button buttonEnTecPrograms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_about_campus);
        } catch (Exception e) {
            Log.e("AboutOurCampus", "Error loading layout: " + e.getMessage());
            finish();
            return;
        }

        // Get shared robot instance
        myRobot = Robot.getInstance(this);
        rMotion = new RobotMotion();

        // Initialize buttons
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonCampusAdmin = (Button) findViewById(R.id.campus_admin_button);
        buttonEnTecPrograms = (Button) findViewById(R.id.entec_programs_button);

        setupButtonListeners();

        try {
            // Speech performed by robot when view is opened
            if (myRobot != null) {
                String introText = "Welcome to the About Our Campus section. Select an option to learn more!";
                myRobot.speak(introText);
            }

            // Action performed by robot when view is opened
            if (rMotion != null) {
                myRobot.doAction(Robot.WAVE);
                rMotion.emoji(RobotMotion.Emoji.SMILE);
            }
        } catch (Exception e) {
            Log.e("AboutOurCampus", "Error during initial actions: " + e.getMessage());
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

    // Set up listeners
    private void setupButtonListeners() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                finish();
            }
        });

        buttonCampusAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(AboutCampus.this, CampusDirectory.class);
                startActivity(intent);
            }
        });

        buttonEnTecPrograms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(AboutCampus.this, StemPrograms.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.toLowerCase().contains("back")) {
            finish();
        } else if (recognizedText.toLowerCase().contains("administration") || recognizedText.toLowerCase().contains("directory")) {
            openActivity(CampusDirectory.class);
        } else if (recognizedText.toLowerCase().contains("entec") || recognizedText.toLowerCase().contains("programs")) {
            openActivity(StemPrograms.class);
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