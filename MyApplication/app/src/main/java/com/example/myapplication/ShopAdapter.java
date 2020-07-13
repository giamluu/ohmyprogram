package com.example.myapplication;

import android.view.View;

import androidx.appcompat.app.AlertController;

public class ShopAdapter extends AlertController.RecycleListView.AdapterContextMenuInfo {

    public ShopAdapter(View targetView, int position, long id) {
        super(targetView, position, id);
    }
}
