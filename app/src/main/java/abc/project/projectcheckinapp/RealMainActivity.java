package abc.project.projectcheckinapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import abc.project.projectcheckinapp.databinding.ActivityRealMainBinding;
import abc.project.projectcheckinapp.ui.home.HomeFragment;

public class RealMainActivity extends AppCompatActivity {

    ActivityRealMainBinding RMbinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RMbinding = ActivityRealMainBinding.inflate(getLayoutInflater());
        setContentView(RMbinding.getRoot());

        //處理登入按鈕
        RMbinding.btnMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳轉到登入畫面
                Intent intent = new Intent();
                intent = new Intent(RealMainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

    }



    //建立 OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //處理 menu-回報問題, 會開啟Gmail並寄郵件給指定email address
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() ) {
            case R.id.main_report:

                Intent intentMaintain = new Intent();
                intentMaintain.setAction(Intent.ACTION_VIEW);
                intentMaintain.setData(Uri.parse("mailto:klun.C88@gmail.com"));
                startActivity(intentMaintain);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}