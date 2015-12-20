package com.library_app.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.library_app.R;
import com.library_app.callbacks.LoginCallback;
import com.library_app.controller.AuthenticationController;
import com.library_app.model.User;

public class LoginActivity extends AppCompatActivity
{

    /* fields */
    EditText editTextEmail;
    EditText editTextPassword;
    ProgressBar progressBar;
    View content;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // reference ui
        content = findViewById(R.id.content);
        editTextEmail = (EditText) findViewById(R.id.editTextUserEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        Button loginButton = (Button) findViewById(R.id.buttonLogIn);
        Button signUpButton = (Button) findViewById(R.id.buttonSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // set button listeners
        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSignUpClicked();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onLogInClicked();
            }
        });

    }

    /**
     * attempts to log the user in
     */
    private void onLogInClicked()
    {
        // gather data
        String mail = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // attempt login
        AuthenticationController controller = new AuthenticationController(this);
        progressBar.setVisibility(View.VISIBLE);
        controller.login(mail, password, new LoginCallback()
        {
            @Override
            public void success(User user, String authenticationToken)
            {
                // go to the start activity again
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void fail(String message)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * when the user asks to sign up
     */
    private void onSignUpClicked()
    {
        // go to the login activity
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
