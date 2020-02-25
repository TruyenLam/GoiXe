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
import com.it.xevai60.model.XeVai_Model;

import java.util.ArrayList;
import java.util.List;

public class LV_xevai_Adapter extends BaseAdapter implements Filterable {
    private Context context;
    private LayoutInflater inflater;

    private List<XeVai_Model> xeVai_modelList;
    List<XeVai_Model> mStringFilterList;
    ValueFilter valueFilter;

    public LV_xevai_Adapter(Context context, List<XeVai_Model> xeVai_modelList) {
        this.context = context;
        this.xeVai_modelList = xeVai_modelList;
        mStringFilterList = xeVai_modelList;
    }

    @Override
    public int getCount() {
        return xeVai_modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return xeVai_modelList.get(position);
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
            view = inflater.inflate(R.layout.item_xetapket, null);
        }

        XeVai_Model xeVai_model = xeVai_modelList.get(i);

        TextView txtmasoxe = view.findViewById(R.id.tv_tapketxe_masoxe);
        txtmasoxe.setText(xeVai_model.getMaSoXe());

        TextView txtloaixe = view.findViewById(R.id.tv_tapketxe_loaixe);
        txtloaixe.setText(xeVai_model.getLoaiXe());

        TextView txtmame = view.findViewById(R.id.tv_tapketxe_mame);
        txtmame.setText(xeVai_model.getMaMe());

        TextView txtvitri = view.findViewById(R.id.tv_tapketxe_vitri);
        txtvitri.setText(xeVai_model.getViTri());
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
                ArrayList<XeVai_Model> filterList = new ArrayList<XeVai_Model>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getMaSoXe().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        XeVai_Model bean = new XeVai_Model(
                                mStringFilterList.get(i).getMaSoXe()
                                ,mStringFilterList.get(i).getLoaiXe()
                                ,mStringFilterList.get(i).getMaMe(),
                                mStringFilterList.get(i).getViTri());
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
            xeVai_modelList = (ArrayList<XeVai_Model>) results.values;
            notifyDataSetChanged();

        }
    }
}
