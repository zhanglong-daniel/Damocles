package com.damocles.sample;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.baidu.naviauto.R;
import com.damocles.android.network.loader.ImageLoader;
import com.damocles.android.network.loader.JsonLoader;
import com.damocles.android.network.loader.StringLoader;
import com.damocles.android.wechat.WechatAPI;
import com.damocles.android.wechat.WechatUserInfo;
import com.damocles.sample.util.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WechatService extends AppCompatActivity implements View.OnClickListener {

    private TextView mUrlTextView;
    private TextView mResultTextView;
    private Button mLoginBtn;
    private Button mContactBtn;
    private Button mSendBtn;

    private ImageView mQrcodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);
        Utils.initToolbar(this, R.id.wechat_toolbar);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initViews() {
        mUrlTextView = (TextView) findViewById(R.id.wechat_txt_url);
        mResultTextView = (TextView) findViewById(R.id.wechat_txt_result);
        mLoginBtn = (Button) findViewById(R.id.wechat_btn_login);
        mLoginBtn.setOnClickListener(this);
        mContactBtn = (Button) findViewById(R.id.wechat_btn_contact);
        mContactBtn.setOnClickListener(this);
        mContactBtn.setVisibility(View.GONE);
        mSendBtn = (Button) findViewById(R.id.wechat_btn_send);
        mSendBtn.setOnClickListener(this);
        mSendBtn.setVisibility(View.GONE);
        mQrcodeImageView = (ImageView) findViewById(R.id.wechat_image_qrcode);
        mQrcodeImageView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechat_btn_login:
                jsLogin();
                break;
            case R.id.wechat_btn_contact:
                startActivity(new Intent(this, WechatContactActivity.class));
                break;
            case R.id.wechat_btn_send:
                break;
        }
    }

    private void jsLogin() {
        StringLoader.Callback cb = new StringLoader.Callback() {
            @Override
            public void onSuccess(String result) {
                mResultTextView.setText("success:\n" + result);
                Pattern pattern =
                        Pattern.compile("window.QRLogin.code = ([0-9]+); window.QRLogin.uuid = \"([0-9a-zA-Z_\\-]+==)"
                                + "\";");
                Matcher matcher = pattern.matcher(result);
                int code = 0;
                String uuid = "";
                if (matcher.find()) {
                    code = Integer.valueOf(matcher.group(1));
                    uuid = matcher.group(2);
                }
                if (code == 200) {
                    qrcode(uuid);
                } else {
                    Toast.makeText(WechatService.this, "获取uuid失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Exception e) {
                mResultTextView.setText("exception:\n" + e.toString());
            }

            @Override
            public void onHttpError(int statusCode, String response) {
                mResultTextView.setText("error:\nstatusCode = " + statusCode + "\nresponse = " + response);
            }

            @Override
            public void onCookies(Map<String, String> cookies) {

            }
        };
        mUrlTextView.setText("1，获取uuid：\n" + WechatAPI.getJSLoginUrl());
        mResultTextView.setText("");
        StringLoader.get(WechatAPI.getJSLoginUrl(), cb);
    }

    private void qrcode(final String uuid) {
        ImageLoader.Callback cb = new ImageLoader.Callback() {
            @Override
            public void onSuccess(String url, Bitmap result) {
                mQrcodeImageView.setVisibility(View.VISIBLE);
                mQrcodeImageView.setImageBitmap(result);
                checkLoginCount = 0;
                checkLogin(uuid);
            }

            @Override
            public void onException(Exception e) {
                mResultTextView.setText("exception:\n" + e.toString());
            }

            @Override
            public void onHttpError(int statusCode) {
                mResultTextView.setText("error:\nstatusCode = " + statusCode);
            }
        };
        mUrlTextView.setText("2，获取二维码：\n" + WechatAPI.getQrCodeUrl(uuid));
        mResultTextView.setText("");
        ImageLoader.get(WechatAPI.getQrCodeUrl(uuid), cb);
    }

    private int checkLoginCount = 0;
    String redirect_uri = "";

    private void checkLogin(final String uuid) {
        StringLoader.Callback cb = new StringLoader.Callback() {
            @Override
            public void onSuccess(String result) {

                mResultTextView.setText("success:\n" + result);

                Pattern pattern =
                        Pattern.compile("window.code=([0-9]+);");
                Matcher matcher = pattern.matcher(result);
                int code = 0;
                if (matcher.find()) {
                    code = Integer.valueOf(matcher.group(1));
                }
                // 扫码成功后，二维码消失
                if (code == 201) {
                    mResultTextView.setText("手机端完成扫码，尚未确认登录");
                    mQrcodeImageView.setVisibility(View.GONE);
                }
                // 未确认登录，轮询
                if (code != 200) {
                    checkLogin(uuid);
                } else {  // 确认登录
                    mResultTextView.setText("登录成功");
                    pattern =
                            Pattern.compile("window.redirect_uri=\"(.+)\";");
                    matcher = pattern.matcher(result);
                    redirect_uri = "";
                    if (matcher.find()) {
                        redirect_uri = matcher.group(1);
                    }
                    redirectUri(redirect_uri);
                }
            }

            @Override
            public void onException(Exception e) {
                mResultTextView.setText("exception:\n" + e.toString());
            }

            @Override
            public void onHttpError(int statusCode, String response) {
                mResultTextView.setText("error:\nstatusCode = " + statusCode + "\nresponse = " + response);
            }

            @Override
            public void onCookies(Map<String, String> cookies) {

            }
        };
        checkLoginCount++;
        mUrlTextView.setText("3，检查是否登录成功，第 " + checkLoginCount + " 次轮询：\n" + WechatAPI.getLoginUrl(uuid));
        StringLoader.get(WechatAPI.getLoginUrl(uuid), cb);
    }

    private void redirectUri(String redirect_uri) {
        StringLoader.Callback callback = new StringLoader.Callback() {
            @Override
            public void onSuccess(String result) {
                WechatUserInfo userInfo = WechatUserInfo.getInstance();
                Pattern pattern =
                        Pattern.compile("<skey>(.+)</skey>");
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    userInfo.skey = matcher.group(1);
                }
                pattern =
                        Pattern.compile("<pass_ticket>(.+)</pass_ticket>");
                matcher = pattern.matcher(result);
                if (matcher.find()) {
                    userInfo.pass_ticket = matcher.group(1);
                }
                mResultTextView.setText("success:\n" + userInfo.toString());
                webwxInit();
            }

            @Override
            public void onException(Exception e) {
                mResultTextView.setText("exception:\n" + e.toString());
            }

            @Override
            public void onHttpError(int statusCode, String response) {
                mResultTextView.setText("error:\nstatusCode = " + statusCode + "\nresponse = " + response);
            }

            @Override
            public void onCookies(Map<String, String> cookies) {
                Iterator<String> it = cookies.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    WechatUserInfo userInfo = WechatUserInfo.getInstance();
                    userInfo.wxsid = cookies.get("wxsid");
                    userInfo.wxuin = cookies.get("wxuin");
                    userInfo.webwx_data_ticket = cookies.get("webwx_data_ticket");
                    userInfo.webwxuvid = cookies.get("webwxuvid");
                    userInfo.mm_lang = cookies.get("mm_lang");
                    userInfo.wxloadtime = cookies.get("wxloadtime");
                }
            }
        };
        mUrlTextView.setText("4，获取登录态信息：\n" + redirect_uri + "&fun=new&version=v2&lang=zh_CN");
        StringLoader.get(redirect_uri + "&fun=new&version=v2&lang=zh_CN", callback);
    }

    private void webwxInit() {
        JsonLoader.Callback callback = new JsonLoader.Callback() {
            @Override
            public void onSuccess(JSONObject json) {
                Log.e("fuck", json.toString());
                try {
                    JSONObject userJson = json.getJSONObject("User");
                    WechatUserInfo.getInstance().userName = userJson.getString("UserName");
                    WechatUserInfo.getInstance().nickName = userJson.getString("NickName");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mResultTextView.setText("success:\nWelcome ，" + WechatUserInfo.getInstance().nickName);
                mContactBtn.setVisibility(View.VISIBLE);
                mLoginBtn.setVisibility(View.GONE);
                synccheck();
            }

            @Override
            public void onException(Exception e) {
                mResultTextView.setText("exception:\n" + e.toString());
            }

            @Override
            public void onHttpError(int statusCode, String response) {
                mResultTextView.setText("error:\nstatusCode = " + statusCode + "\nresponse = " + response);
            }
        };
        WechatUserInfo userInfo = WechatUserInfo.getInstance();
        JSONObject postJson = new JSONObject();
        JSONObject baseRequestJson = userInfo.getBaseRequestJson();
        try {
            postJson.put("BaseRequest", baseRequestJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String payload = postJson.toString();
        String url = WechatAPI.getWebwxInitUrl(userInfo.pass_ticket, userInfo.mm_lang);

        mUrlTextView.setText("5,初始化接口，获取用户信息和聊天记录：\n" + url);
        mResultTextView.setText("");
        Log.e("fuck", payload);
        String cookie = WechatAPI.generateCookie();
        Log.e("fuck", "cookie = \n" + cookie);
        JsonLoader.post(url, payload, cookie, callback);
    }

    private void synccheck() {

    }

}
