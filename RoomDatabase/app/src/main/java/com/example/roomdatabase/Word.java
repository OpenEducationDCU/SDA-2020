package com.example.roomdatabase;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey //(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    public Word(@NonNull String word) {this.mWord = word;}

    public String getWord(){return this.mWord;}


    @Dao
    public interface WordDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Word word);

        @Query("DELETE FROM word_table")
        void deleteAll();

        @Query("SELECT * from word_table ORDER BY word ASC")
        LiveData<List<Word>> getAllWords();
    }
}
