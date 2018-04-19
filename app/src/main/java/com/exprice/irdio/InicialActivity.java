package com.exprice.irdio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InicialActivity extends AppCompatActivity {

    private Button btn_explorar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        btn_explorar = findViewById(R.id.btn_explorar);
        btn_explorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InicialActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
