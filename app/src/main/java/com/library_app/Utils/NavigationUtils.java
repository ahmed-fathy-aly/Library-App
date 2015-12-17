package com.library_app.Utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.library_app.R;
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
     * @param id the id of the menu item correspoding to the activity
     * @param toolbar the activitie's toolbar, to add a burger icon
     */
    public static Drawer setupNavigationBar(AppCompatActivity activity, int id, Toolbar toolbar)
    {
        // get the current user
        User user = AuthenticationController.getCurrentUser();

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
            builder.addDrawerItems(new PrimaryDrawerItem().withIdentifier(0).withName("Browse")
                    , new PrimaryDrawerItem().withIdentifier(1).withName("Add Book")
                    , new DividerDrawerItem()
                    , new PrimaryDrawerItem().withIdentifier(2).withName("Log Out"));
        }
        builder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
        {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
            {
                int id = drawerItem.getIdentifier();

                return false;
            }
        });


        Drawer drawer = builder.build();
        return drawer;
    }
}
