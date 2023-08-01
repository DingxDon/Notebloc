package com.example.notebloc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteActivity extends AppCompatActivity {

    Intent data;

    FirebaseAuth mfirebaseAuth;
    FirebaseFirestore mfirebaseFirestore;
    FirebaseUser mfirebaseUser;
    EditText medittitleofnote, meditcontentofnote;
    FloatingActionButton msavednote;
    AppCompatButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        medittitleofnote =findViewById(R.id.edittitleofnote);
        meditcontentofnote = findViewById(R.id.editcontentofnote);
        data = getIntent();
        msavednote = findViewById(R.id.saveitnote);
        back_btn = findViewById(R.id.edtback_btn);
        mfirebaseFirestore = FirebaseFirestore.getInstance();
        mfirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String notetitle = data.getStringExtra("title");
        String notecontent =data.getStringExtra("content");
        medittitleofnote.setText(notetitle);
        meditcontentofnote.setText(notecontent);

        msavednote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newtitle = medittitleofnote.getText().toString();
                String newcontent = meditcontentofnote.getText().toString();

                if (newtitle.isEmpty() || newcontent.isEmpty()) {
                    Toast.makeText(editnoteActivity.this, "Atleast one Field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    DocumentReference documentReference = mfirebaseFirestore
                            .collection("notes").document(mfirebaseUser.getUid())
                            .collection("myNotes")
                            .document(data.getStringExtra("noteid"));

                    Map<String, Object> note =new HashMap<>();
                    note.put("title", newtitle);
                    note.put("content",newcontent);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(editnoteActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editnoteActivity.this, "Fail to Update", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });






    }
}