package com.example.yodenproject.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.yodenproject.Adapter.OfferRecycler;
import com.example.yodenproject.Adapter.PreviousWorksAdapter;
import com.example.yodenproject.Adapter.ReviewAdapter;
import com.example.yodenproject.Model.Offer;
import com.example.yodenproject.Model.ProfileProfessional;
import com.example.yodenproject.Model.Review;
import com.example.yodenproject.Model.User;
import com.example.yodenproject.R;
import com.example.yodenproject.Registration.UserRegisterActivity;
import com.github.abdularis.civ.CircleImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfessionalProfileActivity  extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");
    DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profile");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("prevwork");

    ArrayList<String> imagesList=new ArrayList<>();
    List<Review> reviewsList=new ArrayList<>();

    RecyclerView recyclerImages;
    RecyclerView recyclerReviews;
    ReviewAdapter reviewsAdapter;
    PreviousWorksAdapter imagesAdapter;
    String professionalId;
    ProfileProfessional professional;
    User userProfile;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    boolean isProOfThisProfile;
    User userNow;

    final int WRITE_PERMISSION_REQUEST = 1;
    final int IMAGE_REQUEST = 2;

    Uri imageUri;
    List<Uri> imageUriList;
    StorageTask uploadTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_profile_professional);

        professionalId = getIntent().getExtras().getString("idProfessional");
        professional = (ProfileProfessional) getIntent().getExtras().getSerializable("professional");
        final String userNowId=firebaseAuth.getCurrentUser().getUid();
        if(professionalId.equals(userNowId))
            isProOfThisProfile=true;

        //information for user main
        final TextView locationTv=findViewById(R.id.public_professional_location_tv);
        final TextView aboutMe=findViewById(R.id.public_professional_aboutme_tv);
        final TextView nameTv=findViewById(R.id.public_professional_name_tv);
        final CircleImageView photoPro=findViewById(R.id.professional_public_profile_pic);
        final Button addInfo=findViewById(R.id.add_info);
        final Button addPhoto=findViewById(R.id.add_photo);
        final Button addReview=findViewById(R.id.add_review);
        final FloatingActionButton messageImageBtn = findViewById(R.id.fab);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile=dataSnapshot.child(professionalId).getValue(User.class);
                if (!isProOfThisProfile)
                    userNow=dataSnapshot.child(userNowId).getValue(User.class);

                locationTv.setText(userProfile.getCity()+" , "+userProfile.getCountry());
                nameTv.setText(userProfile.getFullname());
                if(userProfile.getImageUrl().equals("default"))
                    photoPro.setImageResource(R.drawable.ic_user);
                else Glide.with(photoPro.getContext()).load(userProfile.getImageUrl()).into(photoPro);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //create adapter for prev work
        recyclerImages=findViewById(R.id.previous_work_list);
        recyclerImages.setHasFixedSize(true);
        recyclerImages.setLayoutManager(new LinearLayoutManager(ProfessionalProfileActivity.this,LinearLayoutManager.HORIZONTAL,true));
        //imagesAdapter=new PreviousWorksAdapter(imagesList);

        //create adapter for reviews
        recyclerReviews=findViewById(R.id.review_list);
        recyclerReviews.setHasFixedSize(true);
        recyclerReviews.setLayoutManager(new LinearLayoutManager(ProfessionalProfileActivity.this));;
        //reviewsAdapter=new ReviewAdapter(reviewsList);

        imagesList = professional.getImages();
        imagesAdapter = new PreviousWorksAdapter(imagesList);
        recyclerImages.setAdapter(imagesAdapter);

        reviewsList = professional.getReviews();
        reviewsAdapter=new ReviewAdapter(reviewsList);
        recyclerReviews.setAdapter(reviewsAdapter);

        aboutMe.setText(professional.getInformation());

        imagesAdapter.setListener(new PreviousWorksAdapter.MyWorksImagesListener() {
            @Override
            public void onWorkClick(int position) {

                ImagePopup imagePopup = new ImagePopup(ProfessionalProfileActivity.this);
                imagePopup.setWindowHeight(800); // Optional
                imagePopup.setWindowWidth(800); // Optional
                imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                imagePopup.setFullScreen(true); // Optional
                imagePopup.setHideCloseIcon(true);  // Optional
                imagePopup.setImageOnClickClose(true);
                imagePopup.initiatePopupWithGlide(imagesList.get(position));
                imagePopup.viewPopup();
            }
        });

        reviewsAdapter.setListener(new ReviewAdapter.MyReviewListener() {
            @Override
            public void onReviewClick(int position) {

            }
        });

        if(isProOfThisProfile){

            addInfo.setVisibility(View.VISIBLE);
            addPhoto.setVisibility(View.VISIBLE);
            addReview.setVisibility(View.INVISIBLE);
            messageImageBtn.hide();


            addInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final View dialogView= getLayoutInflater().inflate(R.layout.dialog_add_info_pro_profile,null);
                    final EditText proInformationEt=dialogView.findViewById(R.id.info_pro_et_dialog);

                    AlertDialog.Builder builder=new AlertDialog.Builder(ProfessionalProfileActivity.this);
                    builder.setView(dialogView).setPositiveButton(getResources().getString(R.string.add_info), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String proInformation= proInformationEt.getText().toString();
                            profileRef.child(professionalId).child("information").setValue(proInformation);
                            aboutMe.setText(proInformation);

                        }
                    }).setCancelable(false).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
            });

            addPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Build.VERSION.SDK_INT>=23){
                        int hasWrittenPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if(hasWrittenPermission != PackageManager.PERMISSION_GRANTED)
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
                        else{
                            openFileChooser();
                        }
                    }
                    else{
                        openFileChooser();
                    }
                }
            });
        }
        else{

            addReview.setVisibility(View.VISIBLE);
            addPhoto.setVisibility(View.INVISIBLE);
            addInfo.setVisibility(View.INVISIBLE);
            messageImageBtn.show();

            messageImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfessionalProfileActivity.this,MessageActivity.class);
                    intent.putExtra("userid",professionalId);
                    startActivity(intent);
                }
            });

            addReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View dialogView= getLayoutInflater().inflate(R.layout.dialog_add_review,null);
                    final EditText reviewEt=dialogView.findViewById(R.id.review_et_dialog);

                    AlertDialog.Builder builder=new AlertDialog.Builder(ProfessionalProfileActivity.this);
                    builder.setView(dialogView).setPositiveButton(getResources().getString(R.string.add_review), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String newReview=reviewEt.getText().toString();
                            Review review=new Review(newReview,userNow.getFullname(),userNow.getImageUrl());
                            reviewsList.add(review);
                            profileRef.child(professionalId).child("reviews").setValue(reviewsList);
                            reviewsAdapter.notifyDataSetChanged();
                        }

                    }).setCancelable(false).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

                }
            });
        }

    }

    private void uploadFile(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final  StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        imagesList.add(mUri);
                        profileRef.child(professionalId).child("images").setValue(imagesList);
                        imagesAdapter.notifyDataSetChanged();
                        pd.dismiss();
                    } else {
                        Toast.makeText(ProfessionalProfileActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfessionalProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
        else if(imageUriList != null){

            for(Uri uri:imageUriList){
                final  StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                        +"."+getFileExtension(uri));

                uploadTask = fileReference.putFile(uri);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw  task.getException();
                        }

                        return  fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            String mUri = downloadUri.toString();
                            imagesList.add(mUri);
                            profileRef.child(professionalId).child("images").setValue(imagesList);
                            imagesAdapter.notifyDataSetChanged();
                            pd.dismiss();
                        } else {
                            Toast.makeText(ProfessionalProfileActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfessionalProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
            }
        }
        else {
            Toast.makeText(ProfessionalProfileActivity.this, getResources().getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri){

        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == WRITE_PERMISSION_REQUEST){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, getResources().getString(R.string.permission_notice), Toast.LENGTH_SHORT).show();
            }
            else {
                openFileChooser();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null){
            if(data.getData()!=null){
                imageUri = data.getData();
            }
            else if(data.getClipData()!=null){
                imageUriList = new ArrayList<>();
                ClipData clipData = data.getClipData();
                ClipData.Item item;
                for(int i=0;i<clipData.getItemCount();i++){
                    item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    imageUriList.add(uri);
                }
            }

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(this, getResources().getString(R.string.upload_progress), Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }
        }
    }

}
