package com.example.newbabysisterapp.globals;

import com.example.newbabysisterapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class Globals {
    private static FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static FirebaseStorage storage;
    private static User loggedUser;
    ///private static String managerUserName = "Manager@babysisrerapp.com";
    private static String managerUserName = "M@b.com";
    private static String managerPassword = "123456";

    public static void init() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        Globals.loggedUser = loggedUser;
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static FirebaseDatabase getDatabase() {
        return database;
    }
    public static boolean isManager(String userName, String password) {
        return managerUserName.equals(userName) && managerPassword.equals(password);
    }

    public static FirebaseStorage getStorage() {
        return storage;
    }
}
