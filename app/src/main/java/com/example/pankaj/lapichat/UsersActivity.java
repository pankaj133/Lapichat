package com.example.pankaj.lapichat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    Toolbar user_toolbar;
    RecyclerView mrecyclerView;

    private DatabaseReference mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

    user_toolbar=findViewById(R.id.Users_aapbar);
    setSupportActionBar(user_toolbar);
    getSupportActionBar().setTitle("All USERS");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    //recycler view id
    mrecyclerView=findViewById(R.id.recycler);
    mrecyclerView.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mrecyclerView.getContext(),linearLayoutManager.getOrientation());
    mrecyclerView.setLayoutManager(linearLayoutManager);
    mrecyclerView.addItemDecoration(dividerItemDecoration);
    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Users> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mdatabase,Users.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<Users,UsersViewHolder> abc = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
                holder.setName(model.getName());
                holder.setStatus(model.getStatus());

            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new UsersViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_layout,parent,false));
            }
        };
        mrecyclerView.setAdapter(abc);
    }
    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mview;

        public UsersViewHolder(View itemView) {
            super(itemView);

        mview =itemView;
        }
        public void setName(String name) {
            TextView textView = mview.findViewById(R.id.single_diplay_name);
            textView.setText(name);
        }

        public void setStatus(String status) {

        TextView textView= mview.findViewById(R.id.signle_status);
        textView.setText(status);
        }

    }

}
