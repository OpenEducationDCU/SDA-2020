package com.example.logindemo;
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.net.Uri;

/**
 * {@link UserAdapter} represents data returns relating to active user
 * Each object has 4 properties: name, email, photoUrl and verification status
 * This is a basic arrayAdapter
 */
public class UserAdapter  {

    private String mName;
    private String mEmail;
    private Uri photoUrl;
    private boolean emailVerified;
    private String mUid;

    //new adapter object
    public UserAdapter(String name, String email, Uri photo, boolean isVerified, String uid)
    {
        mName = name;
        mEmail = email;
        photoUrl = photo;
        emailVerified = isVerified;
        mUid = uid;
    }

    //user name
    public String getName() {
        return mName;
    }
    //user email
    public String getEmail() {
        return mEmail;
    }
    //user URL *(photo if it exists)
    public Uri getPhotoUrl() {
        return photoUrl;
    }
    //user status
    public boolean getEmailStatus() {
        return emailVerified;
    }
    //user ID
    public String getUid() {
        return mUid;
    }
}

