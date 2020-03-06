package com.it.xevai60.adapter;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.TextView;

        import com.it.xevai60.R;
        import com.it.xevai60.model.XeVai_Model;

        import java.util.ArrayList;
        import java.util.List;

public class LV_TapKetXe extends BaseAdapter implements Filterable {
    private Context context;
    private LayoutInflater inflater;

    public String masoxenull_Item="";

    private List<XeVai_Model> xeVai_modelList;
    List<XeVai_Model> mStringFilterList;
    ValueFilter valueFilter;

    public LV_TapKetXe(Context context, List<XeVai_Model> xeVai_modelList) {
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
       if (!masoxenull_Item.equals("")){
           return masoxenull_Item;
       }
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
            if (xeVai_modelList.get(i).getMaSoXe().equals("N/A")){
                view = inflater.inflate(R.layout.item_xetapketnull, null);
            }
            else {
                view = inflater.inflate(R.layout.item_xetapket, null);
            }

        }

        XeVai_Model xeVai_model = xeVai_modelList.get(i);

        TextView txtmasoxe = view.findViewById(R.id.tv_xetapket_masoxe);
        EditText edtmasoxe = view.findViewById(R.id.edt_tapketxenull_masoxe_null);
        if (!xeVai_model.getMaSoXe().equals("N/A")){
            txtmasoxe.setText(xeVai_model.getMaSoXe());

        }

        else {
            edtmasoxe.setHint("Bổ sung ms xe");
            edtmasoxe.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    masoxenull_Item=edtmasoxe.getText().toString();
                    Log.e("Masoxenull= ",masoxenull_Item);

                }
            });
        }



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
