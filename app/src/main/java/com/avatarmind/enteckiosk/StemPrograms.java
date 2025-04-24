package com.avatarmind.enteckiosk;

import android.content.Intent;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StemPrograms extends SpeechListeningActivity {

    //initialize robot singleton and robot motion
    private Robot myRobot;
    private RobotMotion rMotion;
    private Button buttonBack;
    private Button buttonAI;
    private Button buttonSoftwareEngineering;
    private Button buttonDataAnalytics;
    private Button buttonCyberSecurity;
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
        rMotion.doAction(RobotMotion.Action.AKIMBO);
        rMotion.emoji(RobotMotion.Emoji.SMILE);

        buttonBack = (Button) findViewById(R.id.button_back);
        buttonAI =(Button) findViewById(R.id.button_ai);
        buttonSoftwareEngineering = (Button) findViewById(R.id.button_software_engineering);
        buttonDataAnalytics = (Button) findViewById(R.id.button_data_analytics);
        buttonCyberSecurity = (Button) findViewById(R.id.button_cybersecurity);
        buttonNetworking = (Button) findViewById(R.id.button_networking);
        buttonElectricalEngineering = (Button) findViewById(R.id.button_electrical_engineering);

        setupButtonListeners();

        try {

            //speech performed by robot when view is opened
            if (myRobot != null) {
                //string that holds intro speech
                String stemProgramIntro = getString(R.string.stem_message);
                myRobot.speak(stemProgramIntro);
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
                Intent intent = new Intent(StemPrograms.this, ArtificialIntelligence.class);
                startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonSoftwareEngineering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Software Engineering button click
                Intent intent = new Intent(StemPrograms.this, SoftwareEngineering.class);
                startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonDataAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Data Analytics button click
                Intent intent = new Intent(StemPrograms.this, DataAnalytics.class);
                startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonCyberSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Cybersecurity button click
                Intent intent = new Intent(StemPrograms.this, CyberSecurity.class);
                startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonNetworking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Networking button click
                Intent intent = new Intent(StemPrograms.this, Networking.class);
                startActivity(intent);
                myRobot.stopSpeaking();
            }
        });

        buttonElectricalEngineering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Electrical Engineering button click
                Intent intent = new Intent(StemPrograms.this, ElectricalEngineering.class);
                startActivity(intent);
                myRobot.stopSpeaking();
            }
        });
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.equalsIgnoreCase("Back")) {
            finish();
        } else if (recognizedText.equalsIgnoreCase("Artificial Intelligence")) {
            openActivity(ArtificialIntelligence.class);
        } else if (recognizedText.equalsIgnoreCase("Software Engineering")) {
            openActivity(SoftwareEngineering.class);
        } else if (recognizedText.equalsIgnoreCase("Data Analytics")) {
            openActivity(DataAnalytics.class);
        } else if (recognizedText.equalsIgnoreCase("Cyber Security")) {
            openActivity(CyberSecurity.class);
        } else if (recognizedText.equalsIgnoreCase("Networking")) {
            openActivity(Networking.class);
        } else if (recognizedText.equalsIgnoreCase("Electrical Engineering")) {
            openActivity(ElectricalEngineering.class);
        } else {
            Log.d("StemPrograms", "Unrecognized speech: " + recognizedText);
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
