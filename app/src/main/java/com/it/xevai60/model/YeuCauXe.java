package com.it.xevai60.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YeuCauXe {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("LoaiYeuCau")
    @Expose
    private String loaiYeuCau;
    @SerializedName("LoaiXe")
    @Expose
    private String loaiXe;
    @SerializedName("MaMe")
    @Expose
    private String maMe;
    @SerializedName("MaSoXe")
    @Expose
    private String maSoXe;
    @SerializedName("NguoiYeuCau")
    @Expose
    private String nguoiYeuCau;
    @SerializedName("ThoiGianCan")
    @Expose
    private String thoiGianCan;
    @SerializedName("ViTriCan")
    @Expose
    private String viTriCan;

    /**
     * No args constructor for use in serialization
     *
     */
    public YeuCauXe() {
    }

    public YeuCauXe(String id, String loaiYeuCau, String loaiXe, String maMe, String maSoXe, String nguoiYeuCau, String thoiGianCan, String viTriCan) {
        this.id = id;
        this.loaiYeuCau = loaiYeuCau;
        this.loaiXe = loaiXe;
        this.maMe = maMe;
        this.maSoXe = maSoXe;
        this.nguoiYeuCau = nguoiYeuCau;
        this.thoiGianCan = thoiGianCan;
        this.viTriCan = viTriCan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoaiYeuCau() {
        return loaiYeuCau;
    }

    public void setLoaiYeuCau(String loaiYeuCau) {
        this.loaiYeuCau = loaiYeuCau;
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

    public String getMaSoXe() {
        return maSoXe;
    }

    public void setMaSoXe(String maSoXe) {
        this.maSoXe = maSoXe;
    }

    public String getNguoiYeuCau() {
        return nguoiYeuCau;
    }

    public void setNguoiYeuCau(String nguoiYeuCau) {
        this.nguoiYeuCau = nguoiYeuCau;
    }

    public String getThoiGianCan() {
        return thoiGianCan;
    }

    public void setThoiGianCan(String thoiGianCan) {
        this.thoiGianCan = thoiGianCan;
    }

    public String getViTriCan() {
        return viTriCan;
    }

    public void setViTriCan(String viTriCan) {
        this.viTriCan = viTriCan;
    }
}
