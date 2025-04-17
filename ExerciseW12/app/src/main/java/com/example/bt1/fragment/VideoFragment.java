package com.example.bt1.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bt1.R;
import com.example.bt1.model.Video1Model;

public class VideoFragment extends Fragment {
    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private Video1Model video;
    private Handler handler = new Handler();
    private Runnable timeoutRunnable;

    public static VideoFragment newInstance(Video1Model video) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putSerializable("video", video);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_video_row, container, false);

        videoView = view.findViewById(R.id.videoView);
        progressBar = view.findViewById(R.id.videoProgressBar);
        titleTextView = view.findViewById(R.id.textVideoTitle);
        descriptionTextView = view.findViewById(R.id.textVideoDescription);

        if (getArguments() != null) {
            video = (Video1Model) getArguments().getSerializable("video");
            if (video != null) {
                setupVideoPlayer();
                updateUI();
            }
        }

        return view;
    }

    private void setupVideoPlayer() {
        progressBar.setVisibility(View.VISIBLE);

        try {
            videoView.setVideoURI(Uri.parse(video.getUrl()));

            videoView.setOnPreparedListener(mp -> {
                progressBar.setVisibility(View.GONE);
                mp.setLooping(true);
                mp.start();
                handler.removeCallbacks(timeoutRunnable);
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error playing video: " + what, Toast.LENGTH_LONG).show();
                return true;
            });

            timeoutRunnable = () -> {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Video loading timeout", Toast.LENGTH_SHORT).show();
                }
            };
            handler.postDelayed(timeoutRunnable, 15000);

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Error setting up video: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI() {
        titleTextView.setText(video.getTitle());
        descriptionTextView.setText(video.getDesc());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
        handler.removeCallbacks(timeoutRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (videoView != null) {
            videoView.stopPlayback();
        }
        handler.removeCallbacks(timeoutRunnable);
    }
}