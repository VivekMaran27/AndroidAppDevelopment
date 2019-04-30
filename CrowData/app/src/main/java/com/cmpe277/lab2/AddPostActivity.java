package com.cmpe277.lab2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private static final String KEY_POST = "post";
    private static final String KEY_USER_PIC = "user_pic";
    private static final String KEY_DATE = "date";
    private static final String KEY_USER_NAME = "user_name";

    private EditText editTextPost;
    private Button submitButton;
    private  String userName;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> postObj = new HashMap<>();

    public void loadUserInfoforPost()
    {
        Drawable profilePic = getDrawable(R.drawable.avatar);
        Bitmap b = ((BitmapDrawable)profilePic).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b,
                50, 50, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapResized.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(bytes, Base64.DEFAULT);
        postObj.put(KEY_USER_PIC, imageEncoded);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        editTextPost = findViewById(R.id.post);
        submitButton = findViewById(R.id.button);

        Intent intent = getIntent();
        userName = intent.getStringExtra("user");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = editTextPost.getText().toString();
                postObj.put(KEY_POST, post);

                /* loadUserInfoforPost */
                loadUserInfoforPost();

                /* Set current date */
                DateFormat dateFormat = new SimpleDateFormat(
                        "MMMM dd, yyyy HH:mm", Locale.US);
                Date date = new Date();
                postObj.put(KEY_DATE, dateFormat.format(date));
                postObj.put(KEY_USER_NAME, userName);

                db.collection("Posts").document().set(postObj)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddPostActivity.this,
                                        "Sucessfully posted requirement",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddPostActivity.this,
                                        "Failed posting requirement",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
