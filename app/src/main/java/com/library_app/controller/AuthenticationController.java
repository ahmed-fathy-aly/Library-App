package com.library_app.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.library_app.callbacks.LoginCallback;
import com.library_app.callbacks.SignUpCallback;
import com.library_app.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ahmed on 12/17/2015.
 */
public class AuthenticationController
{
    /* constants */
    private static final String KEY_PREFERENCES_NAME = "libraryAppPreferences";

    /* fields */
    private Context context;

    public AuthenticationController(Context context)
    {
        this.context = context;
    }

    /* methods */

    /**
     * signs up a new user
     * the result will have the user details (the ones sent)and an authentication token
     * these results will be saved in the pereference
     *
     * @param type Admin or Student Professor
     */
    public void signUp(String type, String mail, String password, String name, String universityCode, final SignUpCallback callback)
    {

        Ion.with(context)
                .load("POST", "http://library-themonster.rhcloud.com/users/create.json")
                .setBodyParameter("mail", mail)
                .setBodyParameter("name", name)
                .setBodyParameter("password", password)
                .setBodyParameter("type", type)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "sign up result = " + result);

                        try
                        {
                            // check if failed
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");
                            if (status != 1)
                            {
                                callback.fail("Failed");
                                return;
                            }

                            // parse response
                            JSONObject response = resultJson.getJSONObject("response");
                            JSONObject userObject = response.getJSONObject("user");
                            User user = User.parseFromJson(userObject);
                            String token = userObject.getString("token");
                            setCurrentUser(user, token);
                            callback.success(user, token);

                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                            callback.fail(e1.getMessage());
                        }

                    }
                });

    }

    /**
     * logs in a user that has signed up before
     * the result will have the user details (the ones sent)and an authentication token
     * these results will be saved in the pereferences
     *
     */
    public void login(String mail, String password, final LoginCallback callback)
    {

        Ion.with(context)
                .load("POST", "http://library-themonster.rhcloud.com/users/login.json")
                .setBodyParameter("mail", mail)
                .setBodyParameter("password", password)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // failed to connect
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "log in result = " + result);

                        // parse response
                        try
                        {
                            // check if failed
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");
                            if (status != 1)
                            {
                                callback.fail("Failed");
                                return;
                            }

                            // parse response
                            JSONObject response = resultJson.getJSONObject("response");
                            JSONObject userObject = response.getJSONObject("user");
                            User user = User.parseFromJson(userObject);
                            String token = userObject.getString("token");
                            setCurrentUser(user, token);
                            callback.success(user, token);

                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                            callback.fail(e1.getMessage());
                        }

                    }
                });

    }


    /**
     * notifies the server that the user has logged out (to stop notifications)
     * clears the preferences
     */
    public void logOut()
    {
        // clear the preferences
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    /**
     * saves the user's info to the preferences
     */
    private void setCurrentUser(User user, String authorizationToken)
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("type", user.getType());
        editor.putString("name", user.getName());
        editor.putString("mail", user.getMail());
        editor.putString("id", user.getId());
        editor.putString("token", authorizationToken);
        editor.commit();
    }

    /**
     * gets the user info stored in preferences
     * @return the logged in user
     */
    public  User getCurrentUser()
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);

        if (!pref.contains("token"))
            return null;

        User user = new User();
        user.setId(pref.getString("id", ""));
        user.setName(pref.getString("name", ""));
        user.setMail(pref.getString("mail", ""));
        user.setType(pref.getString("type", ""));

        return user;
    }

    public String getAuthorizationToken()
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);
        return pref.getString("token", "");
    }

}
