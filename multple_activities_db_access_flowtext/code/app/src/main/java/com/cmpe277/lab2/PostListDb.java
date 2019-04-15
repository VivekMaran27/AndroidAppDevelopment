package com.cmpe277.lab2;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PostListDb {

    private DatabaseManager databaseManager = new DatabaseManager();
    private int postDb;
    private static final String TAG = "MainActivity";
    private Activity activity;

    PostListDb(Activity activity){
        this.activity = activity;
    }

    /**
     * Initialize database
     */
    public void initPostsDB()
    {
        Log.i(TAG, "initPostsDB++");
        /* Get database manager */
        postDb = databaseManager.DbMgr_openOrCreateDb(this.activity, "Posts",
                activity.MODE_PRIVATE);

        /* Create or open a table */
        databaseManager.DbMgr_execSQL(postDb,
                "CREATE TABLE IF NOT EXISTS " +
                        "Posts" +
                        " (LastName VARCHAR, " +
                        "  FirstName VARCHAR," +
                        "  Post TEXT" +
                        "  Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
        Log.i(TAG, "initPostsDB--");
    }

    /**
     * Insert a post into the database
     * @param post Post to add into the DB
     */
    public void insertPostToDb(Post post)
    {
        databaseManager.DbMgr_execSQL(postDb,
                "INSERT INTO " +
                        "Posts" +
                        " Values ("+
                        "'"+post.getName()+"'" + "," +
                        "'"+post.getName()+"'" + "," +
                        "'"+post.getPost()+"'" +"," +
                        "NULL"+
                        ")");
    }

    /**
     * Retrieve posts from DB
     * @return List of posts.
     */
    public List<Post> retrievePostsFromDb()
    {
        List<Post> posts = new ArrayList<>();
        Post post;
        Cursor c = databaseManager.DbMgr_rawQuery(postDb,
                "SELECT Post, FirstName " +
                        "FROM " +
                        "Posts");
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    post = new Post();
                    String postStr = c.getString(c.getColumnIndex("Post"));
                    String Name = c.getString(c.getColumnIndex("FirstName"));
                    post.setPost(postStr);
                    posts.add(post);
                } while (c.moveToNext());
            }
        }
        return posts;
    }
}
