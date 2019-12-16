package com.example.yodenproject.PageFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yodenproject.Adapter.OrderAdapter;
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

public class FinishOrderOfClientFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    DatabaseReference orderRef;

    public interface ViewOrderBtnListener{
        void onViewOrder(Order order, String idOrder);
    }

    ActiveOrderOfClientFragment.ViewOrderBtnListener callBack;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            callBack=(ActiveOrderOfClientFragment.ViewOrderBtnListener)context;
        }catch (ClassCastException ex){
            throw new ClassCastException("the activity must implement ViewOrderBtnListener interface");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        orderRef = FirebaseDatabase.getInstance().getReference("orders").child("finish");
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        View root=inflater.inflate(R.layout.finish_order_for_client,container,false);

        final RecyclerView recyclerView=root.findViewById(R.id.recycler_order_finish_client);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<Order> orderList=new ArrayList<>();
        final List<String> idOrderList=new ArrayList<>();


        final OrderAdapter adapter=new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new OrderAdapter.MyOrderListener() {
            @Override
            public void onViewClick(int position) {
                callBack.onViewOrder(orderList.get(position),idOrderList.get(position));

            }
        });




        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Order order=snapshot.getValue(Order.class);
                        if(order.getIdClientUser().equals(firebaseUser.getUid())){

                            String idOrder=snapshot.getKey();

                            orderList.add(order);
                            idOrderList.add(idOrder);
                        }
                    }


                    //recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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