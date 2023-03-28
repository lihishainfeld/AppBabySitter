package com.example.newbabysisterapp.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newbabysisterapp.R;
import com.example.newbabysisterapp.globals.Globals;
import com.example.newbabysisterapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static signInFragment newInstance(String param1, String param2) {
        signInFragment fragment = new signInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button buttonSignIn = fragmentView.findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(fragmentView);
            }
        });
        return fragmentView;
    }

    private void signIn(View frameView) {
        FirebaseAuth mAuth = Globals.getmAuth();
        EditText signinUserName = frameView.findViewById(R.id.signinUserName);
        String userName = signinUserName.getText().toString();
        EditText editTextTextPassword = frameView.findViewById(R.id.editTextTextPassword);
        String password = editTextTextPassword.getText().toString();
        if (Globals.isManager(userName, password)) {
            Navigation.findNavController(frameView).navigate(R.id.action_signInFragment_to_managerFragment);
            return;
        }
        mAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(frameView.getContext(), "Login successful", Toast.LENGTH_LONG).show();
                            FirebaseDatabase database = Globals.getDatabase();
                            DatabaseReference myRef = database.getReference("users").child(mAuth.getUid());
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    User user = dataSnapshot.getValue(User.class);
                                    Globals.setLoggedUser(user);
                                    Navigation.findNavController(frameView).navigate(R.id.action_signInFragment_to_searchFragment);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {

                                }
                            });

                        } else {


                            Toast.makeText(frameView.getContext(), "Login failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}
