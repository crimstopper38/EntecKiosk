package com.avatarmind.enteckiosk;

import android.content.Intent;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CampusInformation extends SpeechListeningActivity {

    private Robot myRobot;
    private RobotMotion rMotion;

    private Button aboutCampusButton;
    private Button campusHighlightsButton;
    private Button quickLinksButton;
    private Button mapsDirectoryButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_information);

        myRobot = Robot.getInstance(this);
        rMotion = new RobotMotion();

        if (rMotion != null) {
            rMotion.doAction(RobotMotion.Action.WAVE);
            rMotion.emoji(RobotMotion.Emoji.SMILE);
        }

        aboutCampusButton = (Button) findViewById(R.id.about_campus_button);
        campusHighlightsButton = (Button) findViewById(R.id.campus_highlights_button);
        quickLinksButton = (Button) findViewById(R.id.quick_links_button);
        mapsDirectoryButton = (Button) findViewById(R.id.maps_directions_button);
        backButton = (Button) findViewById(R.id.campus_info_back_button);

        setupButtonListeners();

        try {
            // Speech performed by robot when view is opened
            if (myRobot != null) {
                String introText = "Welcome to the Campus Information section. Select an option to learn more!";
                myRobot.speak(introText);
            }

            // Action performed by robot when view is opened
            if (rMotion != null) {
                myRobot.doAction(Robot.WAVE);
                rMotion.emoji(RobotMotion.Emoji.SMILE);
            }
        } catch (Exception e) {
            Log.e("campus_information", "Error during initial actions: " + e.getMessage());
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                finish();
            }
        });

        aboutCampusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(CampusInformation.this, AboutCampus.class);
                startActivity(intent);
            }
        });

        campusHighlightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(CampusInformation.this, Highlights.class);
                startActivity(intent);
            }
        });

        quickLinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(CampusInformation.this, QuickLinks.class);
                startActivity(intent);
            }
        });

        mapsDirectoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(CampusInformation.this, Maps.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.toLowerCase().contains("about") || recognizedText.toLowerCase().contains("campus")) {
            openActivity(AboutCampus.class);
        } else if (recognizedText.toLowerCase().contains("highlights")) {
            openActivity(Highlights.class);
        } else if (recognizedText.toLowerCase().contains("quick") || recognizedText.toLowerCase().contains("links")) {
            openActivity(QuickLinks.class);
        } else if (recognizedText.toLowerCase().contains("maps") || recognizedText.toLowerCase().contains("directions")) {
            openActivity(Maps.class);
        } else if (recognizedText.toLowerCase().contains("back")) {
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
