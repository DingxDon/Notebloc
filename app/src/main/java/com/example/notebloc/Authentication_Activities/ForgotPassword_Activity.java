package com.example.notebloc.Authentication_Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebloc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword_Activity extends AppCompatActivity {

    private EditText mrecoverPassword_edt;
    private AppCompatButton mrecoverPassword_btn;
    private TextView mgobacktologin_tv;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

    mrecoverPassword_edt = findViewById(R.id.recoverPassword_edt);
    mrecoverPassword_btn = findViewById(R.id.passwordRecover_btn);
    mgobacktologin_tv = findViewById(R.id.gobacktologin_tv);


    mAuth = FirebaseAuth.getInstance();

    mgobacktologin_tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        }
    });

    mrecoverPassword_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = String.valueOf(mrecoverPassword_edt.getText()).trim();

            if(TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(),
                        "Enter Email",
                        Toast.LENGTH_SHORT).show();

            }
            else {
                // FirebaseAuth here
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ForgotPassword_Activity.this,
                                    "Mail sent, Open your Mail Application",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent (getApplicationContext(), Login_Activity.class));

                       } else {
                            Toast.makeText(getApplicationContext(),
                                    "Email is Wrong or Account Does not Exist",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    });



    }
}