package com.project.driverdrowsinessdetectionsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.driverdrowsinessdetectionsystem.ui.contact.ContactFragment;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

   private Spinner spinner;
   private EditText editText, edit;
   private Button bLogin;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ddd);

        spinner = findViewById(R.id.spinnerContries);spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        bLogin = findViewById(R.id.bLogin);
        editText = findViewById(R.id.editTextMobile);
        edit = findViewById(R.id.Username);

        mFirebaseInstance = FirebaseDatabase.getInstance();

       mFirebaseDatabase = mFirebaseInstance.getReference("users");

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String mobile = editText.getText().toString().trim();
                String name = edit.getText().toString();

                if (mobile.isEmpty() || mobile.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(userId)) {
                    createUser(name, mobile);
                }
                else {
                    updateUser(name, mobile);
                }
                String Mobile = "+" + code + mobile;

                Intent intent = new Intent(LoginActivity.this, OtpVerification.class);
                intent.putExtra("mobile", Mobile);
                startActivity(intent);
            }
        });

        toggleButton();
        }

        private void toggleButton() {
        if (TextUtils.isEmpty(userId))  {
            bLogin.setText("Save");
        }
        else{
            bLogin.setText("Update");
        }
        }

        private void createUser(String name, String mobile) {

            if (TextUtils.isEmpty(userId)) {
               userId = mFirebaseDatabase.push().getKey();
               // userId = "Bhavya";
            }

            User user = new User(name, mobile);

            mFirebaseDatabase.child(userId).setValue(user);

            addUserChangeListener();
        }
        private void addUserChangeListener()
        {
            mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user == null) {
                        Log.e(TAG, "User data is null");
                        return;
                    }

                    Log.e(TAG, "User data is changed!" + user.name + ", " + user.mobile);

                    editText.setText("");
                    edit.setText("");

                    toggleButton();
                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.e(TAG, "Failed to read user", databaseError.toException());
                }
            });
            }

            private void updateUser(String name, String mobile)  {

              if (!TextUtils.isEmpty(name))
                  mFirebaseDatabase.child(userId).child("name").setValue(name);

              if (!TextUtils.isEmpty(mobile))
                  mFirebaseDatabase.child(userId).child("mobile").setValue(mobile);

            }
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
}



