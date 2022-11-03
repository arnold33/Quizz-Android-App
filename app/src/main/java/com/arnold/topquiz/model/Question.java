package com.arnold.topquiz.model;

import java.util.List;

public class Question {
    private final String mQuestion; //variable qui contiendra l'intitulé de la question
    private final List<String> mChoiceList; //Liste des réponses possibles
    private final int mAnswerIndex; //index de la bonne réponse parmi celles contenues dans la liste

    public Question(String question, List<String> choiceList, int answerIndex) { //constructeur
        mQuestion = question;
        mChoiceList = choiceList;
        mAnswerIndex = answerIndex;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public List<String> getChoiceList() {
        return mChoiceList;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }
}
