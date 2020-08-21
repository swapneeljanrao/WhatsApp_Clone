package com.mrcoder.view.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mrcoder.R;
import com.mrcoder.common.Common;
import com.mrcoder.databinding.ActivityProfileBinding;
import com.mrcoder.view.diaplay.ViewProfileImageActivity;
import com.mrcoder.view.startup.SplashScreen;

import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    BottomSheetDialog bottomSheetDialogUpdateImage, bottomSheetDialogUpdateName;

    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        setSupportActionBar(binding.toolbarProfile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (firebaseUser != null) {
            getInfo();
        }
        initAction();
    }

    private void initAction() {
        binding.pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPickUp();
            }
        });

        binding.imageViewEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetEditUserName();
            }
        });

        binding.profileUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.profileUserProfileImage.invalidate();
                Drawable dr = binding.profileUserProfileImage.getDrawable();
                Common.IMAGE_BITMAP = ((BitmapDrawable) dr.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileActivity.this, binding.profileUserProfileImage, "profile_image");
                Intent intent = new Intent(ProfileActivity.this, ViewProfileImageActivity.class);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });
        binding.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutDialog();
            }
        });
    }

    private void showSignOutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("Do want to sign out?");
        builder.setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, SplashScreen.class));
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showBottomSheetEditUserName() {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.bottom_shit_update_username, null);

        final EditText etUserName = view.findViewById(R.id.etUsername);

        ((View) view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogUpdateName.dismiss();
            }
        });

        ((View) view.findViewById(R.id.btnSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etUserName.getText().toString())) {
                    Toast.makeText(ProfileActivity.this, "Enter New Username", Toast.LENGTH_SHORT).show();
                } else {
                    editUserName(etUserName.getText().toString());
                    bottomSheetDialogUpdateName.dismiss();
                }
            }
        });

        bottomSheetDialogUpdateName = new BottomSheetDialog(this);
        bottomSheetDialogUpdateName.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialogUpdateName.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialogUpdateName.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialogUpdateName.dismiss();
            }
        });
        bottomSheetDialogUpdateName.show();
    }

    private void editUserName(String newUserName) {
        firestore.collection("Users").document(firebaseUser.getUid()).update("userName", newUserName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                getInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void showBottomSheetPickUp() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_image_pick, null);

        ((View) view.findViewById(R.id.gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley();
                bottomSheetDialogUpdateImage.dismiss();
            }
        });
        ((View) view.findViewById(R.id.camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogUpdateImage.dismiss();
            }
        });

        bottomSheetDialogUpdateImage = new BottomSheetDialog(this);
        bottomSheetDialogUpdateImage.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialogUpdateImage.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialogUpdateImage.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialogUpdateImage = null;
            }
        });
        bottomSheetDialogUpdateImage.show();
    }

    private void openGalley() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
//        Intent intent = new Intent();
//        intent.setType("images/*");
//        intent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            uploadToFireBase();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.profileUserProfileImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileExtensions(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadToFireBase() {
        if (imageUri != null) {
            progressDialog.setMessage("Udating profile pic");
            progressDialog.show();

            StorageReference riverseRef = FirebaseStorage.getInstance().getReference().child("imageProfile/" + System.currentTimeMillis() + "." + getFileExtensions(imageUri));
            riverseRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    final String sdownload_url = String.valueOf(downloadUrl);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imageProfile", sdownload_url);

                    progressDialog.dismiss();
                    firestore.collection("Users").document(firebaseUser.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(ProfileActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void getInfo() {
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName = documentSnapshot.getString("userName");
                String bio = documentSnapshot.getString("bio");
                String phone = documentSnapshot.getString("userPhone");
                String imageProfile = documentSnapshot.getString("imageProfile");

                binding.profileTvPersonName.setText(userName);
                binding.tvAboutContent.setText(bio);
                binding.tvNumber.setText(phone);
                Glide.with(ProfileActivity.this).load(imageProfile).into(binding.profileUserProfileImage);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error message", "error" + e.getMessage());
            }
        });
    }
}