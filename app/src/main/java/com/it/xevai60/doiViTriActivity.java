package com.it.xevai60;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.it.xevai60.adapter.LV_viTriXeVai_Adapter;
import com.it.xevai60.adapter.LV_xevai_Adapter;
import com.it.xevai60.model.ViTriXeVai_Model;
import com.it.xevai60.model.XeVai_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class doiViTriActivity extends AppCompatActivity {
    String key;
    List<XeVai_Model> xeVai_modelList = new ArrayList<XeVai_Model>();
    List<ViTriXeVai_Model> viTriXeVai_modelList = new ArrayList<ViTriXeVai_Model>();
    ListView lv_xevai,lv_vitriTK;

    LV_xevai_Adapter xevai_adapter = null;
    LV_viTriXeVai_Adapter viTriXeVai_adapter = null;
    String masoxe="",vitrimoi="",vitricu="";
    ImageButton imgbtn_doivitri_scanVitri,imgbtn_doivitri_scanXe;
    EditText edt_doivitri_sreachxe,edt_doivitri_sreachvitri;
    TextView tv_doivitri_maxe,tv_doivitri_vitriCu,tv_doivitri_vitriMoi;
    Button btn_doivitri_updatexe;
    public int control_xe=-1;
    public int control_vitri=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_vi_tri);

        imgbtn_doivitri_scanVitri = (ImageButton) findViewById(R.id.imgbtn_doivitri_scanVitri) ;
        imgbtn_doivitri_scanXe=(ImageButton) findViewById(R.id.imgbtn_doivitri_scanXe);

        lv_xevai=(ListView) findViewById(R.id.lv_doivitri_xevai);
        lv_vitriTK=(ListView) findViewById(R.id.lv_vitri);
        edt_doivitri_sreachxe = (EditText) findViewById(R.id.edt_doivitri_sreachxe);
        edt_doivitri_sreachvitri=(EditText) findViewById(R.id.edt_doivitri_sreachvitri);
        tv_doivitri_maxe = (TextView) findViewById(R.id.tv_doivitri_maxe);
        tv_doivitri_vitriCu=(TextView) findViewById(R.id.tv_doivitri_vitriCu);
        tv_doivitri_vitriMoi=(TextView) findViewById(R.id.tv_doivitri_vitriMoi);
        btn_doivitri_updatexe = (Button) findViewById(R.id.btn_doivitri_updatexe);
        loadYeuCauTapKet();
        try {
            getHttpResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadvitri();
        //timkiem
        sreach_tapket();
        //update len server
        btn_doivitri_updatexe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masoxe.equals("")){
                    thongBao("chưa chọn xe?",0);
                }else if(vitrimoi.equals("")){
                    thongBao("chưa chọn vị trí mới",0);
                }else {
                    Log.e("load_put_doivitri","masoxe "+masoxe
                            +" "+"vị trí mới "+ vitrimoi);
                    thongBao("đã thực hiên",1);
                }

            }
        });
        imgbtn_doivitri_scanVitri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control_vitri=1;
                control_xe=0;
                init();
            }
        });
        imgbtn_doivitri_scanXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control_vitri=0;
                control_xe=1;
                init();
            }
        });

    }
    public void init(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator .setPrompt("Đang đọc QR code");
        integrator.setCameraId(0);
        // beep khi scan qr thành công
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }
    public void onActivityResult(int requestCode, int resultcode, Intent intent) {
        super.onActivityResult(requestCode, resultcode, intent);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultcode, intent);
        String contents = "";
        if (result != null) {
            contents = result.getContents();

        }
        if(control_xe>0){
            edt_doivitri_sreachxe.setText(contents);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // SET RUNG 400 MILLISECONDS
            v.vibrate(400);
        }else if(control_vitri>0){
            edt_doivitri_sreachvitri.setText(contents);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // SET RUNG 400 MILLISECONDS
            v.vibrate(400);
        }

        //edt_vitriTapKet_sreach.setText(contents);
        // lấy hiệu ứng rung khi scan thành công.

    }

    private void hoanthanh_doivitri() {
//          URL: https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Put_DoiViTriXeVai
//          Phương thức: Put
//          Parameter: string maSoXe, string maViTri


        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Put_DoiViTriXeVai").newBuilder();
        urlBuilder.addQueryParameter("maSoXe", masoxe);
        urlBuilder.addQueryParameter("maViTri", vitrimoi);
        String url = urlBuilder.build().toString();


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "a");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.e("Thanhcong Put doivitri", mMessage);

                doiViTriActivity.this.finish();


            }
        });

    }
    private void sreach_tapket() {
        edt_doivitri_sreachxe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doiViTriActivity.this.xevai_adapter.getFilter().filter(s);
                //Toast.makeText(dsXeTrongActivity.this,s,Toast.LENGTH_SHORT).show();
                Log.i("Sreach_xe",s.toString());
                //load lại vi trí
                //loadvitri();
            }
        });
        edt_doivitri_sreachvitri.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doiViTriActivity.this.viTriXeVai_adapter.getFilter().filter(s);
                //Toast.makeText(dsXeTrongActivity.this,s,Toast.LENGTH_SHORT).show();
                Log.i("Sreach_vitri",s.toString());
                //load lại vị trí.
                //loadvitri();
            }
        });
    }

    private  void thongBao(String thongbao, int ht){
        AlertDialog.Builder builder = new AlertDialog.Builder(doiViTriActivity.this);

        if (ht == 1) {
            builder.setTitle("Thực hiện")
                    //.setMessage(thongbao)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                            hoanthanh_doivitri();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: No",Toast.LENGTH_SHORT).show();
                        }
                    })
            ;
        }else {
            builder.setTitle("Công Việc")
                    .setMessage(thongbao)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                            //hoanthanh_xetrong();
                        }
                    });
        }

        //Creating dialog box
        AlertDialog dialog  = builder.create();
        dialog.show();
    }
    private void loadYeuCauTapKet() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("doivitrixe");

        key=bundle.getString("Key");
        Log.e("DoiViTri",key);
    }
    public void getHttpResponse() throws IOException {
        //https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Get_DanhSachXeVai?danhSachXeVai
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Get_DanhSachXeVai").newBuilder();
        urlBuilder.addQueryParameter("danhSachXeVai", "");

        String url = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.e("GetThanhCong_Tập Kết", mMessage); //đã thành công: thấy log ghi json string là các yêu cầu về xe
                //Dữ liệu cho listview dạng danh sách hoặc mảng --> cần chuyển danh sách các đối tượng ra mảng
                try {
                    JSONObject jObject = new JSONObject(mMessage);
                    Log.e("DanhSach_XeVai", jObject.get("DanhSachXeVai").toString());
                    Log.e("DanhSach_ViTri", jObject.get("DanhSachDanhMucViTriXeVai").toString());

                    JSONArray jsonArrayXeVai = new JSONArray(jObject.get("DanhSachXeVai").toString());
                    JSONArray jsonArrayViTri = new JSONArray(jObject.get("DanhSachDanhMucViTriXeVai").toString());

                    for (int i=0;i<jsonArrayXeVai.length();i++){
                        JSONObject xeVaiDetail = jsonArrayXeVai.getJSONObject(i);
                        XeVai_Model xe = new XeVai_Model(
                                //xeVaiDetail.getString("IDYeuCau"),
                                xeVaiDetail.getString("MaSoXe"),
                                xeVaiDetail.getString("LoaiXe"),
                                xeVaiDetail.getString("MaMe"),
                                xeVaiDetail.getString("ViTriHienHanh"));
                        xeVai_modelList.add(xe);
                    }

                    for (int i=0;i<jsonArrayViTri.length();i++){
                        JSONObject viTriDetail = jsonArrayViTri.getJSONObject(i);
                        ViTriXeVai_Model vitri = new ViTriXeVai_Model(
                                viTriDetail.getString("Id"),
                                viTriDetail.getString("MaViTri"),
                                viTriDetail.getString("MoTa"),
                                viTriDetail.getString("TinhTrangSuDung"),
                                viTriDetail.getString("GhiChu")
                        );
                        viTriXeVai_modelList.add(vitri);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //load listview ở đây
                loadListVew_doivitri();
            }
        });
    }
    private void loadListVew_doivitri() {
        doiViTriActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                xevai_adapter = new LV_xevai_Adapter(doiViTriActivity.this,xeVai_modelList);
                lv_xevai.setAdapter(xevai_adapter);

//                test
                int count = xevai_adapter.getCount();
                Log.e("soItemLV_XeTapKet", "Số lượng: " + String.valueOf(count));
//
                viTriXeVai_adapter = new LV_viTriXeVai_Adapter(doiViTriActivity.this,viTriXeVai_modelList);
                lv_vitriTK.setAdapter(viTriXeVai_adapter);
                //nhan cho item trong danh sachs vị trí

            }
        });
    }
    private void loadvitri() {
        lv_xevai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //lấy vị trí hiện tại của listView Yeucauxe;
//                vitrixe = (int)xevai_adapter.getItem(position);
//                Log.e("ChonVitri",vitri+"");
                XeVai_Model vitrixevai = (XeVai_Model) xevai_adapter.getItem(position);
                masoxe=vitrixevai.getMaSoXe();
                vitricu=vitrixevai.getViTri();
                Log.e("Xe",vitrixevai.getMaSoXe()+" "+vitrixevai.getViTri());

                tv_doivitri_maxe.setText(masoxe);
                tv_doivitri_vitriCu.setText(vitricu);
            }
        });
        lv_vitriTK.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                vitri = (int)xevai_adapter.getItem(position);
//                Log.e("ChonVitri",vitri+"");

                ViTriXeVai_Model vitri = (ViTriXeVai_Model) viTriXeVai_adapter.getItem(position);
                vitrimoi=vitri.getMaVitri();
                Log.e("vitri",vitri.getMaVitri());

                tv_doivitri_vitriMoi.setText(vitrimoi);
            }
        });
    }

}
