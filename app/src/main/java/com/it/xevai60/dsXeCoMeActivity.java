package com.it.xevai60;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.it.xevai60.adapter.LV_dsXeCoMe_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class dsXeCoMeActivity extends AppCompatActivity {
    String idyeuCau,maMe,key;

    ArrayList<String> idYeuCauArray = new ArrayList<>();
    ArrayList<String> maSoXeArray = new ArrayList<>();
    ArrayList<String> loaiXeArray = new ArrayList<>();
    ArrayList<String> viTriArray = new ArrayList<>();
    ArrayList<String> maMeArray = new ArrayList<>();

    ListView lv_dsXeCoMe;
    EditText edtinputdsMasoxe;
    Button btnHoanThanh;

    String dsmasoxe = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_xe_co_me);
        loadThongTin_XeMe();
        edtinputdsMasoxe =(EditText) findViewById(R.id.edt_dsXeCoMe_thongbao);
        btnHoanThanh = (Button) findViewById(R.id.button_dsxecome_ht);
        lv_dsXeCoMe= (ListView) findViewById(R.id.lv_dsXeCoMe);

        Log.e("LoadTT_Xe_co_me",idyeuCau+" "+maMe+" "+key);

        try {
            getHttpResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //edtinputdsMasoxe.setVisibility(View.VISIBLE);



        //nút nhấn hoàn thành

        btnHoanThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maSoXeArray.get(0).equals("N/A")){
                    dsmasoxe=edtinputdsMasoxe.getText().toString();
                }
                //dsmasoxe = maSoXeArray.toString();
                if (dsmasoxe.equals("")){
                    for(int i=0;i < maSoXeArray.size();i++)
                    {
                        if(i==0){
                            dsmasoxe =maSoXeArray.get(i);
                        }else {
                            dsmasoxe += ","+maSoXeArray.get(i);
                        }
                    }
                }

//              //day no len WEB API, 200 BAO THANH CONG TRỞ VỀ MÀN HÌNH BAN ĐẦU

                Log.e("PutYeuCauMe",idyeuCau+" "+dsmasoxe);
               if (dsmasoxe.equals("N/A")){
                   thongBaoError("Chưa điền thông tin mã xe",0);
               }
               else {
                   thongBao();
               }

            }
        });

    }
    private  void thongBaoError(String thongbao, int ht){
        AlertDialog.Builder builder = new AlertDialog.Builder(dsXeCoMeActivity.this);

        if (ht == 1) {
            builder.setTitle("Thông Báo")
                    //.setMessage(thongbao)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();

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
    private void hoanthanh_xeme() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Put_HoanThanhYeuCauMe").newBuilder();
        urlBuilder.addQueryParameter("IDYeuCau", idyeuCau);
        urlBuilder.addQueryParameter("dsMaSoXe", dsmasoxe);
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
                Log.e("Thanhcong Put xe", mMessage);
                if (mMessage.contains("ErrorXeVai"))
                {
                    dsXeCoMeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            thongBaoError("Kiểm tra lại mã số xe ??",0);
                        }
                    });
                }
                else {
                    dsXeCoMeActivity.this.finish();
                }

            }
        });

    }

    private void loadThongTin_XeMe() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ttyeucau");
        idyeuCau = bundle.getString("ID");
        maMe=bundle.getString("MaMe");
        key=bundle.getString("Key");
    }
    public void getHttpResponse() throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Get_XeChuaMe").newBuilder();
        urlBuilder.addQueryParameter("idYeuCau", idyeuCau);
        urlBuilder.addQueryParameter("maMe", maMe);
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
                loadListVew_dsXecome();
            }
        });
    }
    private void loadListVew_dsXecome() {
        dsXeCoMeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Handle UI here
                //đưa vào ListView
                //https://stackoverflow.com/questions/40633639/getting-the-correct-context-in-an-adapter-and-fragment
                final LV_dsXeCoMe_Adapter adapter = new LV_dsXeCoMe_Adapter(dsXeCoMeActivity.this,idYeuCauArray,maSoXeArray,loaiXeArray,maMeArray,viTriArray);
                lv_dsXeCoMe.setAdapter(adapter);

                //test
                int count = adapter.getCount();
                Log.e("soItemLV", "Số lượng: " + String.valueOf(count));

                //nếu n/a thì phải hiển thị lên nhập tay
                if (maSoXeArray.get(0).equals("N/A")){
                   edtinputdsMasoxe.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private  void thongBao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(dsXeCoMeActivity.this);
        builder.setTitle("Thực hiện")
                //.setMessage("Đã hoàn thành")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                        hoanthanh_xeme();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: No",Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog dialog  = builder.create();
        dialog.show();
    }
}
