package com.arnold.topquiz.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arnold.topquiz.R;
import com.arnold.topquiz.model.User;

public class MainActivity extends AppCompatActivity{

    private TextView mGreetingTextView; //Variable qui sera utilisée pour liée la vue TextView du layaout(xml) à l'activité
    private EditText mNameEditText;
    private Button mPlayButton;
    private User mUser; //Variable de type User qui va récupérer le nom nom de l'user qui sera contenu dans mNameEditText et la stocker dans le model User
    private static final int REQUEST_CODE_GAME_ACTIVITY = 42;
    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    //variable de classe qui represente le nom du fichier XML qui va conserver nos donnees
    private static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME"; //clé qui va stocker la valeur du nom de l'user
    private static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) { //fonction qui permet de créer l'activité
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //fonction qui permet de déterminer quel fichier layout utiliser

        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mNameEditText = findViewById(R.id.main_edittext_name); //on reférence l'élmt graphique (vue) EditText dans notre activité
        mPlayButton = findViewById(R.id.main_button_play);
        mPlayButton.setEnabled(false); //le bouton sera désactivé jusqu'à ce qu'un évènement précis se produise

        mNameEditText.addTextChangedListener(new TextWatcher() { //addTextChangedListener() est la methode qui nous notifie lorsqu'un texte est saisit par l'user
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) { //la variable s est celle qui va recevoir le texte saisit
                mPlayButton.setEnabled(!s.toString().isEmpty()); //le bouton va s'activer si s n'est pas vide
                //toString permet de convertir la valeur qui sera saisie en chaîne de caractère
            }
        });

        mPlayButton.setOnClickListener(new OnClickListener() { //l'objet OnClickListener() permet de detecter que l'user a appuyé sur le bouton
            @Override
            public void onClick(View v) { //la methode onClick() est appelée chaque fois que l'user appui sur le bouton
                getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE) //on recupere l'instance des SHARED PREFERENCES
                        .edit()
                        .putString(SHARED_PREF_USER_INFO_NAME, mNameEditText.getText().toString()) //putString permet d'ajouter une information ds le fichier XML
                        //la methode putString prend en parametre une cle et une valeur pr stocker l'information. Ici la valeur c'est le prenom que l'user saisit
                        .apply(); //cette methode termine le processus de sauvegarde

                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                //l'objet intent prend comme 1er paramètre le contexte de l'application (l'activité appelante) et coe 2e param la classe de l'activité à démarrer
                //startActivity(gameActivityIntent); //on demarre la nouvelle activité

                startActivityForResult(gameActivityIntent, REQUEST_CODE_GAME_ACTIVITY);
                //on demarre la nouvelle activité en attendant un resultat de sa part, ici ce sera le score de l'utilisateur
            }
        });
        greetUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE_GAME_ACTIVITY == requestCode && RESULT_OK == resultCode && data != null) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
            //on recupere le score de l'user. S'il n'a pas de score, par defaut c'est 0
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putInt(SHARED_PREF_USER_INFO_SCORE, score)
                    .apply();

            greetUser();
        }
    }

    private void greetUser() {
        String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
        //On recupere la valeur de la cle SHARED_PREF_USER_INFO_NAME ds le fichier XML. si cette valeur n'existe pas, par defaut elle vaut nul
        int score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_SCORE, -1);

        if (firstName != null) {
            if (score != -1) {
                mGreetingTextView.setText(getString(R.string.welcome_back_with_score, firstName, score));
            } else {
                mGreetingTextView.setText(getString(R.string.welcome_back, firstName));
            }

            mNameEditText.setText(firstName);
        }
    }

}