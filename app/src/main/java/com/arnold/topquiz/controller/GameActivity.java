package com.arnold.topquiz.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.topquiz.R;
import com.arnold.topquiz.model.Question;
import com.arnold.topquiz.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
//on implemente l'interface View.OnclickListener directement ds la classe GameActivity pour eviter d'avoir a l'implementer pour chaque bouton

    private TextView mQuestionTextView;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;
    private QuestionBank mQuestionBank;
    Question mCurrentQuestion;
    private int mRemainingQuestionCount;
    private int mScore;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    private boolean mEnableTouchEvents;
    private static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE"; //Clé de la valeur de l'état du score qui sera gardé en mémoire
    private static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION"; //Clé de la valeur de l'état de la question qui sera gardé en mémoire

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) { //cette methode permet d'activer ou de desactiver les boutons
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mRemainingQuestionCount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mQuestionTextView = findViewById(R.id.game_activity_textview_question);
        mAnswerButton1 = findViewById(R.id.game_activity_button_1);
        mAnswerButton2 = findViewById(R.id.game_activity_button_2);
        mAnswerButton3 = findViewById(R.id.game_activity_button_3);
        mAnswerButton4 = findViewById(R.id.game_activity_button_4);
        mQuestionBank = generateQuestions();

        // Use the same listener for the four buttons.
        // The view id value will be used to distinguish the button triggered
        mAnswerButton1.setOnClickListener(this); //le this fait allusion a la methode onClick() de l'objet OnClickListener
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getCurrentQuestion();
        displayQuestion(mCurrentQuestion);

        mEnableTouchEvents = true; //par defaut, les boutons sont activés

        if(savedInstanceState != null){
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
        }
        else{
            mRemainingQuestionCount = 5;
            mScore = 0;
        }
    }

    private void displayQuestion(final Question question) {
        mQuestionTextView.setText(question.getQuestion()); //la methode setText() permet d'afficher le texte mis en parametre
        mAnswerButton1.setText(question.getChoiceList().get(0));
        mAnswerButton2.setText(question.getChoiceList().get(1));
        mAnswerButton3.setText(question.getChoiceList().get(2));
        mAnswerButton4.setText(question.getChoiceList().get(3));
    }

    private QuestionBank generateQuestions() {
        Question question1 = new Question(
                "Who is the creator of Android?",
                Arrays.asList(
                        "Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"
                ),
                0
        );

        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3
        );

        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3
        );

        Question question4 = new Question(
                "What is the seasons number of The GOT?",
                Arrays.asList(
                        "5",
                        "8",
                        "6",
                        "4"
                ),
                1
        );

        Question question5 = new Question(
                "Who is the creator of WANT Technology?",
                Arrays.asList(
                        "Arnold TCHUISSEU",
                        "Francis TOP",
                        "Jeff BEZOS",
                        "Samuel ETO'O"
                ),
                0
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5));
    }

    @Override
    public void onClick(View v) { // v correspond au bouton sur lequel l'user clique à l'instant t
        int index;

        if (v == mAnswerButton1) { // si le bouton sur lequel l'user clique correspond au 1er bouton, alors l'index prend la valeur 0
            index = 0;
        } else if (v == mAnswerButton2) {
            index = 1;
        } else if (v == mAnswerButton3) {
            index = 2;
        } else if (v == mAnswerButton4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }

        if (index == mQuestionBank.getCurrentQuestion().getAnswerIndex()){ //on compare notre index avec celui de la bonne reponse
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            //si la reponse est bonne, on affiche un toast avec le message éphémère "correct", ce toast aura une durée de LONG (3.5 secondes)
            mScore++;
        } else{
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }

        mEnableTouchEvents = false; //apres avoir cliquer sur le bouton de réponse, tous les boutons se désactivent
        new Handler().postDelayed(new Runnable() { //l'objet Handler permet d'ajouter un temps d'execution
            @Override
            public void run() {
                mRemainingQuestionCount--;
                if(mRemainingQuestionCount > 0) {
                    mCurrentQuestion = mQuestionBank.getNextQuestion();
                    displayQuestion(mCurrentQuestion);
                }
                else{
                    endGame();
                }
                mEnableTouchEvents = true; //les boutons se réactivent après l'exéction de la fonction run(), c a d après les 3s
            }
        }, 2500); //la methode run() sera executee apres une attente de 3s.
        //Dc apres avoir cliquer sur le bouton de reponse, 3s vont s'ecouler avant que la prochaine question ne s'affiche

    }

    public void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!")
                .setMessage("Your score is " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);//on renseigne la valeur de mScore dans l'intent
                        setResult(RESULT_OK, intent);
                        finish(); //cette ligne permet de fermer l'activité courante et ainsi revenir à l'activité précédente
                    }
                })
                .create()
                .show();
    }


}