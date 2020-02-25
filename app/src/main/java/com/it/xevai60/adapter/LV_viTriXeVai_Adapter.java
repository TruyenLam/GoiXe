package com.it.xevai60.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.it.xevai60.R;
import com.it.xevai60.model.ViTriXeVai_Model;

import java.util.ArrayList;
import java.util.List;

public class LV_viTriXeVai_Adapter extends BaseAdapter implements Filterable {
    private Context context;
    private LayoutInflater inflater;

    private List<ViTriXeVai_Model> viTriXeVai_modelList;
    List<ViTriXeVai_Model> mStringFilterList;
    ValueFilter valueFilter;

    public LV_viTriXeVai_Adapter(Context context, List<ViTriXeVai_Model> viTriXeVai_modelList) {
        this.context = context;
        this.viTriXeVai_modelList = viTriXeVai_modelList;
        mStringFilterList = viTriXeVai_modelList;
    }

    @Override
    public int getCount() {
        return viTriXeVai_modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return viTriXeVai_modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.item_vitritapket, null);
        }

        ViTriXeVai_Model viTriXeVai_model = viTriXeVai_modelList.get(i);

        TextView txtmavitri = view.findViewById(R.id.tv_itemtapket_vitri);
        txtmavitri.setText(viTriXeVai_model.getMaVitri());

        TextView txtmota = view.findViewById(R.id.tv_itemtapket_mota);
        txtmota.setText(viTriXeVai_model.getMoTa());

        return view;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }
    private class ValueFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<ViTriXeVai_Model> filterList = new ArrayList<ViTriXeVai_Model>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getMaVitri().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        ViTriXeVai_Model bean = new ViTriXeVai_Model(
                                mStringFilterList.get(i).getiD(),
                                mStringFilterList.get(i).getMaVitri()
                                ,mStringFilterList.get(i).getMoTa()
                                ,mStringFilterList.get(i).getTinhTrangSuDung(),
                                mStringFilterList.get(i).getGhiChu());
                        filterList.add(bean);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            viTriXeVai_modelList = (ArrayList<ViTriXeVai_Model>) results.values;
            notifyDataSetChanged();

        }
    }
}
