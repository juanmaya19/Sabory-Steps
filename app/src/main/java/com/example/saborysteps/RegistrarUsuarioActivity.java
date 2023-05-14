package com.example.saborysteps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        dbHelper = new DatabaseHelper(this);

        Button cancelButton = findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrarUsuarioActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            EditText editTextName = findViewById(R.id.editTextName);
            EditText editTextEmail = findViewById(R.id.editTextEmail);
            EditText editTextPassword = findViewById(R.id.editTextPassword);
            EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();


                // Validaciones para el campo de nombre
                if (TextUtils.isEmpty(name)) {
                    editTextName.setError(getString(R.string.name_error));
                    editTextName.requestFocus();
                    return;
                }
                // Validaciones para el campo de correo electrónico
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError(getString(R.string.email_error));
                    editTextEmail.requestFocus();
                    return; }
                // Validaciones para el campo de contraseña
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError(getString(R.string.password_error));
                    editTextPassword.requestFocus();
                    return;
                }

                // Validaciones para el campo de confirmación de contraseña
                if (!confirmPassword.equals(password)) {
                    editTextConfirmPassword.setError(getString(R.string.confirm_password_error));
                    editTextConfirmPassword.requestFocus();
                return;
            }

            // Insertar datos en la base de datos
            long id = dbHelper.insertUser(name, email, password);

                if (id > 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.registration_error), Toast.LENGTH_SHORT).show();
            }
        }
    });

    Button buttonShowUsers = findViewById(R.id.buttonShowUsers);
        buttonShowUsers.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Cursor cursor = dbHelper.getAllUsers();
            StringBuilder sb = new StringBuilder();
            while (cursor.moveToNext()) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
                sb.append("ID: ").append(id).append(", Name: ").append(name).append(", Email: ").append(email).append("");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarUsuarioActivity.this);
            builder.setTitle(getString(R.string.users_title));
            builder.setMessage(sb.toString());
            builder.setPositiveButton(getString(R.string.ok_button), null);
            builder.show();
        }
    });

    Button buttonDeleteUser = findViewById(R.id.buttonDelete);
        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText editTextUserId = findViewById(R.id.editTextName);
            String userIdString = editTextUserId.getText().toString();

            if (TextUtils.isEmpty(userIdString)) {
                editTextUserId.setError(getString(R.string.user_id_error));
                editTextUserId.requestFocus();
                return;
            }

            long userId = Long.parseLong(userIdString);

            int rowsDeleted = dbHelper.deleteUser(userId);

            if (rowsDeleted > 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.user_deleted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.user_delete_error), Toast.LENGTH_SHORT).show();
            }
        }
    });
}
}
