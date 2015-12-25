package com.library_app.controller;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.library_app.R;
import com.library_app.callbacks.FollowBookCallback;
import com.library_app.callbacks.GetBooksCallback;
import com.library_app.callbacks.GetReservationsCallback;
import com.library_app.callbacks.ReserveBookCallback;
import com.library_app.callbacks.SearchBooksCallback;
import com.library_app.callbacks.UpvoteBookCallback;
import com.library_app.model.Book;
import com.library_app.model.Reservation;
import com.library_app.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void getBooks(String subString, final GetBooksCallback callback)
    {
        Ion.with(context)
                .load("GET", context.getString(R.string.host) + "books/index.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("substring", subString)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "get books result = " + result);

                        try
                        {
                            // check status
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");
                            if (status != 1)
                            {
                                callback.fail("Failed");
                                return;
                            }

                            // parse books
                            JSONObject responseJson = resultJson.getJSONObject("response");
                            JSONArray booksJson = responseJson.getJSONArray("books");
                            List<Book> books = new ArrayList<Book>();
                            for (int i = 0; i < booksJson.length(); i++)
                                books.add(Book.parseFromJson(booksJson.getJSONObject(i)));
                            callback.success(books);
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                            callback.fail(e1.getMessage());
                            return;
                        }
                    }
                });

    }


    /**
     * returns the list of reservations
     * The admin gets all reservations, the user only gets his reservation (the backend should handle that)
     * If the reservation's book is not returned then its returnDate should be null
     * If the reservation's book is not lent then its lentDate should be null
     */
    public void getReservations(final GetReservationsCallback callback)
    {
        Ion.with(context)
                .load("GET", context.getString(R.string.host) + "reservations/index.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "get reservations result = " + result);

                        try
                        {
                            // check status
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");
                            if (status != 1)
                            {
                                callback.fail("Failed");
                                return;
                            }

                            // parse reservations
                            JSONObject responseJson = resultJson.getJSONObject("response");
                            JSONArray reservationsJson = responseJson.getJSONArray("reservations");
                            List<Reservation> reservations = new ArrayList<Reservation>();
                            for (int i = 0; i < reservationsJson.length(); i++)
                                reservations.add(Reservation.parseFromJson(reservationsJson.getJSONObject(i)));
                            callback.success(reservations);
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                            callback.fail(e1.getMessage());
                            return;
                        }
                    }

                });
    }

    /**
     * asks to reserve the book
     *
     * @param isbn the unique id of the book
     */
    public void reserveBook(String isbn, final ReserveBookCallback callback)
    {
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "books/reserve.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("isbn", isbn)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "reserve book response = " + result);

                        // check if failed
                        try
                        {
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");

                            if (status != 1)
                                callback.fail("Failed");
                            else
                                callback.success();
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }

                    }
                });
    }

    /**
     * asks to follow the book
     *
     * @param isbn the unique id of the book
     */
    public void followBook(String isbn, final FollowBookCallback callback)
    {
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "books/follow.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("isbn", isbn)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "follow book response = " + result);

                        // check if failed
                        try
                        {
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");

                            if (status != 1)
                                callback.fail("Failed");
                            else
                                callback.success();
                            ;
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }

                    }
                });
    }

    /**
     * asks to upvote the book
     *
     * @param isbn the unique id of the book
     */
    public void upvoteBook(String isbn, final UpvoteBookCallback callback)
    {
        Log.e("Game", "isbn = " + isbn);
        Ion.with(context)
                .load("POST", context.getString(R.string.host) + "books/upvote.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .setBodyParameter("isbn", isbn)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "upvote book response = " + result);

                        // check if failed
                        try
                        {
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");

                            if (status != 1)
                                callback.fail("Failed");
                            else
                                callback.success();
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }

                    }
                });
    }

    public void getFollowedBooks(final GetBooksCallback callback)
    {
        Ion.with(context)
                .load("GET", context.getString(R.string.host) + "books/followed.json")
                .addHeader("Authentication", "Token " + new AuthenticationController(context).getAuthorizationToken())
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "get books result = " + result);

                        try
                        {
                            // check status
                            JSONObject resultJson = new JSONObject(result);
                            int status = resultJson.getInt("status");
                            if (status != 1)
                            {
                                callback.fail("Failed");
                                return;
                            }

                            // parse books
                            JSONObject responseJson = resultJson.getJSONObject("response");
                            JSONArray booksJson = responseJson.getJSONArray("books");
                            List<Book> books = new ArrayList<Book>();
                            for (int i = 0; i < booksJson.length(); i++)
                                books.add(Book.parseFromJson(booksJson.getJSONObject(i)));
                            callback.success(books);
                            return;
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                            callback.fail(e1.getMessage());
                            return;
                        }
                    }
                });

    }
}
