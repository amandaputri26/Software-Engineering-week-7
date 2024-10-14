package com.example.week5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private EditText txtEmail, txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        // this is called when user clicks on done button, and called the validate function
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_NULL) {
                    validate();
                    return true;
                }
                return false;
            }

        });
        btnLogin = findViewById(R.id.btnLogin);
        // NOTE: click login also calls the validate function
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        btnRegister = findViewById(R.id.btnRegister);
        // NOTE: click register and starts new Activity
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openRegister = new Intent(getBaseContext(), Register.class);
                startActivity(openRegister);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        // NOTE: if user is logged in, then show MainActivity
        if (Preferences.getStatusLogin(getBaseContext())) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
    }
    private void validate() {
        txtEmail.setError(null);
        txtPassword.setError(null);
        View focus = null;
        boolean cancel = false;

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Email/Username is required");
            focus = txtEmail;
            cancel = true;
        } else if (!userExists(email)) {
            txtEmail.setError("Email/Username is not found");
            focus = txtEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Password is required");
            focus = txtPassword;
            cancel = true;
        } else if (!passwordMatches(password)) {
            txtPassword.setError("Password does not match");
            focus = txtPassword;
            cancel = true;
        }

        if (cancel) {
            focus.requestFocus();
        } else {
            goLogin();
        }
    }
    private void goLogin() {
        // set User and Status in SharedPreferences and start MainActivity
        // assuming user is already registered
        Preferences.setUserLogin(getBaseContext(), Preferences.getUserLogin(getBaseContext()));
        Preferences.setStatusLogin(getBaseContext(), true);

        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }
    private boolean userExists(String user) {
        // returns true if the user equals to the SharedPreferences User
        return user.equals(Preferences.getUsername(getBaseContext()));
    }

    private boolean passwordMatches(String password) {
        // returns true if the password equals to the SharedPreferences password
        return password.equals(Preferences.getPassword(getBaseContext()));
    }

}
