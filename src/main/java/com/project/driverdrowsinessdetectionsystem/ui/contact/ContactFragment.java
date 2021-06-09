package com.project.driverdrowsinessdetectionsystem.ui.contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.driverdrowsinessdetectionsystem.BackGroundWorker;
import com.project.driverdrowsinessdetectionsystem.OtpVerification;
import com.project.driverdrowsinessdetectionsystem.R;
import com.project.driverdrowsinessdetectionsystem.User;

public class ContactFragment extends AppCompatActivity {

    private static final String TAG = "ContactFragment";

    Button buttonContinue;
    EditText editTextContact,edit;
    int i = 0;
    public String id = "manthan";
    private String name;
    FirebaseUser user;


    private DatabaseReference mDatabase;
    private FirebaseDatabase firebaseInstance;

   // SharedPreferences sharedpreferences;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact);

        editTextContact = (EditText) findViewById(R.id.editTextContact);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);


        user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = firebaseInstance.getReference("users").child(user.getUid()).child("name");
       // mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        //sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    /*mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            id = dataSnapshot.getValue().toString();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                             name = dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                catch(DatabaseException e) {
                    Toast.makeText(ContactFragment.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                if(i==0) {
                   register();
                    //Toast.makeText(ContactFragment.this, "name "+ name, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressLint("NewApi")
    private void register() {
        String mobile = editTextContact.getText().toString();

      //  SharedPreferences.Editor editor = sharedpreferences.edit();

        //editor.putString("name", id);
        //editor.commit();

        String type="register";
        BackGroundWorker backGroundWorker = new BackGroundWorker(this);
        backGroundWorker.execute(type, id, mobile);
    }

}