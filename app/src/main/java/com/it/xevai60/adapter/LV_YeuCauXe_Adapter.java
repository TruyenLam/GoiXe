package com.it.xevai60.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.it.xevai60.LoginActivity;
import com.it.xevai60.R;

import java.util.ArrayList;

public class LV_YeuCauXe_Adapter extends BaseAdapter {

    public static final String TAG = LoginActivity.class.getSimpleName();

    Context context;

    ArrayList<String> idArray ;
    ArrayList<String> loaiXeArray ;
    ArrayList<String> loaiYeuCaunArray;
    ArrayList<String> maMeArray ;
    ArrayList<String> maSoXeArray ;
    ArrayList<String> nguoiYeuCauArray ;
    ArrayList<String> thoiGianCanArray ;
    ArrayList<String> viTriCanArray ;

    public LV_YeuCauXe_Adapter(Context context, ArrayList<String> idArray, ArrayList<String> loaiXeArray, ArrayList<String> loaiYeuCaunArray, ArrayList<String> maMeArray, ArrayList<String> maSoXeArray, ArrayList<String> nguoiYeuCauArray, ArrayList<String> thoiGianCanArray, ArrayList<String> viTriCanArray) {
        this.context = context;
        this.idArray = idArray;
        this.loaiXeArray = loaiXeArray;
        this.loaiYeuCaunArray = loaiYeuCaunArray;
        this.maMeArray = maMeArray;
        this.maSoXeArray = maSoXeArray;
        this.nguoiYeuCauArray = nguoiYeuCauArray;
        this.thoiGianCanArray = thoiGianCanArray;
        this.viTriCanArray = viTriCanArray;
    }

    @Override
    public int getCount() {
        return idArray.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Gọi layoutInflater ra để bắt đầu ánh xạ view và data.
        //LayoutInflater inflater = context.getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.item_yeucauxe, null);
        //context.; //attach to your item
        // Đổ dữ liệu vào biến View, view này chính là những gì nằm trong item_name.xml

        // Đặt chữ cho từng view trong danh sách.
//        TextView tvId = view.findViewById(R.id.tv_yeucauxe_id);
//        tvId.setText(idArray.get(i));

        TextView txtnguoiyeucau = view.findViewById(R.id.tv_yeucauxe_nguoiyeucau);
        txtnguoiyeucau.setText(nguoiYeuCauArray.get(i));

        TextView txtthoigiancan = view.findViewById(R.id.tv_yeucauxe_thoigiancan);
        txtthoigiancan.setText(thoiGianCanArray.get(i));

        TextView txtvitrican = view.findViewById(R.id.tv_yeucauxe_vitrican);
        txtvitrican.setText(viTriCanArray.get(i));
        TextView txtvitricanText=view.findViewById(R.id.tv_yeucauxe_vitrican_text);

        TextView txtLoaiYeuCau=view.findViewById(R.id.tv_yeucauxe_loaiyeucau);


        //ConstraintLayout listView_YeuCauXe=view.findViewById(R.id.item_yeucauxe);
        //txtLoaiYeuCau.setText(loaiYeuCaunArray.get(i));
        String loaiyeucau = loaiYeuCaunArray.get(i);
        if(loaiyeucau.equals("Me")){
            txtLoaiYeuCau.setText("Mẻ: ");
            TextView txtmame = view.findViewById(R.id.tv_yeucauxe_mame);
            txtmame.setText(maMeArray.get(i));
            txtmame.setTextColor(Color.RED);
        }else if (loaiyeucau.equals("XeTrong")){
            txtLoaiYeuCau.setText("Xe: ");
            TextView txtmame = view.findViewById(R.id.tv_yeucauxe_mame);
            txtmame.setText(loaiXeArray.get(i));
            txtmame.setTextColor(Color.BLUE);
            //listView_YeuCauXe.setBackgroundColor("red");
        }else if(loaiyeucau.equals("TapKetMe")){
            txtLoaiYeuCau.setText("Tập Kết: ");
            TextView txtmame = view.findViewById(R.id.tv_yeucauxe_mame);
            txtmame.setText(maMeArray.get(i));
            txtmame.setTextColor(Color.DKGRAY);
            txtvitrican.setVisibility(View.INVISIBLE);
            txtvitricanText.setVisibility(View.INVISIBLE);
        }

//        TextView txtmame = view.findViewById(R.id.tv_yeucauxe_mame);
//        String mame = maMeArray.get(i);
//        if(mame != "null"){
//            txtmame.setText(maMeArray.get(i));
//        }else {
//            txtmame.setText("");
//        }


        //int count = getCount();
        //Log.e(TAG, Integer.toString(count));
        // Trả về view kết quả.
        //Log.e(TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaa");
        return view;
    }
}

