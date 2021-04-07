package com.dvlpr.findme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.dvlpr.findme.fragments.Buscar;
import com.dvlpr.findme.fragments.Feed;
import com.dvlpr.findme.fragments.NewPost;
import com.dvlpr.findme.fragments.User;
import com.dvlpr.findme.fragments.Welcome;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Params;

import java.util.List;


public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new Feed()).commit();

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccentLight));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));

        bottomNavigationView = findViewById(R.id.bottom_navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(itemSelectedListener);

    }

    BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.page_feed:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new Feed()).commit();
                    break;
                case R.id.page_search:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new Buscar()).commit();
                    break;
                case R.id.page_new_post:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new NewPost()).commit();
                    break;
                case R.id.page_user_info:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new User()).commit();
                    break;
            }
            return true;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}