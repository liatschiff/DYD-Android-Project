package com.example.yodenproject.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yodenproject.Behavior.BottomNavigationBehavior;
import com.example.yodenproject.Model.Offer;
import com.example.yodenproject.Model.Order;
import com.example.yodenproject.Model.ProfileProfessional;
import com.example.yodenproject.Model.User;
import com.example.yodenproject.PageFragments.ActiveOrderOfClientFragment;
import com.example.yodenproject.PageFragments.ActiveOrderOfProfessionalFragment;
import com.example.yodenproject.PageFragments.ChatsFragment;
import com.example.yodenproject.PageFragments.FinishOrderOfClientFragment;
import com.example.yodenproject.PageFragments.FinishOrderOfProfessionalFragment;
import com.example.yodenproject.PageFragments.MakeAnOrderFragment;
import com.example.yodenproject.PageFragments.OffersOfProfessionalFragment;
import com.example.yodenproject.PageFragments.TendersPageFragment;
import com.example.yodenproject.PageFragments.UserDetailsFragment;
import com.example.yodenproject.R;
import com.github.abdularis.civ.CircleImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


public class MainPage extends AppCompatActivity implements MakeAnOrderFragment.MakeAnOrderListener,ActiveOrderOfClientFragment.ViewOrderBtnListener,OffersOfProfessionalFragment.OfferOfProListener ,FinishOrderOfProfessionalFragment.ViewOrderBtnListener, ActiveOrderOfProfessionalFragment.ActiveOrderProOrderBtnListener,TendersPageFragment.ShowOrderFragmentListener{
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference orders;
    FirebaseDatabase database;
    DatabaseReference userRef;
    String userId;
    User user;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomMenu;
    Button mainbtn;
    TextView textmain;

    String USER_DETAILS_FRAGMENT ="user detaile";
    String TENDER_PAGE_FRAGMENT = "tender page fragment";
    String ADD_ORDER_FRAGMENT = "add order fragment";
    String CHATS_FRAGMENT = "chats fragment";
    String SHOW_ACTIVE_ORDER_FRAGMENT="show active order";
    String SHOW_ACTIVE_ORDER_PRO_FRAGMENT="show active pro order";
    String SHOW_FINISH_PRO_ORDER_FRAGMENT="show finish pro order";
    String SHOW_FINISH_CLIENT_ORDER_FRAGMENT="show finish client order";
    String SHOW_OFFERS_PRO_FRAGMENT="show offers pro";
    HashMap<String,Order> orderList;


    NavigationView sideMenu;
    View headerView;
    CircleImageView sideProfileImage;


    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((mobile != null && !mobile.isConnected()) && (wifi != null && !wifi.isConnected())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
            View view = getLayoutInflater().inflate(R.layout.dialig_no_wifi, null);
            builder.setView(view);
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            Button button = view.findViewById(R.id.btn_network);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);


        mainbtn=findViewById(R.id.makeorder_or_seebids);
        mainbtn.setVisibility(View.VISIBLE);
        textmain=findViewById(R.id.text);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        sideMenu = findViewById(R.id.side_menu);
        bottomMenu = findViewById(R.id.bottom_menu);
        headerView = sideMenu.getHeaderView(0);
        sideProfileImage = headerView.findViewById(R.id.drawer_profile_image);

        final MakeAnOrderFragment makeAnOrderFragment = new MakeAnOrderFragment();
        final UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
        final TendersPageFragment tendersPageFragment = new TendersPageFragment();
        final ActiveOrderOfClientFragment activeOrderOfClientFragment = new ActiveOrderOfClientFragment();
        final ActiveOrderOfProfessionalFragment activeOrderOfProfessionalFragment = new ActiveOrderOfProfessionalFragment();
        final FinishOrderOfClientFragment finishOrderOfClientFragment=new FinishOrderOfClientFragment();
        final FinishOrderOfProfessionalFragment finishOrderOfProfessionalFragment=new FinishOrderOfProfessionalFragment();

        final ChatsFragment chatsFragment = new ChatsFragment();


        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        Menu menuSide = sideMenu.getMenu();
        final MenuItem sideItem = menuSide.findItem(R.id.offer_side_drawer_btn);
        Menu menuBottom = bottomMenu.getMenu();
        final MenuItem bottomItem = menuBottom.findItem(R.id.bottom_menu_profile);
        userRef.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Glide.with(MainPage.this).load(user.getImageUrl()).into(sideProfileImage);
                TextView sideNameTv = headerView.findViewById(R.id.drawer_user_name);
                TextView sideEmailTv = headerView.findViewById(R.id.drawer_email_tv);

