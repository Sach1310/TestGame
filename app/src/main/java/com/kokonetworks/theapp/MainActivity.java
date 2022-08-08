package com.kokonetworks.theapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private Field field;
    private TextView tvLevel;
    private TextView tvScore;

    private Button btnStart;
    private TextView tvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = findViewById(R.id.field);
        tvLevel = findViewById(R.id.tvLevel);
        btnStart = findViewById(R.id.btnStart);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        setEventListeners();
    }

    void setEventListeners() {
        btnStart.setOnClickListener(view -> {
            btnStart.setVisibility(View.GONE);
            tvScore.setVisibility(View.GONE);
            field.startGame();
        });

        field.setListener(listener);
    }

    private final Field.Listener listener = new Field.Listener() {

        @Override
        public void onGameEnded(int score) {
            btnStart.setVisibility(View.VISIBLE);
            tvScore.setVisibility(View.VISIBLE);
            tvTimer.setVisibility(View.GONE);
            tvScore.setText(String.format(getString(R.string.your_score), score));
        }

        @Override
        public void onUpdateStore(int score) {
            tvScore.setVisibility(View.VISIBLE);
            tvScore.setText(String.format(getString(R.string.your_score), score));
        }

        @Override
        public void onLevelChange(int level) {
            tvLevel.setText(String.format(getString(R.string.level), level));
        }

        @Override
        public void onTimeChange(long time) {
            if (time == -1) {
                tvTimer.setVisibility(View.GONE);
            } else {
                tvTimer.setVisibility(View.VISIBLE);
                tvTimer.setText("Remaining Time:" + (int)Math.ceil(time/1000f));
            }
        }
    };
}