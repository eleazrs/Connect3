package com.example.connect3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private boolean finishedGame = false;
    private int counter = 0;
    private boolean[] positionslocked = new boolean[9];
    private String positionsX = "";
    private String positionsO = "";
    private String[] winnerPositions = {"012", "345", "678", "036", "147", "586", "048", "246"};
    private List<ImageView> buttons;

    public void playAgain(View view) {
        // restart game status to enabled
        finishedGame = false;

        // hide the "play again" button
        Button playAgain = findViewById(R.id.playAgain);
        playAgain.setAlpha(0);

        // set winner text to empty
        TextView textView = findViewById(R.id.andTheWinnerIs);
        textView.setText("");

        // restart counter of movements
        counter = 0;

        // restart positions locked
        positionslocked = new boolean[9];

        // restart recorded positions for both players
        positionsX = "";
        positionsO = "";

        // restart buttons position
        this.buttons.forEach(imageView -> {
            imageView.setX(200f);
            imageView.setY(-200f);
        });

    }

    public void playButton(View view) {
        if(finishedGame) return;

        this.buttons = Arrays.asList(new ImageView[]{
                findViewById(R.id.imageX1),
                findViewById(R.id.imageO1),
                findViewById(R.id.imageX2),
                findViewById(R.id.imageO2),
                findViewById(R.id.imageX3),
                findViewById(R.id.imageO3),
                findViewById(R.id.imageX4),
                findViewById(R.id.imageO4),
                findViewById(R.id.imageX5),
        });

        Button buttonClicked = (Button) view;
        Log.i("Button XY position", String.format("%.2f %.2f", buttonClicked.getX(), buttonClicked.getY()));
        int actualButtonID = Integer.parseInt(buttonClicked.getTag().toString());

        if (positionslocked[actualButtonID]) return;

        ImageView ficha = this.buttons.get(counter);
        Log.i("ficha position", String.format("initial position XY: %.2f %.2f", ficha.getX(), ficha.getY()));
        Log.i("ficha translation", String.format("initial translation XY: %.2f %.2f", ficha.getTranslationX(), ficha.getTranslationY()));
        Log.i("ficha alpha", String.format("alpha: %.2f", ficha.getAlpha()));
        ficha.setX(buttonClicked.getX());
        ficha.animate().translationYBy(buttonClicked.getY() - ficha.getY()).setDuration(500);
        ficha.setTranslationY(buttonClicked.getY() - ficha.getY());
        ficha.animate().alpha(1f).setDuration(1000);

        if(counter % 2 == 0) positionsO += actualButtonID;
        else positionsX += actualButtonID;

        Log.i("ficha position", String.format("actual position: %.2f %.2f", ficha.getX(), ficha.getY()));
        Log.i("ficha alpha", String.format("alpha: %.2f", ficha.getAlpha()));


        String winnerO = "Azules Ganan!!";
        if(checkWinner(positionsO, winnerO)) return;
        String winnerX = "Rojas Ganan!!";
        if(checkWinner(positionsX, winnerX)) return;

        positionslocked[actualButtonID] = true;
        counter++;
        if(counter==9) finishGame("OH NO!! Este tablero da pena!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private boolean checkWinner(String positionsForPlayer, String winnerText) {
        if("".equals(positionsForPlayer)) return false;
        for(String s : winnerPositions) {
            // if we encountered a 3-line combination
            if(stringToHashSet(positionsForPlayer).containsAll(stringToHashSet(s))) {
                finishGame(winnerText);
                return true;
            }
        }
        return false;
    }

    private void finishGame(String winnerText) {
        // show "play again" button
        Button playAgain = findViewById(R.id.playAgain);
        playAgain.setAlpha(1f);
        // show winner text!
        TextView textView = findViewById(R.id.andTheWinnerIs);
        textView.setText(winnerText);
        // set game status
        finishedGame = true;
    }

    private Set<Character> stringToHashSet(String s){
        return Arrays.stream(ArrayUtils.toObject(s.toCharArray())).collect(Collectors.toSet());
    }

}