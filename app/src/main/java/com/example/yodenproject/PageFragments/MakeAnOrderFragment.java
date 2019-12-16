package com.example.yodenproject.PageFragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yodenproject.Model.Order;
import com.example.yodenproject.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class MakeAnOrderFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference orders = FirebaseDatabase.getInstance().getReference("orders").child("active");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("sketches");

    public interface MakeAnOrderListener{
        void onMakeOrder(Order order);
    }

    MakeAnOrderFragment.MakeAnOrderListener callBack;

    List<String> fabrics = new ArrayList<>();
    private int mYear, mMonth, mDay;

    final int PHOTO_TAG = 2;
    final int WRITE_PERMISSION_REQUEST = 0;

    Uri imageUri;
    String imageUrl = "default";

    EditText orderNameEt;
    Spinner sleevesSpinner;
    Spinner glovesSpinner;
    Button preferFabricsBtn;
    Spinner cleavageShapeSpinner;
    Spinner backShapeSpinner;
    Spinner highDressSpinner;
    EditText hipsEt;
    EditText bustEt;
    EditText waistEt;
    EditText distHipsEt;
    EditText distBustEt;
    EditText distWaistEt;
    EditText maxPriceEt;
    EditText anotherDescriptionEt;
    EditText date;
    EditText yourhighEt;
    ImageButton imageButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            callBack=(MakeAnOrderFragment.MakeAnOrderListener)context;
        }catch (ClassCastException ex){
            throw new ClassCastException("the activity must implement MakeAnOrderListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final String idClient=firebaseAuth.getCurrentUser().getUid();

        View root=inflater.inflate(R.layout.client_order,container,false);
        orderNameEt=root.findViewById(R.id.order_name_et);
        glovesSpinner=root.findViewById(R.id.gloves_shape_spinner);
        sleevesSpinner=root.findViewById(R.id.sleeves_shape_spinner);
        preferFabricsBtn=root.findViewById(R.id.prefer_fabrics_btn);
        cleavageShapeSpinner=root.findViewById(R.id.cleavage_shape_spinner);
        backShapeSpinner=root.findViewById(R.id.back_shape_spinner);
        highDressSpinner=root.findViewById(R.id.longDress_shape_spinner);
        hipsEt=root.findViewById(R.id.hips_et);
        bustEt=root.findViewById(R.id.bust_et);
        waistEt=root.findViewById(R.id.waist_et);
        distHipsEt=root.findViewById(R.id.distance_hips_et);
        distBustEt=root.findViewById(R.id.distance_bust_et);
        distWaistEt=root.findViewById(R.id.distance_waist_et);
        maxPriceEt=root.findViewById(R.id.maxprise_et);
        anotherDescriptionEt=root.findViewById(R.id.another_description_et);
        date=root.findViewById(R.id.date_et);
        yourhighEt=root.findViewById(R.id.your_height_et);
        Button finishBtn=root.findViewById(R.id.finish_btn);
        Button calBtn=root.findViewById(R.id.calanderbtn);
        imageButton = root.findViewById(R.id.photo_imagebtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT>=23){
                    int hasWrittenPermission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(hasWrittenPermission != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
                    else openFileChooser();
                }
                else openFileChooser();
            }
        });

        preferFabricsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  builder=new AlertDialog.Builder(getContext());
                builder.setTitle(getResources().getString(R.string.pick_to_call)).setMultiChoiceItems(R.array.fabrics_array, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        String[] daysArr=getResources().getStringArray(R.array.fabrics_array);
                        if(isChecked)
                            fabrics.add(daysArr[which]);
                        else
                            fabrics.remove(daysArr[which]);

                    }
                }).setPositiveButton(getResources().getString(R.string.finished), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), fabrics.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).show();

            }
        });


        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                mDay=dayOfMonth;
                                mMonth=monthOfYear;
                                mYear=year;
                            }
                        }, mYear, mMonth, mDay);
                Calendar mcurrentDate = Calendar.getInstance();
                mcurrentDate.add(Calendar.DATE, 1);
                datePickerDialog.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis());
                datePickerDialog.show();


            }
        });


        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imageUri == null){
                    String idProfessionalUser="";
                    String orderName=orderNameEt.getText().toString();
                    String gloves=glovesSpinner.getSelectedItem().toString();
                    String sleeves=sleevesSpinner.getSelectedItem().toString();
                    String cleavageShape=cleavageShapeSpinner.getSelectedItem().toString();
                    String backShape=backShapeSpinner.getSelectedItem().toString();
                    String highDress=highDressSpinner.getSelectedItem().toString();
                    String stringhips=hipsEt.getText().toString();
                    String stringbust=bustEt.getText().toString();
                    String stringwaist=waistEt.getText().toString();
                    String stringdistHips=distHipsEt.getText().toString();
                    String stringdistBust=distBustEt.getText().toString();
                    String stringdistWaist=distWaistEt.getText().toString();
                    String stringmaxPrice=maxPriceEt.getText().toString();
                    String stringyourHigh=yourhighEt.getText().toString();
                    String anotherDescription=anotherDescriptionEt.getText().toString();
                    String finalDate=date.getText().toString();




                    if(orderName.isEmpty() || gloves.isEmpty() || sleeves.isEmpty() || cleavageShape.isEmpty() || backShape.isEmpty()
                            || highDress.isEmpty() || stringbust.isEmpty() || stringhips.isEmpty() ||stringwaist.isEmpty()||stringdistBust.isEmpty()||
                            stringdistHips.isEmpty()||stringdistWaist.isEmpty()||stringmaxPrice.isEmpty()||stringyourHigh.isEmpty()||anotherDescription.isEmpty()||finalDate.isEmpty()){

                        Toast.makeText(getActivity(), getResources().getString(R.string.failed_details), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int hips=Integer.parseInt(hipsEt.getText().toString());
                        int bust=Integer.parseInt(bustEt.getText().toString());
                        int waist=Integer.parseInt(waistEt.getText().toString());
                        int distHips=Integer.parseInt(distHipsEt.getText().toString());
                        int distBust=Integer.parseInt(distBustEt.getText().toString());
                        int distWaist=Integer.parseInt(distWaistEt.getText().toString());
                        int maxPrice=Integer.parseInt(maxPriceEt.getText().toString());
                        int yourHigh=Integer.parseInt(yourhighEt.getText().toString());

                        final Order newOrder=new Order(idClient, idProfessionalUser,  orderName, imageUrl,
                                gloves, sleeves, fabrics, cleavageShape, backShape, highDress, hips,
                                bust, waist, distHips, distBust, distWaist,yourHigh,maxPrice, anotherDescription, finalDate);

                        callBack.onMakeOrder(newOrder);

                    }


                }
                else if (imageUri != null) {
                    final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(imageUri));

                    fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                    {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                imageUrl = downloadUri.toString();

                                String idProfessionalUser="";
                                String orderName=orderNameEt.getText().toString();
                                String gloves=glovesSpinner.getSelectedItem().toString();
                                String sleeves=sleevesSpinner.getSelectedItem().toString();
                                String cleavageShape=cleavageShapeSpinner.getSelectedItem().toString();
                                String backShape=backShapeSpinner.getSelectedItem().toString();
                                String highDress=highDressSpinner.getSelectedItem().toString();
                                String stringhips=hipsEt.getText().toString();
                                String stringbust=bustEt.getText().toString();
                                String stringwaist=waistEt.getText().toString();
                                String stringdistHips=distHipsEt.getText().toString();
                                String stringdistBust=distBustEt.getText().toString();
                                String stringdistWaist=distWaistEt.getText().toString();
                                String stringmaxPrice=maxPriceEt.getText().toString();
                                String stringyourHigh=yourhighEt.getText().toString();
                                String anotherDescription=anotherDescriptionEt.getText().toString();
                                String finalDate=date.getText().toString();




                                if(orderName.isEmpty() || gloves.isEmpty() || sleeves.isEmpty() || cleavageShape.isEmpty() || backShape.isEmpty()
                                        || highDress.isEmpty() || stringbust.isEmpty() || stringhips.isEmpty() ||stringwaist.isEmpty()||stringdistBust.isEmpty()||
                                        stringdistHips.isEmpty()||stringdistWaist.isEmpty()||stringmaxPrice.isEmpty()||stringyourHigh.isEmpty()||anotherDescription.isEmpty()||finalDate.isEmpty()){

                                    Toast.makeText(getActivity(), getResources().getString(R.string.failed_details), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    int hips=Integer.parseInt(hipsEt.getText().toString());
                                    int bust=Integer.parseInt(bustEt.getText().toString());
                                    int waist=Integer.parseInt(waistEt.getText().toString());
                                    int distHips=Integer.parseInt(distHipsEt.getText().toString());
                                    int distBust=Integer.parseInt(distBustEt.getText().toString());
                                    int distWaist=Integer.parseInt(distWaistEt.getText().toString());
                                    int maxPrice=Integer.parseInt(maxPriceEt.getText().toString());
                                    int yourHigh=Integer.parseInt(yourhighEt.getText().toString());

                                    final Order newOrder=new Order(idClient, idProfessionalUser,  orderName, imageUrl,
                                            gloves, sleeves, fabrics, cleavageShape, backShape, highDress, hips,
                                            bust, waist, distHips, distBust, distWaist,yourHigh,maxPrice, anotherDescription, finalDate);

                                    callBack.onMakeOrder(newOrder);

                                }

                            }
                        }
                    });
                }

            }
        });



        return root;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PHOTO_TAG);
    }

    private String getFileExtension(Uri uri){

        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == WRITE_PERMISSION_REQUEST){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), getResources().getString(R.string.permission_notice), Toast.LENGTH_SHORT).show();
            }
            else openFileChooser();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PHOTO_TAG && resultCode == RESULT_OK
                && data != null &&data.getData() != null){
            imageUri = data.getData();

            Glide.with(this).load(imageUri).into(imageButton);
        }
    }
}