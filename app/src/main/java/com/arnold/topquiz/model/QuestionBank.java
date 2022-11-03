package com.arnold.topquiz.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class QuestionBank implements Serializable {

    private final List<Question> mQuestionList; //Liste de questions qui est de type Question
    private int mQuestionIndex; //index de la question dans la liste des questions

    public QuestionBank(List<Question> questionList) {
        mQuestionList = questionList;
        Collections.shuffle(mQuestionList); //on mélange la liste de questions avant de la stocker
    }

    public Question getCurrentQuestion()
    {
        return mQuestionList.get(mQuestionIndex);
    }

    public Question getNextQuestion() {
        mQuestionIndex++;                //On Fait une boucle sur les questions et
        return getCurrentQuestion();     //on renvoi une nouvelle à chaque appel
    }
}
