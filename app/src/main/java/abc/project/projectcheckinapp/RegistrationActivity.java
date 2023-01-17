package abc.project.projectcheckinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import abc.project.projectcheckinapp.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}