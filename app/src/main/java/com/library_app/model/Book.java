package com.library_app.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ahmed on 12/17/2015.
 */
public class Book
{
    /* constants */

    /* fields */
    String isbn;
    String title;
    String author;
    String imageUrl;
    boolean available;
    boolean isFollowedByMe;
    int nUpvotes;

    /* constructors */

    public Book()
    {
    }

    /* setters and getters */

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable()
    {
        return available;
    }

    public void setAvailable(boolean available)
    {
        this.available = available;
    }

    public int getnUpvotes()
    {
        return nUpvotes;
    }

    public void setnUpvotes(int nUpvotes)
    {
        this.nUpvotes = nUpvotes;
    }

    public boolean isFollowedByMe()
    {
        return isFollowedByMe;
    }

    public void setIsFollowedByMe(boolean isFollowedByMe)
    {
        this.isFollowedByMe = isFollowedByMe;
    }

    /* methods */

    public static Book parseFromJson(JSONObject jsonObject) throws JSONException
    {
        Book book = new Book();

        book.setIsbn(jsonObject.getString("isbn"));
        book.setAuthor(jsonObject.getString("author"));
        book.setTitle(jsonObject.getString("title"));
        if (!jsonObject.isNull("img_url"))
            book.setImageUrl(jsonObject.getString("img_url"));
        book.setnUpvotes(jsonObject.getInt("nUpvotes"));
        book.setAvailable(jsonObject.getInt("available") != 0);

        return book;
    }
}
