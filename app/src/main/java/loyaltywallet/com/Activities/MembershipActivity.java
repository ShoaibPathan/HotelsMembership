package loyaltywallet.com.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import loyaltywallet.com.Applications.Initializer;
import loyaltywallet.com.Fragments.CardFragment;
import loyaltywallet.com.Fragments.OffersFragment;
import loyaltywallet.com.Fragments.RoomReservation;
import loyaltywallet.com.Fragments.TableReservation;
import loyaltywallet.com.Fragments.VenuesFragment;
import loyaltywallet.com.Fragments.VouchersFragment;
import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.Model.Vouchers.Voucher;
import loyaltywallet.com.R;

public class MembershipActivity extends AppCompatActivity implements VouchersFragment.Listener, CardFragment.OnFragmentInteractionListener,
        RoomReservation.OnFragmentInteractionListener, TableReservation.OnFragmentInteractionListener, VenuesFragment.Listener{

    @BindView(R.id.frame)
    FrameLayout frameLayout;
    Membership membership;
    String cardNumber;
    List<Voucher> vouchers;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_card:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, CardFragment.newInstance("",""));
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_vouchers:

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, VouchersFragment.newInstance(((Initializer) getApplication()).getCardContext().getVouchers(),
                            ((Initializer) getApplication()).getCardContext().getCardNumber(),
                            ((Initializer) getApplication()).getCardContext().getMembership()));
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_offers:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, OffersFragment.newInstance(""));
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_venues:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, new VenuesFragment());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        membership = ((Initializer) getApplication()).getCardContext().getMembership();
        cardNumber = membership.getCardNumber();
        vouchers = ((Initializer) getApplication()).getCardContext().getVouchers();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_logo);
//        toolbar.addView(imageView,
//                new Toolbar.LayoutParams(150, 150, Gravity.END));
        Glide.with(this).load(membership.getHotel().getHotelLogoURL()).skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade().into(imageView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().setSubtitle(cardNumber);
        getSupportActionBar().setTitle(membership.getHotel().getHotelName());
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content,  VouchersFragment.newInstance(((Initializer) getApplication()).getCardContext().getVouchers(),
                ((Initializer) getApplication()).getCardContext().getCardNumber(),
                ((Initializer) getApplication()).getCardContext().getMembership()));
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onVoucherClicked(Voucher voucher, String cardNumber, Membership membership) {

    }
    public void callForTableBooking(View view) {
        ArrayList<String> calllist = new ArrayList<>();

        String[] tokens = membership.getHotel().getPhoneNumbers().getTableResevation().replace("|",",").split(",");
        if (tokens.length > 1) {
            Collections.addAll(calllist, tokens);
        }
        chooseNumberToCall(calllist.toArray(tokens), "Call For Table Reservation");
    }
    public void callForRoomBooking(View view) {
        ArrayList<String> calllist = new ArrayList<>();

        String[] tokens = membership.getHotel().getPhoneNumbers().getRoomResevation().replace("|",",").split(",");
        if (tokens.length > 1) {
            Collections.addAll(calllist, tokens);
        }

        chooseNumberToCall(calllist.toArray(tokens), "Call For Room Reservation");
    }
    void chooseNumberToCall(final String[] numbers, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        Toolbar toolbar = new Toolbar(this);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        builder.setCustomTitle(toolbar);
//        builder.setTitle(title);
        builder.setItems(numbers, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if (numbers[item].matches("[0-9]+") && numbers[item].length() < 14) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numbers[item], null)));
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
