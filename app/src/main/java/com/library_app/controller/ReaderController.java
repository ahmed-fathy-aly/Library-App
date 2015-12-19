package com.library_app.controller;

import android.content.Context;

import com.library_app.callbacks.FollowBookCallback;
import com.library_app.callbacks.GetBooksCallback;
import com.library_app.callbacks.GetReservationsCallback;
import com.library_app.callbacks.ReserveBookCallback;
import com.library_app.callbacks.SearchBooksCallback;
import com.library_app.callbacks.UpvoteBookCallback;
import com.library_app.model.Book;
import com.library_app.model.Reservation;
import com.library_app.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public class ReaderController
{
    /* fields */
    Context context;

    /* constructor */
    public ReaderController()
    {
    }

    public ReaderController(Context context)
    {
        this.context = context;
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
     * returns the list of the books that meets the search criteria
     * @param serachCriteria Title or Author or ISBN
     * @param str the substring used for searching
     *
     */
    public void searchBooks(String serachCriteria, String str, SearchBooksCallback callback)
    {
        List<Book> result = new ArrayList<>();
        for (int i = 1; i < 15; i++)
        {
            Book book = new Book();
            book.setTitle( str + " " + i);
            book.setAuthor(str + " " + i);
            book.setAvailable(i % 3 == 0);
            book.setImageUrl("http://ecx.images-amazon.com/images/I/519978ZK54L.jpg");
            book.setnUpvotes(i);
            book.setIsbn(str + " " + i);

            result.add(book);
        }
        callback.success(result);
    }
    /**
     * returns the list of reservations
     * The admin gets all reservations, the user only gets his reservation (the backend should handle that)
     * If the reservation's book is not returned then its returnDate should be null
     * If the reservation's book is not lent then its lentDate should be null
     */
    public void getReservations(GetReservationsCallback callback)
    {
        List<Reservation> result = new ArrayList<>();
        for (int i = 1; i < 20; i++)
        {
            Book book = new Book();
            book.setTitle("book " + i);
            book.setAuthor("author + " + i);
            book.setAvailable(i % 3 == 0);
            book.setImageUrl("http://ecx.images-amazon.com/images/I/519978ZK54L.jpg");
            book.setnUpvotes(i);
            book.setIsbn(i + "");

            User user = new User();
            user.setId(i + "");
            user.setName("user " + i);
            user.setMail("mail " + i);
            user.setType(User.STUDENT);

            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setBook(book);

            Calendar deadline = Calendar.getInstance();
            deadline.add(Calendar.DAY_OF_YEAR, i * 3);
            reservation.setDeadlineDate(deadline);

            Calendar lentDate = Calendar.getInstance();
            lentDate.add(Calendar.DAY_OF_YEAR, i);
            if (i % 2 == 0)
                reservation.setLendDate(lentDate);

            Calendar returnDate = Calendar.getInstance();
            returnDate.add(Calendar.DAY_OF_YEAR, i * 2);
            if (i % 6 == 0)
                reservation.setReturnDate(returnDate);

            result.add(reservation);
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
