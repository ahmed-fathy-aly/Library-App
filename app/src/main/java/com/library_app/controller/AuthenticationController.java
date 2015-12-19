package com.library_app.controller;

import com.library_app.model.User;

/**
 * Created by ahmed on 12/17/2015.
 */
public class AuthenticationController
{

    /**
     * TODO - uses the shared preferencs
     *
     * @return the logged in user
     */
    public static User getCurrentUser()
    {
        User user = new User();

        user.setId("1");
        user.setName("Mr Admin");
        user.setMail("admin@mail.com");
        user.setType(User.PROFESSOR);

        return user;
    }
}
