package com.avatarmind.enteckiosk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends SpeechListeningActivity {

    private Robot myRobot;
    private Button generalQuestionsButton;
    private Button campusInfoButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRobot = Robot.getInstance(this);

        // Initialize buttons
        generalQuestionsButton = (Button) findViewById(R.id.general_questions_button);
        campusInfoButton = (Button) findViewById(R.id.campus_info_button);
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
                Config.START_STT_URL,
                Config.STT_RESULT_URL
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
        campusInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                openActivity(CampusInformation.class);
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
        if (recognizedText.toLowerCase().contains("general") || recognizedText.toLowerCase().contains("questions")) {
            openActivity(GeneralQuestions.class);
        } else if (recognizedText.toLowerCase().contains("campus") || recognizedText.toLowerCase().contains("information"))  {
            openActivity(CampusInformation.class);
        } else if (recognizedText.toLowerCase().contains("exit")) {
            finishAffinity();
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
