package com.example.simpleroomexample;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    public void insert(Item... items);

    @Update
    public void update(Item... items);

    @Delete
    public void delete(Item item);

    @Query("SELECT * FROM items")
    public List<Item> getItems();

    @Query("SELECT * FROM items WHERE id = :id")
    public Item getItemById(Long id);

    @Query("DELETE FROM items")
    void deleteAll();
}
