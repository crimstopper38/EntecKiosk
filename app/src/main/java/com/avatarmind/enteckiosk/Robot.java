package com.avatarmind.enteckiosk;

import android.content.Context;
import android.robot.speech.SpeechManager;
import android.robot.motion.RobotMotion;
import android.robot.speech.nlu.SpeechUnderstander;
import android.util.Log;

public class Robot {

    private static Robot instance;
    private SpeechManager mSpeechManager;
    private RobotMotion mRobotMotion;
    private Context context;
    public static final int WAVE = RobotMotion.Action.WAVE;

    public Robot(Context context) {
        this.context = context.getApplicationContext();
        try {
            mSpeechManager = (SpeechManager) context.getSystemService("speech");
            if (mSpeechManager == null) {
                Log.e("Robot", "SpeechManager service not available.");
            }
        } catch (Exception e) {
            Log.e("Robot", "Error initializing SpeechManager: " + e.getMessage());
        }
        try {
            mRobotMotion = (RobotMotion) context.getSystemService("motion");
            if (mRobotMotion == null) {
                Log.e("Robot", "RobotMotion service not available.");
            }
        } catch (Exception motion) {
            Log.e("Robot", "Error initializing SpeechManager: " + motion.getMessage());
        }
    }

    // Static method to get the single instance of the Robot
    public static Robot getInstance(Context context) {
        if (instance == null) {
            synchronized (Robot.class) { // Thread-safe initialization
                if (instance == null) {
                    instance = new Robot(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void speak(String message) {
        if (mSpeechManager != null) {
            try {
                mSpeechManager.forceStartSpeaking(message);
            } catch (Exception e) {
                Log.e("Robot", "Error during speech: " + e.getMessage());
            }
        } else {
            Log.e("Robot", "SpeechManager is not initialized. Cannot speak.");
        }
    }

    public void stopSpeaking() {
        if (mSpeechManager != null) {
            try {
                mSpeechManager.stopSpeaking(-1);
            } catch (Exception e) {
                Log.e("Robot", "Error stopping speech: " + e.getMessage());
            }
        } else {
            Log.e("Robot", "SpeechManager is not initialized. Cannot stop speaking.");
        }
    }


    public boolean isSpeaking() {
        return mSpeechManager != null && mSpeechManager.isSpeaking();
    }

    public void doAction(int action) {
        if (mRobotMotion != null) {
            try {
                mRobotMotion.doAction(action);
            } catch (Exception e) {
                Log.e("Robot", "doAction error: " + e.getMessage());
            }
        }
    }

    public SpeechManager getSpeechManager() {
        return mSpeechManager;
    }

}
