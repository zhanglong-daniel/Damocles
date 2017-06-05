package com.damocles.common.base;

import com.damocles.common.network.http.HttpManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhanglong02 on 17/3/24.
 */

public class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initFresco(this);
    }

    private void initFresco(Context context) {
        ImagePipelineConfig config =
                OkHttpImagePipelineConfigFactory.newBuilder(context, HttpManager.getOkHttpClient())
                        .setDownsampleEnabled(true).build();
        Fresco.initialize(context, config);
    }
}
