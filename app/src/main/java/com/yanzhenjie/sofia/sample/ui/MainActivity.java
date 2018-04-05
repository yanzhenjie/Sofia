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
package com.yanzhenjie.sofia.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.sofia.Sofia;
import com.yanzhenjie.sofia.sample.OnItemClickListener;
import com.yanzhenjie.sofia.sample.R;
import com.yanzhenjie.sofia.sample.ui.fragment.FragmentActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Entrance.
 * Created by YanZhenjie on 2017/8/2.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter adapter = new Adapter(Arrays.asList(getResources().getStringArray(R.array.main_item)));
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
                    case 3: {
                        startActivity(new Intent(MainActivity.this, DrawerLayoutActivity.class));
                        break;
                    }
                    case 4: {
                        startActivity(new Intent(MainActivity.this, FragmentActivity.class));
                        break;
                    }
                }
            }
        });

        Sofia.with(this)
                .statusBarBackground(ContextCompat.getDrawable(this, R.drawable.status_image))
                .navigationBarBackground(ContextCompat.getDrawable(this, R.drawable.navigation_image_a));
    }

    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> mStringList;
        private OnItemClickListener mItemClickListener;

        public Adapter(List<String> stringList) {
            mStringList = stringList;
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
            viewHolder.mItemClickListener = mItemClickListener;
            return viewHolder;
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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener mItemClickListener;
        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

}
