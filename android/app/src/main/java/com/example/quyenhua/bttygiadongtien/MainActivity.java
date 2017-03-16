package com.example.quyenhua.bttygiadongtien;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.quyenhua.bttygiadongtien.database.GiaTienTe;
import com.example.quyenhua.bttygiadongtien.loadurl.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static GiaTienTe save;
    Button btnXemtg, btnTinhtg;
    ArrayList<String> listID, listName, listBuyOnl;
    String dateTime1 ="";

    ArrayList<GiaTienTe> tienTeArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnXemtg = (Button)findViewById(R.id.btnXemTyGia);
        btnTinhtg = (Button)findViewById(R.id.btnTinhTyGia);

        listID = new ArrayList<>();
        listName = new ArrayList<>();
        listBuyOnl = new ArrayList<>();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadDataURL().execute("https://www.vietcombank.com.vn/exchangerates/ExrateXML.aspx");
            }
        });

        btnXemtg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intXemtg = new Intent(MainActivity.this, XemTyGia.class);
                intXemtg.putExtra("dateTime", dateTime1);
                intXemtg.putStringArrayListExtra("listId", listID);
                intXemtg.putStringArrayListExtra("listName", listName);
                intXemtg.putStringArrayListExtra("listBuyOnl", listBuyOnl);
                startActivity(intXemtg);
            }
        });

        btnTinhtg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intTinhtg = new Intent(MainActivity.this, TinhTyGia.class);
                intTinhtg.putExtra("dateTime", dateTime1);
                intTinhtg.putStringArrayListExtra("listId", listID);
                intTinhtg.putStringArrayListExtra("listName", listName);
                intTinhtg.putStringArrayListExtra("listSotien", listBuyOnl);
                startActivity(intTinhtg);
            }
        });
    }

    class LoadDataURL extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            String url = LoadDataFromURL(strings[0]);
            return url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document doc = parser.getDocument(s);
            NodeList nodeList = doc.getElementsByTagName("Exrate");
            NodeList dateTime = doc.getElementsByTagName("ExrateList");
            Element date = (Element) dateTime.item(0);
            dateTime1 = parser.getValue(date, "DateTime");

            for(int i=0;i<nodeList.getLength();i++) {
                NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();

                listID.add(i,namedNodeMap.item(0).getNodeValue());
                listName.add(i,namedNodeMap.item(1).getNodeValue());
                listBuyOnl.add(i,namedNodeMap.item(3).getNodeValue());

                String id = namedNodeMap.item(0).getNodeName();
                String name = namedNodeMap.item(1).getNodeName();
                String value = namedNodeMap.item(3).getNodeName();

                tienTeArrayList.add(new GiaTienTe(id, name, value));

            }
            //Toast.makeText(MainActivity.this, save.getContent(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainActivity.this, dateTime1, Toast.LENGTH_LONG).show();
        }
    }

    private static String LoadDataFromURL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
