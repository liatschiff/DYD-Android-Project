package com.example.yodenproject.PageFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yodenproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ListOrdersActiveFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    DatabaseReference orderRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        orderRef = FirebaseDatabase.getInstance().getReference("orders").child("active");
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        View root=inflater.inflate(R.layout.list_orders_user_fragment,container,false);



        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
