package com.example.geoquiz;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class Question implements Serializable {
    //问题的文本id
    private int mTextResId;
    //问题的正确答案
    private boolean mAnswerTrue;
    //表示这个问题是否已经被回答
    private boolean mAnswered = false;
    //用户回答的答案
    private boolean mUserAnswerTrue;
    //表示用户回答的答案是否正确
    private boolean mQuestionAnswerTrue;
    //表示本题用户是否作弊偷看了答案
    private boolean mCheated = false;
    //记录用户还可以作弊的次数
    private int mCheatNum = 3;

    public Question() {}

    public Question(int textResid, boolean answerTrue){
        mTextResId = textResid;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswered() {
        return mAnswered;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }

    public boolean isUserAnswerTrue() {
        return mUserAnswerTrue;
    }

    public void setUserAnswerTrue(boolean userAnswerTrue) {
        mUserAnswerTrue = userAnswerTrue;
    }

    public boolean isQuestionAnswerTrue() {
        return mQuestionAnswerTrue;
    }

    public void setQuestionAnswerTrue(boolean questionAnswerTrue) {
        mQuestionAnswerTrue = questionAnswerTrue;
    }

    public boolean isCheated() {
        return mCheated;
    }

    public void setCheated(boolean cheated) {
        mCheated = cheated;
    }

    public int getCheatNum() {
        return mCheatNum;
    }

    public void setCheatNum(int cheatNum) {
        mCheatNum = cheatNum;
    }
}
