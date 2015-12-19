package com.library_app.activities;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.library_app.R;
import com.library_app.Utils.NavigationUtils;
import com.library_app.adapter.BookCardsAdapter;
import com.library_app.callbacks.FollowBookCallback;
import com.library_app.callbacks.GetBooksCallback;
import com.library_app.callbacks.ReserveBookCallback;
import com.library_app.callbacks.SearchBooksCallback;
import com.library_app.callbacks.UpvoteBookCallback;
import com.library_app.controller.AuthenticationController;
import com.library_app.controller.ReaderController;
import com.library_app.model.Book;
import com.mikepenz.materialdrawer.Drawer;

import java.util.List;

public class BrowseBooksActivity extends AppCompatActivity implements BookCardsAdapter.Listener
{

    /* UI */
    Drawer navigationDrawer;
    View content;
    RecyclerView recyclerViewGroups;
    SwipeRefreshLayout swipeRefresh;
    EditText editTextSearch;
    ImageButton imageButtonSearch;
    Spinner spinnerSearchCriteria;

    /* fields */
    BookCardsAdapter adapterCards;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // setup layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        // get extras
        boolean canUpvote = getIntent().getBooleanExtra(getString(R.string.canUpvote), false);
        boolean canReserve = getIntent().getBooleanExtra(getString(R.string.canReserve), false);

        // setup navdrawer and toolbar
        content = findViewById(R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView text = new TextView(this);
        text.setText("Books");
        text.setTextAppearance(this, android.R.style.TextAppearance_Material_Widget_ActionBar_Title_Inverse);
        toolbar.addView(text);
        navigationDrawer = NavigationUtils.setupNavigationBar(this, 1, toolbar);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        imageButtonSearch = (ImageButton) findViewById(R.id.imageButtonSearch);
        spinnerSearchCriteria = (Spinner) findViewById(R.id.spinnerSearchCriteria);

        // reference views
        recyclerViewGroups = (RecyclerView) findViewById(R.id.recyclerViewBooks);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        // setup listeners
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadBooks();
            }
        });
        imageButtonSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchBooks();
            }
        });

        // setup list
        adapterCards = new BookCardsAdapter(this, canReserve, canUpvote);
        adapterCards.setListener(this);
        recyclerViewGroups.setAdapter(adapterCards);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewGroups.setLayoutManager(gridLayoutManager);

        // load data
        loadBooks();

    }


    /**
     * searches for the book using the search criteria selected in the spinner and the substring in the search edit text
     */
    private void searchBooks()
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

        ReaderController controller = new ReaderController(this);
        String str = editTextSearch.getText().toString();
        String searchcriteria = (String) spinnerSearchCriteria.getSelectedItem();
        controller.searchBooks(searchcriteria, str, new SearchBooksCallback()
        {
            @Override
            public void success(List<Book> books)
            {
                swipeRefresh.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        swipeRefresh.setRefreshing(false);

                    }
                });
                adapterCards.setData(books);
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
    public void onBackPressed()
    {
        if (navigationDrawer.isDrawerOpen())
            navigationDrawer.closeDrawer();
        else
            super.onBackPressed();
    }


    private void loadBooks()
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
        ReaderController controller = new ReaderController(this);
        controller.getBooks(new GetBooksCallback()
        {
            @Override
            public void success(List<Book> books)
            {
                swipeRefresh.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        swipeRefresh.setRefreshing(false);

                    }
                });
                adapterCards.setData(books);
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
    public void upvote(Book book)
    {
        ReaderController controller = new ReaderController(this);
        controller.upvoteBook(book.getIsbn(), new UpvoteBookCallback()
        {
            @Override
            public void success()
            {
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                loadBooks();
            }

            @Override
            public void fail(String message)
            {
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void reserve(Book book)
    {
        ReaderController controller = new ReaderController(this);
        controller.reserveBook(book.getIsbn(), new ReserveBookCallback()
        {
            @Override
            public void success()
            {
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                loadBooks();
            }

            @Override
            public void fail(String message)
            {
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void follow(Book book)
    {
        ReaderController controller = new ReaderController(this);
        controller.followBook(book.getIsbn(), new FollowBookCallback()
        {
            @Override
            public void success()
            {
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                loadBooks();
            }

            @Override
            public void fail(String message)
            {
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
