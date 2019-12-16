package com.example.yodenproject.PageFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yodenproject.Adapter.TenderAdapter;
import com.example.yodenproject.Adapter.TenderRecycler;
import com.example.yodenproject.Model.Order;
import com.example.yodenproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TendersPageFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;

    public interface ShowOrderFragmentListener{
        void onShowOrder(Order orderShow,String idOrder);
    }

    ShowOrderFragmentListener callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            callBack=(ShowOrderFragmentListener)context;
        }catch (ClassCastException ex){
            throw new ClassCastException("the activity must implement OnRegisterFragmentListener interface");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("orders").child("active");
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        View root=inflater.inflate(R.layout.tenders_list_fragment,container,false);

        final RecyclerView recyclerView=root.findViewById(R.id.recycler_tenders);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final List<TenderRecycler> tenderList=new ArrayList<>();
        final List<Order> orderList=new ArrayList<>();


        final TenderAdapter adapter=new TenderAdapter(tenderList);

        adapter.setListener(new TenderAdapter.MyTenderListener() {
            @Override
            public void onTenderClick(int position) {

                callBack.onShowOrder(orderList.get(position),tenderList.get(position).getIdOrder());

            }
        });


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Order order=snapshot.getValue(Order.class);
                        if(order.getIdProfessionalUser().equals("")){

                            String idOrder=snapshot.getKey();
                            String nameOrder=order.getOrderName();
                            String finalDate=order.getFinalDate();
                            String imageUrl = order.getPhoto();

                            orderList.add(order);
                            tenderList.add(new TenderRecycler(idOrder,nameOrder,finalDate,imageUrl));
                        }
                    }

                    recyclerView.setAdapter(adapter);
                }
                else Toast.makeText(getContext(), getResources().getString(R.string.no_active_orders), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }
}