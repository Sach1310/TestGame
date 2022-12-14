package com.kokonetworks.theapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

class Field extends LinearLayout {
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final SquareButton[] circles = new SquareButton[9];
    private int currentCircle;
    private Listener listener;

    private int score;
    private Mole mole;

    private final int ACTIVE_TAG_KEY = 873374234;
    private boolean isStarted;
    private boolean isTap = true;

    public Field(Context context) {
        super(context);
        initializeViews(context);
    }

    public Field(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public Field(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public Field(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    public int totalCircles() {
        return circles.length;
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_field, this, true);

        circles[0] = (SquareButton) findViewById(R.id.hole1);
        circles[1] = (SquareButton) findViewById(R.id.hole2);
        circles[2] = (SquareButton) findViewById(R.id.hole3);
        circles[3] = (SquareButton) findViewById(R.id.hole4);
        circles[4] = (SquareButton) findViewById(R.id.hole5);
        circles[5] = (SquareButton) findViewById(R.id.hole6);
        circles[6] = (SquareButton) findViewById(R.id.hole7);
        circles[7] = (SquareButton) findViewById(R.id.hole8);
        circles[8] = (SquareButton) findViewById(R.id.hole9);

    }

    private void resetScore() {
        score = 0;
    }

    public void startGame() {
        resetScore();
        resetCircles();
        for (SquareButton squareButton : circles) {
            squareButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isStarted) {
                        boolean active = (boolean) view.getTag(ACTIVE_TAG_KEY);
                        if (active && !isTap) {
                            isTap = true;
                            score += mole.getCurrentLevel() * 2;
                            listener.onUpdateStore(score);
                        } else {
                            isStarted = false;
                            squareButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_oval));
                            mole.stopHopping();
                            listener.onGameEnded(score);
                        }
                    }
                }
            });
        }

        mole = new Mole(this);
        mole.startHopping();
    }

    public int getCurrentCircle() {
        return currentCircle;
    }

    private void resetCircles() {
        for (SquareButton squareButton : circles) {
            squareButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.hole_inactive));
            squareButton.setTag(ACTIVE_TAG_KEY, false);
        }
    }

    public void setActive(int index,int emptyHole) {
        mainHandler.post(() -> {
            boolean isContinue = setTapAndContinue();
            if (isContinue) {
                resetCircles();
                circles[index].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.hole_active));
                circles[index].setTag(ACTIVE_TAG_KEY, true);
                currentCircle = index;
                circles[emptyHole].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.baseline_blind));
            }
        });
    }

    private boolean setTapAndContinue() {
        if (isTap) {
            isTap = false;
            return true;
        } else {
            isStarted = false;
            mole.stopHopping();
            listener.onGameEnded(score);
            return false;
        }
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setGameStarted(boolean isStarted) {
        this.isTap = true;
        this.isStarted = isStarted;
    }

    public interface Listener {
        void onGameEnded(int score);

        void onUpdateStore(int score);

        void onLevelChange(int level);

        void onTimeChange(long time);
    }
}