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

public class tapKetActivity extends AppCompatActivity {
    String key,idYeuCau,maMeYeucau;
    List<XeVai_Model> xeVai_modelList = new ArrayList<XeVai_Model>();
    List<ViTriXeVai_Model> viTriXeVai_modelList = new ArrayList<ViTriXeVai_Model>();
    ImageButton imgbtn_tapket_Scan;

    ListView lv_xetapket,lv_vitriTK;

    TextView txtmaViTri,txtmoTa,edt_vitriTapKet_sreach;
    Button btn_tapketxe_hoanthanh;

    LV_xevai_Adapter xevai_adapter = null;
    LV_viTriXeVai_Adapter viTriXeVai_adapter = null;
    String dsMaSoXe="",maViTri="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_ket);
        imgbtn_tapket_Scan =(ImageButton) findViewById(R.id.imgbtn_tapket_Scan);
        lv_vitriTK = (ListView) findViewById(R.id.lv_vitrixe_tapket);
        lv_xetapket = (ListView) findViewById(R.id.lv_xetapket);
        txtmaViTri = (TextView) findViewById(R.id.tv_tapket_mavitri) ;
        txtmoTa = (TextView) findViewById(R.id.tv_tapket_mota) ;
        edt_vitriTapKet_sreach = (EditText) findViewById(R.id.edt_vitriTapKet_sreach);
        btn_tapketxe_hoanthanh =(Button) findViewById(R.id.btn_tapketxe_hoanthanh);
        loadYeuCauTapKet();

        try {
            getHttpResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sreach_tapket();

        btn_tapketxe_hoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maViTri.equals(""))
                {

                    //Toast.makeText(dsXeTrongActivity.this,"Bạn chưa chọn xe",Toast.LENGTH_SHORT).show();
                    thongBao("Bạn chưa chọn vị trí",0);
                }else {
                    Log.e("HoanThanhXeTrong","nhấn nút hoan thành");
                    thongBao("Đã hoàn thành",1);

                }
            }
        });

        imgbtn_tapket_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init();
                new IntentIntegrator(tapKetActivity.this).initiateScan();
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
        if (result != null) {
            String contents = result.getContents();

            edt_vitriTapKet_sreach.setText(contents);
            // lấy hiệu ứng rung khi scan thành công.
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // SET RUNG 400 MILLISECONDS
            v.vibrate(400);
        }
    }
    private void sreach_tapket() {
        edt_vitriTapKet_sreach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tapKetActivity.this.viTriXeVai_adapter.getFilter().filter(s);
                //Toast.makeText(dsXeTrongActivity.this,s,Toast.LENGTH_SHORT).show();
                Log.i("Sreach_xeTrong",s.toString());
            }
        });
    }
    private void hoanthanh_tapket() {
//        https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Put_HoanThanhTapKetMe
//        Phương thức: Put
//        Parameter: string idYeuCau, string dsMaSoXe, string maViTri



        for (int i=0;i<xeVai_modelList.size();i++)
        {
            dsMaSoXe +=xeVai_modelList.get(i).getMaSoXe();
        }
        Log.e("dsMaSoXe", dsMaSoXe);

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Put_HoanThanhTapKetMe").newBuilder();
        urlBuilder.addQueryParameter("IDYeuCau", idYeuCau);
        urlBuilder.addQueryParameter("dsMaSoXe", dsMaSoXe);
        urlBuilder.addQueryParameter("maViTri", maViTri);
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
                Log.e("Thanhcong Put tapket", mMessage);

                tapKetActivity.this.finish();


            }
        });

    }

    private  void thongBao(String thongbao, int ht){
        AlertDialog.Builder builder = new AlertDialog.Builder(tapKetActivity.this);

        if (ht == 1) {
            builder.setTitle("Thực hiện")
                    //.setMessage(thongbao)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                            hoanthanh_tapket();
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
    public void getHttpResponse() throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Get_TapKetMe").newBuilder();
        urlBuilder.addQueryParameter("idYeuCau", idYeuCau);
        urlBuilder.addQueryParameter("maMeCanTapKet", maMeYeucau);
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
                loadListVew_dsXeTrong();
            }
        });
    }
    private void loadListVew_dsXeTrong() {
        tapKetActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                xevai_adapter = new LV_xevai_Adapter(tapKetActivity.this,xeVai_modelList);
                lv_xetapket.setAdapter(xevai_adapter);

//                test
                int count = xevai_adapter.getCount();
                Log.e("soItemLV_XeTapKet", "Số lượng: " + String.valueOf(count));

                viTriXeVai_adapter = new LV_viTriXeVai_Adapter(tapKetActivity.this,viTriXeVai_modelList);
                lv_vitriTK.setAdapter(viTriXeVai_adapter);
                //nhan cho item trong danh sachs vị trí
                lv_vitriTK.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //lấy vị trí hiện tại của listView Yeucauxe;
//                        vitri = (int)viTriXeVai_adapter.getItem(position);
//                        Log.e("ChonVitri",vitri+"");
                        ViTriXeVai_Model vitri = (ViTriXeVai_Model) viTriXeVai_adapter.getItem(position);
                        maViTri=vitri.getMaVitri();
                        Log.e("ChonVitri",maViTri+"");

                        txtmaViTri.setText(vitri.getMaVitri());
                        txtmoTa.setText(vitri.getMoTa());

                    }
                });

            }
        });
    }
    private void loadYeuCauTapKet() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("tapket");
        idYeuCau = bundle.getString("ID");
        maMeYeucau=bundle.getString("MaMe");
        key=bundle.getString("Key");
        Log.e("TapKetMe",idYeuCau +" "+maMeYeucau+" "+key);
    }
}
