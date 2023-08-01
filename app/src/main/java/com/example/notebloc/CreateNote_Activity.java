package com.example.notebloc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateNote_Activity extends AppCompatActivity {

    EditText mcreatetitlefornote, mcreatenoteforMainText;
    FloatingActionButton msavenote_fab;
    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar mCreatenote_Progressbar;

    AppCompatButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);



        msavenote_fab = findViewById(R.id.savenote_fab);
        mcreatetitlefornote = findViewById(R.id.createtitlefornote);
        mcreatenoteforMainText = findViewById(R.id.createnoteforMainText);
        mCreatenote_Progressbar = findViewById(R.id.Createnote_Progressbar);
        back_btn = findViewById(R.id.back_btn);
        // Toolbar

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Firebase instances
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        msavenote_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mcreatetitlefornote.getText().toString();
                String content = mcreatenoteforMainText.getText().toString();

                mAuth = FirebaseAuth.getInstance();

                if(title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(CreateNote_Activity.this,
                            "Both Fields Are required", Toast.LENGTH_SHORT).show();
                } else  {

                    mCreatenote_Progressbar.setVisibility(View.VISIBLE);

                    // main code to Store data to database
                    DocumentReference documentReference = firebaseFirestore
                            .collection("notes").document(mFirebaseUser.getUid())
                            .collection("myNotes").document();

                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content", content);


                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateNote_Activity.this,
                                    "Note Created Succesfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(CreateNote_Activity.this,
                                    MainActivity.class));
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNote_Activity.this,
                                    "Failed to Create Note", Toast.LENGTH_SHORT).show();
                            mCreatenote_Progressbar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }
                    });


                }
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onContextItemSelected(item);
    }
}