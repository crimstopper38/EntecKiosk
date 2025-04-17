package com.avatarmind.enteckiosk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechRecognizerHelper {

    public interface OnSpeechResultListener {
        void onResult(String recognizedText);
    }

    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private OnSpeechResultListener resultListener;

    public SpeechRecognizerHelper(Context context, OnSpeechResultListener listener) {
        this.resultListener = listener;

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {
                Log.e("SpeechHelper", "Error: " + error);
                speechRecognizer.startListening(speechIntent); // Retry on error
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && matches.size() > 0) {
                    String recognizedText = matches.get(0).toLowerCase();
                    resultListener.onResult(recognizedText);
                }

                // Restart listening
                speechRecognizer.startListening(speechIntent);
            }

            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
            @Override public void onRmsChanged(float rmsdB) {}
        });
    }

    public void startListening() {
        speechRecognizer.startListening(speechIntent);
    }

    public void stopListening() {
        speechRecognizer.stopListening();
    }

    public void destroy() {
        speechRecognizer.destroy();
    }
}

