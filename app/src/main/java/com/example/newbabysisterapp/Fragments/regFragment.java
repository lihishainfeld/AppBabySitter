package com.example.newbabysisterapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.newbabysisterapp.R;
import com.example.newbabysisterapp.globals.Globals;
import com.example.newbabysisterapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link regFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class regFragment extends Fragment {

    static private final int REQUEST_CAMERA = 0;
    // the photo of the user
    Bitmap bitmap = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public regFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment regFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static regFragment newInstance(String param1, String param2) {
        regFragment fragment = new regFragment();
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
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Bundle bundle = data.getExtras();
                            bitmap = (Bitmap) bundle.get("data");
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_reg, container, false);
        Button finishRegButton = fragmentView.findViewById(R.id.finishRegButtonID);
        finishRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishRegButton(fragmentView);
            }
        });
        Button uploadPictureButton = fragmentView.findViewById(R.id.uploadPictureButtonID);
        uploadPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPicture(fragmentView);
            }
        });
        return fragmentView;
    }

    private void uploadPicture(View frameView) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        someActivityResultLauncher.launch(intent);
    }

    public void finishRegButton(View view) {
        if (bitmap == null) {
            Toast.makeText(view.getContext(), "Photo is missing", Toast.LENGTH_LONG).show();
            return;
        }
        TextInputEditText privateNameText = view.findViewById(R.id.privateNameTextID);
        String privateName = privateNameText.getText().toString().trim();
        if (privateName.isEmpty()) {
            Toast.makeText(view.getContext(), "Private name is empty", Toast.LENGTH_LONG).show();
            return;
        }
        TextInputEditText familyNameText = view.findViewById(R.id.familyNameTextID);
        String familyName = familyNameText.getText().toString().trim();
        if (familyName.isEmpty()) {
            Toast.makeText(view.getContext(), "Family name is empty", Toast.LENGTH_LONG).show();
            return;
        }
        EditText editTextPhone = view.findViewById(R.id.editTextPhone);
        String phone = editTextPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            Toast.makeText(view.getContext(), "Phone is empty", Toast.LENGTH_LONG).show();
            return;
        }
        EditText editTextTextEmailAddress = view.findViewById(R.id.editTextTextEmailAddress);
        String email = editTextTextEmailAddress.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(view.getContext(), "Email is empty", Toast.LENGTH_LONG).show();
            return;
        }
        TextInputEditText editTextTextPassword = view.findViewById(R.id.textPassword);
        String password = editTextTextPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(view.getContext(), "Password is empty", Toast.LENGTH_LONG).show();
            return;
        }
        TextInputEditText facebookLink = view.findViewById(R.id.facebookLinkID);
        String facebookLinkStr = facebookLink.getText().toString().trim();
        if (facebookLinkStr.isEmpty()) {
            Toast.makeText(view.getContext(), "Facebook link is empty", Toast.LENGTH_LONG).show();
            return;
        }
        TextInputEditText editTextTextPostalAddress = view.findViewById(R.id.editTextTextPostalAddress);
        String postalAddress = editTextTextPostalAddress.getText().toString();
        if (postalAddress.isEmpty()) {
            Toast.makeText(view.getContext(), "Address is empty", Toast.LENGTH_LONG).show();
            return;
        }
        TextInputEditText textAboutMe = view.findViewById(R.id.textAboutMe);
        String textAboutMeStr = textAboutMe.getText().toString();
        if (textAboutMeStr.isEmpty()) {
            Toast.makeText(view.getContext(), "Description is empty", Toast.LENGTH_LONG).show();
            return;
        }
        RadioButton radioButton = view.findViewById(R.id.registerWorkerRadioButton);
        String userKind = "";
        if (radioButton.isChecked())
            userKind = User.WORKER_KIND;
        else
            userKind = User.CLIENT_KIND;
        User user = new User();
        user.setAddress(postalAddress);
        user.setEmail(email);
        user.setDescription(textAboutMeStr);
        user.setFacebookLink(facebookLinkStr);
        user.setPassword(password);
        user.setPhone(phone);
        user.setPrivateName(privateName);
        user.setFamilyName(familyName);
        user.setKind(userKind);
        createUser(user, view);
    }
    private void createUser(User user, View view) {
        FirebaseStorage storage = Globals.getStorage();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        String path = "photos/" + UUID.randomUUID() + ".png";
        StorageReference firememesRef = storage.getReference(path);

        UploadTask uploadTask = firememesRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(view.getContext(), "You'r picture has been uploaded" , Toast.LENGTH_SHORT).show();
                user.setPhotoLink(path);
                actuallyCreateTheUser(user, view);
            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "The upload has failed" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void actuallyCreateTheUser(User user, View view) {
        FirebaseAuth mAuth = Globals.getmAuth();

        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                Toast.makeText(view.getContext(), "New user created", Toast.LENGTH_LONG).show();
                                user.setUserID(mAuth.getUid());
                                saveUser(user);
                                Navigation.findNavController(view).navigate(R.id.action_regFragment_to_mainFragment);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

                            Toast.makeText(view.getContext(), "Failed to create a new user", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    private void saveUser(User user) {
        if (user == null)
            return;
        FirebaseDatabase database = Globals.getDatabase();
        DatabaseReference myRef = database.getReference("users").child(user.getUserID());
        myRef.setValue(user);
    }
}
