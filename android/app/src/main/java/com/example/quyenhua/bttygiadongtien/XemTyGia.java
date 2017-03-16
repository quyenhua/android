package com.example.quyenhua.bttygiadongtien;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quyenhua.bttygiadongtien.database.GiaTienTe;

import java.util.ArrayList;

public class XemTyGia extends AppCompatActivity {

    RelativeLayout layoutHienthi;
    TextView tvId, tvDatetime, tvName, tvBuyOnl;
    Spinner spinnerXem;
    ArrayList<String> listId, listName, listBuyOnl;
    String dateTime;
    Button btnReturn;

    ArrayList<GiaTienTe> giaTienTeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_ty_gia);

        layoutHienthi = (RelativeLayout)findViewById(R.id.layoutHienthi);
        tvDatetime = (TextView) findViewById(R.id.tvDatetime);
        tvId = (TextView)findViewById(R.id.tvId);
        tvName = (TextView) findViewById(R.id.tvHienthi);
        tvBuyOnl = (TextView) findViewById(R.id.tvBuyOnl);
        spinnerXem = (Spinner) findViewById(R.id.spinnerLoaitiente);
        btnReturn = (Button)findViewById(R.id.btnReturn);

        Bundle db = getIntent().getExtras();
        if(db != null){
            dateTime = db.getString("dateTime");
            listId = db.getStringArrayList("listId");
            listName = db.getStringArrayList("listName");
            listBuyOnl = db.getStringArrayList("listBuyOnl");
        }

        listId.add(listId.size(), "VND");
        listName.add(listName.size(), "VIETNAM DONG");
        listBuyOnl.add(listBuyOnl.size(), "1000");

        tvDatetime.setText("Ngày cập nhật: " + dateTime);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listName);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerXem.setAdapter(arrayAdapter);

        spinnerXem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                layoutHienthi.setVisibility(View.VISIBLE);
                //tvId.setText("Mã tiền tệ: " + giaTienTeArrayList.get(i).getId());
                tvId.setText("Mã tiền tệ: " + listId.get(i));
                tvBuyOnl.setText("Giá trị: " + listBuyOnl.get(i) + "VND");
                Toast.makeText(XemTyGia.this, listName.get(i),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intReturn = new Intent(XemTyGia.this, MainActivity.class);
                startActivity(intReturn);
            }
        });
    }
}
