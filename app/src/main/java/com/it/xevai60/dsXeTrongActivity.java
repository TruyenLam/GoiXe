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
import com.it.xevai60.adapter.LV_dsXeTrong_Adapter;
import com.it.xevai60.model.dsXeTrongModel;

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

public class dsXeTrongActivity extends AppCompatActivity {
    String key,idyeuCau,loaiXe;
    TextView txtMaSoXe,txtLoaiXe,txtViTri,txtThongbao;
    Button btn_xetrong_ht;
    ArrayList<String> idYeuCauArray = new ArrayList<>();
    ArrayList<String> maSoXeArray = new ArrayList<>();
    ArrayList<String> loaiXeArray = new ArrayList<>();
    ArrayList<String> viTriArray = new ArrayList<>();
    ArrayList<String> maMeArray = new ArrayList<>();
    //int vitri=-1;
    ListView lv_dsXeTrong;
    EditText edt_dsXeTrong_sreach;
    private LV_dsXeTrong_Adapter adapter=null;
    ImageButton imgbtn_dsxetrong_Scan;
    List<dsXeTrongModel> dsXeTrongModelList;
    String masoxe="",loaixe="",vitri="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_xe_trong);
        loadThongTin_YeuCau();

        imgbtn_dsxetrong_Scan=(ImageButton) findViewById(R.id.imgbtn_dsxetrong_Scan);
        lv_dsXeTrong=(ListView) findViewById(R.id.listView_dsXeTrong);
        txtMaSoXe = (TextView) findViewById(R.id.tv_dsxetrong_masoxe);
        txtLoaiXe = (TextView) findViewById(R.id.tv_dsxetrong_loaixe);
        txtViTri  = (TextView) findViewById(R.id.tv_dsxetrong_vitri);
        txtThongbao=(TextView) findViewById(R.id.tv_dsxetrong_thongbao);
        btn_xetrong_ht=(Button) findViewById(R.id.button_dsxetrong);
        edt_dsXeTrong_sreach=(EditText) findViewById(R.id.edt_dsXeTrong_sreach);
        Log.e("Nhan_Thông_Tin_YeuCau",idyeuCau+" "+loaiXe);
        try {
            getHttpResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sreach_xeTrong();
        btn_xetrong_ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vitri.equals(""))
                {
                    txtThongbao.setText("Bạn chưa chọn xe");
                    //Toast.makeText(dsXeTrongActivity.this,"Bạn chưa chọn xe",Toast.LENGTH_SHORT).show();
                    thongBao("Bạn chưa chọn xe",0);
                }else {
                    Log.e("HoanThanhXeTrong",idyeuCau+" "+masoxe+""+vitri);
                    thongBao("Đã hoàn thành",1);

                }
            }
        });

        //scan QA
        imgbtn_dsxetrong_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

    }
    public void init(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
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

            edt_dsXeTrong_sreach.setText(contents);
            // lấy hiệu ứng rung khi scan thành công.
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // SET RUNG 400 MILLISECONDS
            v.vibrate(400);
        }
    }
    private void sreach_xeTrong() {
        edt_dsXeTrong_sreach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dsXeTrongActivity.this.adapter.getFilter().filter(s);
                //Toast.makeText(dsXeTrongActivity.this,s,Toast.LENGTH_SHORT).show();
                Log.i("Sreach_xeTrong",s.toString());
            }
        });
    }

    private  void thongBao(String thongbao, int ht){
        AlertDialog.Builder builder = new AlertDialog.Builder(dsXeTrongActivity.this);

        if (ht == 1) {
            builder.setTitle("Thực hiện")
                    //.setMessage(thongbao)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                            hoanthanh_xetrong();
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
            builder.setTitle("Thực hiện")
                    //.setMessage(thongbao)
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
    private void hoanthanh_xetrong() {
//        URL: https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Put_HoanThanhYeuCauXeTrong
//        Phương thức: Put
//        Parameter: int idYeuCau, string maSoXe

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Put_HoanThanhYeuCauMe").newBuilder();
        urlBuilder.addQueryParameter("IDYeuCau", idyeuCau);
        urlBuilder.addQueryParameter("maSoXe", masoxe);
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
                Log.e("Thanhcong Put xe trong", mMessage);

                dsXeTrongActivity.this.finish();


            }
        });

    }
    private void loadThongTin_YeuCau() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ttyeucau");
        idyeuCau = bundle.getString("ID");
        loaiXe=bundle.getString("LoaiXe");
        key=bundle.getString("Key");
    }
    public void getHttpResponse() throws IOException {
//        URL: https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Get_XeTrong
//        Phương thức: Get
//        Parameter: int idYeuCau, string loaiXe

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Get_XeTrong").newBuilder();
        urlBuilder.addQueryParameter("idYeuCau", idyeuCau);
        urlBuilder.addQueryParameter("loaiXe", loaiXe);
        String url = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .build();

//        Response response = client.newCall(request).execute();
//        Log.e(TAG, response.body().string());

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
                Log.e("GetThanhCong_XeTrong", mMessage); //đã thành công: thấy log ghi json string là các yêu cầu về xe
                //Dữ liệu cho listview dạng danh sách hoặc mảng --> cần chuyển danh sách các đối tượng ra mảng
                try {
                    JSONArray jArray = new JSONArray(mMessage);
                    for (int i=0;i< jArray.length();i++) {
                        JSONObject yeuCauXeDetail = jArray.getJSONObject(i);

                        idYeuCauArray.add(yeuCauXeDetail.getString("IDYeuCau"));
                        maSoXeArray.add(yeuCauXeDetail.getString("MaSoXe"));
                        loaiXeArray.add(yeuCauXeDetail.getString("LoaiXe"));
                        maMeArray.add(yeuCauXeDetail.getString("MaMe"));
                        viTriArray.add(yeuCauXeDetail.getString("ViTriHienHanh"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadListVew_dsXeTrong();
            }
        });
    }
    private void loadListVew_dsXeTrong() {
        dsXeTrongActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Handle UI here
                //đưa vào ListView
                //https://stackoverflow.com/questions/40633639/getting-the-correct-context-in-an-adapter-and-fragment

                dsXeTrongModelList = new ArrayList<dsXeTrongModel>();
                for (int i = 0; i <idYeuCauArray.size() ; i++) {
                    dsXeTrongModel dsXeTrongModel = new dsXeTrongModel(idYeuCauArray.get(i),maSoXeArray.get(i),loaiXeArray.get(i),maMeArray.get(i),viTriArray.get(i));
                    dsXeTrongModelList.add(dsXeTrongModel);
                }


                adapter = new LV_dsXeTrong_Adapter(dsXeTrongActivity.this,dsXeTrongModelList);
                lv_dsXeTrong.setAdapter(adapter);

                //test
                int count = adapter.getCount();
                Log.e("soItemLV", "Số lượng: " + String.valueOf(count));

                //lấy giá trị vị trí hiện tại
                lv_dsXeTrong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //lấy vị trí hiện tại của listView Yeucauxe;
                        dsXeTrongModel xeTrongModel = (dsXeTrongModel) adapter.getItem(position);
                        masoxe=xeTrongModel.getMaSoXe();
                        loaiXe=xeTrongModel.getLoaiXe();
                        vitri=xeTrongModel.getViTri();
                        Log.e("VitriChonListView",vitri+"");

                        //đẩy dữ liệu lên ô chọn loại xe
                        txtMaSoXe.setText(masoxe);
                        txtLoaiXe.setText(loaiXe);
                        txtViTri.setText(vitri);

                        //put lên server-------------->

                    }
                });
            }
        });
    }
}
