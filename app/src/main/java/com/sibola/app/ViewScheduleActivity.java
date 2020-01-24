package com.sibola.app;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ViewScheduleActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private List<Booking> myBookings = new ArrayList<>();
    private RecyclerView rView;
    private MyBookingAdapter mAdapter;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        //add to activity you want to pull variables from
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.user = (User) getIntent().getSerializableExtra("user");
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(R.drawable.ic_action_calendar);
        actionBar.setTitle("Jadwal Saya");

        mAdapter = new MyBookingAdapter(this.myBookings);

        // Initialize Firebase Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("bookings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    collectMyBooking(dataSnapshot);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //refreshBookingList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("DB_ERROR", "The read failed: " + databaseError.getMessage());
            }
        });

        rView = (RecyclerView) findViewById(R.id.recycleView2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rView.setLayoutManager(mLayoutManager);
        rView.setItemAnimator(new DefaultItemAnimator());
        // add line decoration
        rView.addItemDecoration(new DividerItemDecoration(this));

        // set adapter
        rView.setAdapter(mAdapter);

    }

    public void collectMyBooking(DataSnapshot bookingData) throws ParseException {

        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
        Date todayDate = c.getTime();
        Locale id = new Locale("in", "ID");

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyy",id);

        /*String todayDateString = sdf.format(todayDate);
        Log.d("LOCALE", Locale.getDefault().getDisplayLanguage());
        Log.d("TODAY_DATE", todayDateString);*/

        for (DataSnapshot tanggal : bookingData.getChildren()) {

            Log.d("BOOKING_DATE", tanggal.getKey());
            Date date = sdf.parse(tanggal.getKey());

            if (!date.before(todayDate)) {
                for (DataSnapshot jam : tanggal.getChildren()) {
                    Log.d("USER_ID", jam.child("userId").getValue().toString());
                    if (user.getUserId().equals(jam.child("userId").getValue().toString())) {
                        Booking b = jam.getValue(Booking.class);
                        myBookings.add(b);
                    }
                }

            }
        }

        mAdapter.notifyDataSetChanged();
    }

}
