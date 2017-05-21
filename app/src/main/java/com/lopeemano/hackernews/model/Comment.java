package com.lopeemano.hackernews.model;

import java.util.List;

public class Comment {

    List<Comment> comments;

    Comment parentComment;

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public static Comment translate(HNItem item) {
        return new Comment();
    }

    public List<Comment> getComments() {
        return comments;
    }
}
