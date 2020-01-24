package com.sibola.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private User mUser;
    private CustomCalendarView calendarView;
    Locale locale = new Locale("in");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Locale.setDefault(locale);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadSignInView();
        } else {

            Toast.makeText(HomeActivity.this, "Selamat datang di aplikasi SIBOLA", Toast.LENGTH_SHORT).show();

            mUserId = mFirebaseUser.getUid();
            mDatabase.child("users").child(mUserId).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    mUser = dataSnapshot.getValue(User.class);
                    TextView haiUsernameText = (TextView) findViewById(R.id.hai_username);
                    TextView depositText = (TextView) findViewById(R.id.depositText);
                    String deposit = NumberFormat.getNumberInstance(locale).format(mUser.getDeposit());
                    haiUsernameText.setText("Hai " + mUser.getUsername() + "! Deposit kamu sebanyak:");
                    depositText.setText("Rp " + deposit);

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    TextView haiUsernameText = (TextView) findViewById(R.id.hai_username);
                    haiUsernameText.setText("Gagal membaca database: " + databaseError.getCode());
                    // ...
                }
            });

            //Initialize CustomCalendarView from layout
            calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

            //Initialize calendar with date
            Calendar currentCalendar = Calendar.getInstance(locale);

            //Show Monday as first date of week
            calendarView.setFirstDayOfWeek(Calendar.MONDAY);

            //Show/hide overflow days of a month
            calendarView.setShowOverflowDate(false);

            //call refreshCalendar to update calendar the view
            calendarView.refreshCalendar(currentCalendar);

            calendarView.setCalendarListener(new CalendarListener() {
                @Override
                public void onDateSelected(Date date) {
                    if (!CalendarUtils.isPastDay(date)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM", locale);
                        Toast.makeText(HomeActivity.this, sdf.format(date), Toast.LENGTH_SHORT).show();
                        //selectedDateTv.setText(df.format(date));
                        sdf = new SimpleDateFormat("dd MMMM yyy", locale);
                        String selectedDate = sdf.format(date);
                        Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
                        intent.putExtra("thisDate", selectedDate);
                        intent.putExtra("user", mUser);
                        startActivity(intent);
                    }else{
                        Toast.makeText(HomeActivity.this, "Silahkan pilih hari lain", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onMonthChanged(Date date) {
                    //SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
                    //Toast.makeText(HomeActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                }
            });

            //adding calendar day decorators
            List<DayDecorator> decorators = new ArrayList<>();
            decorators.add(new CalendarColorDecorator());
            calendarView.setDecorators(decorators);
            calendarView.refreshCalendar(currentCalendar);
                      //touchable deposit card view
            CardView depositCardView = (CardView) findViewById(R.id.cardView2);
            depositCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDepositDialog();
                }
            });
        }
    }

    private void showDepositDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        //set title dialog
        alertDialogBuilder.setTitle("Deposit");

        //set pesan dari dialog
        alertDialogBuilder
                .setMessage("Untuk dapat melakukan booking jadwal lapangan, anda harus memiliki deposit. " +
                        "Hubungi admin SIBOLA untuk menambah deposit anda.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });

        //membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        //menampilkan alert dialog
        alertDialog.show();
    }

    private void loadSignInView() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            mFirebaseAuth.signOut();
            Toast.makeText(this, "Anda telah keluar akun", Toast.LENGTH_LONG).show();
            loadSignInView();
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_signout) {
                    mFirebaseAuth.signOut();
                    Toast.makeText(HomeActivity.this, "Anda telah keluar", Toast.LENGTH_LONG).show();
                    loadSignInView();
                    return true;
                } else if (id == R.id.action_view_schedule){
                    Intent intent = new Intent(HomeActivity.this, ViewScheduleActivity.class);
                    intent.putExtra("user", mUser);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        popup.inflate(R.menu.menu_home);
        popup.show();
    }

    private class CalendarColorDecorator implements DayDecorator {

        public void decorate(DayView dayView) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dayView.getDate());

            if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor("#CFD8DC");
                dayView.setTextColor(color);
            } else if (CalendarUtils.isToday(cal)){
                int colorBG = Color.parseColor("#bef67a");
                dayView.setTypeface(null, Typeface.BOLD);
                dayView.setBackgroundColor(colorBG);
            }
        }
    }
}