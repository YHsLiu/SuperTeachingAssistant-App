package abc.project.projectcheckinapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import abc.project.projectcheckinapp.databinding.ActivityTeacherBinding;
import abc.project.projectcheckinapp.Other.ActionBarTitleSetter;
import abc.project.projectcheckinapp.Fragment.Teacher.NewClassFragment;
import abc.project.projectcheckinapp.Fragment.Teacher.ReviseTchDataFragment;
import abc.project.projectcheckinapp.Fragment.Teacher.SelectRoomFragment;
import abc.project.projectcheckinapp.Fragment.Teacher.TeacherMainFragment;

public class TeacherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , ActionBarTitleSetter {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTeacherBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor contextEditor;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.appBarSecond.toolbar);
        /*binding.appBarSecond.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tec_main, R.id.nav_tec_newclass, R.id.nav_tec_enter, R.id.nav_reviseTchData)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_second);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_reportQA:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mailto:service@gmail.com"));
                startActivity(intent);
                break;

            case R.id.action_logout:
                Intent intent2 = new Intent(TeacherActivity.this, WelcomeActivity.class);
                preferences = getSharedPreferences("userInfo",MODE_PRIVATE);
                contextEditor = preferences.edit();
                contextEditor.putInt("sid",0);
                contextEditor.putInt("tid",0);
                contextEditor.putInt("cid",0);
                contextEditor.putBoolean("isRollCall",false);
                contextEditor.putBoolean("isLogin",false);
                contextEditor.putBoolean("mesToEnter",false);
                contextEditor.apply();
                SQLiteDatabase db = openOrCreateDatabase("allList",MODE_PRIVATE,null);
                db.close();
                startActivity(intent2);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_second);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_tec_main:
                fragment = new TeacherMainFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_tec_main,fragment).commit();
                break;

            case R.id.nav_tec_newclass:
                fragment = new NewClassFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_tec_newclass,fragment).commit();
                break;

            case R.id.nav_selectRoom:
                fragment = new SelectRoomFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_selectRoom,fragment).commit();
                break;

            case R.id.nav_reviseTchData:
                fragment = new ReviseTchDataFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_reviseTchData,fragment).commit();
                break;

        }
        return true;
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        contextEditor = preferences.edit();
        contextEditor.putInt("sid",0);
        contextEditor.putInt("tid",0);
        contextEditor.putInt("cid",0);
        contextEditor.putBoolean("isRollCall",false);
        contextEditor.putBoolean("isLogin",false);
        contextEditor.putBoolean("mesToEnter",false);
        contextEditor.apply();
    }
}