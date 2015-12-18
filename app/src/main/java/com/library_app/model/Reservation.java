package com.library_app.model;

import java.util.Calendar;

/**
 * Created by ahmed on 12/18/2015.
 */
public class Reservation
{
    String id;
    User user;
    Book book;
    Calendar reservationData, deadlineDate, lendDate, returnDate;

    public Reservation()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

    public Calendar getReservationData()
    {
        return reservationData;
    }

    public void setReservationData(Calendar reservationData)
    {
        this.reservationData = reservationData;
    }

    public Calendar getDeadlineDate()
    {
        return deadlineDate;
    }

    public void setDeadlineDate(Calendar deadlineDate)
    {
        this.deadlineDate = deadlineDate;
    }

    public Calendar getLendDate()
    {
        return lendDate;
    }

    public void setLendDate(Calendar lendDate)
    {
        this.lendDate = lendDate;
    }

    public Calendar getReturnDate()
    {
        return returnDate;
    }

    public void setReturnDate(Calendar returnDate)
    {
        this.returnDate = returnDate;
    }
}
