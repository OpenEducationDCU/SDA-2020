package com.example.simpleroomexample;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private int quantity;

    int getId(){return this.id;}
    public void setId(int id) {this.id = id;}

    String getName(){return this.name;}
    public void setName(String name) {this.name = name;}

    String getDescription(){return this.description;}
    public void setDescription(String description) {this.description = description;}

    int getQuantity(){return this.quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

}