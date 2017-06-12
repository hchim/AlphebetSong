package com.sleepaiden.alphebetsong.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.sleepaiden.alphebetsong.R;
import com.sleepaiden.androidcommonutils.FileUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoiceFragmentDialog extends DialogFragment {
    private static final String ARG_VOICE_FILE = "alphebet.voice_file";
    private static final String ARG_CACHE_VOICE_FILE = "alphebet.cache_voice_file";

    @BindView(R.id.playButton) ImageButton playBtn;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private String voiceFile;
    private String cacheVoiceFile;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;

    public static VoiceFragmentDialog createInstance(String cacheVoiceFile, String voiceFile) {
        VoiceFragmentDialog dialog = new VoiceFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_VOICE_FILE, voiceFile);
        bundle.putString(ARG_CACHE_VOICE_FILE, cacheVoiceFile);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        voiceFile = getArguments().getString(ARG_VOICE_FILE);
        cacheVoiceFile = getArguments().getString(ARG_CACHE_VOICE_FILE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_show_voice, null, false);
        ButterKnife.bind(this, view);

        builder.setView(view)
                .setPositiveButton(R.string.save_button, null)
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new File(voiceFile).delete();
                        VoiceFragmentDialog.this.getDialog().cancel();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileUtils.moveFile(cacheVoiceFile, voiceFile, true);
                        VoiceFragmentDialog.this.getDialog().cancel();
                    }
                });
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaying();
            }
        });

        return dialog;
    }

    private void startPlaying() {
        if (isPlaying) {
            stopPlaying();
        } else {
            mPlayer = new MediaPlayer();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });

            try {
                mPlayer.setDataSource(cacheVoiceFile);
                mPlayer.prepare();
                mPlayer.start();
                playBtn.setBackgroundResource(R.drawable.ic_pause_48dp);
                isPlaying = true;
            } catch (IOException e) {
                Log.e("VoiceFragmentDialog", "prepare play voice failed.");
            }
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        playBtn.setBackgroundResource(R.drawable.ic_play_48dp);
        isPlaying = false;
    }
}
