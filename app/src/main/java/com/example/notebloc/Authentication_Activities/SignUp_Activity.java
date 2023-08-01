package com.example.notebloc.Authentication_Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.notebloc.MainActivity;
import com.example.notebloc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUp_Activity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private EditText mloginEmail_edt, mloginPassword_edt;
    private RelativeLayout mSignupButton_holder, mgotosignup_activity;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mloginEmail_edt = findViewById(R.id.loginEmail_edt);
        mloginPassword_edt = findViewById(R.id.loginPassword_edt);
        mSignupButton_holder = findViewById(R.id.SignupButton_holder);
        mgotosignup_activity = findViewById(R.id.gotosignup_activity);

        mAuth = FirebaseAuth.getInstance();



        mgotosignup_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        mSignupButton_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = String.valueOf(mloginEmail_edt.getText()).trim();
                password = String.valueOf(mloginPassword_edt.getText()).trim();


                if(email.isEmpty()) {
                    Toast.makeText(SignUp_Activity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }
                else  if(password.length()<7) {
                    Toast.makeText(SignUp_Activity.this, "Password Must be Greater then Seven Characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                Toast.makeText(SignUp_Activity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                sendEmailVerification();


                            } else {
                                Toast.makeText(SignUp_Activity.this, "Failed to Register+", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser != null) {
            mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(SignUp_Activity.this,
                            "Verification Sent to Email",
                            Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                    startActivity(intent);
                    finish();


                }
            });
        } else {
            Toast.makeText(SignUp_Activity.this,
                    "Failed to send Verification Mail",
                    Toast.LENGTH_SHORT).show();

        }
    }


}