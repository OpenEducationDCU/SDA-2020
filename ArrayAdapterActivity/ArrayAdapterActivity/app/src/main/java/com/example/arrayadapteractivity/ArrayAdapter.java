package com.example.arrayadapteractivity;
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

/**
 * {@link ArrayAdapter} represents a single Android platform release.
 * Each object has 3 properties: name, version number, and image resource ID.
 * This is a basic arrayAdapter
 */
public class ArrayAdapter {

    // String one
    private String mStringOne;

    // some resource id
    private int mImageResourceId;

    /*
     * Create a new ArrayAdapter object.
     *
     */
    public ArrayAdapter(String vString, int imageResourceId)
    {
        mStringOne = vString;
        mImageResourceId = imageResourceId;
    }

    /**
     * Get and return the string
     */
    public String getString() {
        return mStringOne;
    }

    /**
     * Get and return the resource ID
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

}

