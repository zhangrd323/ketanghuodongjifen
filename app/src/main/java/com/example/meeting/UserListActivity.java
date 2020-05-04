package com.example.meeting;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meeting.bean.Group;
import com.example.meeting.bean.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class UserListActivity extends AppCompatActivity {
    private RecyclerView rv_select;
    private SearchView searchView;
    public List<User> listDatas = new ArrayList<>();
    private GridAdapter gridAdapter = new GridAdapter(this);
    DataAccess dataAccess = new DataAccess(this);
    Gson gson = new Gson();
    private ImageView iv_add;
    /**
     * 1 收到  2 发出
     */
    private int category = 1;
    private Group group;
    private static final String TAG = "MeetinglListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        group = getIntent().getExtras().getParcelable("group");
        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().setTitle("学生列表");
        rv_select = findViewById(R.id.rv_shop);
        //初始化列表配置
        rv_select.setLayoutManager(new LinearLayoutManager(this));
        rv_select.setHasFixedSize(true);
        rv_select.setAdapter(gridAdapter);
        iv_add = findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UserListActivity.this,SelectListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("group",group);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        listDatas.clear();
        List<User> list = new ArrayList<>();
        //查询当前分组成员
        String[] selectionArgs = new String[]{group.getId() + ""};
        String selectStr = "GroupId = ?";
        list = dataAccess.queryUser(selectStr, selectionArgs);
        listDatas.addAll(list);
        //填充数据
        gridAdapter.setGridDataList(listDatas);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        gridAdapter.setGridDataList(listDatas);
    }


    /**
     * 网格适配器
     */
    class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

        private Context mContext;
        private List<User> mList = new ArrayList<>();

        public GridAdapter(Context context) {
            mContext = context;
        }

        public void setGridDataList(List<User> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public List<User> getmList() {
            return mList;
        }

        @NonNull
        @Override
        public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_note_list, parent, false);
            return new GridViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull GridViewHolder holder, final int position) {
            holder.tv_title.setText("姓名" + mList.get(position).getName());
            holder.tv_category.setText("学号:" + mList.get(position).getNum());
            holder.tv_score.setText("积分:" + mList.get(position).getScore());
            holder.tv_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserListActivity.this, UserDetaillActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", listDatas.get(position));
                    bundle.putParcelable("group", group);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.tv_star.setText("打分");

        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        public class GridViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_title;
            private TextView tv_category;
            private TextView tv_score;
            private TextView tv_star;
            private View itemView;
            CardView cardView;
            LinearLayout ll_bottom;

            public GridViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                tv_title = (TextView) itemView.findViewById(R.id.tv_title);
                tv_category = (TextView) itemView.findViewById(R.id.tv_category);
                tv_score = (TextView) itemView.findViewById(R.id.tv_score);
                tv_star = (TextView) itemView.findViewById(R.id.tv_star);
                cardView = itemView.findViewById(R.id.card_view);
            }
        }
    }


}
