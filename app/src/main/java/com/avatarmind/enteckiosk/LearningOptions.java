package com.avatarmind.enteckiosk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LearningOptions extends Activity {

    private Robot my_robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_options);

        //get shared robot instance
        my_robot = Robot.getInstance(this);

        speakAboutProgram();

        //back button functionality
        Button backButton = (Button) findViewById(R.id.learning_options_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {

            //stops any robot actions when pressed
            @Override
            public void onClick(View v) {
                // *** might be unnecessary because of onDestroy check, test with robot ***
                my_robot.stopSpeaking();
                finish();
            }
        });
    }

    //text-to-speech description for the current page
    private void speakAboutProgram() {
        if (my_robot != null) {
            String learningMessage = getString(R.string.learning_options_message);
            my_robot.speak(learningMessage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (my_robot != null) {
            my_robot.stopSpeaking();
        }
    }
}
