package com.avatarmind.enteckiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.view.View;
import android.widget.Button;

public class StemPrograms extends Activity {

    //initialize robot singleton and robot motion
    private Robot myRobot;
    private RobotMotion rMotion;
    private Button buttonBack;
    private Button buttonAI;
    private Button buttonSoftwareEngineering;
    private Button buttonDataAnalytics;
    private Button buttonCybersecurity;
    private Button buttonNetworking;
    private Button buttonElectricalEngineering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stem_programs);

        //get shared robot instance
        myRobot = Robot.getInstance(this);

        //initiate robot motion
        rMotion = new RobotMotion();

        String stemProgramIntro = getString(R.string.stem_message);
        rMotion.doAction(RobotMotion.Action.AKIMBO);
        rMotion.emoji(RobotMotion.Emoji.SMILE);
        myRobot.speak(stemProgramIntro);

        buttonBack = (Button) findViewById(R.id.button_back);
        buttonAI =(Button) findViewById(R.id.button_ai);
        buttonSoftwareEngineering = (Button) findViewById(R.id.button_software_engineering);
        buttonDataAnalytics = (Button) findViewById(R.id.button_data_analytics);
        buttonCybersecurity = (Button) findViewById(R.id.button_cybersecurity);
        buttonNetworking = (Button) findViewById(R.id.button_networking);
        buttonElectricalEngineering = (Button) findViewById(R.id.button_electrical_engineering);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle AI button click
                //Intent intent = new Intent(StemProgram.this, ArtificialIntelligenceActivity.class);
                //startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonSoftwareEngineering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Software Engineering button click
                //Intent intent = new Intent(StemProgram.this, SoftwareEngineeringActivity.class);
                //startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonDataAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Data Analytics button click
                //Intent intent = new Intent(StemProgram.this, DataAnalyticsActivity.class);
                //startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonCybersecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Cybersecurity button click
                //Intent intent = new Intent(StemProgram.this, CybersecurityActivity.class);
                //startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonNetworking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Networking button click
                //Intent intent = new Intent(StemProgram.this, NetworkingActivity.class);
                //startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonElectricalEngineering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Electrical Engineering button click
                //Intent intent = new Intent(StemProgram.this, ElectricalEngineeringActivity.class);
                //startActivity(intent);
                myRobot.stopSpeaking();
            }
        });
    }

    private void startNewActivity(Class<?> targetActivity) {
        Intent intent = new Intent(StemPrograms.this, targetActivity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myRobot != null) {
            myRobot.stopSpeaking();
        }
    }
}
