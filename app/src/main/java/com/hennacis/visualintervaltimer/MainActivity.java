package com.hennacis.visualintervaltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Chronometer.OnChronometerTickListener {

    private int totalIntervalLength, totalIimerLength = 0;
    private int minsLength, secsLength, minsInterval, secsInterval = 0;
    private long pauseOffset;
    private long prevInterval;
    private boolean running;

    private RelativeLayout mLayoutBG;
    private LinearLayout mLayoutPlusMinus;
    private EditText mTxtMinutesLength, mTxtSecondsLength,
            mTxtMinutesInterval, mTxtSecondsInterval;
    private Chronometer chronometer;
    private CircleView mCircleView;
    private Button mStartBtn;
    private ImageButton mBtnMinsPlusLength, mBtnMinsMinusLength, mBtnSecsPlusLength, mBtnSecsMinusLength,
            mBtnMinsPlusInterval, mBtnMinsMinusInterval, mBtnSecsPlusInterval, mBtnSecsMinusInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mLayoutBG = findViewById(R.id.layout);
        mLayoutPlusMinus = findViewById(R.id.llPlusMinus);
        mTxtMinutesLength = findViewById(R.id.txt_minutes_length);
        mTxtSecondsLength = findViewById(R.id.txt_seconds_length);
        mTxtMinutesInterval = findViewById(R.id.txt_minutes_interval);
        mTxtSecondsInterval = findViewById(R.id.txt_seconds_interval);
        chronometer = findViewById(R.id.chronometer);
        mCircleView = findViewById(R.id.circle);
        mStartBtn = findViewById(R.id.btn_timer_start);
        mBtnMinsPlusLength = findViewById(R.id.btn_plus_mins_length);
        mBtnMinsMinusLength = findViewById(R.id.btn_minus_mins_length);
        mBtnSecsPlusLength = findViewById(R.id.btn_plus_secs_length);
        mBtnSecsMinusLength = findViewById(R.id.btn_minus_secs_length);

        mBtnMinsPlusInterval = findViewById(R.id.btn_plus_mins_interval);
        mBtnMinsMinusInterval = findViewById(R.id.btn_minus_mins_interval);
        mBtnSecsPlusInterval = findViewById(R.id.btn_plus_secs_interval);
        mBtnSecsMinusInterval = findViewById(R.id.btn_minus_secs_interval);

        chronometer.setVisibility(View.INVISIBLE);

        setListeners();
    }

    public void setListeners() {
        mStartBtn.setOnClickListener(this);
        mBtnMinsPlusLength.setOnClickListener(this);
        mBtnMinsMinusLength.setOnClickListener(this);
        mBtnSecsPlusLength.setOnClickListener(this);
        mBtnSecsMinusLength.setOnClickListener(this);

        mBtnMinsPlusInterval.setOnClickListener(this);
        mBtnMinsMinusInterval.setOnClickListener(this);
        mBtnSecsPlusInterval.setOnClickListener(this);
        mBtnSecsMinusInterval.setOnClickListener(this);

        mTxtMinutesLength.addTextChangedListener(generalTextWatcher);
        mTxtSecondsLength.addTextChangedListener(generalTextWatcher);
        mTxtMinutesInterval.addTextChangedListener(generalTextWatcher);
        mTxtSecondsInterval.addTextChangedListener(generalTextWatcher);
        chronometer.setOnChronometerTickListener(this);
    }

    public void complete() {
        disableRunMode();
        AlphaAnimation blinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(1000); // duration - half a second
        blinkanimation.setRepeatCount(3); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        mLayoutBG.setBackgroundColor(Color.parseColor("#64DD17")); //green
        mLayoutBG.startAnimation(blinkanimation);
        chronometer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_timer_start: {
                if (!running) {
                    enableRunMode();
                } else {
                    disableRunMode();
                }
                break;
            }
            case R.id.btn_plus_mins_length: {
                minsLength++;
                mTxtMinutesLength.setText(String.valueOf(minsLength));
                break;
            }
            case R.id.btn_minus_mins_length: {
                if (minsLength > 0) {
                    minsLength--;
                    mTxtMinutesLength.setText(String.valueOf(minsLength));
                } else {
                    minsLength = 59;
                    mTxtMinutesLength.setText(String.valueOf(minsLength));
                }
                break;
            }
            case R.id.btn_plus_secs_length: {
                secsLength++;
                mTxtSecondsLength.setText(String.valueOf(secsLength));
                break;
            }
            case R.id.btn_minus_secs_length: {
                if (secsLength > 0) {
                    secsLength--;
                    mTxtSecondsLength.setText(String.valueOf(secsLength));
                } else {
                    secsLength = 59;
                    mTxtSecondsLength.setText(String.valueOf(secsLength));
                }
                break;
            }


            case R.id.btn_plus_mins_interval: {
                minsInterval++;
                mTxtMinutesInterval.setText(String.valueOf(minsInterval));
                break;
            }
            case R.id.btn_minus_mins_interval: {
                if (minsInterval > 0) {
                    minsInterval--;
                    mTxtMinutesInterval.setText(String.valueOf(minsInterval));
                } else {
                    minsInterval = 59;
                    mTxtMinutesInterval.setText(String.valueOf(minsInterval));
                }
                break;
            }
            case R.id.btn_plus_secs_interval: {
                secsInterval++;
                mTxtSecondsInterval.setText(String.valueOf(secsInterval));
                break;
            }
            case R.id.btn_minus_secs_interval: {
                if (secsInterval > 0) {
                    secsInterval--;
                    mTxtSecondsInterval.setText(String.valueOf(secsInterval));
                } else {
                    secsInterval = 59;
                    mTxtSecondsInterval.setText(String.valueOf(secsInterval));
                }
                break;
            }
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        if (running && SystemClock.elapsedRealtime() - chronometer.getBase() >= TimeUnit.SECONDS.toMillis(totalIimerLength)) {
            complete();
        } else if (running && (SystemClock.elapsedRealtime() - prevInterval) >= TimeUnit.SECONDS.toMillis(totalIntervalLength)) {
            mLayoutBG.setBackgroundColor(mCircleView.getColor());
            prevInterval = SystemClock.elapsedRealtime();
            //chronometer.setBase(SystemClock.elapsedRealtime());
            mCircleView.start(totalIntervalLength);
        }
    }

    private void enableRunMode() {
        totalIimerLength = (minsLength * 60) + secsLength;
        totalIntervalLength = (minsInterval * 60) + secsInterval;
        if ((totalIimerLength > 0) && (totalIntervalLength > 0)) {
            mLayoutPlusMinus.setVisibility(View.INVISIBLE);
            chronometer.setVisibility(View.VISIBLE);
            mStartBtn.setText("stop");
            mCircleView.start(totalIntervalLength);
            chronometer.setTextColor(Color.WHITE);
            resetChronometer();
            startChronometer();
        }
    }

    private void disableRunMode() {
        pauseChronometer();
        mLayoutPlusMinus.setVisibility(View.VISIBLE);
        mLayoutBG.setBackgroundColor(getResources().getColor(R.color.colorDark));
        chronometer.setVisibility(View.INVISIBLE);
        mStartBtn.setText("start");
        mCircleView.stop();
    }

    public void startChronometer() {
        if (!running) {
            prevInterval = SystemClock.elapsedRealtime();
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    private TextWatcher generalTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mTxtSecondsLength.getText().hashCode() == s.hashCode()) {
                if (count > 0) {
                    secsLength = Integer.parseInt(s.toString());
                } else {
                    secsLength = 0;
                }
            } else if (mTxtMinutesLength.getText().hashCode() == s.hashCode()) {
                if (count > 0) {
                    minsLength = Integer.parseInt(s.toString());
                } else {
                    minsLength = 0;
                }
            } else if (mTxtSecondsInterval.getText().hashCode() == s.hashCode()) {
                if (count > 0) {
                    secsInterval = Integer.parseInt(s.toString());
                } else {
                    secsInterval = 0;
                }
            } else if (mTxtMinutesInterval.getText().hashCode() == s.hashCode()) {
                if (count > 0) {
                    minsInterval = Integer.parseInt(s.toString());
                } else {
                    minsInterval = 0;
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}