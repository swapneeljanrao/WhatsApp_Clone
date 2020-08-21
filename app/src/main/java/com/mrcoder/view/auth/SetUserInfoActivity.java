package com.mrcoder.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrcoder.MainActivity;
import com.mrcoder.R;
import com.mrcoder.databinding.ActivitySetUserInfoBinding;
import com.mrcoder.model.users.Users;

public class SetUserInfoActivity extends AppCompatActivity {

    private ActivitySetUserInfoBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_user_info);
        progressDialog = new ProgressDialog(this);
        initButtonClick();

    }

    private void initButtonClick() {
        binding.btnProfileInfoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etPersonName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "enter username", Toast.LENGTH_SHORT).show();

                } else {
                    doUpdate();
                }
            }
        });

        binding.profileImageProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                picImage();

            }
        });
    }

    private void doUpdate() {
        progressDialog.setMessage("Updating Info");
        progressDialog.show();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userID = firebaseUser.getUid();
            Users users = new Users(userID, binding.etPersonName.getText().toString(), firebaseUser.getPhoneNumber(), "", "", "", "", "", "", "");
            firebaseFirestore.collection("Users").document(firebaseUser.getUid()).set(users)
//                    update("userName", "binding")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
                        }
                    });
            //Users users = new Users(userID, "", user.getPhoneNumber(), "", "", "", "", "", "", "");
        } else {

            Toast.makeText(getApplicationContext(), "You need to Login first", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}