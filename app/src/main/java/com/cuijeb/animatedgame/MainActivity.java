package com.cuijeb.animatedgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {
    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set Fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set no title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        drawView = findViewById(R.id.drawView);
    }

    public void moveLeft(View view) {
        // Set horizontal speed to move left
        drawView.sprite.setdX(-3);
    }
    public void moveRight(View view) {
        // Set horizontal speed to move right
        drawView.sprite.setdX(3);
    }
    public void redCheckBoxClicked(View view) {
        setColor();
    }
    public void greenCheckedBoxClicked(View view) {
        setColor();
    }
    public void setColor() {
        CheckBox greenCheckedBox = findViewById(R.id.greenCheckBox);
        CheckBox redCheckBox = findViewById(R.id.redCheckBox);
        if (redCheckBox.isChecked()) {
            if (greenCheckedBox.isChecked())
                drawView.sprite.setColor(Color.YELLOW);
            else drawView.sprite.setColor(Color.RED);
        }else if(greenCheckedBox.isChecked())
            drawView.sprite.setColor(Color.GREEN);
        else drawView.sprite.setColor(Color.BLUE);
    }
}