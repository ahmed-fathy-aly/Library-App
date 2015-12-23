package com.library_app.controller;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.library_app.callbacks.AddBookCallback;
import com.library_app.callbacks.MarkAsLentCallback;
import com.library_app.callbacks.MarkAsReturnedCallback;
import com.library_app.model.Reservation;

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
     * @param isbn      identifier for the book
     * @param isn       identifier for the copy
     * @param title     book's titles (if it's a new book)
     * @param author    book's author (if it's a new book)
     * @param imageFile file containing the cover image of the book (leave this one if it's hard to upload it)
     */
    public void addBook(String isbn, String isn, String title, String author, File imageFile, final AddBookCallback callback)
    {
        Log.e("Game", "token = " + new AuthenticationController(context).getAuthorizationToken());
        Ion.with(context)
                .load("POST", "http://library-themonster.rhcloud.com/books/create.json")
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
                                callback.success();;
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
    public void addCopy(String isbn, String isn, AddBookCallback callback)
    {
        callback.success();
    }

    /**
     * the admins marks the reservation as lent now (the user comes and takes the book )
     */
    public void markAsLent(Reservation reservation, MarkAsLentCallback callback)
    {
        callback.success();
    }

    /**
     * the admins marks the reservation as returned now (the user comes and returns the book )
     */
    public void markAsReturned(Reservation reservation, MarkAsReturnedCallback callback)
    {
        callback.success();
    }


}
