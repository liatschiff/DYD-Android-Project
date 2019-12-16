package com.example.yodenproject.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.yodenproject.Adapter.OfferAdapter;
import com.example.yodenproject.Adapter.OfferRecycler;
import com.example.yodenproject.Model.Offer;
import com.example.yodenproject.Model.Order;
import com.example.yodenproject.Model.ProfileProfessional;
import com.example.yodenproject.Model.User;
import com.example.yodenproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OffersForTenderActivity extends AppCompatActivity {

    Order order;
    Offer offer;
    User user;
    String idOrder;
    DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("offers");
    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child("active");
    DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profile");
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    String PUBLIC_PROFILE_FRAGMENT_TAG="public profile";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_for_tender);


        user =(User)getIntent().getSerializableExtra("user");
        order=(Order)getIntent().getSerializableExtra("order");
        idOrder=getIntent().getStringExtra("id order");

        ImageButton messageImageBtn = findViewById(R.id.message_client_btn);

        final ImageView imageView = findViewById(R.id.photo_image);
        if(order.getPhoto().equals("default"))
            imageView.setImageResource(R.drawable.ic_dress);
        else Glide.with(this).load(order.getPhoto()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePopup imagePopup = new ImagePopup(OffersForTenderActivity.this);
                imagePopup.setWindowHeight(800); // Optional
                imagePopup.setWindowWidth(800); // Optional
                imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                imagePopup.setFullScreen(true); // Optional
                imagePopup.setHideCloseIcon(true);  // Optional
                imagePopup.setImageOnClickClose(true);
                imagePopup.initiatePopupWithGlide(order.getPhoto());
                imagePopup.viewPopup();
            }
        });

        if(firebaseAuth.getCurrentUser().getUid().equals(order.getIdClientUser())){
            messageImageBtn.setVisibility(View.GONE);
        } else {
            messageImageBtn.setVisibility(View.VISIBLE);
        }

        messageImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OffersForTenderActivity.this,MessageActivity.class);
                intent.putExtra("userid",order.getIdClientUser());
                startActivity(intent);
            }
        });

        final RecyclerView recyclerView=findViewById(R.id.recycler_offers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<OfferRecycler> offersRList=new ArrayList<>();


        final OfferAdapter adapter=new OfferAdapter(offersRList);

        adapter.setListener(new OfferAdapter.MyOfferListener() {
            @Override
            public void onOfferClick(int position) {

                final int pos = position;
                profileRef.child(offersRList.get(position).getIdProfessional()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ProfileProfessional professional = dataSnapshot.getValue(ProfileProfessional.class);

                        Intent intent = new Intent(OffersForTenderActivity.this,ProfessionalProfileActivity.class);
                        intent.putExtra("professional",professional);
                        intent.putExtra("idProfessional",offersRList.get(pos).getIdProfessional());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onOfferLongClick(int position) {

                if((user.getUserId().equals(order.getIdClientUser()))&& (order.getIdProfessionalUser().equals(""))){

                    final String idProfessionalChosen=offersRList.get(position).getIdProfessional();
                    String nameProfessionalChosen=offersRList.get(position).getNameProfessional();

                    AlertDialog.Builder builder = new AlertDialog.Builder(OffersForTenderActivity.this);
                    builder.setTitle(getResources().getString(R.string.choosing)+nameProfessionalChosen+" "+getResources().getString(R.string.to_make)).setMessage(nameProfessionalChosen+" "+getResources().getString(R.string.will_start)+" "+getResources().getString(R.string.not_able))
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    orderRef.child(idOrder).child("idProfessionalUser").setValue(idProfessionalChosen);
                                    Intent chatMessageIntent=new Intent(OffersForTenderActivity.this,MessageActivity.class);
                                    chatMessageIntent.putExtra("isMymsg",true);
                                    chatMessageIntent.putExtra("Myidclient",offer.getIdProfessional());
                                    startActivity(chatMessageIntent);

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(false).show();

                }

            }
        });

        //read all the offer and put in recycler
        offerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        offer = snapshot.getValue(Offer.class);
                        if(offer.getIdOrder().equals(idOrder)) {

                            final String idProfessional = offer.getIdProfessional();
                            String idOrder = offer.getIdOrder();
                            int relevantPrice = offer.getRelevantPrice();
                            String moreInformation = offer.getMoreInformation();
                            final String photo = offer.getImageUrl();
                            final String namePro = offer.getFullNamePro();

                            offersRList.add(new OfferRecycler(idOrder, idProfessional, namePro, relevantPrice, moreInformation, photo));
                        }
                    }
                    recyclerView.setAdapter(adapter);
                }
                else Toast.makeText(OffersForTenderActivity.this, getResources().getString(R.string.no_offers), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TextView nameOrderTv=findViewById(R.id.order_name_tv);
        TextView glovesTv=findViewById(R.id.order_category_tv);
        TextView sleevesTv=findViewById(R.id.order_purpose_tv);
        TextView preferFabricsTv=findViewById(R.id.order_fabrics_tv);
        TextView cleavageShapeTv=findViewById(R.id.order_cleavage_shape_tv);
        TextView backShapeTv=findViewById(R.id.order_back_shape_tv);
        TextView lengthDressTv=findViewById(R.id.order_length_dress_tv);
        TextView clientHighTv=findViewById(R.id.order_client_high_tv);
        TextView hipsTv=findViewById(R.id.hips_tv);
        TextView bustTv=findViewById(R.id.bust_tv);
        TextView waistTv=findViewById(R.id.waist_tv);
        TextView distHipsTv=findViewById(R.id.dist_hips_tv);
        TextView distBustTv=findViewById(R.id.dist_bust_tv);
        TextView distWaistTv=findViewById(R.id.dist_waist_tv);
        TextView anotherDescriptionTv=findViewById(R.id.another_description_tv);
        TextView maxPriceTv=findViewById(R.id.maxprise_tv);
        TextView finalDateTv=findViewById(R.id.final_date_tv);

        nameOrderTv.setText(order.getOrderName());
        glovesTv.setText(glovesTv.getText()+order.getGolves());
        sleevesTv.setText(sleevesTv.getText()+order.getSleeves());

        if(order.getFabrics().size()>0) {
            String fabrics = order.getFabrics().get(0);
            for (int i = 1; i < order.getFabrics().size(); i++)
                fabrics = fabrics + "," + order.getFabrics().get(i).toString();

        preferFabricsTv.setText(preferFabricsTv.getText()+fabrics);
        } else preferFabricsTv.setText(preferFabricsTv.getText()+getResources().getString(R.string.no_preferences));

        cleavageShapeTv.setText(cleavageShapeTv.getText()+order.getCleavageShape());
        backShapeTv.setText(backShapeTv.getText()+order.getBackShape());
        lengthDressTv.setText(lengthDressTv.getText()+order.getLengthDress());
        clientHighTv.setText(order.getYourHeight()+"");
        hipsTv.setText(order.getHips()+"");
        waistTv.setText(order.getWaist()+"");
        bustTv.setText(order.getBust()+"");
        distHipsTv.setText(order.getDistHips()+"");
        distWaistTv.setText(order.getDistWaist()+"");
        distBustTv.setText(order.getDistBust()+"");
        anotherDescriptionTv.setText(anotherDescriptionTv.getText()+order.getAnotherDescription());
        maxPriceTv.setText(maxPriceTv.getText()+""+order.getMaxPrice() );
        finalDateTv.setText(finalDateTv.getText()+order.getFinalDate());

        Button cancelOrderBtn = findViewById(R.id.cancel_order_btn);
        cancelOrderBtn.setVisibility(View.INVISIBLE);
        FloatingActionButton fab=findViewById(R.id.fab);
        fab.hide();
        cancelOrderBtn.setVisibility(View.INVISIBLE);


        if(user.isClient()){
            fab.hide();

            if(user.getUserId().equals(order.getIdClientUser())) {

                if (order.getIdProfessionalUser() == "") {

                    cancelOrderBtn.setVisibility(View.VISIBLE);
                    cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(OffersForTenderActivity.this);
                            builder.setTitle(getResources().getString(R.string.cancel_order)).setMessage(getResources().getString(R.string.are_sure_cancel))
                                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent intent = new Intent(OffersForTenderActivity.this, MainPage.class);
                                            intent.putExtra("user want to delete his order", true);
                                            intent.putExtra("id delete order", idOrder);
                                            startActivity(intent);

                                        }
                                    }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setCancelable(false).show();

                        }

                    });

                }

            }


        }
        else if(!user.isClient()&& order.getIdProfessionalUser().equals("")) {
            //add new offer

            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_offer, null);
                    final EditText relevantPriceEt = dialogView.findViewById(R.id.price_et_dialog);
                    final EditText moreInformationEt = dialogView.findViewById(R.id.information_et_diag);

                    AlertDialog.Builder builder = new AlertDialog.Builder(OffersForTenderActivity.this);
                    builder.setView(dialogView).setPositiveButton(getResources().getString(R.string.add_offer), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String userId = firebaseAuth.getCurrentUser().getUid();
                            String photo = user.getImageUrl();
                            String fullName = user.getFullname();
                            String moreInformation = moreInformationEt.getText().toString();
                            int relevantPrice;
                            try {
                                relevantPrice = Integer.parseInt(relevantPriceEt.getText().toString());
                                offerRef.push().setValue(new Offer(userId, idOrder, relevantPrice, moreInformation, photo, fullName));
                                offersRList.add(new OfferRecycler(idOrder, userId, fullName, relevantPrice, moreInformation, photo));
                                adapter.notifyDataSetChanged();
                            } catch (NumberFormatException nfe) {
                                System.out.println("Could not parse " + nfe);
                            }

                        }
                    }).setCancelable(false).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

                }
            });
        }

    }
}
