package com.cmpe277.lab2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadActivity extends AppCompatActivity {

    /* TAG for logging */
    private final String TAG = "ImageUplaodActivity";

    /* Image list received from the main activity */
    private ArrayList<Uri> ImageList;

    /* Widgets */
    private Button mUploadButton;
    private ProgressDialog mprogressDialog;
    private ProgressBar mprogressBar;
    private EditText label;

    /* Backend stuff */
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private String project_id;
    private FirebaseVisionImageLabeler labeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        mprogressDialog = new ProgressDialog(this);

        /* Views */
        label = (EditText) findViewById(R.id.editText2);

        /* View pager initialization */
        ImageList = (ArrayList<Uri>)(getIntent().
                                     getBundleExtra("ImageBundle").
                                     getSerializable("ImageList"));
        project_id = (String) getIntent().
                              getBundleExtra("ImageBundle").
                              getString("ProjectId");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomSwipeAdapter
                (this,ImageList, mUploadButton));

        /* Setup upload */
        mUploadButton = findViewById(R.id.upload_btn);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mprogressDialog.setTitle("Upload Progress");
        mprogressDialog.setMessage("Please wait..");
        mprogressDialog.setIndeterminate(false);
        mprogressDialog.setCancelable(true);

        /* ML stuff */
        labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
        mprogressBar = findViewById(R.id.progressbar3);
        /* Perform upload on button click */
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(ImageUploadActivity.this,
                            "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        /* listen for page changes so we can track the current index */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.d(TAG,"onPageScrolled++"+" "+arg0+" "+arg2);
                Log.d(TAG,"onPageScrolled--");
            }

            public void onPageSelected(int currentPage) {
                label.getText().clear();
                mprogressBar.setVisibility( View.VISIBLE);
                LabelImage(currentPage);

                /* Generate label for image */
                if(currentPage == ImageList.size()-1)
                {
                    Log.d(TAG,"Setting upload button visibility to true" +
                            " on page number: "+currentPage);
                    mUploadButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    Log.d(TAG,"Setting upload button visibility to false" +
                            " on page number: "+currentPage);
                    mUploadButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void LabelImage(int position)
    {
        FirebaseVisionImage image;
        try {
            image = FirebaseVisionImage.fromFilePath(getApplicationContext(),
                    ImageList.get(position));
            Log.d(TAG,"Generating label for: "+ImageList.get(position));
            labeler.processImage(image)
                    .addOnSuccessListener(
                     new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            String result = null;
                            for (FirebaseVisionImageLabel label: labels) {
                                result = label.getText();
                                break;
                            }
                            Log.d(TAG,"Generated label: "+label);
                            label.setText(result);
                            mprogressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImageUploadActivity.this,
                                    "Label generation failed "+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Get the extension of the file, given an URI
     * @param uri
     * @return
     */
    private String getFileExtension(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType.split("/")[1];
    }

    /**
     * Upload file to firestorage
     */
    private void uploadFile() {
        Log.d(TAG,"uploadFile++");


        Log.d(TAG,"Showing progress bar");
        mprogressDialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                Integer idx = 0;
                for (Uri imageUri : ImageList) {
                    ++idx;
                    Log.d(TAG, "Image URI: " +imageUri);
                    String imageFile = project_id + "/"+ System.currentTimeMillis()
                            + "." + getFileExtension(imageUri);
                    StorageReference fileReference = mStorageRef.child(imageFile);
                    Log.d(TAG, "Uploading image: " + imageFile);

                    /* Wait for the upload task to complete */
                    if (mUploadTask != null) {
                        Log.d(TAG,"Waiting for previous upload to complete");
                        while (mUploadTask.isInProgress()) ;
                    }
                    String progressTitle = "Uploading file " + idx;
                    mUploadTask = fileReference.putFile(imageUri).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot
                                                              taskSnapshot) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImageUploadActivity.this,
                                    "Upload failed "+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask
                            .TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.
                                    getBytesTransferred() /
                                    taskSnapshot.getTotalByteCount());
                            Log.d(TAG,"Setting progress: "+(int)progress);
                        }
                    });
                }
                mprogressDialog.dismiss();
            }
        };
        mThread.start();
        /* For each Image */
        //Log.d(TAG,"Hiding progress bar");
        //mprogressDialog.hide();
        Log.d(TAG,"uploadFile--");
    }
}
