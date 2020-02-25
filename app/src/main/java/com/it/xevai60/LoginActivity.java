package com.it.xevai60;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.it.xevai60.model.Token;
import com.it.xevai60.model.User;
import com.it.xevai60.network.APIService;
import com.it.xevai60.network.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {
    TextView edtuesr,edtpass;
    TextView btnLogin;
    //ProgressBar loading;
    String user="",pass="";
    //public static final String TAG = LoginActivity.class.getSimpleName();

    public static String API_KEY = "";

    //load api
    private APIService mAPIService;

    private final String TAG="okPost";
    ProgressDialog mypDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //-----api web api--------//----------
        mAPIService = ApiUtils.getAPIService();

        edtuesr = (EditText) findViewById(R.id.editTextUser);
        edtpass= (EditText) findViewById(R.id.editTextPass);
        btnLogin = (TextView) findViewById(R.id.buttonLogin);
        //loading = (ProgressBar) findViewById(R.id.pBar);

        mypDialog = new ProgressDialog(LoginActivity.this);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mypDialog.setMessage("đang kiểm tra ...");
        mypDialog.setCanceledOnTouchOutside(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = edtuesr.getText().toString();
                pass = edtpass.getText().toString();
                mypDialog.show();
                if (user.equals("")||pass.equals("")){
                    thongBao("Vui lòng nhập đầy đủ thông tin tài khoản và mật khẩu",0);
                }
                else {
                    //loading.setVisibility(View.VISIBLE);
//
//                    try {
//                        postRequest();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    postUser();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //loading.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private  void thongBao(String thongbao, int ht){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        if (ht == 1) {
            builder.setTitle("Thông Báo")
                    .setMessage(thongbao)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(dsXeCoMeActivity.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                            //chay đang nhập;

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
            builder.setTitle("Thông Báo")
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
//    private void addtoken() {
//        SharedPreferences sharedpreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//
//        editor.clear();
//
//        editor.putString("token", API_KEY);
//
//        editor.commit();
//        //
//    }
//    public void postRequest() throws IOException {
//        MediaType MEDIA_TYPE = MediaType.parse("application/json");
//        String url = "https://local.thttextile.com.vn/thtapigate/api/login/login";
//
//        OkHttpClient client = new OkHttpClient();
//
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("UserId", user);
//            postdata.put("PassWord", pass);
//        } catch(JSONException e){
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
//
//        final Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                String mMessage = e.getMessage().toString();
//                Log.w("failure Response", mMessage);
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                final String mMessage = response.body().string();
//                Log.e(TAG, mMessage); //chỗ này ra: {"TokenString":"..."} --> cần parse ra object, không phải array
//
//                if(mMessage.contains("không đúng")){
//                    LoginActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            loading.setVisibility(View.INVISIBLE);
//                            thongBao("Tên hoặc mật khẩu không đúng",0);
//
//                        }
//                    });
//                }
//
//                //đã test ok - có TokenString
//                //kiểm tra response code
//                if(response.code() == 200){
//                    //chuẩn bị truyền intent
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    //truyền thêm ToKenString
//                    try{
//                        //lệnh dưới OK, tuy nhiên muốn lấy trực tiếp response, vốn đã là JSONObject rồi
//                        //kq: không cast response.body() ra JSONObject được !!!!
//                        JSONObject jObj = new JSONObject(mMessage);
//                        String token = jObj.getString("TokenString");
//                        //Truyền Token sang man hinh mới
//                        intent.putExtra("TokenString", token);
//
//                        API_KEY = token;
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    //thực hiện
//                    startActivity(intent);
//                }
//            }
//        });
//    }
    public void postUser(){
        mAPIService.postUser(new User(user,pass)).enqueue(new retrofit2.Callback<Token>() {
            @Override
            public void onResponse(retrofit2.Call<Token> call, retrofit2.Response<Token> response) {
                //loading.setVisibility(View.INVISIBLE);
                mypDialog.cancel();
                if (response.code() ==200){
                    Token token = response.body();
                    API_KEY=token.getTokenString();
                    Log.i(TAG,API_KEY);

                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //test dsyeucau moi
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //truyền thêm ToKenStrinG
                    intent.putExtra("TokenString", API_KEY);
                    startActivity(intent);
                }
                else {
                    thongBao("Tài khoản mật khẩu không đúng",0);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Token> call, Throwable t) {
                Log.i(TAG,"post that bai");
                thongBao("Kiểm tra lại mạng",0);
                mypDialog.cancel();
            }
        });
    }

}
