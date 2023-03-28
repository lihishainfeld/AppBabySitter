package com.example.newbabysisterapp.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
 * Use the {@link managerFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class managerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<User> users = new ArrayList<User>();
    private int index = -1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment managerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static managerFragment newInstance(String param1, String param2) {
        managerFragment fragment = new managerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public managerFragment() {
        // Required empty public constructor
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
        View fragmentView = inflater.inflate(R.layout.fragment_manager, container, false);
        Button approveButton = fragmentView.findViewById(R.id.approveButton);
        fillNotCheckedUsers(fragmentView);
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = users.get(index);
                user.setApproved(User.APPROVED);
                save(user);
                moveToNextUser(fragmentView);
            }
        });
        Button declineButton = fragmentView.findViewById(R.id.declineButton);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = users.get(index);
                user.setApproved(User.DECLINED);
                save(user);
                moveToNextUser(fragmentView);
            }
        });
        return fragmentView;
    }

    private void save(User user) {
        FirebaseDatabase database = Globals.getDatabase();
        DatabaseReference myRef = database.getReference("users").child(user.getUserID());
        myRef.setValue(user);
    }
    private void fillNotCheckedUsers(View fragmentView) {
        FirebaseDatabase database = Globals.getDatabase();
        DatabaseReference myRef = database.getReference("users");
        users.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    user = postSnapshot.getValue(User.class);
                    if (user.getApproved().equals(User.NOT_CHECKED))
                        users.add(user);
                }
                readPhotos(fragmentView);
                moveToNextUser(fragmentView);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    private void readPhotos(View fragmentView) {
        for (User user : users) {
            readPhoto(user, fragmentView);
        }
    }
    private void readPhoto(User user, View fragmentView) {
        String photoLink = user.getPhotoLink();
        if ((photoLink == null) || photoLink.isEmpty())
            return;
        FirebaseStorage storage = Globals.getStorage();
        StorageReference pathReference = storage.getReference().child(photoLink);
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                user.setBitmap(bitmap);
                if (index == users.indexOf(user)) {
                    ImageView image = fragmentView.findViewById(R.id.userImage);
                    image.setImageBitmap(user.theBitmap());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    private void moveToNextUser(View fragmentView) {
        index++;
        Button approveButton = fragmentView.findViewById(R.id.approveButton);
        Button declineButton = fragmentView.findViewById(R.id.declineButton);
        TextView privateNameManagerPage = fragmentView.findViewById(R.id.privateNameManagerPage);
        TextView familyNameManagerPage = fragmentView.findViewById(R.id.familyNameManagerPage);
        TextView addressManagerPage = fragmentView.findViewById(R.id.addressManagerPage);
        TextView phoneManagerPage = fragmentView.findViewById(R.id.phoneManagerPage);
        TextView TextEmailAddressManagerPage = fragmentView.findViewById(R.id.TextEmailAddressManagerPage);
        TextView facebookLinkManagerPage = fragmentView.findViewById(R.id.facebookLinkManagerPage);
        TextView aboutmeManagerPage = fragmentView.findViewById(R.id.aboutmeManagerPage);
        ImageView image = fragmentView.findViewById(R.id.userImage);
        if (index < users.size()) {
            approveButton.setEnabled(true);
            declineButton.setEnabled(true);
            User user = users.get(index);
            privateNameManagerPage.setText(user.getPrivateName());
            familyNameManagerPage.setText(user.getFamilyName());
            addressManagerPage.setText(user.getAddress());
            phoneManagerPage.setText(user.getPhone());
            TextEmailAddressManagerPage.setText(user.getEmail());
            facebookLinkManagerPage.setText(user.getFacebookLink());
            aboutmeManagerPage.setText(user.getDescription());
            if (user.theBitmap() != null) {
                image.setImageBitmap(user.theBitmap());
            }
        }
        else {
            approveButton.setEnabled(false);
            declineButton.setEnabled(false);
            privateNameManagerPage.setText("");
            familyNameManagerPage.setText("");
            addressManagerPage.setText("");
            phoneManagerPage.setText("");
            TextEmailAddressManagerPage.setText("");
            facebookLinkManagerPage.setText("");
            aboutmeManagerPage.setText("");
            image.setImageBitmap(null);
        }
    }
}
