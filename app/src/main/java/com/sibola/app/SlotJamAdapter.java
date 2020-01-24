package com.sibola.app;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aizen on 16 Mei 2017.
 */

public class SlotJamAdapter extends RecyclerView.Adapter<SlotJamAdapter.MyViewHolder>{

    private List<Booking> bookingList;
    public static MyAdapterListener onClickListener;

    public interface MyAdapterListener {

        void buttonBookingOnClick(View v, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView slotJam, status;
        public Button buttonBooking;

        public MyViewHolder(View view) {
            super(view);
            slotJam = (TextView) view.findViewById(R.id.textJam);
            status = (TextView) view.findViewById(R.id.textStatus);
            buttonBooking = (Button) view.findViewById(R.id.buttonBooking);

            buttonBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.buttonBookingOnClick(v, getAdapterPosition());
                }
            });

        }
    }

    public SlotJamAdapter (List<Booking> bookingList,  MyAdapterListener listener){
        this.bookingList = bookingList;
        this.onClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_jam_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Booking booking= bookingList.get(position);
        holder.slotJam.setText(booking.getSlotJam());
        if(booking.getUsername() == null){
            holder.status.setText("Belum dibooking");
            holder.status.setTextColor(Color.parseColor("#757575"));
            holder.buttonBooking.setAlpha(1f);
            holder.buttonBooking.setEnabled(true);
        } else {
            holder.status.setText("Dibooking oleh " + booking.getUsername());
            holder.status.setTextColor(Color.parseColor("#689F38"));
            holder.buttonBooking.setAlpha(.25f);
            holder.buttonBooking.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}
