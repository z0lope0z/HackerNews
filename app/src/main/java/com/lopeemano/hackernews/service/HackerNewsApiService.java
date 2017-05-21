package com.lopeemano.hackernews.service;

import com.lopeemano.hackernews.model.HNItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HackerNewsApiService {
    @GET("topstories.json")
    Observable<List<Integer>> getTopStorieIds();

    @GET("item/{storyId}.json")
    Observable<HNItem> getStory(@Path("storyId") String storyId);

    @GET("item/{commentId}.json")
    Observable<List<HNItem>> getComments(@Path("commentId") String commentId);
}
