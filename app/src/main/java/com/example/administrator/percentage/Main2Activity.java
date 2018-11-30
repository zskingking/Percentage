package com.example.administrator.percentage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.percentage.custom.ElectricColumnView;

public class Main2Activity extends AppCompatActivity {

    ElectricColumnView electricColumnView;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        electricColumnView = (ElectricColumnView) findViewById(R.id.per_electric);

        button = (Button) findViewById(R.id.button);
        electricColumnView.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electricColumnView.setInit((float) 0.15);
            }
        });
    }
}
