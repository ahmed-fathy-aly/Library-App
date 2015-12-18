package com.library_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.library_app.R;
import com.library_app.Utils.NavigationUtils;
import com.library_app.Utils.PhotoFileUtils;
import com.library_app.callbacks.AddBookCallback;
import com.library_app.controller.AdminController;
import com.library_app.controller.AuthenticationController;
import com.mikepenz.materialdrawer.Drawer;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

public class AddBookActivity extends AppCompatActivity
{

    /* Ui */
    View content;
    Drawer navigationDrawer;
    EditText editTextISBN, editTextISN, editTextTitle, editTextAuthor;
    ImageView imageViewCover;
    Button buttonAddBook, buttonPickImage;
    CheckBox checkBoxIsCopy;
    ProgressBar progressBar;

    /* fields */
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // setup content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // setup navdrawer and toolbar
        content = findViewById(R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Book");
        navigationDrawer = NavigationUtils.setupNavigationBar(this, 2, toolbar);

        // reference views
        editTextISBN = (EditText) findViewById(R.id.editTextISBN);
        editTextISN = (EditText) findViewById(R.id.editTextISN);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextAuthor = (EditText) findViewById(R.id.editTextAuthor);
        imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        buttonAddBook = (Button) findViewById(R.id.buttonAddBook);
        buttonPickImage = (Button) findViewById(R.id.buttonPickCover);
        checkBoxIsCopy = (CheckBox) findViewById(R.id.checkBocCopy);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // set listeners
        buttonPickImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pickImage();
            }
        });
        buttonAddBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addBook();
            }
        });
        checkBoxIsCopy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                onIsCopyCheckChange(isChecked);
            }
        });


        // prepare temp image file
        try
        {
            imageFile = PhotoFileUtils.createImageFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (navigationDrawer.isDrawerOpen())
            navigationDrawer.closeDrawer();
        else
            super.onBackPressed();
    }

    /**
     * changes the visibility of the UI only needed for new books not just new coies
     */
    private void onIsCopyCheckChange(boolean isChecked)
    {
        int newVisibility = isChecked ? View.GONE : View.VISIBLE;
        editTextTitle.setVisibility(newVisibility);
        editTextTitle.setVisibility(newVisibility);
        editTextAuthor.setVisibility(newVisibility);
        buttonPickImage.setVisibility(newVisibility);
        imageViewCover.setVisibility(newVisibility);
    }

    /**
     * uploads the new book
     */
    private void addBook()
    {
        // gather data
        String isbn = editTextISBN.getText().toString();
        String isn = editTextISN.getText().toString();
        String title = editTextTitle.getText().toString();
        String author = editTextAuthor.getText().toString();
        Log.e("Game", "file size = " + imageFile.getTotalSpace());

        // upload new book
        progressBar.setVisibility(View.VISIBLE);
        buttonAddBook.setVisibility(View.INVISIBLE);
        AdminController controller = new AdminController(this);
        controller.addBook(isbn, isn, title, author, imageFile, new AddBookCallback()
        {
            @Override
            public void success()
            {
                progressBar.setVisibility(View.INVISIBLE);
                buttonAddBook.setVisibility(View.VISIBLE);
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void fail(String message)
            {
                progressBar.setVisibility(View.INVISIBLE);
                buttonAddBook.setVisibility(View.VISIBLE);
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * picks an image from the gallery
     */
    private void pickImage()
    {
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode)
            {
                case Crop.REQUEST_PICK:
                    Crop.of(data.getData(), Uri.fromFile(imageFile)).asSquare().start(this);
                    break;

                case Crop.REQUEST_CROP:
                    Uri uri = Crop.getOutput(data);
                    imageViewCover.setImageURI(uri);
                    break;
            }
    }
}
