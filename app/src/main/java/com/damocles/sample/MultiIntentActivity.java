package com.damocles.sample;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

import com.baidu.naviauto.R;
import com.damocles.sample.util.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MultiIntentActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_OR_TAKE_PICTURE = 0x1001;

    private Button mButton;
    private ImageView mImageView;
    private File mImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_intent);
        Utils.initToolbar(this, R.id.multi_intent_toolbar);
        initViews();
    }

    private void initViews() {
        mButton = (Button) findViewById(R.id.multi_intent_btn);
        mImageView = (ImageView) findViewById(R.id.multi_intent_imgv);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickOrTakePicture();
            }
        });
    }

    private void pickOrTakePicture() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickPictureIntent.setType("image/**");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageFile = new File(this.getExternalCacheDir() + File.separator + "temp_image.jpg");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        Intent chooserIntent = Intent.createChooser(pickPictureIntent, "Pick or tacke picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {takePictureIntent});
        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_OR_TAKE_PICTURE);
    }

    private void setImageView(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_PICK_OR_TAKE_PICTURE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (resultCode) {
            case RESULT_CANCELED:
                Toast.makeText(this, "operation canceled.", Toast.LENGTH_SHORT).show();
                break;
            case RESULT_OK:
                if (data != null) {
                    try {
                        setImageView(MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    setImageView(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                }
                break;
            default:
                Toast.makeText(this, "resultCode = " + requestCode, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
