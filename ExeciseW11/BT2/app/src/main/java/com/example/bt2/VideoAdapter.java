package com.example.bt2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bt2.model.Video1Model;

import java.util.List;

public class VideoAdapter extends FragmentStateAdapter {
    private final List<Video1Model> videoList;

    public VideoAdapter(FragmentActivity fragmentActivity, List<Video1Model> videoList) {
        super(fragmentActivity);
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return VideoFragment.newInstance(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}