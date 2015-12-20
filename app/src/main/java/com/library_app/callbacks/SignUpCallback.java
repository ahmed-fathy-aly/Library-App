package com.library_app.callbacks;

import com.library_app.model.User;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface SignUpCallback
{
    void success(User user, String authenticationToken);

    void fail(String message);
}
