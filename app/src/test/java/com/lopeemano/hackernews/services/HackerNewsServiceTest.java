package com.lopeemano.hackernews.services;

import com.lopeemano.hackernews.model.Story;
import com.lopeemano.hackernews.service.HackerNewsService;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HackerNewsServiceTest {
    @Test
    public void addition_isCorrect() throws Exception {
        HackerNewsService service = new HackerNewsService();
        Story story = service.getTopStories().blockingFirst();
        assertEquals(4, 2 + 2);
    }
}
