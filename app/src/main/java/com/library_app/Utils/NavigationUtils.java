package com.library_app.Utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.library_app.R;
import com.library_app.activities.AddBookActivity;
import com.library_app.activities.BrowseBooksActivity;
import com.library_app.activities.ReservationsActivity;
import com.library_app.activities.StartActivity;
import com.library_app.controller.AuthenticationController;
import com.library_app.model.User;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public class NavigationUtils
{
    /**
     * sets up the navigation drawer in the actibity
     *
     * @param itemToBeSelectedId the id of the menu item correspoding to the activity
     * @param toolbar            the activitie's toolbar, to add a burger icon
     */
    public static Drawer setupNavigationBar(final AppCompatActivity activity, final int itemToBeSelectedId, Toolbar toolbar)
    {
        // get the current user
        final User user = new AuthenticationController(activity).getCurrentUser();

        // profile header
        ProfileDrawerItem userProfile = new ProfileDrawerItem().withName(user.getName());
        userProfile = userProfile.withIcon(user.getType().equals(User.ADMIN) ? R.drawable.ic_admin : R.drawable.ic_reader);
        final AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_profile_header)
                .addProfiles(userProfile)
                .build();

        // build navigation drawer
        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(accountHeader)
                .withToolbar(toolbar);
        if (user.getType().equals(User.ADMIN))
        {
            builder.addDrawerItems(new PrimaryDrawerItem().withIdentifier(1).withName("Browse Books")
                    , new PrimaryDrawerItem().withIdentifier(2).withName("Add Book")
                    , new PrimaryDrawerItem().withIdentifier(3).withName("Reservations")
                    , new DividerDrawerItem()
                    , new PrimaryDrawerItem().withIdentifier(10).withName("Log Out"));
        } else if (user.getType().equals(User.STUDENT) || user.getType().equals(User.PROFESSOR))
        {
            builder.addDrawerItems(new PrimaryDrawerItem().withIdentifier(1).withName("Browse Books")
                    , new PrimaryDrawerItem().withIdentifier(3).withName("Reservations")
                    , new DividerDrawerItem()
                    , new PrimaryDrawerItem().withIdentifier(10).withName("Log Out"));
        }

        builder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
        {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
            {
                int id = drawerItem.getIdentifier();
                if (id == itemToBeSelectedId)
                    return false;

                // launch another activity
                if (id <= 3)
                {
                    // select the activity
                    final Intent intent = new Intent();
                    if (id == 1)
                        intent.setClass(activity, BrowseBooksActivity.class);
                    else if (id == 2)
                        intent.setClass(activity, AddBookActivity.class);
                    else if (id == 3)
                        intent.setClass(activity, ReservationsActivity.class);

                    // add the extras
                    intent.putExtra(activity.getString(R.string.canUpvote), user.canVote());
                    intent.putExtra(activity.getString(R.string.canReserve), user.canReserve());
                    intent.putExtra(activity.getString(R.string.canChangeReservation), user.canChangeReservation());

                    // launch the activity after some milliseconds to show the drawer close animation
                    android.os.Handler handler = new android.os.Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }, 500);
                } else if (id == 10)
                {
                    // log out
                    AuthenticationController controller = new AuthenticationController(activity);
                    controller.logOut();
                    Intent intent = new Intent(activity, StartActivity.class);
                    activity.startActivity(intent);
                }

                return false;
            }
        });


        Drawer drawer = builder.build();
        drawer.setSelection(itemToBeSelectedId);
        return drawer;
    }
}
