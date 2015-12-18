package com.library_app.controller;

import android.content.Context;

import com.library_app.callbacks.AddBookCallback;
import com.library_app.callbacks.MarkAsLentCallback;
import com.library_app.callbacks.MarkAsReturnedCallback;
import com.library_app.model.Reservation;

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
     * adds a copy
     * It could be a copy of a new book or a copy of an existing book
     *
     * @param isbn      identifier for the book
     * @param isn       identifier for the copy
     * @param title     book's titles (if it's a new book)
     * @param author    book's author (if it's a new book)
     * @param imageFile file containing the cover image of the book (leave this one if it's hard to upload it)
     */
    public void addBook(String isbn, String isn, String title, String author, File imageFile, AddBookCallback callback)
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
