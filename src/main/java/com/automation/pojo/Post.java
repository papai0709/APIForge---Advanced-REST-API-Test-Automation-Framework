package com.automation.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Post POJO class representing Post entity
 * Example POJO that extends BasePojo for posts/articles
 */
public class Post extends BasePojo {
    
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("body")
    private String body;
    
    // Constructors
    public Post() {}
    
    public Post(Long userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "userId=" + userId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", " + super.toString() +
                '}';
    }
}
