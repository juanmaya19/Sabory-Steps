package com.example.saborysteps;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class CrearReceta extends AppCompatActivity {
    private RecipeDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        dbHelper = new RecipeDatabaseHelper(this);
        Button btn_save_recipe = findViewById(R.id.btn_save_recipe);
        btn_save_recipe.setOnClickListener(new View.OnClickListener() {

            EditText et_recipe_name = findViewById(R.id.et_recipe_name);
            EditText et_ingredients = findViewById(R.id.et_ingredients);
            EditText et_prep = findViewById(R.id.et_prep);
            @Override
            public void onClick(View v) {
                String name = et_recipe_name.getText().toString();
                String ingredients = et_ingredients.getText().toString();
                String prep = et_prep.getText().toString();

                insertRecipe(name, ingredients, prep);

                Toast.makeText(CrearReceta.this, "Receta creada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void insertRecipe(String name, String ingredients, String prep) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("ingredients", ingredients);
        values.put("prep", prep);

        db.insert("recipes", null, values);
        Log.v("DB_INSERT", "New Row ID: " + ", Recipe Name: " + name + ", Ingredients: " + ingredients + ", Preparation: " + prep);
        db.close();
    }

}
