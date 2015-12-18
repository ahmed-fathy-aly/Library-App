package com.library_app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.library_app.R;
import com.library_app.controller.AuthenticationController;
import com.library_app.model.User;

public class StartActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // select which activity to launch
        User user = AuthenticationController.getCurrentUser();
        Intent intent = new Intent();
        if (user != null)
        {
            intent.setClass(this, ReservationsActivity.class);
            intent.putExtra(getString(R.string.canUpvote), user.canVote());
            intent.putExtra(getString(R.string.canReserve), user.canReserve());
            intent.putExtra(getString(R.string.canChangeReservation), user.canChangeReservation());
        }
        startActivity(intent);
        finish();
    }
}
