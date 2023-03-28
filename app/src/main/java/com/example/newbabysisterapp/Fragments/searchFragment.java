package com.example.newbabysisterapp.Fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newbabysisterapp.R;
import com.example.newbabysisterapp.globals.Globals;
import com.example.newbabysisterapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link searchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String place = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<User> users = new ArrayList<>();
    ListView usersList;
    private CustomAdapter adapter;

    public searchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Globals.getLoggedUser() != null) {
            place = Globals.getLoggedUser().getAddress();
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        Button changePlaceButton = fragmentView.findViewById(R.id.changePlaceButton);
        changePlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newPlace = fragmentView.findViewById(R.id.changePlaceText);
                place = newPlace.getText().toString();
                fillAllFitUsers();
            }
        });
        usersList = fragmentView.findViewById(R.id.searchUsersList);
        adapter= new CustomAdapter(users, getActivity());
        usersList.setAdapter(adapter);
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /// Lihi fill that and pass the selected user
            }
        });
        fillAllFitUsers();
        return fragmentView;
    }
    private void fillAllFitUsers() {
        FirebaseDatabase database = Globals.getDatabase();
        DatabaseReference myRef = database.getReference("users");
        User loggedUser = Globals.getLoggedUser();
        if (loggedUser == null)
            return;
        users.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    user = postSnapshot.getValue(User.class);
                    if (user.getAddress().equals(place) &&
                        user.getKind().equals(User.WORKER_KIND) &&
                        !user.getUserID().equals(loggedUser.getUserID()) &&
                            user.getApproved().equals(User.APPROVED))
                        users.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

}
