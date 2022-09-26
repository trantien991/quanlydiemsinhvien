package com.example.danhbadienthoai;

public class DanhBa {
    private int id;
    private String kytu;
    private String mau;
    private String TenNguoi;
    private  String sdt;

    public DanhBa(int id,String kytu, String mau, String tenNguoi, String sdt) {
        this.id = id;
        this.kytu = kytu;
        this.mau = mau;
        TenNguoi = tenNguoi;
        this.sdt = sdt;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKytu() {
        return kytu;
    }

    public void setKytu(String kytu) {
        this.kytu = kytu;
    }

    public String getMau() {
        return mau;
    }

    public void setMau(String mau) {
        this.mau = mau;
    }

    public String getTenNguoi() {
        return TenNguoi;
    }

    public void setTenNguoi(String tenNguoi) {
        TenNguoi = tenNguoi;
    }
}
