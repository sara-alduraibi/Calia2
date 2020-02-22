package com.voice.calia;

import android.content.Intent;
import android.speech.tts.TextToSpeech;

import com.voice.calia.model.GCPTTSAdapter;
import com.voice.calia.model.SpeechManager;
import com.voice.calia.model.gcp.AudioConfig;
import com.voice.calia.model.gcp.EAudioEncoding;
import com.voice.calia.model.gcp.GCPVoice;
import com.voice.calia.model.gcp.VoiceList;


public class MainActivityPresenter implements VoiceList.IVoiceListener {

    private VoiceList mVoiceList;

    private SpeechManager mSpeechManager;
    private GCPTTSAdapter mGCPTTSAdapter;

    public MainActivityPresenter() {

        mVoiceList = new VoiceList();
        mVoiceList.addVoiceListener(this);

        mSpeechManager = new SpeechManager();

        // init GCPTTSAdapter and set default
        mGCPTTSAdapter = new GCPTTSAdapter();
        mSpeechManager.setSpeech(mGCPTTSAdapter);
    }

    public void initGCPTTSSettings() {
        mVoiceList.start();
    }

    public void initAndroidTTSSetting() {

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
    }

    private void initGCPTTSVoice() {
        if (mGCPTTSAdapter == null) return;

        String languageCode = "ar-XA";
        String name = "ar-XA-Wavenet-A";
        float pitch = 1;
        float speakRate = 0.8f;

        GCPVoice gcpVoice = new GCPVoice(languageCode, name);
        AudioConfig audioConfig = new AudioConfig.Builder()
                .addAudioEncoding(EAudioEncoding.MP3)
                .addSpeakingRate(speakRate)
                .addPitch(pitch)
                .build();

        mGCPTTSAdapter.setGCPVoice(gcpVoice);
        mGCPTTSAdapter.setAudioConfig(audioConfig);
    }

    public void startSpeak(String text) {

        mSpeechManager.stopSpeak();

        initGCPTTSVoice();

        mSpeechManager.startSpeak(text);
    }

    @Override
    public void onResponse(String text) {

    }

    @Override
    public void onFailure(String error) {

    }
}



