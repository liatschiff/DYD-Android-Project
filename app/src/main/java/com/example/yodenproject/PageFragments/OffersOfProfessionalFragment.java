package com.example.yodenproject.PageFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yodenproject.Adapter.OfferForProAdapter;
import com.example.yodenproject.Adapter.OfferRecycler;
import com.example.yodenproject.Model.Offer;
import com.example.yodenproject.Model.Order;
import com.example.yodenproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OffersOfProfessionalFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    DatabaseReference orderRef;
    DatabaseReference offerRef;
    String userid;
    HashMap<String, Order> orderList;
    List<String> idOfferList;
    List<OfferRecycler> offerRecyclerList;
    OfferForProAdapter adapter;


    public interface OfferOfProListener{
        void onViewOrder(Order orderView, String idOrder);
    }

    OffersOfProfessionalFragment.OfferOfProListener callBack;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            callBack=(OffersOfProfessionalFragment.OfferOfProListener )context;
        }catch (ClassCastException ex){
            throw new ClassCastException("the activity must implement OfferOfProListener interface");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        orderRef = FirebaseDatabase.getInstance().getReference("orders").child("active");
        offerRef = FirebaseDatabase.getInstance().getReference("offers");
        userid=firebaseAuth.getCurrentUser().getUid();


        View root=inflater.inflate(R.layout.all_offers_of_professional,container,false);

        final RecyclerView recyclerView=root.findViewById(R.id.recycler_offers_of_professional);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList= (HashMap<String,Order>) getArguments().getSerializable("order list");
        idOfferList=new ArrayList<>();
        offerRecyclerList=new ArrayList<>();


        adapter=new OfferForProAdapter(offerRecyclerList);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new OfferForProAdapter.MyOfferListener() {
            @Override
            public void onViewClick(int position) {
                String idOrder=offerRecyclerList.get(position).getIdOrder();
                callBack.onViewOrder(orderList.get(idOrder),idOrder);
            }

            @Override
            public void onCancelClick(int position) {
                offerRef.child(idOfferList.get(position)).removeValue();
                adapter.notifyItemChanged(position);
                idOfferList.remove(position);
                offerRecyclerList.remove(position);
            }
        });




        offerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                offerRecyclerList.clear();
                idOfferList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        Offer offer=snapshot.getValue(Offer.class);

                        Order order=orderList.get(offer.getIdOrder());
                        if(order!=null && order.getIdProfessionalUser().equals(""))
                        {
                            String idOffer=snapshot.getKey();
                            idOfferList.add(idOffer);
                            offerRecyclerList.add(new OfferRecycler(offer.getIdOrder(),userid,offer.getFullNamePro(),offer.getRelevantPrice(),offer.getMoreInformation(),offer.getImageUrl()));
                        }
                        else
                            orderList.remove(offer.getIdOrder());

                    }
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }
}