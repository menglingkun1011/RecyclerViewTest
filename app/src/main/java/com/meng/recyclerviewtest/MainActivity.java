package com.meng.recyclerviewtest;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hr.nipuream.NRecyclerView.view.NRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        NRecyclerView.RefreshAndLoadingListener{

    private NRecyclerView mRecyclerView;
    private Toolbar mToolBar;

    List<String> totalDatas = new ArrayList<>();
    private MyAdapter adapter;

    List<String> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 20; i++) {
            int a = i+1;
            datas.add("条目"+a);
        }

        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mRecyclerView = (NRecyclerView) findViewById(R.id.recyclerView);

        mToolBar.setTitle("刷新库");
        mToolBar.setTitleTextColor(Color.WHITE);
        //toolbar替换actionbar
        setSupportActionBar(mToolBar);
        //显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).marginResId(R.dimen.margin_left).build(),2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLoadDataScrollable(!mRecyclerView.getLoadDataScrollable());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setOnRefreshAndLoadingListener(this);

        adapter = new MyAdapter(totalDatas);
        mRecyclerView.setAdapter(adapter);
    }

    Handler handler = new Handler();

    @Override
    public void refresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.resetEntryView();
                adapter.setItems(datas);
                mRecyclerView.endRefresh();
            }
        },2000);

    }

    @Override
    public void load() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                totalDatas.addAll(datas);
                adapter.notifyDataSetChanged();
                mRecyclerView.endLoadingMore();
            }
        },2000);

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private List<String> data;

        public MyAdapter(List<String> datas) {
            this.data = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = (View) View.inflate(MainActivity.this,android.R.layout.simple_list_item_1,null);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv.setText(data.get(position));
            holder.tv.setTextColor(Color.RED);
        }

        @Override
        public int getItemCount() {
            return data == null ?0:data.size();
        }
        public void setItems(List<String> data){
            this.data = data;
            this.notifyDataSetChanged();
        }

        public void clearData(){
            this.data.clear();
            notifyDataSetChanged();
        }


        class ViewHolder extends RecyclerView.ViewHolder{

            private TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
