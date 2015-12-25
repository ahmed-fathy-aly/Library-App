package com.library_app.model;

import com.library_app.Utils.TimeFormatUtils;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Reservation parseFromJson(JSONObject json) throws JSONException
    {
        Reservation reservation = new Reservation();

        reservation.setId(json.getString("reservation_code"));
        reservation.setUser(User.parseFromJson(json.getJSONObject("user")));
        reservation.setBook(Book.parseFromJson(json.getJSONObject("book")));
        reservation.setDeadlineDate(TimeFormatUtils.parseCalendar(json.getString("return_deadline")));
        if (!json.isNull("lending_date"))
            reservation.setLendDate(TimeFormatUtils.parseCalendar(json.getString("lending_date")));
        if (!json.isNull("return_date"))
            reservation.setReturnDate(TimeFormatUtils.parseCalendar(json.getString("return_date")));
        return reservation;
    }
}
