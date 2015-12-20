package com.library_app.callbacks;

import com.library_app.model.Contact;

import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public interface TestGetCalback
{
    void success(List<Contact> books);
    void fail(String message);
}
