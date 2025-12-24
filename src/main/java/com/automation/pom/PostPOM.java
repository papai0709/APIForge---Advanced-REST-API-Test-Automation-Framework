package com.automation.pom;

import com.automation.pojo.Post;
import io.restassured.response.Response;

import java.util.List;

/**
 * Post API POM class
 * Contains methods specific to Post API operations
 */
public class PostPOM extends BasePOM {
    
    private static final String POSTS_ENDPOINT = "/posts";
    
    public PostPOM() {
        super(POSTS_ENDPOINT);
    }
    
    /**
     * Get all posts
     */
    public Response getAllPosts() {
        return performGet(POSTS_ENDPOINT);
    }
    
    /**
     * Get post by ID
     */
    public Response getPostById(long postId) {
        String endpoint = buildEndpoint(POSTS_ENDPOINT, String.valueOf(postId));
        return performGet(endpoint);
    }
    
    /**
     * Get posts by user ID
     */
    public Response getPostsByUserId(long userId) {
        return getRequestSpec()
                .queryParam("userId", userId)
                .when()
                .get(POSTS_ENDPOINT)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Create new post
     */
    public Response createPost(Post post) {
        return performPost(POSTS_ENDPOINT, post);
    }
    
    /**
     * Update existing post
     */
    public Response updatePost(long postId, Post post) {
        String endpoint = buildEndpoint(POSTS_ENDPOINT, String.valueOf(postId));
        return performPut(endpoint, post);
    }
    
    /**
     * Delete post
     */
    public Response deletePost(long postId) {
        String endpoint = buildEndpoint(POSTS_ENDPOINT, String.valueOf(postId));
        return performDelete(endpoint);
    }
    
    /**
     * Get all posts as Post objects
     */
    public List<Post> getAllPostsAsObjects() {
        Response response = getAllPosts();
        validateStatusCode(response, 200);
        return response.jsonPath().getList("", Post.class);
    }
    
    /**
     * Get post by ID as Post object
     */
    public Post getPostByIdAsObject(long postId) {
        Response response = getPostById(postId);
        validateStatusCode(response, 200);
        return extractResponse(response, Post.class);
    }
    
    /**
     * Validate post creation
     */
    public Post validatePostCreation(Post post) {
        Response response = createPost(post);
        validateStatusCode(response, 201);
        Post createdPost = extractResponse(response, Post.class);
        
        // Validate that the created post has the expected properties
        if (createdPost.getTitle() == null || !createdPost.getTitle().equals(post.getTitle())) {
            throw new AssertionError("Created post title does not match expected");
        }
        
        if (createdPost.getBody() == null || !createdPost.getBody().equals(post.getBody())) {
            throw new AssertionError("Created post body does not match expected");
        }
        
        logger.info("Post creation validation passed for post: {}", post.getTitle());
        return createdPost;
    }
    
    /**
     * Check if post exists
     */
    public boolean postExists(long postId) {
        try {
            Response response = getPostById(postId);
            return response.getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
