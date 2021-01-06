package com.amansiol.magicdiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BasicActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE = 100;
    FirebaseAuth mAuth;
    Button nextButton;
    EditText userName;
    EditText statusName;
    ProgressBar progressBar;
    CircularImageView profile;
    CircularImageView addprofile;
    String statusText;
    String userNameText;
    String profilelink="";
    StorageReference mStorageRef;
    String StoragePath = "user_profile_cover_pic/";
    Uri profile_uri_path;
    ProgressDialog pd;


    void init() {
        nextButton = findViewById(R.id.login_next);
        userName = findViewById(R.id.login_name);
        statusName = findViewById(R.id.login_status);
        progressBar = findViewById(R.id.progressBar);
        profile = findViewById(R.id.profilepic);
        addprofile = findViewById(R.id.addprofilepic);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(BasicActivity.this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat
                .getColor(getApplicationContext(),
                        R.color.background));
        setContentView(R.layout.activity_basic);
        init();
        addprofile.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"Bad me hi laga lena pic",Toast.LENGTH_LONG).show();
//            Dexter.withActivity(BasicActivity.this)
//                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .withListener(new MultiplePermissionsListener() {
//                        @Override
//                        public void onPermissionsChecked(MultiplePermissionsReport report) {
//                            if (report.areAllPermissionsGranted()) {
//                                showImagePickerOptions();
//                            }
//
//                            if (report.isAnyPermissionPermanentlyDenied()) {
//                                showSettingsDialog();
//                            }
//                        }
//
//                        @Override
//                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                            token.continuePermissionRequest();
//                        }
//                    }).check();
        });
        profile.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"Bad me hi laga lena pic",Toast.LENGTH_LONG).show();
//            Dexter.withActivity(BasicActivity.this)
//                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .withListener(new MultiplePermissionsListener() {
//                        @Override
//                        public void onPermissionsChecked(MultiplePermissionsReport report) {
//                            if (report.areAllPermissionsGranted()) {
//                                showImagePickerOptions();
//                            }
//
//                            if (report.isAnyPermissionPermanentlyDenied()) {
//                                showSettingsDialog();
//                            }
//                        }
//
//                        @Override
//                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                            token.continuePermissionRequest();
//                        }
//                    }).check();
        });
        nextButton.setOnClickListener(v -> {

            userNameText = userName.getText().toString().trim();
            statusText = statusName.getText().toString().trim();
            if (userNameText.isEmpty() || userNameText.length() > 25) {
                userName.setError("Write Name");
                userName.requestFocus();
                return;
            }
            if (statusText.isEmpty()) {
                statusText = "I'm Chatting With You Secretly";
//                statusName.setError("Emotion Or Qoutes");
//                statusName.requestFocus();
            }
//            if (profilelink.isEmpty()) {
////                profile_uri_path = Uri.parse("android.resource://com.amansiol.magicdiary/" + R.drawable.profilepic);
////                profilelink = profile_uri_path.toString();
//            }

            saveUserDetails();

        });

    }

    private void saveUserDetails() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = mAuth.getCurrentUser();
        String useremail = user.getEmail();
        String userUID = user.getUid();
        HashMap<Object, String> userhashmap = new HashMap<>();
        userhashmap.put("email", useremail);
        userhashmap.put("UID", userUID);
        userhashmap.put("name", userNameText);
        userhashmap.put("number", "+91 0000000000");
        userhashmap.put("status", statusText);
        userhashmap.put("onlineStatus", "online");
        userhashmap.put("typing", "noOne");
        userhashmap.put("image", profilelink);
        userhashmap.put("gender", "Male");
        userhashmap.put("isverified", "Unverified");

       FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
       mDatabase.setPersistenceEnabled(true);
       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
       databaseReference.keepSynced(true);
       databaseReference
                .child(userUID)
                .setValue(userhashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(BasicActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
//              Animatoo.animateSplit(RegisterActivity.this);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Try again", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(BasicActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(BasicActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                profile_uri_path = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profile_uri_path);

                    // loading profile image from local cache
                    Picasso.get().load(profile_uri_path).into(profile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BasicActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                BasicActivity.this.openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void Uploading_profile_tofirebase() {
        pd.setTitle("Upload");
        pd.setMessage("Profile Pic Uploading....");
        pd.show();
        String FilePathName = StoragePath + "" + "image" + "_" + mAuth.getUid();
        StorageReference storageref2 = mStorageRef.child(FilePathName);

        storageref2.putFile(profile_uri_path)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        final Uri profile_pic_uri_path = uriTask.getResult();
                        if (uriTask.isSuccessful()) {
                            //image uploaded...
                            pd.dismiss();
                            profilelink = profile_pic_uri_path.toString();

                        } else {
                            pd.dismiss();
                            Toast.makeText(BasicActivity.this, "Error while uploading image", Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(BasicActivity.this, "" + exception.getMessage(), Toast.LENGTH_LONG).show();
                        // ...
                    }
                });
    }
}