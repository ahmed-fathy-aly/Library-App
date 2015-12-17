package com.library_app.callbacks;

import com.library_app.model.Book;

import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public interface GetBooksCallback
{
    void success(List<Book> books);
    void fail(String message);
}
