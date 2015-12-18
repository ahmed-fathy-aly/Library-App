package com.library_app.callbacks;

import com.library_app.model.Book;
import com.library_app.model.Reservation;

import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public interface GetReservationsCallback
{
    void success(List<Reservation> reservations);
    void fail(String message);
}
