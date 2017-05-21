package com.lopeemano.hackernews;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lopeemano.hackernews.databinding.ActivityCommentsBinding;
import com.lopeemano.hackernews.model.Story;
import com.lopeemano.hackernews.viewmodel.CommentsViewModel;

public class CommentsActivity extends AppCompatActivity {

    CommentsViewModel commentsViewModel;
    Story story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCommentsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_comments);

        commentsViewModel = new CommentsViewModel(story);
        binding.setViewModel(commentsViewModel);
    }

    @Override
    protected void onPause() {
        super.onPause();
        commentsViewModel.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentsViewModel.onResume();
    }
}
