package com.cmpe277.lab2;

import android.app.Activity;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class PostListDb {

    private DatabaseManager databaseManager = new DatabaseManager();
    private int postDb;
    private static final String TAG = "MainActivity";
    private Activity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BaseAdapter mAdapter;
    private List<Post> mPosts;

    PostListDb(Activity activity, BaseAdapter adapter, List<Post> posts){
        this.activity = activity;
        mAdapter = adapter;
        mPosts = posts;
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
    public void retrievePostsFromDb(final ProgressBar progressBar)
    {
//        final int MAX_AVAILABLE = 100;
//        final Semaphore available = new Semaphore(0, true);

        Log.d(TAG, "retrievePostsFromDb++");
        final List<Post> posts = new ArrayList<>();
        db.collection("Posts").get().addOnCompleteListener(
            new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "Retrieved posts from database");
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = new Post();
                        Map<String, Object> doc = document.getData();
                        String postVal = (String)document.get("post");
                        String id = document.getId();
                        String date = (String)document.get("date");
                        String image = (String)document.get("user_pic");
                        String name = (String)document.get("user_name");
                        Log.d(TAG, "DB Post: "+ postVal);
                        post.setPost(postVal);
                        post.setId(id);
                        post.setTimeStamp(date);
                        post.setImge(image);
                        post.setName(name);
                        mPosts.add(post);
                        progressBar.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
//                        available.release();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
//                    available.release();
                }
            }
        });
//        Post post;
//        Cursor c = databaseManager.DbMgr_rawQuery(postDb,
//                "SELECT Post, FirstName " +
//                        "FROM " +
//                        "Posts");
//        if (c != null) {
//            if (c.moveToFirst()) {
//                do {
//                    post = new Post();
//                    String postStr = c.getString(c.getColumnIndex("Post"));
//                    String Name = c.getString(c.getColumnIndex("FirstName"));
//                    post.setPost(postStr);
//                    posts.add(post);
//                } while (c.moveToNext());
//            }
//        }
    }
}
