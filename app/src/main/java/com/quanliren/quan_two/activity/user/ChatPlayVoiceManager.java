package com.quanliren.quan_two.activity.user;

import android.media.MediaPlayer;

import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.radio.AmrEngine;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by Shen on 2015/9/17.
 */
public class ChatPlayVoiceManager {

    public DfMessage playingMsg;

    private static boolean playState = false; // 播放状态

    private MediaPlayer mediaPlayer;

    private static ChatPlayVoiceManager instance;

    public static synchronized ChatPlayVoiceManager getInstance() {
        if (instance == null) {
            instance = new ChatPlayVoiceManager();
        }
        return instance;
    }

    public ChatPlayVoiceManager() {
    }

    public void playVoice(DfMessage msg) {
        if (playState) {
            stopArm(playingMsg);
            if (playingMsg.getId() != msg.getId()) {
                playArm(msg);
            }
        } else {
            playArm(msg);
        }
    }

    public void playArm(final DfMessage dm) {
        File file = new File(dm.getContent());
        if (!file.exists()) {
            return;
        }

        mediaPlayer = new MediaPlayer();
        try {
            playingMsg = dm;
            mediaPlayer.setDataSource(dm.getContent());
            mediaPlayer.prepare();
            mediaPlayer.start();
            dm.setPlaying(true);
            EventBus.getDefault().post(dm);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    playState = false;
                    dm.setPlaying(false);
                    EventBus.getDefault().post(dm);
                    mediaPlayer = null;
                }
            });
            playState = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopArm(DfMessage msg) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        AmrEngine.getSingleEngine().stopRecording();
        if (msg != null) {
            msg.setPlaying(false);
            EventBus.getDefault().post(msg);
        }
        playState = false;
    }
}
