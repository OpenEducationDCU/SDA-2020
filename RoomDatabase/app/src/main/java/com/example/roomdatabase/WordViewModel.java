package com.example.roomdatabase;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    //getter method for all the words.
    LiveData<List<Word>> getAllWords() { return mAllWords; }

    //wrapper insert() method that calls the Repository's insert() method.

    // In this way, the implementation of insert() is completely hidden from the UI.
    public void insert(Word word) { mRepository.insert(word); }





}
