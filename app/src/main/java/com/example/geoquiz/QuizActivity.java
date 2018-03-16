package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String QUESTION_BANK = "question_bank";

    private static final int RESULT_CODE_CHEAT = 0;

    public static final int PREV = 0;
    public static final int NEXT = 1;
    public static final int CURRENT = 2;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPreButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_ameicas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Log.d(TAG, "onCreate(Bundle) method called.");

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionBank = (Question[]) savedInstanceState.getSerializable(QUESTION_BANK);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion(NEXT);
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        updateCheatBtn(mQuestionBank[mCurrentIndex].getCheatNum());

        mPreButton = (ImageButton) findViewById(R.id.pre_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionBank[mCurrentIndex].getCheatNum() <= 0) {
                    toastMsg("本题的作弊次数用完了！还不记得答案，该背时了哦。");
                } else {
                    Intent intent = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex]);
                    startActivityForResult(intent, RESULT_CODE_CHEAT);
                }
            }
        });

        mPreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion(PREV);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion(NEXT);
            }
        });

        updateQuestion(CURRENT);
    }

    private void checkAnswer(boolean userAnswer) {
        Question question = mQuestionBank[mCurrentIndex];
        if (question.isCheated()) {
            toastMsg("这道题你作弊了！");
        }
        if (userAnswer == question.isAnswerTrue()) {
            answerIsTrue();
        } else {
            answerIsFalse();
        }
        question.setAnswered(true);
        question.setUserAnswerTrue(userAnswer);
        question.setQuestionAnswerTrue(userAnswer == question.isAnswerTrue());
        mQuestionBank[mCurrentIndex] = question;
    }

    private void updateQuestion(int orientation) {
        switch (orientation) {
            case PREV:
                if (mCurrentIndex == 0) {
                    toastMsg("已经是第一道题了");
                } else {
                    mCurrentIndex--;
                }
                break;
            case NEXT:
                if (mCurrentIndex == mQuestionBank.length - 1) {
                    countPercent();
                } else {
                    mCurrentIndex++;
                }
                break;
            default:
                break;
        }
        Question question = mQuestionBank[mCurrentIndex];
        int questionTextResId = question.getTextResId();
        mQuestionTextView.setText(questionTextResId);

        if (question.isAnswered() && mTrueButton.isEnabled()) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else if (!question.isAnswered() && !mTrueButton.isEnabled()){
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }

        updateCheatBtn(question.getCheatNum());
    }

    private final void answerIsTrue() {
        Toast toast = Toast.makeText(this, R.string.answer_true, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                mNextButton.callOnClick();
            }
        });
        toast.show();
    }

    private final void answerIsFalse() {
        Toast toast = Toast.makeText(this, R.string.answer_false, Toast.LENGTH_SHORT);
        View view = toast.getView();

        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                mNextButton.callOnClick();
            }
        });

        toast.show();
    }

    private void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void countPercent() {
        int count = 0;
        for (Question question : mQuestionBank) {
            if (question.isQuestionAnswerTrue()) {
                count++;
            }
        }
        double truePercent = new BigDecimal((double) count / mQuestionBank.length * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        toastMsg("您答对了：" + count + " 道题，占总题数的：" +  truePercent + "%。");
    }

    private void updateCheatBtn(int num) {
        mCheatButton.setText("Can Cheat ( " + num + " )");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        * @requestCode: 请求码，标识由哪个子activity返回
        * @resultCode： 结果码，标识子activity执行的结果状态（常用 Activity.RESULT_OK: 表示成功； Activity.RESULT_CANCELED： 表示失败）
        * @data： Intent对象，用于传递信息
        * */
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_CODE_CHEAT) {
            if (data != null) {
                Question answerShownQuestion = CheatActivity.wasAnswerShown(data);
                mQuestionBank[mCurrentIndex] = answerShownQuestion;
                updateCheatBtn(answerShownQuestion.getCheatNum());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() method called.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() method called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() method called.");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState method called.");

        outState.putInt(KEY_INDEX, mCurrentIndex);

        outState.putSerializable(QUESTION_BANK, mQuestionBank);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() method called.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() method called.");
    }
}
