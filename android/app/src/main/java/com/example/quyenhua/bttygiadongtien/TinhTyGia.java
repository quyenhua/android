package com.example.quyenhua.bttygiadongtien;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class TinhTyGia extends AppCompatActivity {

    TextView tvdateTime, tvKetqua;
    Spinner spinnerLoaitiente1, spinnerLoaitiente2;
    EditText edInput;
    Button btnHienthi, btnReturn;

    ArrayList<String> listId, listName, listSotien;
    String dateTime;

    int position1, position2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinh_ty_gia);

        tvdateTime = (TextView)findViewById(R.id.tvDateTime);
        tvKetqua = (TextView)findViewById(R.id.tvKetqua);
        spinnerLoaitiente1 = (Spinner)findViewById(R.id.spinnerLoaitiente1);
        spinnerLoaitiente2 = (Spinner)findViewById(R.id.spinnerLoaitiente2);
        edInput = (EditText)findViewById(R.id.edInput);
        btnReturn =(Button)findViewById(R.id.btnReturn);
        btnHienthi = (Button)findViewById(R.id.btnHienthi);

        Bundle db = getIntent().getExtras();
        if(db != null){
            dateTime = db.getString("dateTime");
            listId = db.getStringArrayList("listId");
            listName = db.getStringArrayList("listName");
            listSotien = db.getStringArrayList("listSotien");
        }

        listId.add(listId.size(), "VND");
        listName.add(listName.size(), "VIETNAM DONG");
        listSotien.add(listSotien.size(), "1");

        tvdateTime.setText("Ngày cập nhật: " + dateTime);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerLoaitiente1.setAdapter(arrayAdapter);
        spinnerLoaitiente2.setAdapter(arrayAdapter);

        spinnerLoaitiente1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position1 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerLoaitiente2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position2 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnHienthi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edInput.getText().toString();
                int dob_input = Integer.parseInt(input);
                Double tong = (dob_input * (Double.parseDouble(listSotien.get(position1)))) / (Double.parseDouble(listSotien.get(position2)));
                tvKetqua.setText("Kết quả: " + String.valueOf((Math.round(tong * 100)) / 100) + " " + listId.get(position2));
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intReturn = new Intent(TinhTyGia.this, MainActivity.class);
                startActivity(intReturn);
            }
        });
    }
}
