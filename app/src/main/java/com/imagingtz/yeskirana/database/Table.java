package com.imagingtz.yeskirana.database;

/**
 * Created by Mishra on 1/12/2016.
 */
public class Table {
    public static class Product {
        public static final String PRODUCT_TABLE = "product";
        public static final String ID = "ID",
                Product_ID = "product_id",
                ITEM_ID = "item_id",
                ITEM_NAME = "item_name",
                WEIGHT = "weight",
                PRIZE = "prize",
                ITEM_IMAGE = "item_image",
                DELETED = "deleted";

        public static String CREATE_TABLE = "create table " + PRODUCT_TABLE
                + " ( " + ID + " integer  primary key autoincrement ,"
                + Product_ID + " INTEGER,"
                + ITEM_ID + " INTEGER,"
                + ITEM_NAME + " TEXT,"
                + WEIGHT + " TEXT,"
                + PRIZE + " TEXT,"
                + ITEM_NAME + " TEXT,"
                + DELETED + " INTEGER DEFAULT 0)";

    }

    public static class Cart {
        public static final String CART_TABLE = "cart";
        public static final String ID = "ID",
                PRODUCT_ID = "product_id",
                QUANTITY = "quantity",
                DELETED = "deleted";

        public static String CREATE_TABLE = "create table " + CART_TABLE
                + " ( " + ID + " integer  primary key autoincrement ,"
                + PRODUCT_ID + " TEXT NOT NULL UNIQUE,"
                + QUANTITY + " TEXT,"
                + DELETED + " INTEGER DEFAULT 0)";
    }
}