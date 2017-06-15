package com.sleepaiden.alphebetsong.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sleepaiden.alphebetsong.R;
import com.sleepaiden.alphebetsong.models.AlphebetPage;
import com.sleepaiden.alphebetsong.settings.PreferenceConstants;
import com.sleepaiden.androidcommonutils.FileUtils;
import com.sleepaiden.androidcommonutils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements FragmentLifecycle {
    private static final String TAG = "PlaceholderFragment";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private AlphebetPage alphebetPage;
    private PreferenceUtils preferenceUtils;
    private String learningMode;
    private String soundSource;

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.wordTextView) TextView textView;
    @BindView(R.id.voiceButton) ImageButton voiceBtn;
    @BindView(R.id.alphebetToolbar) LinearLayout alphebetToolbar;
    @BindView(R.id.playButton) ImageButton playBtn;

    private MediaRecorder mRecorder;
    private boolean isRecording = false;
    private String voiceCachePath;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    private String soundPath;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ALPHEBET_PAGE = "alphebet_page";

    public PlaceholderFragment() {}

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(AlphebetPage alphebetPage) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALPHEBET_PAGE, alphebetPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferenceUtils = new PreferenceUtils(getContext());
        soundSource = preferenceUtils.getString(
                PreferenceConstants.PREF_KEY_SOUND_SOURCE,
                PreferenceConstants.SOUND_SOURCE_DEFAULT);

        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, rootView);
        alphebetPage = getArguments().getParcelable(ARG_ALPHEBET_PAGE);

        Picasso.with(this.getContext())
                .load(alphebetPage.getImageId())
                .into(imageView);
        textView.setText(alphebetPage.getWord());
        voiceBtn.setOnTouchListener(voiceBtnTouchListener);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaying();
            }
        });

        return rootView;
    }

    @Override
    public void onResumeFragment() {
        startPlaying();
    }

    @Override
    public void onPauseFragment() {
        stopPlaying();
    }

    private View.OnTouchListener voiceBtnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                int permission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    startRecording();
                } else {
                    requestRecordAudioPermission();
                }
            } else if (action == MotionEvent.ACTION_UP) {
                stopRecording();
            }

            return false;
        }
    };

    private void requestRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startRecording() {
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            voiceCachePath = getActivity().getExternalCacheDir().getAbsolutePath()
                    + String.format("/astemp-%d.3gp", System.currentTimeMillis());
            mRecorder.setOutputFile(voiceCachePath);
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
            Snackbar.make(voiceBtn, R.string.start_recording, Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "Start recording...");
        } catch (Exception e) {
            Log.e(TAG, "Prepare media recorder failed.", e);
        }
    }

    private void stopRecording() {
        if (mRecorder != null && isRecording) {
            try {
                Snackbar.make(voiceBtn, R.string.stop_recording, Snackbar.LENGTH_SHORT).show();
                mRecorder.stop();
                String customVoiceFile = FileUtils.getDataDir(getContext())
                        + String.format("/alphebet_%s.3gp", alphebetPage.getWord().toLowerCase());
                VoiceFragmentDialog
                        .createInstance(voiceCachePath, customVoiceFile, alphebetPage.getWord())
                        .show(getFragmentManager(), null);
            } catch (Exception e) {
                Log.e(TAG, "Failed to stop voice recorder.", e);
            } finally {
                mRecorder.release();
                isRecording = false;
                mRecorder = null;
            }
        }
        Log.d(TAG, "Stop recording...");
    }

    private void startPlaying() {
        if (isPlaying) {
            stopPlaying();
        } else {
            boolean shouldPlayDefault = shouldPlayDefaultSound();
            if (shouldPlayDefault) {
                mPlayer = MediaPlayer.create(getContext(), alphebetPage.getSound());
            } else {
                mPlayer = new MediaPlayer();
            }
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });

            try {
                if (!shouldPlayDefault) {
                    mPlayer.setDataSource(alphebetPage.getCustomSoundSource());
                    mPlayer.prepare();
                }
                mPlayer.start();
                playBtn.setBackgroundResource(R.drawable.ic_pause_48dp);
                isPlaying = true;
            } catch (Exception e) {
                Log.e(TAG, "prepare play voice failed.");
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

    private boolean shouldPlayDefaultSound() {
        return soundSource.equals(PreferenceConstants.SOUND_SOURCE_DEFAULT)
                || alphebetPage.getCustomSoundSource() == null;
    }
}
