package com.it.xevai60.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.xevai60.LoginActivity;
import com.it.xevai60.R;

import java.util.ArrayList;

public class LV_dsXeCoMe_Adapter extends BaseAdapter {
    public static final String TAG = LoginActivity.class.getSimpleName();

    Context context;

    ArrayList<String> idYeuCauArray ;
    ArrayList<String> maSoXeArray ;
    ArrayList<String> loaiXeArray ;
    ArrayList<String> maMeArray ;
    ArrayList<String> viTriArray ;

    public LV_dsXeCoMe_Adapter(Context context, ArrayList<String> idYeuCauArray, ArrayList<String> maSoXeArray, ArrayList<String> loaiXeArray, ArrayList<String> maMeArray, ArrayList<String> viTriArray) {
        this.context = context;
        this.idYeuCauArray = idYeuCauArray;
        this.maSoXeArray = maSoXeArray;
        this.loaiXeArray = loaiXeArray;
        this.maMeArray = maMeArray;
        this.viTriArray = viTriArray;
    }

    @Override
    public int getCount() {
        return idYeuCauArray.size();
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
        view = inflater.inflate(R.layout.item_ds_xecome, null);
        //context.; //attach to your item
        // Đổ dữ liệu vào biến View, view này chính là những gì nằm trong item_name.xml

        // Đặt chữ cho từng view trong danh sách.
//        TextView tvId = view.findViewById(R.id.tv_yeucauxe_id);
//        tvId.setText(idArray.get(i));

        TextView txtmasoxe = view.findViewById(R.id.tv_dsxecome_masoxe);
        txtmasoxe.setText(maSoXeArray.get(i));

        TextView txtmame = view.findViewById(R.id.tv_dsxecome_mame);
        txtmame.setText(maMeArray.get(i));

        TextView txtvitri = view.findViewById(R.id.tv_dsxecome_vitrihienhanh);
        txtvitri.setText(viTriArray.get(i));


        //int count = getCount();
        //Log.e(TAG, Integer.toString(count));
        // Trả về view kết quả.
        //Log.e(TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaa");
        return view;
    }
}
