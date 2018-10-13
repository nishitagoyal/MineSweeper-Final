package com.example.goyal.minesweeper2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class HomeActivity extends AppCompatActivity {


    EditText editText;
    RadioGroup radioGroup;

    public static final int EASY = 0;
    public static final int INTERMEDIATE = 1;
    public static final int HARD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        radioGroup = findViewById(R.id.radioGroup);
        editText=findViewById(R.id.nameEditText);
    }
    public void StartMainActivity(View view){
        String name= editText.getText().toString();
        Intent intent= new Intent(this,MainActivity.class);
        intent.putExtra("Player_Name",name);

        int id = radioGroup.getCheckedRadioButtonId();
        int difficulty = INTERMEDIATE;
        switch (id){
            case R.id.easy:
                difficulty = EASY;
                break;
            case R.id.hard:
                difficulty = HARD;
                break;
            case R.id.medium:
                difficulty = INTERMEDIATE;
                break;
        }


        intent.putExtra("difficulty",difficulty);
         Log.i("HomeActivity", "difficulty   :  " + difficulty);

        startActivity(intent);
    }


}
