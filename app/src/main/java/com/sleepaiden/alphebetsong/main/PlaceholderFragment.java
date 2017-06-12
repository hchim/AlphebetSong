package com.sleepaiden.alphebetsong.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.sleepaiden.androidcommonutils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String TAG = "PlaceholderFragment";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private AlphebetPage alphebetPage;
    private PreferenceUtils preferenceUtils;
    private String learningMode;

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.wordTextView) TextView textView;
    @BindView(R.id.voiceButton) ImageButton voiceBtn;
    @BindView(R.id.alphebetToolbar) LinearLayout alphebetToolbar;

    private MediaRecorder mRecorder;
    private String voiceFilePath;

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
        voiceFilePath = getActivity().getExternalCacheDir().getAbsolutePath()
                + String.format("/astemp-%d.3gp", System.currentTimeMillis());
        preferenceUtils = new PreferenceUtils(getContext());
        learningMode = preferenceUtils.getString(
                PreferenceConstants.PREF_KEY_LEARNING_MODE,
                PreferenceConstants.LEARNING_MODE_MANUAL);

        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, rootView);
        alphebetPage = getArguments().getParcelable(ARG_ALPHEBET_PAGE);

        Picasso.with(this.getContext())
                .load(alphebetPage.getImageId())
                .into(imageView);
        textView.setText(alphebetPage.getWord());

        voiceBtn.setOnTouchListener(voiceBtnTouchListener);

        if (learningMode.equals(PreferenceConstants.LEARNING_MODE_AUTOMATIC)) {
            alphebetToolbar.setVisibility(View.INVISIBLE);
        }

        return rootView;
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
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(voiceFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Prepare media recorder failed.", e);
        }

        mRecorder.start();
        Log.d(TAG, "Start recording...");
    }

    private void stopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                VoiceFragmentDialog.createInstance(voiceFilePath).show(getFragmentManager(), null);
            } catch (RuntimeException e) {
                Log.e(TAG, "Failed to stop voice recorder.", e);
            } finally {
                mRecorder.release();
                mRecorder = null;
            }
        }
        Log.d(TAG, "Stop recording...");
    }
}
