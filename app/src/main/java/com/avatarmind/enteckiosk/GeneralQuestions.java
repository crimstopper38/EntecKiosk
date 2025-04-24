package com.avatarmind.enteckiosk;

import android.content.Intent;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GeneralQuestions extends SpeechListeningActivity {
    private Robot myRobot;
    private RobotMotion rMotion;
    private Button buttonBack;
    private Button buttonAdvisement;
    private Button buttonLearningOptions;
    private Button buttonScholarships;
    private Button buttonFinancialAid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_questions);

        //get shared robot instance
        myRobot = Robot.getInstance(this);
        rMotion = new RobotMotion();

        // Initialize buttons
        buttonBack = (Button) findViewById(R.id.back_button);
        buttonAdvisement = (Button) findViewById(R.id.advisement_info_button);
        buttonLearningOptions = (Button) findViewById(R.id.learning_options_button);
        buttonScholarships = (Button) findViewById(R.id.scholarships_button);
        buttonFinancialAid = (Button) findViewById(R.id.financial_aid_button);

        setupButtonListeners();

        try {

            //speech performed by robot when view is opened
            if (myRobot != null) {
                //string that holds intro speech
                String introText = getString(R.string.general_questions_intro);
                myRobot.speak(introText);
            }

            //action performed by robot when view is opened
            if (rMotion != null) {
                myRobot.doAction(Robot.WAVE);
                rMotion.emoji(RobotMotion.Emoji.SHY);
            }

        } catch (Exception e) {
            Log.e("general_questions", "Error during initial actions: " + e.getMessage());
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
    // Set up listeners
    private void setupButtonListeners() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                finish();
            }
        });

        buttonAdvisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(GeneralQuestions.this, AdvisementInformation.class);
                startActivity(intent);
            }
        });

        buttonLearningOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(GeneralQuestions.this, LearningOptions.class);
                startActivity(intent);
            }
        });

        buttonScholarships.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(GeneralQuestions.this, Scholarships.class);
                startActivity(intent);
            }
        });

        buttonFinancialAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(GeneralQuestions.this, FinancialAid.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.equalsIgnoreCase("Advisement Information")) {
            openActivity(AdvisementInformation.class);
        } else if (recognizedText.equalsIgnoreCase("Learning Options")) {
            openActivity(LearningOptions.class);
        } else if (recognizedText.equalsIgnoreCase("Scholarships")) {
            openActivity(Scholarships.class);
        } else if (recognizedText.equalsIgnoreCase("Financial Aid")) {
            openActivity(FinancialAid.class);
        } else if (recognizedText.equalsIgnoreCase("Back")) {
            finish();
        } else {
            Log.d("GeneralQuestions", "Unrecognized speech: " + recognizedText);
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
