package com.library_app.callbacks;

import com.library_app.model.Contact;

import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public interface TestPostCallback
{
    void success(String postResult);
    void fail(String message);
}
