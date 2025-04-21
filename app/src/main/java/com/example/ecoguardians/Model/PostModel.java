package com.example.ecoguardians.Model;

public class PostModel {
    public String postId, userId, message, imageUrl;
    public long timestamp;
    public int likesCount;
    public int commentsCount;

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public PostModel() { }

    public PostModel(String postId, String userId, String message, String imageUrl, long timestamp) {
        this.postId = postId;
        this.userId = userId;
        this.message = message;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.likesCount = 0;
        this.commentsCount = 0;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
}
