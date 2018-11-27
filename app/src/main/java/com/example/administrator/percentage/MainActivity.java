package com.example.administrator.percentage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.percentage.custom.PercentageView;

public class MainActivity extends AppCompatActivity {

    private PercentageView perElectric;
    private Button btn_per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        perElectric = (PercentageView) findViewById(R.id.per_electric);
        btn_per = (Button) findViewById(R.id.btn_per);
        perElectric.create();
        btn_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //剩余电量
                perElectric.setCurrentElectric(88);
            }
        });
    }
}
