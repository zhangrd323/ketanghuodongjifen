package com.example.meeting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.bean.Group;

public class GroupDetaillActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_face;
    private TextView tv_title;
    private TextView tv_star;
    private Button btn_3;
    private Button btn_2;
    private Button btn_1;
    private Button btn_2_;
    private Button btn_1_;
    private Group group;
    private DataAccess dataAccess = new DataAccess(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_detail);
        group = getIntent().getExtras().getParcelable("group");
        initView();
        bindData();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_star = (TextView) findViewById(R.id.tv_star);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2_ = (Button) findViewById(R.id.btn_2_);
        btn_1_ = (Button) findViewById(R.id.btn_1_);
        btn_3.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_2_.setOnClickListener(this);
        btn_1_.setOnClickListener(this);
    }


    private void bindData() {
        if (group != null) {
            tv_title.setText(group.getName());
            tv_star.setText(group.getStar()+"");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                group.setStar(group.getStar() + 1);
                dataAccess.updateGroup(group);
                break;
            case R.id.btn_2:
                group.setStar(group.getStar() + 2);
                dataAccess.updateGroup(group);
                break;
            case R.id.btn_3:
                group.setStar(group.getStar() + 3);
                dataAccess.updateGroup(group);
                break;
            case R.id.btn_1_:
                group.setStar(group.getStar() - 1);
                dataAccess.updateGroup(group);
                break;
            case R.id.btn_2_:
                group.setStar(group.getStar() - 2);
                dataAccess.updateGroup(group);
                break;
        }
        bindData();
    }
}
