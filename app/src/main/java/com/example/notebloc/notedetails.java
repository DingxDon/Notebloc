package com.example.notebloc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import javax.security.auth.login.LoginException;

public class notedetails extends AppCompatActivity {

    private TextView mtitleofnotedetail , mcontentofnotedetail;
    FloatingActionButton mgotoeditnote_fab;
    AppCompatButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);


        mtitleofnotedetail =findViewById(R.id.titleofnotedetail);
        mcontentofnotedetail = findViewById(R.id.contentofnotedetail);
        mgotoeditnote_fab = findViewById(R.id.gotoeditnote_fab);
        back_btn = findViewById(R.id.back_btn);

        Toolbar toolbar = findViewById(R.id.toolbarofnotedetail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
//        if(getSupportActionBar() != null ) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//           getSupportActionBar().setDisplayShowHomeEnabled(true);
//       }



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Intent data = getIntent();


        mgotoeditnote_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), editnoteActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteid",data.getStringExtra("noteid"));
                v.getContext().startActivity(intent);


            }
        });

        mcontentofnotedetail.setText(data.getStringExtra("content"));
        mtitleofnotedetail.setText(data.getStringExtra("title"));


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onContextItemSelected(item);
    }
}