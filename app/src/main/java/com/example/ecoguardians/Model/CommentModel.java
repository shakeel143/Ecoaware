package com.example.ecoguardians.Model;

public class CommentModel {
    private String commentId, userId, text, userName, userProfileImage;
    private long timestamp;

    public CommentModel() {
        // Default constructor required for Firebase
    }

    public CommentModel(String commentId, String userId, String text, long timestamp) {
        this.commentId = commentId;
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getCommentId() { return commentId; }
    public String getUserId() { return userId; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
    public String getUserName() { return userName; }
    public String getUserProfileImage() { return userProfileImage; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setUserProfileImage(String userProfileImage) { this.userProfileImage = userProfileImage; }
}