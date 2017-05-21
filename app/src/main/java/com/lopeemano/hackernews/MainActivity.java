package com.lopeemano.hackernews;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lopeemano.hackernews.databinding.ActivityMainBinding;
import com.lopeemano.hackernews.viewmodel.StoryViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    StoryViewModel storyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        storyViewModel = new StoryViewModel(this);
        binding.setViewModel(storyViewModel);
    }

    @Override
    protected void onPause() {
        super.onPause();
        storyViewModel.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        storyViewModel.onResume();
    }
}
