package com.it.xevai60.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.it.xevai60.LoginActivity;
import com.it.xevai60.R;
import com.it.xevai60.model.dsXeTrongModel;

import java.util.ArrayList;
import java.util.List;

public class LV_dsXeTrong_Adapter extends BaseAdapter implements Filterable {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private Context context;
    private LayoutInflater inflater;

    private List<dsXeTrongModel> dsXeTrongModelList;
    List<dsXeTrongModel> mStringFilterList;
    ValueFilter valueFilter;

    public LV_dsXeTrong_Adapter(Context context, List<dsXeTrongModel> dsXeTrongModelList) {
        this.context = context;
        this.dsXeTrongModelList = dsXeTrongModelList;
        mStringFilterList = dsXeTrongModelList;
    }

    @Override
    public int getCount() {
        return dsXeTrongModelList.size();
    }

    @Override
    public Object getItem(int position) {

        return dsXeTrongModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.item_ds_xetrong, null);
        }

        dsXeTrongModel xeTrongModel = dsXeTrongModelList.get(i);

        TextView txtmasoxe = view.findViewById(R.id.tv_dsxetrong_masoxe);
        txtmasoxe.setText(xeTrongModel.getMaSoXe());

        TextView txtloaixe = view.findViewById(R.id.tv_dsxetrong_loaixe);
        txtloaixe.setText(xeTrongModel.getLoaiXe());

        TextView txtvitri = view.findViewById(R.id.tv_dsxetrong_vitri);
        txtvitri.setText(xeTrongModel.getViTri());

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
                ArrayList<dsXeTrongModel> filterList = new ArrayList<dsXeTrongModel>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getMaSoXe().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        dsXeTrongModel bean = new dsXeTrongModel(
                                mStringFilterList.get(i).getIdYeuCau(),
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
            dsXeTrongModelList = (ArrayList<dsXeTrongModel>) results.values;
            notifyDataSetChanged();

        }
    }
}
