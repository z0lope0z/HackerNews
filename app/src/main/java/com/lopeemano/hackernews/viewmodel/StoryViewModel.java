package com.lopeemano.hackernews.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.lopeemano.hackernews.BR;
import com.lopeemano.hackernews.CommentsActivity;
import com.lopeemano.hackernews.R;
import com.lopeemano.hackernews.model.Story;
import com.lopeemano.hackernews.service.HackerNewsService;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class StoryViewModel {
    public final ObservableList<Story> items = new ObservableArrayList<>();
    public final ItemBinding<Story> itemBinding = ItemBinding.<Story>of(BR.story, R.layout.view_story);

    private HackerNewsService hackerNewsService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public StoryViewModel(final Context context) {
        hackerNewsService = new HackerNewsService();

        OnItemClickListener listener = new OnItemClickListener() {
            @Override
            public void onItemClick(Story story) {
                Intent intent = new Intent(context, CommentsActivity.class);
                context.startActivity(intent);
            }
        };

        itemBinding.bindExtra(BR.listener, listener);
    }

    public void getItems() {
        hackerNewsService
                .getTopStories()
                .subscribe(new Observer<Story>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Story story) {
                        items.add(story);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onPause() {
        disposable.dispose();
    }

    public void onResume() {
        getItems();
    }

    public void isLoading() {

    }

    public void onRefresh() {
        getItems();
    }

    public interface OnItemClickListener {
        void onItemClick(Story story);
    }
}
