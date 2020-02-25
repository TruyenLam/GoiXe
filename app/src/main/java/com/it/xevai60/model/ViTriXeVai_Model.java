package com.it.xevai60.model;

public class ViTriXeVai_Model {

    private String iD;
    private String maVitri;
    private String moTa;
    private String tinhTrangSuDung;
    private String ghiChu;

    public ViTriXeVai_Model(String iD, String maVitri, String moTa, String tinhTrangSuDung, String ghiChu) {
        this.iD = iD;
        this.maVitri = maVitri;
        this.moTa = moTa;
        this.tinhTrangSuDung = tinhTrangSuDung;
        this.ghiChu = ghiChu;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getMaVitri() {
        return maVitri;
    }

    public void setMaVitri(String maVitri) {
        this.maVitri = maVitri;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTinhTrangSuDung() {
        return tinhTrangSuDung;
    }

    public void setTinhTrangSuDung(String tinhTrangSuDung) {
        this.tinhTrangSuDung = tinhTrangSuDung;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
