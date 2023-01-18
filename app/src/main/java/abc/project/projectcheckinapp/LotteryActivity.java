package abc.project.projectcheckinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import abc.project.projectcheckinapp.databinding.ActivityLotteryBinding;

public class LotteryActivity extends AppCompatActivity {

    ActivityLotteryBinding binding;
    Intent intent;
    Button lotteryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLotteryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 要從前面的Intent帶入cid
        Bundle objGetBundle = this.getIntent().getExtras();
        int cid = objGetBundle.getInt("cid");

        lotteryBtn = binding.buttonLottery;
        lotteryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                try {
                    packet.put("type",1);
                    packet.put("cid",cid);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}