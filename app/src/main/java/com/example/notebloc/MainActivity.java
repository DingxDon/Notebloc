package com.example.notebloc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebloc.Authentication_Activities.Login_Activity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mcreatenote_fab;
    private FirebaseAuth mAuth;




    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager mstaggeredGridLayoutManager;
    FirebaseUser mFirebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mcreatenote_fab = findViewById(R.id.createnote_fab);

        // to get data to recycler view
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        firebaseFirestore = FirebaseFirestore.getInstance();

        mcreatenote_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CreateNote_Activity.class);
                startActivity(intent);

            }
        });

        Query query = firebaseFirestore.collection("notes").document(mFirebaseUser.getUid())
                .collection("myNotes").orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes =
                new FirestoreRecyclerOptions.Builder<firebasemodel>()
                        .setQuery(query, firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {

            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {

                ImageView popupbutton = holder.itemView.findViewById(R.id.menupopupbutton);

                int colorcode=getRandomColor();
                holder.mnote.setBackgroundColor(holder.itemView.getResources().getColor(colorcode, null));
                holder.notetile.setText(model.getTitle());
                holder.notecontent.setText(model.getContent());

                // get id for delete / update
                String docId= noteAdapter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), notedetails.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("content", model.getContent());
                        intent.putExtra("noteid",docId);
                        v.getContext().startActivity(intent);
                    }
                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem item) {

                                Intent intent = new Intent(getApplicationContext(), editnoteActivity.class);
                                intent.putExtra("title", model.getTitle());
                                intent.putExtra("content", model.getContent());
                                intent.putExtra("noteid",docId);
                                v.getContext().startActivity(intent);
                                finish();

                                return false;


                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem item) {

                                DocumentReference documentReference =firebaseFirestore.collection("notes")
                                        .document(mFirebaseUser.getUid()).collection("myNotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Not Deleted", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                return false;
                            }
                        });
                        popupMenu.show();

                    }
                });


            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        mRecyclerView = findViewById(R.id.notes_rcvw);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mstaggeredGridLayoutManager= new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mstaggeredGridLayoutManager);
        mRecyclerView.setAdapter(noteAdapter);


    }




    public static class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView notetile;
        private TextView notecontent;
        LinearLayout mnote;



        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetile = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter != null ) {
            noteAdapter.stopListening();
        }
    }


    private int getRandomColor() {

        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.color4);
        colorcode.add(R.color.color5);
        colorcode.add(R.color.color6);
        colorcode.add(R.color.color7);

        Random random =new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);
    }
}