package abc.project.projectcheckinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import abc.project.projectcheckinapp.databinding.ActivityRealMainBinding;
import abc.project.projectcheckinapp.ui.home.HomeFragment;

public class RealMainActivity extends AppCompatActivity {

    private ActivityRealMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRealMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent = new Intent(RealMainActivity.this, RegistrationActivity.class);
                startActivity(intent);


            }
        });


    }
}