package com.myandroidapps.yogi.sqliteintro;

/**
 * Created by yogi on 2/22/16.
 */
public class ShoppingKart {
    private int _ID;
    private String _itemName;

    public ShoppingKart() {
    }

    public ShoppingKart(String _itemName) {
        this._itemName = _itemName;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String get_itemName() {
        return _itemName;
    }

    public void set_itemName(String _itemName) {
        this._itemName = _itemName;
    }
}
