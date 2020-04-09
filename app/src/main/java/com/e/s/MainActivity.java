package com.e.s;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;
    RecyclerView my_contact_list;
    ImageView findpeoplebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        navView= findViewById (R.id.nav_view);
navView.setOnNavigationItemSelectedListener ( NavigationItemSelectedListener);
my_contact_list=findViewById (R.id.contact_list);
findpeoplebtn=findViewById (R.id.find_people_btn);
        my_contact_list.setHasFixedSize (true);
        my_contact_list.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));

        findpeoplebtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent (MainActivity.this,Find_peopleActivity.class);
                startActivity (intent);
            }
        });
    }
private  BottomNavigationView.OnNavigationItemSelectedListener NavigationItemSelectedListener
        =new BottomNavigationView.OnNavigationItemSelectedListener () {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId ())
            {
                case R.id.navigation_setting:
                Intent i=new Intent (MainActivity.this,SettingActivity.class);
                startActivity (i);
                    break;
                case R.id.navigation_notifications:
                    Intent intentnotifications=new Intent (MainActivity.this,NotificationActivity.class);
                    startActivity (intentnotifications);
                    break;
                case R.id.navigation_logout:
                    FirebaseAuth.getInstance ().signOut ();
                    Intent intentlogout=new Intent (MainActivity.this,MainActivity.class);
                    startActivity (intentlogout);
                    break;
            }

            return true;

      }
    };
}
