package com.lopeemano.hackernews.service;

import com.lopeemano.hackernews.model.Comment;
import com.lopeemano.hackernews.model.HNItem;
import com.lopeemano.hackernews.model.Story;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HackerNewsService {

    private HackerNewsApiService hackerNewsApiService;

    public HackerNewsService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hacker-news.firebaseio.com/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        hackerNewsApiService = retrofit.create(HackerNewsApiService.class);
    }

    public Observable<Story> getTopStories() {
        return hackerNewsApiService
                .getTopStorieIds()
                .flatMap(new Function<List<Integer>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(@NonNull List<Integer> integers) throws Exception {
                        return Observable.fromIterable(integers);
                    }
                })
                .buffer(5)
                .flatMap(new Function<List<Integer>, ObservableSource<Story>>() {
                    @Override
                    public ObservableSource<Story> apply(@NonNull List<Integer> integers) throws Exception {
                        return Observable
                                .fromIterable(integers)
                                .flatMap(new Function<Integer, ObservableSource<Story>>() {
                                    @Override
                                    public ObservableSource<Story> apply(@NonNull Integer storyId) throws Exception {
                                        return hackerNewsApiService
                                                .getStory(String.valueOf(storyId))
                                                .map(new Function<HNItem, Story>() {
                                                    @Override
                                                    public Story apply(@NonNull HNItem hnItem) throws Exception {
                                                        return Story.translate(hnItem);
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    public Observable<Comment> getComments(Story story) {
        return hackerNewsApiService.getComments(String.valueOf(story.id))
                .flatMap(new Function<List<HNItem>, ObservableSource<Comment>>() {
                    @Override
                    public ObservableSource<Comment> apply(@NonNull final List<HNItem> items) throws Exception {
                        return Observable
                                .fromIterable(items)
                                .flatMap(new Function<HNItem, ObservableSource<Comment>>() {
                                    @Override
                                    public ObservableSource<Comment> apply(@NonNull final HNItem rootHNItem) throws Exception {
                                        return Observable
                                                .fromIterable(rootHNItem.getKids())
                                                .flatMap(new Function<Integer, ObservableSource<List<HNItem>>>() {
                                                    @Override
                                                    public ObservableSource<List<HNItem>> apply(@NonNull Integer commentId) throws Exception {
                                                        return hackerNewsApiService.getComments(String.valueOf(commentId));
                                                    }
                                                })
                                                .map(new Function<List<HNItem>, Comment>() {
                                                    @Override
                                                    public Comment apply(@NonNull List<HNItem> hnItems) throws Exception {
                                                        Comment mainComment = Comment.translate(rootHNItem);
                                                        List<Comment> subComments = new ArrayList<>();
                                                        for (HNItem hnItem : hnItems) {
                                                            subComments.add(Comment.translate(hnItem));
                                                        }
                                                        mainComment.setComments(subComments);
                                                        return mainComment;
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }
}
