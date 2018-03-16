package com.example.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Arrays;

import static android.R.attr.radius;

public class CheatActivity extends AppCompatActivity {
    private static final String TAG = "CheatActivity";
    private static final String EXTRA_QUESTION = "com.example.geoquiz.QuizActivity.question";
    private static final String EXTRA_ANSWER_SHOWN_QUESTION = "com.example.geoquiz.CheatActivity.answer_shown_question";

    private static final String CHEATED_QUESTION = "cheated_question";

    private Question mQuestion;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mShowSystemAPILevel;

    public static Intent newIntent(Context packgeContext, Object obj) {
        Intent intent = new Intent(packgeContext, CheatActivity.class);
        intent.putExtra(EXTRA_QUESTION, (Serializable) obj);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowSystemAPILevel = (TextView) findViewById(R.id.system_aip_level);
        mQuestion = (Question) getIntent().getSerializableExtra(EXTRA_QUESTION);

        if (savedInstanceState != null) {
            mQuestion = (Question) savedInstanceState.getSerializable(CHEATED_QUESTION);
            showQuestionAnswer();
            //隐藏显示答案的按钮
            mShowAnswerButton.setVisibility(View.INVISIBLE);
        }

        mShowSystemAPILevel.setText("System API Level " + Build.VERSION.SDK);
//        showSystemInfo();

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示问题答案
                showQuestionAnswer();

                //将问题作弊属性设置为true
                mQuestion.setCheated(true);
                //将可作弊次数减1
                mQuestion.setCheatNum(mQuestion.getCheatNum() - 1);
                Log.d(TAG, "Can Cheat Num: " + mQuestion.getCheatNum());

                //设置作弊按钮不可点击
                buttonAnimHidden(mShowAnswerButton);

                //设置要返回给父activity的信息
                setAnswerShownResult(mQuestion);
            }
        });

        if (mQuestion.isCheated()) {
            //设置要返回给父activity的信息,主要用于应对屏幕旋转返回给父activity的信息丢失的情况
            setAnswerShownResult(mQuestion);
        }
    }

    private void showQuestionAnswer() {
        if (mQuestion.isAnswerTrue()) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    private void buttonAnimHidden(final Button btn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = btn.getWidth() / 2;
            int cy = btn.getHeight() / 2;
            float radius = btn.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(btn, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    btn.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    public static Question wasAnswerShown(Intent data) {
        return (Question) data.getSerializableExtra(EXTRA_ANSWER_SHOWN_QUESTION);
    }

    private void setAnswerShownResult(Question question) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ANSWER_SHOWN_QUESTION, question);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CHEATED_QUESTION, mQuestion);
    }



    private void showSystemInfo() {
        Log.i(TAG, "SDK: " + Build.VERSION.SDK);
        Log.i(TAG, "RELEASE: " + Build.VERSION.RELEASE);
        Log.i(TAG, "BASE_OS: " + Build.VERSION.BASE_OS);
        Log.i(TAG, "CODENAME: " + Build.VERSION.CODENAME);
        Log.i(TAG, "INCREMENTAL: " + Build.VERSION.INCREMENTAL);
        Log.i(TAG, "SECURITY_PATCH: " + Build.VERSION.SECURITY_PATCH);

        Log.i(TAG, "---------------------------------------------------------");

        Log.i(TAG, "BOARD: " + Build.BOARD);
        Log.i(TAG, "BOOTLOADER: " + Build.BOOTLOADER);
        Log.i(TAG, "BRAND: " + Build.BRAND);
        Log.i(TAG, "CPU_ABI: " + Build.CPU_ABI);
        Log.i(TAG, "CPU_ABI2: " + Build.CPU_ABI2);
        Log.i(TAG, "DEVICE: " + Build.DEVICE);
        Log.i(TAG, "DISPLAY: " + Build.DISPLAY);
        Log.i(TAG, "FINGERPRINT: " + Build.FINGERPRINT);
        Log.i(TAG, "HARDWARE: " + Build.HARDWARE);
        Log.i(TAG, "HOST: " + Build.HOST);
        Log.i(TAG, "ID: " + Build.ID);
        Log.i(TAG, "MANUFACTURER: " + Build.MANUFACTURER);
        Log.i(TAG, "MODEL: " + Build.MODEL);
        Log.i(TAG, "PRODUCT: " + Build.PRODUCT);
        Log.i(TAG, "RADIO: " + Build.RADIO);
        Log.i(TAG, "SERIAL: " + Build.SERIAL);
        Log.i(TAG, "SUPPORTED_32_BIT_ABIS: " + Arrays.toString(Build.SUPPORTED_32_BIT_ABIS));
        Log.i(TAG, "SUPPORTED_64_BIT_ABIS: " + Arrays.toString(Build.SUPPORTED_64_BIT_ABIS));
        Log.i(TAG, "SUPPORTED_ABIS: " + Arrays.toString(Build.SUPPORTED_ABIS));
        Log.i(TAG, "TAGS: " + Build.TAGS);
        Log.i(TAG, "TIME: " + Build.TIME);
        Log.i(TAG, "USER: " + Build.USER);
    }
}
