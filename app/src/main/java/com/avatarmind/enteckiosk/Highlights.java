package com.avatarmind.enteckiosk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Highlights extends SpeechListeningActivity {
    private Robot myRobot;
    private RobotMotion rMotion;
    private Button buttonBack;
    private Button justiceButton;
    private Button scienceButton;
    private Button designButton;
    private Button sculptureButton;
    private Button contEdButton;
    private Button meekButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);

        // Get shared robot instance
        myRobot = Robot.getInstance(this);
        rMotion = new RobotMotion();

        // Initialize buttons
        buttonBack = (Button) findViewById(R.id.highlights_back_button);
        justiceButton = (Button) findViewById(R.id.justice_button);
        scienceButton = (Button) findViewById(R.id.science_button);
        designButton = (Button) findViewById(R.id.entertainment_button);
        sculptureButton = (Button) findViewById(R.id.sculpture_button);
        contEdButton = (Button) findViewById(R.id.continuing_education_button);
        meekButton = (Button) findViewById(R.id.meek_button);


        setupButtonListeners();

        try {
            // Speech performed by robot when view is opened
            if (myRobot != null) {
                String introText = "Miami Dade College's North Campus is home to nationally recognized schools and programs. Tap or speak any highlight to learn more about what makes each one unique.";
                myRobot.speak(introText);
            }

        } catch (Exception e) {
            Log.e("CampusHighlights", "Error during initial actions: " + e.getMessage());
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

        justiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(Highlights.this, Justice.class);
                startActivity(intent);
            }
        });

        scienceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(Highlights.this, Science.class);
                startActivity(intent);
            }
        });

        designButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(Highlights.this, Design.class);
                startActivity(intent);
            }
        });

        sculptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Highlights.this, Sculpture.class);
                startActivity(intent);
            }
        });

        contEdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Highlights.this, ContinuingEducation.class);
                startActivity(intent);
            }
        });

        meekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Highlights.this, Meek.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.toLowerCase().contains("back") || recognizedText.toLowerCase().contains("return")) {
            finish();
        } else if (recognizedText.toLowerCase().contains("justice")) {
            openActivity(Justice.class);
        } else if (recognizedText.toLowerCase().contains("science")) {
            openActivity(Science.class);
        } else if (recognizedText.toLowerCase().contains("entertainment") || recognizedText.toLowerCase().contains("design")) {
            openActivity(Design.class);
        } else if (recognizedText.toLowerCase().contains("sculpture") || recognizedText.toLowerCase().contains("park")) {
            openActivity(Sculpture.class);
        } else if (recognizedText.toLowerCase().contains("continuing") || recognizedText.toLowerCase().contains("education")) {
            openActivity(ContinuingEducation.class);
        } else if (recognizedText.toLowerCase().contains("meek")) {
            openActivity(Meek.class);
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