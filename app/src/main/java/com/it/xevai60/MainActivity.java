package com.it.xevai60;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.it.xevai60.adapter.LV_YeuCauXe_Adapter;
import com.it.xevai60.model.YeuCauXe;
import com.it.xevai60.network.APIService;
import com.it.xevai60.network.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {
    private final String TAG="okYeuCauXe";

    //load api
    private APIService mAPIService;

    String key="";
    ListView listView_YeuCauXe;
    //khai báo mảng
    ArrayList<String> idArray = new ArrayList<>();
    ArrayList<String> loaiXeArray = new ArrayList<>();
    ArrayList<String> loaiYeuCaunArray = new ArrayList<>();
    ArrayList<String> maMeArray = new ArrayList<>();
    ArrayList<String> maSoXeArray = new ArrayList<>();
    ArrayList<String> nguoiYeuCauArray = new ArrayList<>();
    ArrayList<String> thoiGianCanArray = new ArrayList<>();
    ArrayList<String> viTriCanArray = new ArrayList<>();
    LV_YeuCauXe_Adapter adapter_lv = null; //new LV_YeuCauXe_Adapter(MainActivity.this,idArray,loaiXeArray,loaiYeuCaunArray,maMeArray,maSoXeArray,nguoiYeuCauArray,thoiGianCanArray,viTriCanArray);
    int vitri =-1; // Vi tri chon tren man hinh
    Button btn_yeucauxe_doixe;

    //auto referch
    SwipeRefreshLayout swipeLayout;
    Handler mHandler; //tự động load lại dữ liệu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //-----api web api--------//----------
        mAPIService = ApiUtils.getAPIService();

        Log.d("MainActivity","onCreate MainActivity");
        // Get token đã lưu khi đăng nhập !
        Intent intent = getIntent();
        key = intent.getStringExtra("TokenString");
        Log.e("nhan key LoginActivity",key);

        //key = LoginActivity.API_KEY;

        listView_YeuCauXe = (ListView) findViewById(R.id.listView_YeuCauXe);
        btn_yeucauxe_doixe=(Button) findViewById(R.id.btn_yeucauxe_doixe);
        //Tải dử liệu từ API về đổ vào các Array
//        try {
//            getHttpResponse();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        getYeuCauXe();
        btn_yeucauxe_doixe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_doixe = new Intent(MainActivity.this,doiViTriActivity.class);
                Bundle bundle = new Bundle();

                // đóng gói kiểu dữ liệu

                bundle.putString("Key",key);

                intent_doixe.putExtra("doivitrixe",bundle);
                startActivity(intent_doixe);
            }
        });

        //auto refrech============bằng tay=============

        swipeLayout = findViewById(R.id.swipe_container);
        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                //Toast.makeText(getApplicationContext(), "Works!", Toast.LENGTH_LONG).show();
                //load lai listview
                Clear_dataListView();
                getYeuCauXe();
                // To keep animation for 3 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 3000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        //end auto refrech=====bằng tay
        //load lại dữ liệu theo thời gian
        this.mHandler = new Handler();
        m_Runnable.run();
        //ket thúc load
    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()
        {
            Clear_dataListView();
            getYeuCauXe();
            //Toast.makeText(MainActivity.this,"in runnable",Toast.LENGTH_SHORT).show();
            //2 phut load lại 1 lần
            MainActivity.this.mHandler.postDelayed(m_Runnable,120000);
        }

    };
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","onStart MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","onResume MainActivity");

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","onPause MainActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity","onStop MainActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity","onRestart MainActivity");
        //xoa toàn bộ data array củ cập nhật mới
        Clear_dataListView();
        getYeuCauXe();
