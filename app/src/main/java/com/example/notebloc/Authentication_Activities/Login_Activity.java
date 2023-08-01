package com.example.notebloc.Authentication_Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebloc.MainActivity;
import com.example.notebloc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login_Activity extends AppCompatActivity {

    private EditText mloginEmail_edt, mloginPassword_edt;
    private RelativeLayout mloginButton_holder, mgotosignup_activity;
    private TextView mforgotPassword_txt;

    ProgressBar mLogin_Progressbar;



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

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mloginEmail_edt= findViewById(R.id.loginEmail_edt);
        mloginPassword_edt = findViewById(R.id.loginPassword_edt);
        mloginButton_holder = findViewById(R.id.loginButton_holder);
        mgotosignup_activity = findViewById(R.id.gotosignup_activity);
        mforgotPassword_txt = findViewById(R.id.forgotPassword_txt);
        mLogin_Progressbar = findViewById(R.id.Login_Progressbar);

        mAuth = FirebaseAuth.getInstance();
        //gets current user data
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();


        mgotosignup_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, SignUp_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        mforgotPassword_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, ForgotPassword_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        mloginButton_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(mloginEmail_edt.getText()).trim();
                password = String.valueOf(mloginPassword_edt.getText()).trim();

                if (email.isEmpty() || password.isEmpty()) {

                    Toast.makeText(Login_Activity.this,
                            "Email and Password are Required",
                            Toast.LENGTH_SHORT).show();
                } else {

                    mLogin_Progressbar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                checkMailVerification();

                            } else {
                                Toast.makeText(getApplicationContext(),
                                    "Account Does not Exist",
                                    Toast.LENGTH_SHORT).show();
                                mLogin_Progressbar.setVisibility(View.INVISIBLE);

                            }
                        }
                    });
                }
            }
        });
    }

    private void checkMailVerification() {
          FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

          if(mFirebaseUser.isEmailVerified() == true) {
              Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
              finish();
              startActivity(new Intent(Login_Activity.this, MainActivity.class));

          }    else {
              mLogin_Progressbar.setVisibility(View.INVISIBLE);
              Toast.makeText(this, "Verify your Email First", Toast.LENGTH_SHORT).show();
              mAuth.signOut();
          }
    }
}