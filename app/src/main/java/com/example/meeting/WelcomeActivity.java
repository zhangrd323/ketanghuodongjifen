package com.example.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class WelcomeActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Thread() {
            @Override
            public void run() {
                super.run();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        initDatabase();

                        Intent intent = new Intent(WelcomeActivity.this, GroupListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        }.start();
    }

    /**
     * 数据库初始化
     */
    private void initDatabase() {
        File dir = new File("data/data/com.example.stugra/databases"); //找到数据库文件夹
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!(new File(SqlHelper.DB_NAME)).exists()) {
            //如果该数据库文件不存在，通过资源文件wordorid导入数据库
            Log.e("test", "不存在");
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(SqlHelper.DB_NAME);

                byte[] buffer = new byte[8192];
                int count = 0;
                InputStream is = getResources().openRawResource(
                        R.raw.stugra);
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
