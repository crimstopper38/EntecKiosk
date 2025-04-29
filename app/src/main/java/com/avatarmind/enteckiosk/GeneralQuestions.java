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
    private Button buttonAdmissions;
    private Button buttonEnrollment;
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
        buttonEnrollment = (Button) findViewById(R.id.enrollment_button);
        buttonAdmissions = (Button) findViewById(R.id.admissions_button);
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

        buttonAdvisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(GeneralQuestions.this, AdvisementInformation.class);
                startActivity(intent);
            }
        });

        buttonEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(GeneralQuestions.this, Enrollment.class);
                startActivity(intent);
            }
        });

        buttonAdmissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                Intent intent = new Intent(GeneralQuestions.this, Admissions.class);
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
        if (recognizedText.toLowerCase().contains("advisement") || recognizedText.toLowerCase().contains("information")) {
            openActivity(AdvisementInformation.class);
        } else if (recognizedText.toLowerCase().contains("enrollment")) {
            openActivity(Enrollment.class);
        } else if (recognizedText.toLowerCase().contains("admissions")) {
            openActivity(Admissions.class);
        } else if (recognizedText.toLowerCase().contains("financial") || recognizedText.toLowerCase().contains("aid")) {
            openActivity(FinancialAid.class);
        } else if (recognizedText.toLowerCase().contains("back")) {
            finish();
        } else {
            myRobot.speak("Sorry, I didn't understand . Please tap or speak an option");
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
