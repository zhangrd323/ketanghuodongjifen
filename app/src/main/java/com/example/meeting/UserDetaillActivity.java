package com.example.meeting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.bean.Group;
import com.example.meeting.bean.User;

public class UserDetaillActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_face;
    private TextView tv_title;
    private TextView tv_score;
    private TextView tv_fscore;
    private Button btn_3;
    private Button btn_2;
    private Button btn_1;
    private Button btn_2_;
    private Button btn_1_;
    private User user;
    private DataAccess dataAccess = new DataAccess(this);
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        user = getIntent().getExtras().getParcelable("user");
        group = getIntent().getExtras().getParcelable("group");
        initView();
        bindData();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_fscore = (TextView) findViewById(R.id.tv_fscore);
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
        if (user != null) {
            tv_title.setText(user.getName());
            tv_score.setText(user.getScore()+"");
            tv_fscore.setText((group.getScore() + user.getScore() * Constant.scale) + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                user.setScore(user.getScore() + 1);
                dataAccess.updateUser(user);
                break;
            case R.id.btn_2:
                user.setScore(user.getScore() + 2);
                dataAccess.updateUser(user);
                break;
            case R.id.btn_3:
                user.setScore(user.getScore() + 3);
                dataAccess.updateUser(user);
                break;
            case R.id.btn_1_:
                user.setScore(user.getScore() - 1);
                dataAccess.updateUser(user);
                break;
            case R.id.btn_2_:
                user.setScore(user.getScore() - 2);
                dataAccess.updateUser(user);
                break;
        }
        bindData();
    }
}
