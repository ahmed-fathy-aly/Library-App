package com.library_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.library_app.R;
import com.library_app.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public class BookCardsAdapter extends RecyclerView.Adapter<BookCardsAdapter.ViewHolder>
{
    /* fields */
    Context context;
    List<Book> data;
    Listener listener;
    boolean canReserve;
    boolean canUpvote;

    /* constructor */
    public BookCardsAdapter(Context context, boolean canReserve, boolean canUpvote )
    {
        this.context = context;
        this.data = new ArrayList<>();
        this.canReserve = canReserve;
        this.canUpvote = canUpvote;
    }

    /* methods */

    /**
     * replaces the data and updates UI
     *
     * @param books
     */
    public void setData(List<Book> books)
    {
        this.data.clear();
        this.data.addAll(books);
        notifyDataSetChanged();
    }

    /**
     * registers to be invoked for clicks
     */
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.book_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Book book = data.get(position);

        // set data to view
        holder.textViewBookTitle.setText(book.getTitle());
        holder.textViewnUpvotes.setText(book.getnUpvotes() + "");
        holder.buttonReserverOrFollow.setText(book.isAvailable() ? "RESERVE" : "FOLLOW");
        if (!book.isAvailable() && book.isFollowedByMe())
        {
            holder.buttonReserverOrFollow.setText("FOLLOWED");
            holder.buttonReserverOrFollow.setEnabled(false);
        }
        else
            holder.buttonReserverOrFollow.setEnabled(true);


        if (book.getImageUrl() != null)
        {
            Ion.with(holder.imageViewBook)
                    .placeholder(R.drawable.ic_book)
                    .load(book.getImageUrl());
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public List<Book> getData()
    {
        return data;
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imageViewBook;
        TextView textViewBookTitle;
        TextView textViewnUpvotes;
        ImageButton imageButtonUpvote;
        Button buttonReserverOrFollow;

        public ViewHolder(View view)
        {
            super(view);

            // reference views
            imageViewBook = (ImageView) view.findViewById(R.id.imageViewBook);
            textViewBookTitle = (TextView) view.findViewById(R.id.textViewBookName);
            buttonReserverOrFollow = (Button) view.findViewById(R.id.buttonReserverOrFollow);
            textViewnUpvotes = (TextView) view.findViewById(R.id.textViewNUpvotes);
            imageButtonUpvote = (ImageButton) view.findViewById(R.id.imageButtonUpvote);

            // set listener
            imageButtonUpvote.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                        listener.upvote(data.get(getAdapterPosition()));
                }
            });
            buttonReserverOrFollow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                    {
                        Book book = data.get(getAdapterPosition());
                        if (book.isAvailable())
                            listener.reserve(book);
                        else
                            listener.follow(book);
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });
            // disable stuff based on privilliges
            buttonReserverOrFollow.setVisibility(canReserve ?  View.VISIBLE : View.GONE);
            imageButtonUpvote.setEnabled(canUpvote);
        }

    }

    public interface Listener
    {
        void upvote(Book book);

        void reserve(Book book);

        void follow(Book book);

    }
}
