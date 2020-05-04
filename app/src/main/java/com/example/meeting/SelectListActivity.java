package com.example.meeting;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.bean.Group;
import com.example.meeting.bean.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class SelectListActivity extends AppCompatActivity {
    private RecyclerView rv_select;
    private ImageView iv_add;
    private SearchView searchView;
    public List<User> listDatas = new ArrayList<>();
    private GridAdapter gridAdapter = null;
    DataAccess dataAccess = new DataAccess(this);
    private Group group;
    private Button btn_send;

    Gson gson = new Gson();
    private static final String TAG = "SelectListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_list);
        group = getIntent().getExtras().getParcelable("group");
        initView();
        initData();

    }

    private void initView() {
        getSupportActionBar().setTitle("添加学生到分组");
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> list = new ArrayList<>();
                for (int i = 0; i < listDatas.size(); i++) {
                    if (listDatas.get(i).getIsSelect() == 1) {
                        list.add(listDatas.get(i));
                    }
                }
                if (list.size() > 0) {
                    //更新数据库
                    StringBuilder sb = new StringBuilder();
                    //更新用户
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setGroupId(group.getId());
                        dataAccess.updateUser(list.get(i));
                        sb.append(list.get(i).getId() + ",");
                    }
                    group.setUserIds(sb.toString().substring(0, (sb.toString().length()) - 1));
                    //更新组别
                    dataAccess.updateGroup(group);
                    T.showShort(SelectListActivity.this, "添加成功");
                    finish();
                } else {
                    T.showShort(SelectListActivity.this, "您还没有选中任何学生");
                }
            }
        });
        rv_select = findViewById(R.id.rv_shop);
        //初始化列表配置
        rv_select.setLayoutManager(new LinearLayoutManager(this));
        rv_select.setHasFixedSize(true);
        gridAdapter = new GridAdapter(this);
        rv_select.setAdapter(gridAdapter);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        listDatas.clear();
        //查询所有未分组成员
        String[] selectionArgs = new String[]{"0"};
        String selectStr = "GroupId = ?";
        List<User> list = dataAccess.queryUser(selectStr, selectionArgs);
//        List<User> list = dataAccess.queryUser(null, null);
        listDatas.addAll(list);
        gridAdapter.notifyDataSetChanged();
    }


    /**
     * 网格适配器
     */
    class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

        private Context mContext;

        public GridAdapter(Context context) {
            mContext = context;
        }


        @NonNull
        @Override
        public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_info, parent, false);
            return new GridViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull GridViewHolder holder, final int position) {
            holder.tvTitle.setText(listDatas.get(position).getName());
            if (listDatas.get(position).getIsSelect() == 1) {
                holder.ivCheck.setBackgroundResource(R.mipmap.msg_box_choose_now);
            } else if (listDatas.get(position).getIsSelect() == 0) {
                holder.ivCheck.setBackgroundResource(R.mipmap.msg_box);
            } else {
                holder.ivCheck.setBackgroundResource(R.mipmap.msg_box_choose_before);
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listDatas.get(position).getIsSelect() == 1) {
                        listDatas.get(position).setIsSelect(0);
                    } else if (listDatas.get(position).getIsSelect() == 0) {
                        listDatas.get(position).setIsSelect(1);
                    }
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return listDatas == null ? 0 : listDatas.size();
        }

        public class GridViewHolder extends RecyclerView.ViewHolder {

            TextView tvTitle, tvContent, tv_category;
            ImageView ivFace, ivCheck;

            CardView cardView;

            public GridViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_name);
                ivFace = itemView.findViewById(R.id.iv_face);
                ivCheck = itemView.findViewById(R.id.iv_check);
                cardView = itemView.findViewById(R.id.card_view);
            }
        }
    }


}
