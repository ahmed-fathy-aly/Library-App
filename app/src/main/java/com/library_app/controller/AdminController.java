package com.library_app.controller;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.library_app.R;
import com.library_app.callbacks.AddBookCallback;
import com.library_app.callbacks.AddUserCallback;
import com.library_app.callbacks.MarkAsLentCallback;
import com.library_app.callbacks.MarkAsReturnedCallback;
import com.library_app.model.Reservation;
import com.library_app.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by ahmed on 12/18/2015.
 */
public class AdminController extends ReaderController
{
    /* fields */
    Context context;

    /* constructor */
    public AdminController(Context context)
    {

        this.context = context;
    }

    /* methods */

    /**
     * adds a new book (not a copy)
     *
     * @param isbn   identifier for the book
     * @param isn    identifier for the copy
     * @param title  book's titles (if it's a new book)
     * @param author book's author (if it's a new book)
     */
    public void addBook(String isbn, String isn, String title, String author, final AddBookCallback callback)
    {
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "books/create.json")
                .addHeader("Authentication ", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("isbn", isbn)
                .setBodyParameter("isn", isn)
                .setBodyParameter("title", title)
                .setBodyParameter("author", author)
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
                        Log.e("Game", "add book response = " + result);

                        // check if failed
                        try
                        {
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");

                            if (status != 1)
                                callback.fail("Failed");
                            else
                                callback.success();
                            ;
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }

                    }
                });

    }

    /**
     * adds a copy of an existing book
     *
     * @param isbn the isbn of the original book
     * @param isn  the isn of the copy
     */
    public void addCopy(String isbn, String isn, final AddBookCallback callback)
    {
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "books/add_copy.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("isbn", isbn)
                .setBodyParameter("isn", isn)
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
                        Log.e("Game", "add copy response = " + result);

                        // check if failed
                        try
                        {
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");

                            if (status != 1)
                                callback.fail("Failed");
                            else
                                callback.success();
                            ;
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }

                    }
                });
    }

    public void setImage(String isbn, File imageFile, final AddBookCallback callback)
    {
        Log.e("Game", isbn + " uploaded file size = " + imageFile.getTotalSpace());
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "books/add_image.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setMultipartParameter("isbn", isbn)
                .setMultipartFile("file", imageFile)
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
                        Log.e("Game", "add image response = " + result);

                        callback.success();
                    }
                });
    }

    /**
     * the admins marks the reservation as lent now (the user comes and takes the book )
     */
    public void markAsLent(Reservation reservation, final MarkAsLentCallback callback)
    {
        Log.e("Game", "id = " + reservation.getId());
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "reservations/lend.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("reservation_code", reservation.getId())
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
                        Log.e("Game", "mark as lentresponse = " + result);

                        // check if failed
                        try
                        {
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");

                            if (status != 1)
                                callback.fail("Failed");
                            else
                                callback.success();
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }

                    }
                });
    }

    /**
     * the admins marks the reservation as returned now (the user comes and returns the book )
     */
    public void markAsReturned(Reservation reservation, final MarkAsReturnedCallback callback)
    {
        Log.e("Game", "id = " + reservation.getId());
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "reservations/return.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("reservation_code", reservation.getId())
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
                        Log.e("Game", "mark as returned response = " + result);

                        // check if failed
                        try
                        {
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");

                            if (status != 1)
                                callback.fail("Failed");
                            else
                                callback.success();
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }

                    }
                });
    }

    /**
     * signs up a user
     */
    public void addUser(String type, String mail, String password, String name, String universityCode, String bookLimit, final AddUserCallback callback)
    {
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "users/create.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("mail", mail)
                .setBodyParameter("name", name)
                .setBodyParameter("password", password)
                .setBodyParameter("type", type)
                .setBodyParameter("code", universityCode)
                .setBodyParameter("book_limit", bookLimit)
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
                        Log.e("Game", "add user result = " + result);

                        try
                        {
                            // check if failed
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");
                            if (status != 1)
                                callback.fail("Failed");
                            else
                                callback.success();

                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                            callback.fail(e1.getMessage());
                        }

                    }
                });
    }


}
