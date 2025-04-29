package com.avatarmind.enteckiosk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.robot.motion.RobotMotion;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class QuickLinks extends SpeechListeningActivity {
    private Robot myRobot;
    private RobotMotion rMotion;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_links);

        // Get shared robot instance
        myRobot = Robot.getInstance(this);
        rMotion = new RobotMotion();

        // Initialize buttons
        buttonBack = (Button) findViewById(R.id.quick_links_back_button);

        setupButtonListeners();

        try {
            // Speech performed by robot when view is opened
            if (myRobot != null) {
                String introText = "Welcome to Quick Links. Here you can access helpful campus resources like the library, financial aid, bookstore, and more. Tap a button to explore each service.";
                myRobot.speak(introText);
            }

        } catch (Exception e) {
            Log.e("QuickLinksActivity", "Error during initial actions: " + e.getMessage());
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

    private void setupButtonListeners() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRobot.stopSpeaking();
                finish();
            }
        });

        setupLinkButton(R.id.button_learning_resources, "https://www.mdc.edu/north/library/");
        setupLinkButton(R.id.button_campus_services, "https://www.mdc.edu/north/campusservices/");
        setupLinkButton(R.id.button_bookstore, "https://www.bkstr.com/miamidadenorthstore/home");
        setupLinkButton(R.id.button_dining_services, "https://www.mdc.edu/north/dining/");
        setupLinkButton(R.id.button_financial_aid, "https://www.mdc.edu/financialaid/");
        setupLinkButton(R.id.button_fitness_center, "https://www.mdc.edu/north/wellnesscenter/");
    }

    private void setupLinkButton(int buttonId, final String url) {
        Button button = (Button) findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRobot.stopSpeaking();
                    openWebPage(url);
                }
            });
        }
    }

    private void openWebPage(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    protected void handleRecognizedText(String recognizedText) {
        if (recognizedText.toLowerCase().contains("back") || recognizedText.toLowerCase().contains("return")) {
            finish();
        } else if (recognizedText.toLowerCase().contains("library") || recognizedText.toLowerCase().contains("learning")) {
            openWebPage("https://www.mdc.edu/north/library/");
        } else if (recognizedText.toLowerCase().contains("services") || recognizedText.toLowerCase().contains("campus")) {
            openWebPage("https://www.mdc.edu/north/campusservices/");
        } else if (recognizedText.toLowerCase().contains("bookstore")) {
            openWebPage("https://www.bkstr.com/mdcnorthstore");
        } else if (recognizedText.toLowerCase().contains("parking")) {
            openWebPage("https://www.mdc.edu/north/studentservices/parking.aspx");
        } else if (recognizedText.toLowerCase().contains("dining") || recognizedText.toLowerCase().contains("food")) {
            openWebPage("https://www.mdc.edu/north/dining/");
        } else if (recognizedText.toLowerCase().contains("financial") || recognizedText.toLowerCase().contains("aid")) {
            openWebPage("https://www.mdc.edu/financialaid/");
        } else if (recognizedText.toLowerCase().contains("fitness") || recognizedText.toLowerCase().contains("gym")) {
            openWebPage("https://www.mdc.edu/north/wellnesscenter/");
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