package com.example.goyal.minesweeper2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {
    Random random;
    LinearLayout rootLayout;

    public static final int INCOMPLETE=1;
    public static int currentStatus=INCOMPLETE;
    public static final int GAME_OVER=2;
    public static int count=0;

    public static int[] X= {-1,-1,-1,0,0,1,1,1};
    public static int[] Y ={-1,0,1,-1,1,-1,0,1};

    public static int m,n,t;

    public ArrayList<LinearLayout> rows;


    public MineButton[][] board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent  intent=getIntent();
        String name= intent.getStringExtra("Player_Name");
        TextView textView=findViewById(R.id.nameTextValue);
        textView.setText(name);
        int difficulty = intent.getIntExtra("difficulty",HomeActivity.INTERMEDIATE);
        switch (difficulty){
            case HomeActivity.EASY:
                m=7;
                n=7;
                t=5;
                break;
            case HomeActivity.INTERMEDIATE:
                m=10;
                n=10;
                t=10;
                break;
            case HomeActivity.HARD:
                m=12;
                n=12;
                t=15;
                break;
        }

        rootLayout = findViewById(R.id.rootLayout);
        random= new Random();

        setupBoard();

    }
    public void setupBoard(){
        rows= new ArrayList<>();
        board=new MineButton[m][n];
        rootLayout.removeAllViews();

        for(int i=0;i<m;i++){
            LinearLayout linearLayout=new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            linearLayout.setLayoutParams(layoutParams);
            rootLayout.addView(linearLayout);
            rows.add(linearLayout);

        }

        for(int i=0;i<m;i++){

            for(int j=0;j<n;j++) {

                MineButton button= new MineButton(this);
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                button.setLayoutParams(layoutParams);
                button.setBackgroundResource(R.drawable.button_bg);

                button.setOnClickListener(this);
                button.setOnLongClickListener(this);


                LinearLayout row=rows.get(i);
                row.addView(button);
                button.x = i;
                button.y = j;
                board[i][j]=button;
            }
        }

        setMines();
    }

    public void setMines() {
        int x = 0;
        while (x < t) {
            int randomInt = random.nextInt(m * n);
            int i = randomInt / m;
            int j = randomInt % n;

            Log.i("MainActivity","i : "+i+"j : "+j);
            if (board[i][j].MINE != -1) {
                board[i][j].MINE = -1;
                countOfNeighbour(i, j);
                x++;


            }
        }
    }

    public void countOfNeighbour(int x, int y){
        int Ni, Nj;
        for(int i=0;i<8;i++){
            Ni=x+X[i];
            Nj=y+Y[i];
            if( Ni>=0 && Ni<m && Nj>=0 && Nj<m && board[Ni][Nj].MINE != -1){

                board[Ni][Nj].VALUE ++;
                Log.i("MainActivity","i = "+Ni+" j = "+Nj+" value = "+board[Ni][Nj].VALUE);
            }
        }

    }

    public void revealNeighbour(int x, int y) {
        int Ni, Nj;
        for (int i = 0; i < 8; i++) {
            Ni = x + X[i];
            Nj = y + Y[i];
            if (Ni >= 0 && Ni < m && Nj >= 0 && Nj < m && board[Ni][Nj].MINE != -1) {
                if(!board[Ni][Nj].IS_REVEALED){
                    board[Ni][Nj].setText(board[Ni][Nj].VALUE + "");
                    board[Ni][Nj].IS_REVEALED=true;
                    if(board[Ni][Nj].VALUE==0){
                        revealNeighbour(Ni,Nj);
                    }
                }
            }
        }
    }

    public void checkWin() {
        boolean winner = true;

        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (count!=t || !board[x][y].IS_REVEALED) {
                    winner = false;
                }
            }
        }
        if (winner == true) {
            Toast.makeText(this, "YOU WON", Toast.LENGTH_LONG).show();
            currentStatus = GAME_OVER;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setupBoard();
        currentStatus=INCOMPLETE;
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        MineButton button = (MineButton) view;
        if (currentStatus != GAME_OVER) {

            if (button.MINE == -1) {
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        if (board[i][j].MINE == -1) {
                            board[i][j].setBackgroundResource(R.drawable.mine);
                        }
                    }
                }

                Toast.makeText(this, "YOU LOOSE", Toast.LENGTH_LONG).show();
                currentStatus = GAME_OVER;
            } else if (button.MINE != -1 && button.VALUE != 0 && button.IS_FLAG == false) {
                button.setText(button.VALUE + "");
                button.IS_REVEALED = true;
                checkWin();
            } else if (button.MINE != -1 && button.VALUE == 0 && button.IS_REVEALED == false && button.IS_FLAG == false) {
                button.setText("0");
                revealNeighbour(button.x, button.y);
                button.IS_REVEALED = true;
                checkWin();
            }
        }
    }


    @Override
    public boolean onLongClick(View view) {
        MineButton button = (MineButton) view;
        if (button.IS_REVEALED==false ) {
            button.setBackgroundResource(R.drawable.flag);
            button.IS_REVEALED = true;
            button.IS_FLAG = true;
            count++;
            checkWin();
        }
        else if(button.IS_FLAG){
            button.setBackgroundResource(R.drawable.button_bg);
            button.IS_REVEALED=false;
            button.IS_FLAG=false;
            count--;
        }

        return true;
    }
}
