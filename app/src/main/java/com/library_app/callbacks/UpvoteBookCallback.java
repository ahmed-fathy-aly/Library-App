package com.library_app.callbacks;

import com.library_app.model.Book;

import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public interface UpvoteBookCallback
{
    void success();
    void fail(String message);
}
