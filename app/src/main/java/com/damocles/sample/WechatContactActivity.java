package com.damocles.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.naviauto.R;
import com.damocles.android.network.loader.ImageLoader;
import com.damocles.android.network.loader.JsonLoader;
import com.damocles.android.wechat.WechatAPI;
import com.damocles.android.wechat.WechatContact;
import com.damocles.android.wechat.WechatUserInfo;
import com.damocles.sample.util.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WechatContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;

    private ContactListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_contact);
        Utils.initToolbar(this, R.id.wechat_contact_toolbar);
        initViews();
        if (WechatUserInfo.getInstance().contacts == null || WechatUserInfo.getInstance().contacts.size() == 0) {
            loadContacts();
        }
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
        mListView = (ListView) findViewById(R.id.wechat_contact_list);
        mAdapter = new ContactListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSendMsgDialog(mAdapter.getItem(position));
            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    private void loadContacts() {
        final ProgressDialog dialog = new ProgressDialog(this, android.R.style.Theme_Holo_Light_Dialog);
        dialog.setTitle("加载通讯录");
        dialog.show();
        JsonLoader.Callback callback = new JsonLoader.Callback() {
            @Override
            public void onSuccess(JSONObject json) {

                WechatUserInfo.getInstance().contacts = jsonParse(json);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
                Log.e("fuck", "count = " + WechatUserInfo.getInstance().contacts.size());
            }

            @Override
            public void onException(Exception e) {
                Toast.makeText(WechatContactActivity.this, "exception:\n" + e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHttpError(int statusCode, String response) {
                Toast.makeText(WechatContactActivity.this,
                        "error:\nstatusCode = " + statusCode + "\nresponse = " + response, Toast.LENGTH_SHORT).show();
            }
        };
        WechatUserInfo userInfo = WechatUserInfo.getInstance();
        String url = WechatAPI.getWebwxContactUrl(userInfo.pass_ticket, userInfo.skey, userInfo.mm_lang);
        String cookie = WechatAPI.generateCookie();
        Log.e("fuck", "cookie = \n" + cookie);
        JsonLoader.get(url, cookie, callback);
    }

    private class ContactListAdapter extends BaseAdapter {

        public ContactListAdapter() {

        }

        @Override
        public int getCount() {
            return WechatUserInfo.getInstance().contacts == null ? 0 : WechatUserInfo.getInstance().contacts.size();
        }

        @Override
        public WechatContact getItem(int position) {
            return WechatUserInfo.getInstance().contacts == null ? null
                    : WechatUserInfo.getInstance().contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(WechatContactActivity.this).inflate(R.layout
                        .list_item_wechat_contact, parent, false);
                viewHolder.mTextView = (TextView) convertView.findViewById(R.id.list_item_wechat_contact_txt);
                viewHolder.mIcon = (ImageView) convertView.findViewById(R.id.list_item_wechat_contact_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String sex = "未知";
            if (getItem(position).Sex == 1) {
                sex = "男";
            } else if (getItem(position).Sex == 2) {
                sex = "女";
            }
            viewHolder.mTextView.setText(
                    "备注：" + getItem(position).RemarkName + "\n昵称：" + getItem(position).NickName + "\n性别:" +
                            sex + "    position: " + position);
            //            viewHolder.mIcon.setTag("https://wx2.qq.com" + getItem(position).HeadImgUrl);
            //            ImageLoader.get("https://wx2.qq.com" + getItem(position).HeadImgUrl, new ImageLoader
            // .Callback() {
            //                @Override
            //                public void onSuccess(String url, Bitmap result) {
            //                    if (((String) viewHolder.mIcon.getTag()).equals(url)) {
            //                        viewHolder.mIcon.setImageBitmap(result);
            //                    } else {
            //                        viewHolder.mIcon.setImageBitmap(null);
            //                    }
            //                }
            //
            //                @Override
            //                public void onException(Exception e) {
            //
            //                }
            //
            //                @Override
            //                public void onHttpError(int statusCode) {
            //
            //                }
            //            });
            return convertView;
        }

        class ViewHolder {
            public ImageView mIcon;
            public TextView mTextView;
        }
    }

    private List<WechatContact> jsonParse(JSONObject json) {
        List<WechatContact> contacts = new ArrayList<WechatContact>();
        WechatContact contact;
        try {
            JSONArray array = json.getJSONArray("MemberList");
            for (int i = 0, size = array.length(); i < size; i++) {
                json = array.getJSONObject(i);
                contact = new WechatContact();
                contact.UserName = json.getString("UserName");
                contact.NickName = json.getString("NickName");
                contact.HeadImgUrl = json.getString("HeadImgUrl");
                contact.Sex = json.getInt("Sex");
                contact.RemarkName = json.getString("RemarkName");
                contact.PYQuanPin = json.getString("PYQuanPin");
                contact.RemarkPYQuanPin = json.getString("RemarkPYQuanPin");
                contact.ContactFlag = json.getInt("ContactFlag");
                contact.SnsFlag = json.getInt("SnsFlag");
                contact.StarFriend = json.getInt("StarFriend");
                if (contact.StarFriend == 1 || contact.SnsFlag != 0) {
                    contacts.add(contact);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(contacts, new Comparator<WechatContact>() {
            @Override
            public int compare(WechatContact lhs, WechatContact rhs) {

                String left = TextUtils.isEmpty(lhs.RemarkPYQuanPin) ? lhs.PYQuanPin : lhs.RemarkPYQuanPin;
                String right = TextUtils.isEmpty(rhs.RemarkPYQuanPin) ? rhs.PYQuanPin : rhs.RemarkPYQuanPin;
                return left.toUpperCase().compareTo(right.toUpperCase());
            }
        });
        return contacts;
    }

    Dialog sendMsgdialog;

    private void showSendMsgDialog(final WechatContact contact) {
        sendMsgdialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        String name = TextUtils.isEmpty(contact.RemarkName) ? contact.NickName : contact.RemarkName;
        sendMsgdialog.setTitle("发消息给 " + name);
        sendMsgdialog.setContentView(R.layout.dialog_wechat_send_msg);
        final EditText editText = (EditText) sendMsgdialog.findViewById(R.id.dialog_wechat_send_msg_edittext);
        Button button = (Button) sendMsgdialog.findViewById(R.id.dialog_wechat_send_msg_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(WechatContactActivity.this, "消息为空", Toast.LENGTH_SHORT).show();
                } else {
//                    sendMsg(msg, contact.UserName);
                }
            }
        });
        sendMsgdialog.show();
    }


}
