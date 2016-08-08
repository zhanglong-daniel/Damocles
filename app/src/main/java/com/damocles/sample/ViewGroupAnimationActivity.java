package com.damocles.sample;

import com.baidu.naviauto.R;
import com.damocles.sample.util.Utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewGroupAnimationActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_viewgroup);
        Utils.initToolbar(this, R.id.animation_viewgroup_toolbar);
        initViews();
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.animation_viewgroup_listview);
        String[] strs = new String[50];
        for (int i = 0, count = strs.length; i < count; i++) {
            strs[i] = "listView item " + i;
        }
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs));
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(250);
        animationSet.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation
                .RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        animationSet.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(animationSet, 0.2f);
        mListView.setLayoutAnimation(controller);
    }

}
