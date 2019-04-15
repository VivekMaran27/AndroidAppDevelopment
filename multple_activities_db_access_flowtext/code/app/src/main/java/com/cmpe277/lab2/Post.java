package com.cmpe277.lab2;


public class Post {

    private int id;
    private String name, post, image, timeStamp;

    /**
     * Default constructor
     */
    public Post() {
    }

    /**
     * Argumented constructor
     */
    public Post(int id, String name, String image, String post,
                String timeStamp) {
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.post = post;
        this.timeStamp = timeStamp;
    }

    /**
     * Get the identifier for this post
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID for this post
     * @param id Integer ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the name of the person who posted
     * @return Name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Set the the name of the person who posts
     * @param name Name of the person
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the profile picture of the person who posted
     * @return Profile picture
     */
    public String getImge() {
        return image;
    }

    /**
     * Set the profile picture corresponding to this post
     * @return Profile picture
     */
    public void setImge(String image) {
        this.image = image;
    }

    /**
     * Get the post
     * @return Post
     */
    public String getPost() {
        return post;
    }

    /**
     * Set the post
     * @param post post
     */
    public void setPost(String post) {
        this.post = post;
    }

    /**
     * Get the timestamp corresponding to his post
     * @return Timestamp value
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Set the timestamp corresponding to his post
     * @param timeStamp Timestamp value
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
