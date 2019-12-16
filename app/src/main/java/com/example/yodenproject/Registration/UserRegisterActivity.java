package com.example.yodenproject.Registration;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yodenproject.Activities.MainPage;
import com.example.yodenproject.Model.ProfileProfessional;
import com.example.yodenproject.Model.Review;
import com.example.yodenproject.Model.User;
import com.example.yodenproject.R;
import com.github.abdularis.civ.CircleImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserRegisterActivity extends Activity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");
    DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profile");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");

    StorageTask uploadTask;

    CircleImageView profileImageView;
    EditText emailEt;
    EditText phoneEt;
    EditText passwordEt;
    EditText repasswordEt;
    EditText fullNameEt;
    EditText genderEt;
    EditText countryEt;
    EditText ageEt;
    EditText cityEt;
    EditText addressEt;

    private final int IMAGE_REQUEST = 1;
    private final int WRITE_PERMISSION_REQUEST = 0;

    Uri imageUri;
    String imageUrl = "default";
    boolean isClient;
    ProgressDialog progressDialog;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_activity);

        emailEt = findViewById(R.id.user_email_register_et);
        passwordEt = findViewById(R.id.user_password_register_et);
        repasswordEt = findViewById(R.id.user_repassword_register_et);
        fullNameEt=findViewById(R.id.user_fullname_register_et);
        genderEt=findViewById(R.id.user_gender_register_et);
        countryEt=findViewById(R.id.user_country_register_et);
        ageEt=findViewById(R.id.user_age_register_et);
        cityEt=findViewById(R.id.user_city_register_et);
        addressEt=findViewById(R.id.user_address_register_et);
        phoneEt = findViewById(R.id.user_phone_register_et);
        profileImageView = findViewById(R.id.user_register_img);

        Intent intent = getIntent();
        isClient = intent.getExtras().getBoolean("isClient");

        Button uploadImgBtn = findViewById(R.id.register_img_upload_btn);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=23){
                    int hasWrittenPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(hasWrittenPermission != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
                    else openFileChooser();
                }
                else openFileChooser();
            }
        });

        Button signUpBtn = findViewById(R.id.register_sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(UserRegisterActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = new ProgressDialog(UserRegisterActivity.this);
                    progressDialog.setMessage("Loading please wait...");
                    progressDialog.show();
                    uploadFile();

                }
            }
        });

    }

    private String getFileExtension(Uri uri){

        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void uploadFile() {

        if(imageUri == null){
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            String repassword = repasswordEt.getText().toString();
            final String address = addressEt.getText().toString();
            final String country = countryEt.getText().toString();
            final String gender = genderEt.getText().toString();
            final String ageStr = ageEt.getText().toString();
            final String fullName = fullNameEt.getText().toString();
            final String city = cityEt.getText().toString();
            final String phone = phoneEt.getText().toString();
            final String status = "offline";

            if(password.isEmpty() || repassword.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || country.isEmpty()
                    || gender.isEmpty() || city.isEmpty() || fullName.isEmpty() ||ageStr.isEmpty()){
                progressDialog.dismiss();
                Toast.makeText(this, "Make sure to fill all the fields correctly", Toast.LENGTH_SHORT).show();
            }

            else if(!password.equals(repassword)) {
                progressDialog.dismiss();
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }

            else {
                final int age = Integer.parseInt(ageStr);
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String userid = firebaseAuth.getCurrentUser().getUid();
                            user = new User(phone,imageUrl, fullName, country, city, address, gender, userid, status, age, isClient);
                            progressDialog.dismiss();
                            Toast.makeText(UserRegisterActivity.this, "sign up succesful", Toast.LENGTH_SHORT).show();
                            userRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);

                            if(!user.isClient()){
                                String image = "default";
                                Review review = new Review("We believe in you!","Yoden!","default");
                                ArrayList<String> images=new ArrayList<>();
                                ArrayList<Review> reviews=new ArrayList<>();
                                images.add(image);
                                reviews.add(review);
                                String information="I have amazing works";
                                ProfileProfessional profileProfessional=new ProfileProfessional(images,reviews,information);
                                profileRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(profileProfessional);
                            }

                            Intent intent = new Intent(UserRegisterActivity.this, MainPage.class);
                            startActivity(intent);
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(UserRegisterActivity.this, "sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        if (imageUri != null)
        {
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
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        imageUrl = downloadUri.toString();

                        String email = emailEt.getText().toString();
                        String password = passwordEt.getText().toString();
                        String repassword = repasswordEt.getText().toString();
                        final String address = addressEt.getText().toString();
                        final String country = countryEt.getText().toString();
                        final String gender = genderEt.getText().toString();
                        final String ageStr = ageEt.getText().toString();
                        final String phone = phoneEt.getText().toString();
                        final String fullName = fullNameEt.getText().toString();
                        final String city = cityEt.getText().toString();
                        final String status = "offline";

                        if(password.isEmpty() || repassword.isEmpty()|| phone.isEmpty() || email.isEmpty() || address.isEmpty() || country.isEmpty()
                                || gender.isEmpty() || city.isEmpty() || fullName.isEmpty() ||ageStr.isEmpty()){
                            progressDialog.dismiss();
                            Toast.makeText(UserRegisterActivity.this, getResources().getString(R.string.failed_details), Toast.LENGTH_SHORT).show();
                        }

                        else if(!password.equals(repassword)) {
                            progressDialog.dismiss();
                            Toast.makeText(UserRegisterActivity.this, getResources().getString(R.string.password_match), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            final int age = Integer.parseInt(ageStr);
                            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        String userid = firebaseAuth.getCurrentUser().getUid();
                                        progressDialog.dismiss();
                                        user = new User(phone,imageUrl, fullName, country, city, address, gender, userid, status, age, isClient);
                                        Toast.makeText(UserRegisterActivity.this, getResources().getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                                        userRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                                        if(!user.isClient()){
                                            String image = "image";
                                            Review review = new Review("something review","something name","default");
                                            ArrayList<String> images=new ArrayList<>();
                                            ArrayList<Review> reviews=new ArrayList<>();
                                            images.add(image);
                                            reviews.add(review);
                                            String information="I have amazing works";
                                            ProfileProfessional profileProfessional=new ProfileProfessional(images,reviews,information);
                                            profileRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(profileProfessional);
                                        }
                                        Intent intent = new Intent(UserRegisterActivity.this, MainPage.class);
                                        startActivity(intent);
                                    } else{
                                        progressDialog.dismiss();
                                        Toast.makeText(UserRegisterActivity.this, getResources().getString(R.string.sign_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    } else
                    {
                        Toast.makeText(UserRegisterActivity.this, getResources().getString(R.string.upload_failed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
                Toast.makeText(this, getResources().getString(R.string.permission_notice), Toast.LENGTH_SHORT).show();
            }
            else openFileChooser();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null &&data.getData() != null){
            imageUri = data.getData();

            Glide.with(this).load(imageUri).into(profileImageView);
        }
    }
}