//        try {
//            getHttpResponse();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity","onDestroy MainActivity");
    }

    private void Clear_dataListView(){
        idArray.clear();
        loaiXeArray.clear();
        loaiYeuCaunArray.clear();
        maMeArray.clear();
        maSoXeArray.clear();
        nguoiYeuCauArray.clear();
        thoiGianCanArray.clear();
        viTriCanArray.clear();
    }
    private void loadListVew_YeuCauXe() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Handle UI here
                //đưa vào ListView
                //https://stackoverflow.com/questions/40633639/getting-the-correct-context-in-an-adapter-and-fragment
                //final LV_YeuCauXe_Adapter adapter = new LV_YeuCauXe_Adapter(MainActivity.this,idArray,loaiXeArray,loaiYeuCaunArray,maMeArray,maSoXeArray,nguoiYeuCauArray,thoiGianCanArray,viTriCanArray);


                adapter_lv = new LV_YeuCauXe_Adapter(MainActivity.this,idArray,loaiXeArray,loaiYeuCaunArray,maMeArray,maSoXeArray,nguoiYeuCauArray,thoiGianCanArray,viTriCanArray);

                listView_YeuCauXe.setAdapter(adapter_lv);



                //test
                int count = adapter_lv.getCount();
                Log.e("soItemLV", "Số lượng: " + String.valueOf(count));

                //lấy giá trị vị trí hiện tại
                listView_YeuCauXe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //lấy vị trí hiện tại của listView Yeucauxe;
                        vitri = (int)adapter_lv.getItem(position);
                        Log.e("VitriChonListView",vitri+"");

                        //kiểm tra loại yeu cầu chuyển sang man hình của yêu câu đó
                        loadChiTiet_YeuCau();


                    }
                });
            }
        });
    }

    private void loadChiTiet_YeuCau() {
        String check_yeucau =loaiYeuCaunArray.get(vitri);

        switch (check_yeucau) {
            case "XeTrong" :
                Log.e("ActivityXeTrong",check_yeucau);
                Intent intent = new Intent(MainActivity.this,dsXeTrongActivity.class);

                //truyền biến qua Activity xe trong
                Bundle bundle = new Bundle();

                // đóng gói kiểu dữ liệu
                bundle.putString("ID",idArray.get(vitri));
                bundle.putString("LoaiXe",loaiXeArray.get(vitri));
                bundle.putString("Key",key);

                intent.putExtra("ttyeucau",bundle);
                startActivity(intent);
                break;

            case "Me" :
                Log.e("Activity_Me",check_yeucau);
                Intent intent1 = new Intent(MainActivity.this,dsXeCoMeActivity.class);

                //truyền biến qua Activity xe me
                Bundle bundle1 = new Bundle();

                // đóng gói kiểu dữ liệu
                bundle1.putString("ID",idArray.get(vitri));
                bundle1.putString("MaMe",maMeArray.get(vitri));
                bundle1.putString("Key",key);

                intent1.putExtra("ttyeucau",bundle1);
                startActivity(intent1);
                break;
            case "TapKetMe":
                Log.e("Activity_tapket",check_yeucau);
                Intent intent2 = new Intent(MainActivity.this,tapKetActivity.class);

                //truyền biến qua Activity xe me
                Bundle bundle2 = new Bundle();

                // đóng gói kiểu dữ liệu
                bundle2.putString("ID",idArray.get(vitri));
                bundle2.putString("MaMe",maMeArray.get(vitri));
                bundle2.putString("Key",key);

                intent2.putExtra("tapket",bundle2);
                startActivity(intent2);
                break;
            default:
                break;
        }

//        if(check_yeucau.equals("XeTrong")){ //yeu cầu xe trống
//            Log.e("ActivityXeTrong",check_yeucau);
//            Intent intent = new Intent(MainActivity.this,dsXeTrongActivity.class);
//
//            //truyền biến qua Activity xe trong
//            Bundle bundle = new Bundle();
//
//            // đóng gói kiểu dữ liệu
//            bundle.putString("ID",idArray.get(vitri));
//            bundle.putString("LoaiXe",loaiXeArray.get(vitri));
//            bundle.putString("Key",key);
//
//            intent.putExtra("ttyeucau",bundle);
//            startActivity(intent);
//        }else if(check_yeucau.equals("Me")){ //yeu cau xe có mẻ
//            Log.e("ActivityMe",check_yeucau);
//            Intent intent = new Intent(MainActivity.this,dsXeCoMeActivity.class);
//
//            //truyền biến qua Activity xe me
//            Bundle bundle = new Bundle();
//
//            // đóng gói kiểu dữ liệu
//            bundle.putString("ID",idArray.get(vitri));
//            bundle.putString("MaMe",maMeArray.get(vitri));
//            bundle.putString("Key",key);
//
//            intent.putExtra("ttyeucau",bundle);
//            startActivity(intent);
//        }
    }

    public void getHttpResponse() throws IOException {
        //tạo url có parameter
        //String url = "https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Get_YeuCauXe";

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://local.thttextile.com.vn/thtapigate/api/QLSX/XeVai/Get_YeuCauXeVai").newBuilder();
        urlBuilder.addQueryParameter("yeucauxevai", "");
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
                Log.e("GetThanhCong", mMessage); //đã thành công: thấy log ghi json string là các yêu cầu về xe
                //Dữ liệu cho listview dạng danh sách hoặc mảng --> cần chuyển danh sách các đối tượng ra mảng

                try {
                    JSONArray jArray = new JSONArray(mMessage);

                    for (int i=0;i< jArray.length();i++){
                        JSONObject yeuCauXeDetail = jArray.getJSONObject(i);

                        idArray.add(yeuCauXeDetail.getString("Id"));
                        loaiXeArray.add(yeuCauXeDetail.getString("LoaiXe"));
                        loaiYeuCaunArray.add(yeuCauXeDetail.getString("LoaiYeuCau"));
                        maMeArray.add(yeuCauXeDetail.getString("MaMe"));
                        maSoXeArray.add(yeuCauXeDetail.getString("MaSoXe"));
                        nguoiYeuCauArray.add(yeuCauXeDetail.getString("NguoiYeuCau"));
                        thoiGianCanArray.add(yeuCauXeDetail.getString("ThoiGianCan"));
                        viTriCanArray.add(yeuCauXeDetail.getString("ViTriCan"));

                    }
                    //quay lại main thread

                    loadListVew_YeuCauXe();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getYeuCauXe(){
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("Authorization","Bearer "+key);
        mAPIService.getYeuCauXe(headers).enqueue(new retrofit2.Callback<List<YeuCauXe>>() {
            @Override
            public void onResponse(retrofit2.Call<List<YeuCauXe>> call, retrofit2.Response<List<YeuCauXe>> response) {
                if (response.code()==200){

                    ArrayList<YeuCauXe> yeuCauXeArrayList = (ArrayList<YeuCauXe>) response.body();
                    for (int i=0;i<yeuCauXeArrayList.size();i++){

                        YeuCauXe yeuCauXe = yeuCauXeArrayList.get(i);

                        idArray.add(yeuCauXe.getId());
                        loaiXeArray.add(yeuCauXe.getLoaiXe());
                        loaiYeuCaunArray.add(yeuCauXe.getLoaiYeuCau());
                        maMeArray.add(yeuCauXe.getMaMe());
                        maSoXeArray.add(yeuCauXe.getMaSoXe());
                        nguoiYeuCauArray.add(yeuCauXe.getNguoiYeuCau());
                        thoiGianCanArray.add(yeuCauXe.getThoiGianCan());
                        viTriCanArray.add(yeuCauXe.getViTriCan());


                    }
                    loadListVew_YeuCauXe();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<YeuCauXe>> call, Throwable t) {
                Log.i(TAG,"okGet yeu that bai"+t);
                //Log.i(TAG,key);
            }
        });

    }

}
