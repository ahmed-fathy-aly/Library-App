package com.library_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.library_app.R;
import com.library_app.Utils.TimeFormatUtils;
import com.library_app.model.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 12/18/2015.
 */
public class ReservationCardAdapter extends RecyclerView.Adapter<ReservationCardAdapter.ViewHolder>
{

    /* fields */
    private final Context context;
    private final boolean canChangeReservation;
    private List<Reservation> data;
    private Listener listener;

    public ReservationCardAdapter(Context context, boolean canChangeReservation)
    {
        this.context = context;
        this.canChangeReservation = canChangeReservation;
        this.data = new ArrayList<>();
    }

    /**
     * registers to be notified on the item's clicks
     */
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    /**
     * updates the data and updates UI
     */
    public void setData(List<Reservation> newData)
    {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Reservation reservation = data.get(position);
        holder.textViewDeadline.setText(TimeFormatUtils.foramtDate(reservation.getDeadlineDate()));
        holder.textViewBook.setText(reservation.getBook().getTitle());
        holder.textViewUser.setText(reservation.getUser().getName());

        // choose the text to be put on the button
        if (reservation.getLendDate() == null)
        {
            holder.buttonLendOrReturn.setText("MARK AS LENT");
            holder.buttonLendOrReturn.setEnabled(true);
        } else if (reservation.getReturnDate() == null)
        {
            holder.buttonLendOrReturn.setText("MARK AS RETURNED");
            holder.buttonLendOrReturn.setEnabled(true);
        } else
        {
            holder.buttonLendOrReturn.setText("RETURNED");
            holder.buttonLendOrReturn.setEnabled(false);
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewUser, textViewBook, textViewDeadline;
        Button buttonLendOrReturn;

        public ViewHolder(View view)
        {
            super(view);

            // reference views
            textViewBook = (TextView) view.findViewById(R.id.textViewReservationBook);
            textViewUser = (TextView) view.findViewById(R.id.textViewReservationUser);
            textViewDeadline = (TextView) view.findViewById(R.id.textViewReservationDeadline);
            buttonLendOrReturn = (Button) view.findViewById(R.id.buttonLendOrReturn);

            // add listeners
            buttonLendOrReturn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                    {
                        Reservation reservation = data.get(getAdapterPosition());
                        if (reservation.getLendDate() == null)
                            listener.markAsLent(reservation);
                        else if (reservation.getReturnDate() == null)
                            listener.markAsReturned(reservation);
                    }

                }
            });

            // check if updaing the status is enabled
            buttonLendOrReturn.setVisibility(canChangeReservation ? View.VISIBLE : View.GONE);
        }
    }

    public interface Listener
    {
        void markAsLent(Reservation reservation);

        void markAsReturned(Reservation reservation);
    }
}
