package com.damocles.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Text;
import com.damocles.R;
import com.damocles.sample.recyclerview.DividerGridItemDecoration;
import com.damocles.sample.recyclerview.DividerItemDecoration;
import com.damocles.sample.util.Utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private List<String> mDatas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        Utils.initToolbar(this, R.id.recyclerview_toolbar);
        initDatas();
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

    private void initDatas() {
        mDatas = new ArrayList<String>();
        for (int i = 0; i <= 100; i++) {
            mDatas.add(String.valueOf(i));
        }
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setAdapter(new TestAdapter());
    }

    private class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(RecyclerViewActivity.this).inflate(R.layout
                    .activity_recyclerview_item, parent, false);
            TestViewHolder viewHolder = new TestViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(TestViewHolder holder, int position) {
            final String item = getItem(position);
            holder.textView.setText(item);
            int w = FrameLayout.LayoutParams.MATCH_PARENT;
            float density = RecyclerViewActivity.this.getResources().getDisplayMetrics().density;
            int h = (int) (density * 80 + new Random().nextInt(10) * 15);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h);
            holder.textView.setLayoutParams(params);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RecyclerViewActivity.this, item, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        public String getItem(int position) {
            return mDatas == null ? null : mDatas.get(position);
        }

        class TestViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public TestViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.recyclerview_item_txt);
            }
        }
    }

}
