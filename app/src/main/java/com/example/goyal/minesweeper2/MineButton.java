package com.example.goyal.minesweeper2;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class MineButton extends AppCompatButton {
    int MINE=0;
    boolean IS_REVEALED=false;
    boolean IS_FLAG=false;
    int x;
    int y;
    int VALUE = 0;

    public MineButton(Context context) {
        super(context);
    }}
