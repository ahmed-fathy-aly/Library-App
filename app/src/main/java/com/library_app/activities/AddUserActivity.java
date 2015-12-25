package com.library_app.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.library_app.R;
import com.library_app.Utils.NavigationUtils;
import com.library_app.callbacks.AddUserCallback;
import com.library_app.callbacks.SignUpCallback;
import com.library_app.controller.AdminController;
import com.library_app.controller.AuthenticationController;
import com.library_app.model.User;
import com.mikepenz.materialdrawer.Drawer;

public class AddUserActivity extends AppCompatActivity
{
    /* UI */
    EditText editTextUserName;
    EditText editTextPassword;
    EditText editTextEmail;
    EditText editTextUniversityCode;
    EditText editTextBookLimit;
    Spinner spinnerUserType;
    ProgressBar progressBar;
    private View content;
    private Drawer navigationDrawer;
    private Button buttonSIgnUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        // setup navdrawer and toolbar
        content = findViewById(R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView text = new TextView(this);
        text.setText("Add User");
        text.setTextAppearance(this, android.R.style.TextAppearance_Material_Widget_ActionBar_Title_Inverse);
        toolbar.addView(text);
        navigationDrawer = NavigationUtils.setupNavigationBar(this, 4, toolbar);

        // reference ui
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUniversityCode = (EditText) findViewById(R.id.editTextUniversityCode);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextBookLimit = (EditText) findViewById(R.id.editTextBookLimit);
        buttonSIgnUp = (Button) findViewById(R.id.buttonAddUser);
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

    private void onSignUpClicked()
    {
        // gather data
        final String userName = editTextUserName.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String universityCode = editTextUniversityCode.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String userType = (String) spinnerUserType.getSelectedItem();
        final String bookLimit = (String) editTextBookLimit.getText().toString();

        // sign up in background
        progressBar.setVisibility(View.VISIBLE);
        buttonSIgnUp.setVisibility(View.INVISIBLE);
        AdminController controller = new AdminController(this);
        controller.addUser(userType, email, password, userName, universityCode,bookLimit, new AddUserCallback()
        {
            @Override
            public void success()
            {
                // go to the start activity again
                progressBar.setVisibility(View.INVISIBLE);
                buttonSIgnUp.setVisibility(View.VISIBLE);
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void fail(String message)
            {
                progressBar.setVisibility(View.INVISIBLE);
                buttonSIgnUp.setVisibility(View.VISIBLE);
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (navigationDrawer.isDrawerOpen())
            navigationDrawer.closeDrawer();
        else
            super.onBackPressed();
    }

}
