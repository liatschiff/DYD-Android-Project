package com.example.yodenproject.PageFragments;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yodenproject.Activities.MainPage;
import com.example.yodenproject.Model.User;
import com.example.yodenproject.R;
import com.github.abdularis.civ.CircleImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class UserDetailsFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");

    TextView userEmail;
    TextView userAddress;
    TextView userAge;
    TextView userGender;
    TextView userName;
    TextView userLocation;
    TextView phoneNumber;
    CircleImageView profileImageView;
    ImageButton editDetailsBtn;
    View dialogView;

    User user;

    private final int IMAGE_REQUEST = 1;
    private final int WRITE_PERMISSION_REQUEST = 0;

    Uri imageUri;
    String imageUrl = "default";

    StorageTask uploadTask;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainPage mainPage = (MainPage) getActivity();
        user = mainPage.getUser();

        View root = inflater.inflate(R.layout.user_details_layout,container,false);
        userEmail = root.findViewById(R.id.user_email_tv);
        userAddress = root.findViewById(R.id.user_adress_tv);
        userAge = root.findViewById(R.id.user_age_tv);
        userGender = root.findViewById(R.id.user_gender_tv);
        userName = root.findViewById(R.id.user_name_tv);
        userLocation = root.findViewById(R.id.user_location_tv);
        profileImageView = root.findViewById(R.id.user_profile_pic);
        phoneNumber = root.findViewById(R.id.user_phone_tv);

        editDetailsBtn = root.findViewById(R.id.user_edit_btn);
        editDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogView= getLayoutInflater().inflate(R.layout.private_details_edit_dialog,null);
                final EditText fullNameEt = dialogView.findViewById(R.id.user_fullname_edit_et);
                final EditText genderEt = dialogView.findViewById(R.id.user_gender_edit_et);
                final EditText ageEt=dialogView.findViewById(R.id.user_age_edit_et);
                final EditText countryEt = dialogView.findViewById(R.id.user_country_edit_et);
                final EditText cityEt = dialogView.findViewById(R.id.user_city_edit_et);
                final EditText phoneEt = dialogView.findViewById(R.id.user_phone_edit_et);
                final EditText addressEt = dialogView.findViewById(R.id.user_address_edit_et);
                final CircleImageView profileUpdateImage = dialogView.findViewById(R.id.user_edit_img);
                if(user.getImageUrl().equals("default")){
                    profileUpdateImage.setImageResource(R.drawable.ic_user);
                } else Glide.with(getActivity()).load(user.getImageUrl()).into(profileUpdateImage);

                Button UpdateImageBtn = dialogView.findViewById(R.id.edit_img_upload_btn);
                UpdateImageBtn.setOnClickListener(new View.OnClickListener() {
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

                FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        fullNameEt.setText(user.getFullname());
                        genderEt.setText(user.getGender());
                        countryEt.setText(user.getCountry());
                        ageEt.setText(user.getAge()+"");
                        cityEt.setText(user.getCity());
                        addressEt.setText(user.getAddress());
                        phoneEt.setText(user.getPhone());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setView(dialogView).setTitle(getResources().getString(R.string.edit_title)).setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                if( fullNameEt.getText().toString().isEmpty()|| phoneEt.getText().toString().isEmpty() || genderEt.getText().toString().isEmpty()
                                        || ageEt.getText().toString().isEmpty() || countryEt.getText().toString().isEmpty()
                                        || cityEt.getText().toString().isEmpty() ||addressEt.getText().toString().isEmpty()){
                                    Toast.makeText(getActivity(), getResources().getString(R.string.failed_details), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if(imageUri!=null){
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
                                                    imageUrl = downloadUri.toString();
                                                    Glide.with(getActivity()).load(imageUrl).into(profileUpdateImage);
                                                    if(!user.getImageUrl().equals("default")){
                                                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.getImageUrl());
                                                        storageReference.delete();
                                                    }

                                                    String fullname = fullNameEt.getText().toString();
                                                    String gender = genderEt.getText().toString();
                                                    int age = Integer.parseInt(ageEt.getText().toString());
                                                    String country = countryEt.getText().toString();
                                                    String city = cityEt.getText().toString();
                                                    String address = addressEt.getText().toString();
                                                    String phone = phoneEt.getText().toString();
                                                    String userid = firebaseAuth.getCurrentUser().getUid();
                                                    User userUpdate = new User(phone,imageUrl, fullname, country, city, address, gender, userid, user.getStatus(), age, user.isClient());
                                                    FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(userUpdate);


                                                } else {
                                                    Toast.makeText(getActivity(), getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else{
                                        HashMap<String,Object> hashMap = new HashMap<>();
                                        hashMap.put("fullname",fullNameEt.getText().toString());
                                        hashMap.put("gender",genderEt.getText().toString());
                                        hashMap.put("age",Integer.parseInt(ageEt.getText().toString()));
                                        hashMap.put("country",countryEt.getText().toString());
                                        hashMap.put("city",cityEt.getText().toString());
                                        hashMap.put("address",addressEt.getText().toString());
                                        hashMap.put("phone",phoneEt.getText().toString());
                                        dataSnapshot.getRef().updateChildren(hashMap);
                                        showData();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).setCancelable(false).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        showData();

        return root;
    }

    private void showData() {

        userName.setText(user.getFullname());
        userGender.setText(user.getGender());
        userLocation.setText(user.getCity()+", "+user.getCountry());
        userAddress.setText(user.getAddress());
        userAge.setText(user.getAge()+"");
        phoneNumber.setText(user.getPhone());
        userEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        if(user.getImageUrl().equals("default")){
            profileImageView.setImageResource(R.drawable.ic_user);
        }
        else Glide.with(getActivity())
                .load(user.getImageUrl())
                .into(profileImageView);
    }

    private String getFileExtension(Uri uri){

        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
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

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null &&data.getData() != null){
            imageUri = data.getData();

            Glide.with(this).load(imageUri).into(profileImageView);
        }
    }
}