package com.library_app.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.library_app.R;
import com.library_app.callbacks.SignUpCallback;
import com.library_app.controller.AuthenticationController;
import com.library_app.model.User;

public class SignUpActivity extends AppCompatActivity
{

    /* UI */
    EditText editTextUserName;
    EditText editTextPassword;
    EditText editTextEmail;
    EditText editTextUniversityCode;
    Spinner spinnerUserType;
    ProgressBar progressBar;
    private View content;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // reference ui
        content = findViewById(R.id.content);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUniversityCode = (EditText) findViewById(R.id.editTextUniversityCode);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        Button buttonSIgnUp = (Button) findViewById(R.id.buttonSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);

        // add listeners
        buttonSIgnUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSignUpClicked();
            }
        });
    }

    /**
     * signs up the user
     */
    private void onSignUpClicked()
    {
        // gather data
        final String userName = editTextUserName.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String universityCode = editTextUniversityCode.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String userType = (String) spinnerUserType.getSelectedItem();

        // sign up in background
        progressBar.setVisibility(View.VISIBLE);
        AuthenticationController controller = new AuthenticationController(this);
        controller.signUp(userType, email, password, userName, universityCode, new SignUpCallback()
        {
            @Override
            public void success(User user, String authenticationToken)
            {
                // go to the start activity again
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();

            }

            @Override
            public void fail(String message)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
