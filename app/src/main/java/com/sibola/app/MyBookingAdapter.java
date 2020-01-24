package com.sibola.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aizen on 21 Mei 2017.
 */

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyViewHolder>{

    private List<Booking> myBookings;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tanggal, jam;

        public MyViewHolder(View view){
            super(view);
            tanggal = (TextView) view.findViewById(R.id.textTanggal);
            jam = (TextView) view.findViewById(R.id.textJam2);
        }
    }

    public MyBookingAdapter(List<Booking> myBookings) {
        this.myBookings = myBookings;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_schedule_row, parent, false);

        return new MyBookingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Booking booking= myBookings.get(position);
        holder.jam.setText(booking.getSlotJam());
        holder.tanggal.setText(booking.getTanggal());
    }

    @Override
    public int getItemCount() {
        return myBookings.size();
    }


}
