package com.it.xevai60.model;



public class dsXeTrongModel {
    private String idYeuCau;
    private  String maSoXe;
    private String loaiXe;
    private String maMe;
    private String viTri;

    public dsXeTrongModel(String idYeuCau, String maSoXe, String loaiXe, String maMe, String viTri) {
        this.idYeuCau = idYeuCau;
        this.maSoXe = maSoXe;
        this.loaiXe = loaiXe;
        this.maMe = maMe;
        this.viTri = viTri;
    }

    public String getIdYeuCau() {
        return idYeuCau;
    }

    public void setIdYeuCau(String idYeuCau) {
        this.idYeuCau = idYeuCau;
    }

    public String getMaSoXe() {
        return maSoXe;
    }

    public void setMaSoXe(String maSoXe) {
        this.maSoXe = maSoXe;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public String getMaMe() {
        return maMe;
    }

    public void setMaMe(String maMe) {
        this.maMe = maMe;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }
}
