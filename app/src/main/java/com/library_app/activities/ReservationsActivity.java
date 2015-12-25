package com.library_app.activities;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.library_app.R;
import com.library_app.Utils.NavigationUtils;
import com.library_app.adapter.BookCardsAdapter;
import com.library_app.adapter.ReservationCardAdapter;
import com.library_app.callbacks.GetReservationsCallback;
import com.library_app.callbacks.MarkAsLentCallback;
import com.library_app.callbacks.MarkAsReturnedCallback;
import com.library_app.controller.AdminController;
import com.library_app.controller.AuthenticationController;
import com.library_app.controller.ReaderController;
import com.library_app.model.Reservation;
import com.mikepenz.materialdrawer.Drawer;

import java.util.List;

public class ReservationsActivity extends AppCompatActivity implements ReservationCardAdapter.Listener
{

    /* UI */
    private View content;
    private Drawer navigationDrawer;
    private RecyclerView recyclerViewReservations;
    private SwipeRefreshLayout swipeRefresh;
    Spinner spinnerReservationFilter;
    private ReservationCardAdapter adapterReservations;

    /* fields */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // setup layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        // setup navdrawer and toolbar
        content = findViewById(R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView text = new TextView(this);
        text.setText("Reservations");
        text.setTextAppearance(this, android.R.style.TextAppearance_Material_Widget_ActionBar_Title_Inverse);
        toolbar.addView(text);
        navigationDrawer = NavigationUtils.setupNavigationBar(this, 3, toolbar);

        // reference views
        recyclerViewReservations = (RecyclerView) findViewById(R.id.recyclerViewReservations);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        spinnerReservationFilter = (Spinner) findViewById(R.id.spinnerReservationFilter);

        // setup listeners
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadReservations();
            }
        });
      spinnerReservationFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
      {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
          {
              String filter = (String) spinnerReservationFilter.getSelectedItem();
              adapterReservations.filterData(filter);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent)
          {

          }
      });

        // setup list
        adapterReservations = new ReservationCardAdapter(this, new AuthenticationController(this).getCurrentUser().canChangeReservation());
        adapterReservations.setListener(this);
        recyclerViewReservations.setAdapter(adapterReservations);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewReservations.setLayoutManager(linearLayoutManager);

        // load data
        loadReservations();
    }

    @Override
    public void onBackPressed()
    {
        if (navigationDrawer.isDrawerOpen())
            navigationDrawer.closeDrawer();
        else
            super.onBackPressed();
    }


    private void loadReservations()
    {
        // start swipe refersh refershing
        swipeRefresh.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefresh.setRefreshing(true);
            }
        });
        spinnerReservationFilter.setEnabled(false);

        // download the reservations
        ReaderController controller = new ReaderController(this);
        controller.getReservations(new GetReservationsCallback()
        {
            @Override
            public void success(List<Reservation> reservations)
            {
                swipeRefresh.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        swipeRefresh.setRefreshing(false);

                    }
                });
                spinnerReservationFilter.setEnabled(true);
                adapterReservations.setOriginalData(reservations);
                String filter = (String) spinnerReservationFilter.getSelectedItem();
                adapterReservations.filterData(filter);
            }

            @Override
            public void fail(String message)
            {
                swipeRefresh.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        swipeRefresh.setRefreshing(false);

                    }
                });
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void markAsLent(Reservation reservation)
    {
        AdminController controller = new AdminController(this);
        controller.markAsLent(reservation, new MarkAsLentCallback()
        {
            @Override
            public void success()
            {
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                loadReservations();
            }

            @Override
            public void fail(String message)
            {
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void markAsReturned(Reservation reservation)
    {
        AdminController controller = new AdminController(this);
        controller.markAsReturned(reservation, new MarkAsReturnedCallback()
        {
            @Override
            public void success()
            {
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                loadReservations();
            }

            @Override
            public void fail(String message)
            {
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