                if (user.isClient()){
                    mainbtn.setText(getResources().getString(R.string.desgin_your_dress));
                    textmain.setText(getResources().getString(R.string.prologue));
                    sideItem.setVisible(false);
                    bottomItem.setVisible(false);
                }
                else {
                    mainbtn.setText(getResources().getString(R.string.watch_open_bids));
                    textmain.setText(getResources().getString(R.string.introdaction));
                    sideItem.setVisible(true);
                    bottomItem.setVisible(true);
                }
                sideEmailTv.setText(firebaseAuth.getCurrentUser().getEmail());
                sideNameTv.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainbtn.setVisibility(View.GONE);
                if(user.isClient())
                    getSupportFragmentManager().beginTransaction().add(R.id.content_layout,new MakeAnOrderFragment(),ADD_ORDER_FRAGMENT).addToBackStack(null).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,tendersPageFragment,TENDER_PAGE_FRAGMENT).addToBackStack(null).commit();
            }
        });
        sideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();


                if (id == R.id.messages_side_drawer_btn) {
                    mainbtn.setVisibility(View.GONE);
                    textmain.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,chatsFragment,CHATS_FRAGMENT).addToBackStack(null).commit();
                }
                else if (id == R.id.myaccount_side_drawer_btn) {
                    mainbtn.setVisibility(View.GONE);
                    textmain.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,userDetailsFragment,USER_DETAILS_FRAGMENT).addToBackStack(null).commit();
                }
                else if(id==R.id.activeorder_side_drawer_btn){
                    mainbtn.setVisibility(View.GONE);
                    textmain.setVisibility(View.GONE);
                    if(user.isClient())
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,activeOrderOfClientFragment,SHOW_ACTIVE_ORDER_FRAGMENT).addToBackStack(null).commit();
                    else
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,activeOrderOfProfessionalFragment,SHOW_ACTIVE_ORDER_PRO_FRAGMENT).addToBackStack(null).commit();

                }
                else if(id==R.id.finishorder_side_drawer_btn){
                    mainbtn.setVisibility(View.GONE);
                    textmain.setVisibility(View.GONE);
                    if(user.isClient())
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,finishOrderOfClientFragment,SHOW_FINISH_CLIENT_ORDER_FRAGMENT).addToBackStack(null).commit();

                    else
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,finishOrderOfProfessionalFragment,SHOW_FINISH_PRO_ORDER_FRAGMENT).addToBackStack(null).commit();
                }
                else if(id == R.id.sign_out){
                    mainbtn.setVisibility(View.GONE);
                    textmain.setVisibility(View.GONE);
                    firebaseAuth.signOut();
                    Intent intent = new Intent(MainPage.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if(id==R.id.offer_side_drawer_btn){
                    if(!user.isClient()) {
                        mainbtn.setVisibility(View.GONE);
                        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child("active");
                        orderList = new HashMap<>();
                        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                orderList.clear();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Order order = snapshot.getValue(Order.class);
                                        if (order.getIdProfessionalUser().equals("")) {

                                            String idOrder = snapshot.getKey();
                                            orderList.put(idOrder, order);
                                        }
                                    }

                                    Fragment offerOfProfessional = new OffersOfProfessionalFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("order list", orderList);
                                    offerOfProfessional.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, offerOfProfessional, SHOW_OFFERS_PRO_FRAGMENT).addToBackStack(null).commit();
                                } else
                                    Toast.makeText(MainPage.this, "don't exists waiting offer", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.bottom_menu_profile:
                        FirebaseDatabase.getInstance().getReference("profile").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                ProfileProfessional professional = dataSnapshot.getValue(ProfileProfessional.class);

                                Intent intent = new Intent(MainPage.this,ProfessionalProfileActivity.class);
                                intent.putExtra("professional",professional);
                                intent.putExtra("idProfessional",firebaseAuth.getCurrentUser().getUid());
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        return true;
                    case R.id.bottom_menu_homepage:
                        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
                            clearStack();
                            mainbtn.setVisibility(View.VISIBLE);
                        }
                        return true;
                    case  R.id.open_side_menu:
                        drawerLayout.openDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomMenu.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomMenu.setSelectedItemId(R.id.bottom_menu_homepage);

        //if i client that get to this activity from offerfortenter and delete my tenderorder
        boolean wantDeleteIsOrder=getIntent().getBooleanExtra("user want to delete his order",false);
        if(wantDeleteIsOrder) {
            String idOrder=getIntent().getStringExtra("id delete order");
            DeleteOrder(idOrder);
        }

    }

    public void DeleteOrder(final String idOrder) {

        //delete all the offers for this order
        final DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("offers");
        offerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Offer offer=snapshot.getValue(Offer.class);
                        if(offer.getIdOrder().equals(idOrder))
                            offerRef.child(snapshot.getKey()).removeValue();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //delete order
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child("active");
        orderRef.child(idOrder).removeValue();

    }


    //return functions from fragments!!


    @Override
    public void onMakeOrder(Order order) {
        orders = FirebaseDatabase.getInstance().getReference("orders").child("active");
        Fragment fragment=getSupportFragmentManager().findFragmentByTag(ADD_ORDER_FRAGMENT);
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        mainbtn.setVisibility(View.VISIBLE);
        orders.push().setValue(order);

    }

    @Override
    public void onShowOrder(Order orderShow, String idOrder) {

        Intent intent=new Intent(MainPage.this, OffersForTenderActivity.class);
        intent.putExtra("order",orderShow);
        intent.putExtra("id order",idOrder);
        intent.putExtra("user",user);
        startActivity(intent);

    }

    @Override
    public void onFinishOrder(Order orderFinish, String idOrder) {
        final DatabaseReference orderFinishRef = FirebaseDatabase.getInstance().getReference("orders").child("finish");
        final DatabaseReference orderActiveRef = FirebaseDatabase.getInstance().getReference("orders").child("active");

        orderActiveRef.child(idOrder).removeValue();
        orderFinishRef.child(idOrder).setValue(orderFinish);

        Fragment fragment=getSupportFragmentManager().findFragmentByTag(SHOW_ACTIVE_ORDER_PRO_FRAGMENT);
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onViewOrder(Order order, String idOrder) {

        Intent intent=new Intent(MainPage.this, OffersForTenderActivity.class);
        intent.putExtra("order",order);
        intent.putExtra("id order",idOrder);
        intent.putExtra("user",user);
        startActivity(intent);
    }


    public User getUser(){
        return user;
    }

    private void status(String status){

        DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        statusRef.updateChildren(hashMap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackEntry; i++) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            clearStack();
            mainbtn.setVisibility(View.VISIBLE);
        } else  moveTaskToBack(true);

    }
}

