package com.example.myapplication;

public class DataShop {
    private int HinhAnh;
    private String Ten;

    public int getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public DataShop(int hinhAnh, String ten) {
        HinhAnh = hinhAnh;
        Ten = ten;
    }
}
