package com.lopeemano.hackernews.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.lopeemano.hackernews.BR;
import com.lopeemano.hackernews.R;
import com.lopeemano.hackernews.model.Comment;
import com.lopeemano.hackernews.model.Story;
import com.lopeemano.hackernews.service.HackerNewsService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class CommentsViewModel {
    public final ObservableList<Comment> items = new ObservableArrayList<>();
    public final ItemBinding<Comment> itemBinding = ItemBinding.of(BR.story, R.layout.view_story);
    public final OnItemBind<String> onItemBind = new OnItemBind<String>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, String item) {
            Comment comment = items.get(position);
            itemBinding.set(BR.comment, comment.getParentComment() == null ? R.layout.view_comment : R.layout.view_sub_comment);
        }
    };

    private HackerNewsService hackerNewsService;
    private CompositeDisposable disposable = new CompositeDisposable();

    private Story story;

    public CommentsViewModel(Story story) {
        hackerNewsService = new HackerNewsService();
        this.story = story;
    }

    public void getComments() {
        hackerNewsService
                .getComments(story)
                .map(new Function<Comment, List<Comment>>() {
                    @Override
                    public List<Comment> apply(@NonNull Comment mainComment) throws Exception {
                        List<Comment> comments = new ArrayList<>();
                        comments.add(mainComment);
                        for (Comment comment : mainComment.getComments()) {
                            comment.setParentComment(mainComment);
                            comments.add(comment);
                        }
                        return comments;
                    }
                })
                .subscribe(new Observer<List<Comment>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Comment> comments) {
                        items.addAll(comments);
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
        getComments();
    }
}
