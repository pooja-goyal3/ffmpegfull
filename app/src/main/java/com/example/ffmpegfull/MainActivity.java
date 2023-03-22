package com.example.ffmpegfull;

import static com.example.ffmpegfull.FfmpegConv.ffmpegAudio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button audioBtn;
    private Button videoBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioBtn = findViewById(R.id.audio_btn);
        videoBtn = findViewById(R.id.vidoe_btn);

        audioBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vidoe_btn:
                startActivity(new Intent(this, FfmpegActivity.class));
                break;
            case R.id.audio_btn:
                startActivity(new Intent(this, FfmpegAudioActivity.class));

                break;
        }
    }

}
