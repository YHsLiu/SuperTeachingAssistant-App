package abc.project.projectcheckinapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutorService;
import abc.project.projectcheckinapp.databinding.ActivityStudentBinding;
import abc.project.projectcheckinapp.databinding.AppBarMainBinding;
import abc.project.projectcheckinapp.rawData.ClickListener;
import abc.project.projectcheckinapp.ui.Student.ClassTableFragment;
import abc.project.projectcheckinapp.ui.Student.EnterClassFragment;
import abc.project.projectcheckinapp.ui.Student.InputCourseCodeFragment;
import abc.project.projectcheckinapp.ui.Student.ReviseStdDataFragment;

public class StudentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityStudentBinding binding;
    private ExecutorService executorService;
    Fragment fragment;
    DrawerLayout drawer;
    SharedPreferences preferences;
    SharedPreferences.Editor contextEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.appBarMain.toolbar);

        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,R.id.nav_inputCourseCode,R.id.nav_EnterClass,R.id.nav_reviseStdData,R.id.nav_classTable)   //影響設定是返回建or Menu
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
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
                Intent intent2 = new Intent(StudentActivity.this,WelcomeActivity.class);
                preferences = getSharedPreferences("userInfo",MODE_PRIVATE);
                contextEditor = preferences.edit();
                contextEditor.putInt("sid",0);
                contextEditor.putInt("tid",0);
                contextEditor.putInt("cid",0);
                contextEditor.putBoolean("isRollCall",false);
                contextEditor.putBoolean("isLogin",false);
                contextEditor.putString("t01","");
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_inputCourseCode:
                fragment = new InputCourseCodeFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_inputCourseCode,fragment).commit();
                break;

            case R.id.nav_EnterClass:
                fragment = new EnterClassFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_EnterClass,fragment).commit();
                break;

            case R.id.nav_classTable:
                fragment = new ClassTableFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_classTable,fragment).commit();
                break;

            case R.id.nav_reviseStdData:
                fragment = new ReviseStdDataFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_reviseStdData,fragment).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}