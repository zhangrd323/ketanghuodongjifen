package com.example.meeting;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meeting.bean.Group;
import com.example.meeting.bean.OutData;
import com.example.meeting.bean.User;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xw.repo.XEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.functions.Action1;


public class GroupListActivity extends AppCompatActivity {
    private RecyclerView rv_select;
    private SearchView searchView;
    public List<Group> listDatas = new ArrayList<>();
    private GridAdapter gridAdapter = new GridAdapter(this);
    DataAccess dataAccess = new DataAccess(this);
    Gson gson = new Gson();
    private RxPermissions rxPermissions;
    private static final int REQUEST_CODE_FILE_CHOOSE = 2;
    /**
     * 1 收到  2 发出
     */
    private int category = 1;
    private static final String TAG = "MeetinglListActivity";
    private String fileName;
    private Dialog mDelDialog;
    private Dialog mDefaultDialog;
    private Dialog mScaleDialog;
    private int groupCount = 4;
    private SqlHelper sqlHelper = new SqlHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_list);
        getSupportActionBar().setTitle("分组列表");
        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        initView();
        initData();

    }

    private void initView() {
        fileName = sdCardIsAvailable() + "/studentDetail.xls";
        rv_select = findViewById(R.id.rv_shop);
        //初始化列表配置
        rv_select.setLayoutManager(new LinearLayoutManager(this));
        rv_select.setHasFixedSize(true);
        rv_select.setAdapter(gridAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        listDatas.clear();
        List<Group> list = new ArrayList<>();
        String[] selectionArgs = null;
        String selectStr = null;
        list = dataAccess.queryGroup(selectStr, selectionArgs);
        listDatas.addAll(list);
        sort();
        for (int i = 0 ; i < listDatas.size();i++){
                if (listDatas.size() == 4) {
                    listDatas.get(i).setScore(Constant.defaultScore[i]);
                } else if (listDatas.size() == 8){
                    listDatas.get(i).setScore(Constant.defaultScore[i / 2]);
                }
                //更新组分
                dataAccess.updateGroup(listDatas.get(i));
        }
        //填充数据
        gridAdapter.setGridDataList(listDatas);
    }

    /**
     * 得到要导出的数据
     *
     * @return
     */
    private List<OutData> getOutData() {
        List<User> list = new ArrayList<>();
        list = dataAccess.queryUser(null, null);
        List<OutData> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < listDatas.size(); j++) {
                if (list.get(i).getGroupId() == listDatas.get(j).getId()) {
                    //处理得到最终分j
                    list.get(i).setfScore((list.get(i).getScore() * Constant.scale + listDatas.get(j).getScore()) + "");
                    list1.add(new OutData(listDatas.get(j), list.get(i)));
                }
            }
        }
        return list1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        gridAdapter.setGridDataList(listDatas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting1) {
            //导入
            checkPermission(true);
        }else if(id == R.id.action_setting2){
            //导出
            checkPermission(false);
        }else if (id == R.id.action_setting3){
            //设置默认组分
            initDefaultDialog();
            mDefaultDialog.show();
        }else {
            //设置分数系数
            initScaleDialog();
            mScaleDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //根据star排序
    private void sort(){
        Collections.sort(listDatas, new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                return o2.getStar() - o1.getStar();
            }
        });
    }

    /**
     * 网格适配器
     */
    class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

        private Context mContext;
        private List<Group> mList = new ArrayList<>();

        public GridAdapter(Context context) {
            mContext = context;
        }

        public void setGridDataList(List<Group> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public List<Group> getmList() {
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
            holder.tv_title.setText(mList.get(position).getName());
            holder.tv_category.setText("评星数:" + mList.get(position).getStar() + "个");
            holder.tv_score.setText("积分:" + mList.get(position).getScore());
            holder.tv_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GroupListActivity.this, GroupDetaillActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("group", listDatas.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupListActivity.this, UserListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("group", listDatas.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

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

    private void checkPermission(final boolean isImport) {
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            if (isImport) {
                                //导入
                                importExcel();
                            } else {
                                //导出
                                ExcelUtil.initExcel(fileName, Constant.excelTitles);
                                ExcelUtil.writeObjListToExcel(getOutData(), fileName, GroupListActivity.this);
                                T.showLong(GroupListActivity.this, "导出成功,导出路径:" + fileName);
                            }

                        } else {
                            T.showShort(GroupListActivity.this, "没有获取到权限");
                        }
                    }
                });
//
    }

    private void importExcel() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//多选
        startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSE);
    }

    public static String sdCardIsAvailable() {
        //首先判断外部存储是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            Log.e("qq", "sd = " + sd);//sd = /storage/emulated/0
            return sd.getAbsolutePath();
        } else {
            return "";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_CODE_FILE_CHOOSE:
                Uri uri = data.getData();
//                String path = FileUtils.getFilePathByUri(this, uri);
                String path = getFilePathFromUri(uri);
                List<User> list = ExcelUtil.readExcel(path);
                if (listDatas.size() > 0){
                    initDialog(true,list);
                    mDelDialog.show();
                }else {
                    initDialog(false,list);
                    mDelDialog.show();
//                    T.showLong(this, "导入数据成功,共导入" + list.size() + "条数据");
                }

                break;
        }

    }

    /**
     * 通过Uri，获取录音文件的路径（绝对路径）
     *
     * @param uri 录音文件的uri
     * @return 录音文件的路径（String）
     */
    private String getFilePathFromUri(Uri uri) {
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
        String temp = cursor.getString(index);
        cursor.close();
        return temp;
    }

    /**
     * 初始化底部删除弹出框
     */
    private void initDialog(final boolean isClear, final List<User> list) {
        mDelDialog = new Dialog(this, R.style.dialog_bottom_full);
        mDelDialog.setCanceledOnTouchOutside(true);
        mDelDialog.setCancelable(true);
        Window window = mDelDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        View view = View.inflate(this, R.layout.lay_bottom_dialog, null);
        TextView txt_discript = (TextView) view.findViewById(R.id.txt_discript);
        TextView txt_action = (TextView) view.findViewById(R.id.txt_action);
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_discript.setText(isClear ? "你是否确认要导入新的学生信息，导入后，分组信息将会被重置!":"请选择要导入的学生信息，分成多少组?");
        txt_action.setText(isClear?"导入":"4组");
        txt_cancel.setText(isClear?"取消":"8组");
        txt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClear){
                    //清空 之前数据，重新导入
                    sqlHelper.DeleteTableData(GroupListActivity.this,SqlHelper.User_Table);
                    sqlHelper.DeleteTableData(GroupListActivity.this,SqlHelper.Group_TABLE);
                    //这里去执行某项操作。
                    if (mDelDialog != null && mDelDialog.isShowing()) {
                        mDelDialog.dismiss();
                    }
                    initDialog(false,list);
                    mDelDialog.show();
                }else {
                    //增加分组和用户
                    groupCount = 4;
                    for (int i = 0 ; i< 4;i++){
                        dataAccess.insertGroup(new Group("分组"+(i+1),0));
                    }
                    for (int i = 0 ; i< list.size(); i++){
                        dataAccess.insertUser(new User(list.get(i).getName(),list.get(i).getNum()));
                    }
                    initData();
                    //这里去执行某项操作。
                    if (mDelDialog != null && mDelDialog.isShowing()) {
                        mDelDialog.dismiss();
                    }
                }


            }
        });
        view.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClear){
                    mDelDialog.dismiss();
                }else {
                    groupCount = 8;
                    //增加分组和用户
                    for (int i = 0 ; i< 8;i++){
                        dataAccess.insertGroup(new Group("分组"+(i+1),0));
                    }
                    for (int i = 0 ; i< list.size(); i++){
                        dataAccess.insertUser(new User(list.get(i).getName(),list.get(i).getNum()));
                    }
                    initData();
                }
                if (mDelDialog != null && mDelDialog.isShowing()) {
                    mDelDialog.dismiss();
                }
            }
        });
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }


    /**
     * 初始化底部删除弹出框
     */
    private void initDefaultDialog() {
        mDefaultDialog = new Dialog(this, R.style.dialog_bottom_full);
        mDefaultDialog.setCanceledOnTouchOutside(true);
        mDefaultDialog.setCancelable(true);
        Window window = mDefaultDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        View view = View.inflate(this, R.layout.lay_edit2_dialog, null);
        final XEditText edt1 = view.findViewById(R.id.edt_1);
        final XEditText edt2 = view.findViewById(R.id.edt_2);
        final XEditText edt3 = view.findViewById(R.id.edt_3);
        final XEditText edt4 = view.findViewById(R.id.edt_4);
        TextView txt_action = (TextView) view.findViewById(R.id.txt_sure);
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt1.getText().toString().isEmpty() ||
                        edt2.getText().toString().isEmpty() ||
                        edt3.getText().toString().isEmpty() ||
                        edt4.getText().toString().isEmpty() ){
                    T.showShort(GroupListActivity.this,"所有默认分数为比填项");
                }else {
                    //设置分数后重新排序
                    Constant.defaultScore[0] = Integer.parseInt(edt1.getText().toString());
                    Constant.defaultScore[1] = Integer.parseInt(edt2.getText().toString());
                    Constant.defaultScore[2] = Integer.parseInt(edt3.getText().toString());
                    Constant.defaultScore[3] = Integer.parseInt(edt4.getText().toString());
                    sort();
                    for (int i = 0 ; i < listDatas.size();i++){
                        if (listDatas.size() == 4){
                            listDatas.get(i).setScore(Constant.defaultScore[i]);
                        }else {
                            listDatas.get(i).setScore(Constant.defaultScore[i/2]);
                        }
                        //更新组分
                        dataAccess.updateGroup(listDatas.get(i));
                    }
                    gridAdapter.notifyDataSetChanged();
                    mDefaultDialog.dismiss();
                }

            }
        });
        view.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultDialog.dismiss();
            }
        });
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }


    /**
     * 初始化底部删除弹出框
     */
    private void initScaleDialog() {
        mScaleDialog = new Dialog(this, R.style.dialog_bottom_full);
        mScaleDialog.setCanceledOnTouchOutside(true);
        mScaleDialog.setCancelable(true);
        Window window = mScaleDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        View view = View.inflate(this, R.layout.lay_edit_dialog, null);
        final EditText edt1 = view.findViewById(R.id.edt_content);
        TextView txt_action = (TextView) view.findViewById(R.id.txt_sure);
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt1.getText().toString().isEmpty()){
                    T.showShort(GroupListActivity.this,"系数不能为空");
                }else {
                    Constant.scale = Float.valueOf(edt1.getText().toString());
                    T.showShort(GroupListActivity.this,"修改系数成功");
                    mScaleDialog.dismiss();
                }


            }
        });
        view.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScaleDialog.dismiss();
            }
        });
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }
}
