package com.library_app.controller;

import android.content.Context;

import com.library_app.callbacks.FollowBookCallback;
import com.library_app.callbacks.GetBooksCallback;
import com.library_app.callbacks.ReserveBookCallback;
import com.library_app.callbacks.UpvoteBookCallback;
import com.library_app.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public class ReaderController
{
    /* fields */
    Context context;
    String userId;

    /* constructor */
    public ReaderController(Context context, String userId)
    {
        this.context = context;
        this.userId = userId;
    }

    /* methods */

    /**
     * returns the list of all books
     */
    public void getBooks(GetBooksCallback callback)
    {
        List<Book> result = new ArrayList<>();
        for (int i = 1; i < 15; i++)
        {
            Book book = new Book();
            book.setTitle("book " + i);
            book.setAuthor("author + " + i);
            book.setAvailable(i % 3 == 0);
            book.setImageUrl("http://ecx.images-amazon.com/images/I/519978ZK54L.jpg");
            book.setnUpvotes(i);
            book.setIsbn(i + "");
            result.add(book);
        }
        callback.success(result);
    }

    /**
     * asks to reserve the book
     *
     * @param isbn the unique id of the book
     */
    public void reserveBook(String isbn, ReserveBookCallback callback)
    {
        callback.success();
    }

    /**
     * asks to follow the book
     *
     * @param isbn the unique id of the book
     */
    public void followBook(String isbn, FollowBookCallback callback)
    {
        callback.success();
    }

    /**
     * asks to upvote the book
     *
     * @param isbn the unique id of the book
     */
    public void upvoteBook(String isbn, UpvoteBookCallback callback)
    {
        callback.success();
    }

}
