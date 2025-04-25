package com.avatarmind.enteckiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public abstract class SpeechListeningActivity extends Activity {

    protected Robot myRobot;
    private boolean isFirstResume = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myRobot = Robot.getInstance(this);
    }

    protected void startSpeechListening(final String triggerUrl, final String resultUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (myRobot.isSpeaking()) {
                        Log.d("SpeechListening", "Robot is still speaking...");
                        Thread.sleep(1000);
                    }

                    Log.d("SpeechListening", "Speech finished — sending signal to server.");

                    SpeechTriggerManager.triggerSTTAndPoll(
                            myRobot,
                            triggerUrl,
                            resultUrl,
                            new SpeechTriggerManager.SpeechResultListener() {
                                @Override
                                public void onResultReceived(final String recognizedText) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            handleRecognizedText(recognizedText);
                                        }
                                    });
                                }

                                @Override
                                public void onError(final Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.e("SpeechListening", "Error: ", e);
                                        }
                                    });
                                }
                            }
                    );
                } catch (InterruptedException e) {
                    Log.e("SpeechListening", "Thread interrupted!", e);
                }
            }
        }).start();
    }

    // Child classes must override this.
    protected abstract void handleRecognizedText(String recognizedText);

    protected void openActivity(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isFirstResume) {
            isFirstResume = false;
            return;  // skip resume prompt on initial entry
        }

        //myRobot.speak(getResumePrompt());

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startSpeechListening(Config.START_STT_URL, Config.STT_RESULT_URL);
    }

    protected String getResumePrompt() {
        return "Please select or speak an option.";
    }


}


