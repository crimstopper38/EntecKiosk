package com.avatarmind.enteckiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GeneralQuestions extends Activity {
    private Robot myRobot;
    private RobotMotion rMotion;
    private SpeechRecognizerHelper speechHelper;
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

        // Initialize buttons
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonAdvisement = (Button) findViewById(R.id.advisement_info_button);
        buttonLearningOptions = (Button) findViewById(R.id.learning_options_button);
        buttonScholarships = (Button) findViewById(R.id.scholarships_button);
        buttonFinancialAid = (Button) findViewById(R.id.financial_aid_button);

        setupButtonListeners();


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
                // Intent intent = new Intent(GeneralQuestions.this, AdvisementInformation.class);
                // startActivity(intent);
            }
        });

        buttonLearningOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                // Intent intent = new Intent(GeneralQuestions.this, LearningOptions.class);
                // startActivity(intent);
            }
        });

        buttonScholarships.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                // Intent intent = new Intent(GeneralQuestions.this, ScholarshipsActivity.class);
                // startActivity(intent);
            }
        });

        buttonFinancialAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                // Intent intent = new Intent(GeneralQuestions.this, AdvisementInformation.class);
                // startActivity(intent);
            }
        });
    }

    //
    private void startNewActivity(Class<?> targetActivity) {
        Intent intent = new Intent(GeneralQuestions.this, targetActivity);
        startActivity(intent);
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
