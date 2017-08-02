/*
 * Copyright 2017 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.statusview.sample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.ListItemDecoration;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Entrance.
 * </p>
 * Created by YanZhenjie on 2017/8/2.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SwipeMenuRecyclerView recyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ListItemDecoration(Color.GRAY));
        recyclerView.setAdapter(new Adapter(Arrays.asList(getResources().getStringArray(R.array.main_item))));

        recyclerView.setSwipeItemClickListener(mClickListener);
    }

    /**
     * Item click.
     */
    private SwipeItemClickListener mClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            switch (position) {
                case 0: {
                    startActivity(new Intent(MainActivity.this, CommonActivity.class));
                    break;
                }
                case 1: {
                    startActivity(new Intent(MainActivity.this, GoodsDetailsActivity.class));
                    break;
                }
                case 2: {
                    startActivity(new Intent(MainActivity.this, DarkFontStatusActivity.class));
                    break;
                }
            }
        }
    };

    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> mStringList;

        public Adapter(List<String> stringList) {
            mStringList = stringList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mStringList.get(position));
        }

        @Override
        public int getItemCount() {
            return mStringList.size();
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

}
