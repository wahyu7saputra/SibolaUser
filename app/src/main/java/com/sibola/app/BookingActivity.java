package com.sibola.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aizen on 10 Mei 2017.
 */

public class BookingActivity extends AppCompatActivity {

    private static final String TAG = "BOOKING_LIST";
    private DatabaseReference mDatabase;

    private List<Booking> bookingList = new ArrayList<>();
    private List<Booking> bookingListFromFirebase = new ArrayList<>();
    private RecyclerView rView;
    private SlotJamAdapter mAdapter;
    private String thisDate;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //add to activity you want to pull variables from
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.thisDate = extras.getString("thisDate");
            this.user = (User) getIntent().getSerializableExtra("user");
        }

        final ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_calendar);
        actionBar.setTitle(thisDate);

        // Initialize Firebase Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("bookings").child(thisDate).addValueEventListener(new ValueEventListener() {
            public static final String TAG = "LIST_ADDED_FIREBASE";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()){
                    Booking b = bookingSnapshot.getValue(Booking.class);
                    bookingListFromFirebase.add(b);
                    Log.i(TAG,"add booking hour = " + b.getSlotJam());
                }
                refreshBookingList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "The read failed: " + databaseError.getMessage());
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        rView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new SlotJamAdapter(this.bookingList, new SlotJamAdapter.MyAdapterListener() {
            @Override
            public void buttonBookingOnClick(View v, int position) {
                Booking booking = bookingList.get(position);
                showDialog(booking);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rView.setLayoutManager(mLayoutManager);
        rView.setItemAnimator(new DefaultItemAnimator());
        // add line decoration
        rView.addItemDecoration(new DividerItemDecoration(this));

        // set adapter
        rView.setAdapter(mAdapter);

        /*rView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Booking booking = bookingList.get(position);
                Toast.makeText(getApplicationContext(), booking.getTanggal() + " pukul "
                        + booking.getSlotJam(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/

        initBookingList();
    }

    private void showDialog(final Booking b) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        //set title dialog
        alertDialogBuilder.setTitle("Konfirmasi Booking");

        //set pesan dari dialog
        alertDialogBuilder
                .setMessage("Anda akan booking lapangan futsal pada " + b.getTanggal()
                        + " pukul " + b.getSlotJam() + "?\n\n"
                        + "DP Rp 50.000 akan dikurangi dari deposit anda.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        createBooking(b);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });

        //membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        //menampilkan alert dialog
        alertDialog.show();
    }

    private void createBooking(Booking b){

        long deposit = user.getDeposit();
        b.setUsername(user.getUsername());
        b.setUserId(user.getUserId());

        if(deposit < 50000){
            Toast.makeText(getApplicationContext(), "Deposit tidak mencukupi!", Toast.LENGTH_SHORT).show();
        } else {
            deposit = deposit - 50000;
            user.setDeposit(deposit);
            mDatabase.child("bookings").child(b.getTanggal()).child(b.getSlotJam()).setValue(b);
            mDatabase.child("users").child(user.getUserId()).setValue(user);
            Toast.makeText(getApplicationContext(), "Booking berhasil!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initBookingList(){

        /**
         * Tambahkan item ke dataset
         */
        Booking booking = new Booking(thisDate, "09:00 - 10:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "10:00 - 11:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "11:00 - 12:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "12:00 - 13:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "13:00 - 14:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "14:00 - 15:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "15:00 - 16:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "16:00 - 17:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "17:00 - 18:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "18:00 - 19:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "19:00 - 20:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "20:00 - 21:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "21:00 - 22:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "22:00 - 23:00");
        bookingList.add(booking);

        booking = new Booking(thisDate, "23:00 - 24:00");
        bookingList.add(booking);

        /*for(Booking bData : bookingList) {
            //Booking bData = bList;
            Log.i(TAG, "booking hour = " + bData.getSlotJam() + " | " + bData.getTanggal());
        }*/

        mAdapter.notifyDataSetChanged();
    }

    private void refreshBookingList(){

        /*for(Booking bData : bookingListFromFirebase) {
            //Booking bData = bList;
            Log.i(TAG, "booking hour = " + bData.getSlotJam() + " | " + bData.getTanggal());
        }*/

        for(Booking bDataFireBase : bookingListFromFirebase){
            int index = 0;
            for(Booking bData : bookingList){

                Log.i(TAG,"booking hour = " + bData.getSlotJam() + " | " + bData.getTanggal());

                if(bData.getSlotJam().equals(bDataFireBase.getSlotJam())){
                    bookingList.set(index, bDataFireBase);
                    Log.i(TAG,"ganti di indeks = " + index + " | username = " + bDataFireBase.getUsername());
                }
                index++;
            }
        }

        mAdapter.notifyDataSetChanged();
    }

}
