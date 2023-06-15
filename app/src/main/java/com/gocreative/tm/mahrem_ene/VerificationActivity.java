package com.gocreative.tm.mahrem_ene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    Button accept, wrongNumber;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId, userPhoneNumber;
    private PinView pinFromUser;
    private TextView info;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        accept = findViewById(R.id.accept);

//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(VerificationActivity.this, FillInfo2Activity.class));
//            }
//        });
        pinFromUser = findViewById(R.id.verification_pin);
        wrongNumber = findViewById(R.id.wrong_number_button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Biraz garaşyň...");

        mAuth = FirebaseAuth.getInstance();

        userPhoneNumber = getIntent().getStringExtra("number");
        // Setting phone number to text view
        info = findViewById(R.id.verification_info_text);
        String infoText = userPhoneNumber + " " + info.getText().toString();
        info.setText(infoText);

        wrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerificationActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @org.jetbrains.annotations.NotNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null){
                    pinFromUser.setText(code);
                    verifyCode(code);
                }
            }
            @Override
            public void onVerificationFailed(@NonNull @org.jetbrains.annotations.NotNull FirebaseException e) {
                Toast.makeText(VerificationActivity.this, "Näsazlyk ýüze çykdy!", Toast.LENGTH_SHORT).show();
                Log.d("Phone otp", "onVerificationFailed: " + e.getMessage());
            }
            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, token);
                mVerificationId = s;
            }
        };
        sendVerificationCode(userPhoneNumber);
    }
    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyCode(String code) {
        progressDialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = fStore.collection("users").document(currentUid);

                    Map<String, Object> info = new HashMap<>();
                    info.put("user_id", currentUid);
                    info.put("phone_number", userPhoneNumber);

                    documentReference.set(info, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(VerificationActivity.this, FillInfoActivity.class);
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        }
                    });

                } else{
                    progressDialog.dismiss();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(VerificationActivity.this, "Tassyklaýyşda näsazlyk ýüze çykdy!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void verifyManually(View view){
        String code = pinFromUser.getText().toString().trim();
        if (!(code.length() < 6)){
            verifyCode(code);
        }
        else{
            Toast.makeText(this, "Tassyklaýyş kodyny doly giriziň!", Toast.LENGTH_SHORT).show();
        }
    }
}