package com.example.saborysteps;

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

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUser, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUser = findViewById(R.id.editTextUser);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                startActivity(new Intent(LoginActivity.this, RegistrarUsuarioActivity.class));
                finish();
                break;
            case R.id.buttonSave:
                String user = editTextUser.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(user)) {
                    editTextUser.setError("Por favor, ingrese su nombre de usuario");
                    editTextUser.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                    editTextUser.setError("Por favor, introduzca su correo electrónico válido");
                    editTextUser.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Por favor, ingrese su contraseña");
                    editTextPassword.requestFocus();
                } else {
                    DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    String[] projection = {
                            DatabaseHelper.COLUMN_PASSWORD
                    };
                    String selection = DatabaseHelper.COLUMN_EMAIL + " = ?";
                    String[] selectionArgs = { user };
                    Cursor cursor = db.query(
                            DatabaseHelper.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            null
                    );
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            String passwordFromDB = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                            if (passwordFromDB.equals(password)) {
                                Toast.makeText(LoginActivity.this, R.string.login_successful, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, RecetasActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.incorrect_password, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.user_not_registered, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, R.string.database_error, Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", "Error accessing database", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        db.close();
                    }
                }
                break;
        }
    }
}


