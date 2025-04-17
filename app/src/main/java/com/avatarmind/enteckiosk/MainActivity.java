package com.avatarmind.enteckiosk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends Activity {

    private static MainActivity instance;
    private Robot myRobot;
    private Button generalQuestionsButton;
    private Button stemProgramsButton;
    private Button exitButton;
    private SpeechRecognizerHelper speechHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);

        myRobot = Robot.getInstance(this);

        // Initialize buttons
        generalQuestionsButton = (Button) findViewById(R.id.general_questions_button);
        stemProgramsButton = (Button) findViewById(R.id.STEM_programs_button);
        exitButton = (Button) findViewById(R.id.exit_button);

        // Set up button listeners
        setupButtonListeners();
        deliverWelcomeMessage();
        SpeechTriggerManager.triggerSTTAndPoll(
                myRobot,
                "http://192.168.1.200:8080/startSTT", //replace with actual server address
                "http://192.168.1.200:5000/sttResult", //replace with server polling address
                new SpeechTriggerManager.SpeechResultListener() {
                    @Override
                    public void onResultReceived(final String recognizedText) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (recognizedText.equalsIgnoreCase("General Questions")) {
                                    openActivity(GeneralQuestions.class);
                                } else if (recognizedText.equalsIgnoreCase("STEM Programs")) {
                                    openActivity(StemPrograms.class);
                                } else {
                                    Log.d("Robot", "Unrecognized speech: " + recognizedText);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("SpeechTrigger", "Error: ", e);
                            }
                        });
                    }
                }
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

    public static void openActivity(Class<?> targetActivity) {
        if (instance != null) {
            Intent intent = new Intent(instance, targetActivity);
            instance.startActivity(intent);
        } else {
            Log.e("MainActivity", "Instance is null, cannot open activity.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myRobot != null) {
            myRobot.stopSpeaking();
        }
        if (speechHelper != null) {
            speechHelper.destroy();
        }
    }
}
