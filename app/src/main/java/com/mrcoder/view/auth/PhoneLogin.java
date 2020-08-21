package com.mrcoder.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrcoder.MainActivity;
import com.mrcoder.R;
import com.mrcoder.databinding.ActivityPhoneLoginBinding;
import com.mrcoder.model.users.Users;

import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {

    private ActivityPhoneLoginBinding binding;
    private static String TAG = "PhoneLoginActivity";

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ProgressDialog progressDialog;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_login);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);

        if (firebaseUser != null) {
            startActivity(new Intent(PhoneLogin.this, SetUserInfoActivity.class));
        }

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btnNext.getText().toString().equals("Next")) {
                    String phone = "+" + binding.etCountryISDCode.getText().toString() + binding.etPhone.getText().toString();
                    startPhoneNumberVerification(phone);
                } else {
                    progressDialog.setMessage("Verifying..");
                    progressDialog.show();
                    verifyPhoneNumberWithCode(mVerificationId, binding.etCountryISDCode.getText().toString());
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted : Completed");
                signInWithPhoneAuthCredential(phoneAuthCredential);
                progressDialog.dismiss();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: " + e.getMessage());

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;
                binding.btnNext.setText("Confirm");
                progressDialog.dismiss();
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential : success");
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(PhoneLogin.this, SetUserInfoActivity.class));
                            /*if (user != null) {
                                String userID = user.getUid();
                                Users users = new Users(userID, "", user.getPhoneNumber(), "", "", "", "", "", "", "");
                                firestore.collection("Users").document("UserInfo").collection(userID).add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }*/

                        } else {
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential : failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.d(TAG, "onComplete : Error Code");
                            }
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
}