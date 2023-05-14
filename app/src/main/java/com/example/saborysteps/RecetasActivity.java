package com.example.saborysteps;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class RecetasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
