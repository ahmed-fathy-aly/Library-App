package com.library_app.model;

/**
 * Created by ahmed on 12/17/2015.
 */
public class User
{
    /* constants */
    public static final String ADMIN = "admin";
    public static final String STUDENT = "student";
    public static final String PROFESSOR = "professor";

    /* fields */
    String id;
    String mail;
    String name;
    String type;

    /* cosntructor */
    public User()
    {

    }

    /* getters and setters */

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
