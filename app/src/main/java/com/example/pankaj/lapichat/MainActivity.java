package com.example.pankaj.lapichat;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private android.support.v7.widget.Toolbar mtoolbar;

    private ViewPager mviewPager;

    private SectionPagerAdapter mSectionPagerAdapter;

    private TabLayout tabLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //viewpager
        mviewPager=findViewById(R.id.main_tab_pager);

        mSectionPagerAdapter =  new SectionPagerAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(mSectionPagerAdapter);

        tabLayout = findViewById(R.id.main_tab);

        tabLayout.setupWithViewPager(mviewPager);

        mtoolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Lapi Chat");

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToStart();
        }
    }

    private void sendToStart() {
        startActivity(new Intent(MainActivity.this, Start_Activity.class));
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.main_menu,menu);
         return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()==R.id.main_logout_btn){
             FirebaseAuth.getInstance().signOut();
            sendToStart();
         }
         if(item.getItemId()==R.id.users_settings){

             Intent setting = new Intent(MainActivity.this,SettingsActivity.class);
             startActivity(setting);

         }
         if(item.getItemId()==R.id.All_users){

             Intent setting = new Intent(MainActivity.this,UsersActivity.class);
             startActivity(setting);

         }
        return true;
    }
}
