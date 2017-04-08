package com.example.quyenhua.bttygiadongtien;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quyenhua.bttygiadongtien.database.GiaTienTe;
import com.example.quyenhua.bttygiadongtien.loadurl.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String fileSaveName = "savedata.xml";

    Button btnXemtg, btnTinhtg;
    ArrayList<String> listID, listName, listBuyOnl;
    String dateTime1 ="";

    ArrayList<GiaTienTe> arrayTiente = new ArrayList<>();

    String filePath = "SaveXMLdata";
    String fileName = "savefile.xml";


    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

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

    private void readDataFromExternalStorage() {

        String data = askPermissionAndReadFile();
        //Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
        XMLDOMParser parser = new XMLDOMParser();
        Document doc = parser.getDocument(data);

        NodeList nodeList = doc.getElementsByTagName("Exrate");
        NodeList dateTime = doc.getElementsByTagName("ExrateList");
        Element date = (Element) dateTime.item(0);
        dateTime1 = parser.getValue(date, "DateTime");

        for(int i=0;i<nodeList.getLength();i++) {
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();

            listID.add(i, namedNodeMap.item(0).getNodeValue());
            listName.add(i, namedNodeMap.item(1).getNodeValue());
            listBuyOnl.add(i, namedNodeMap.item(3).getNodeValue());

            String id = namedNodeMap.item(0).getNodeValue();
            String name = namedNodeMap.item(1).getNodeValue();
            String value = namedNodeMap.item(3).getNodeValue();

            arrayTiente.add(new GiaTienTe(id, name, value));
        }
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

            if(s != ""){
                askPermissionAndWriteFile(s);
            }
            else{
                Toast.makeText(MainActivity.this, "Chua cap nhat duoc du lieu hien tai", Toast.LENGTH_SHORT).show();
            }

            readDataFromExternalStorage();
            /*String data = askPermissionAndReadFile();
            Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
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

                String id = namedNodeMap.item(0).getNodeValue();
                String name = namedNodeMap.item(1).getNodeValue();
                String value = namedNodeMap.item(3).getNodeValue();

                arrayTiente.add(new GiaTienTe(id, name, value));

            }*/
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

    private void askPermissionAndWriteFile(String data) {
        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //
        if (canWrite) {
            this.writeFile(data);
        }
    }

    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Kiểm tra quyền
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {

                // Nếu không có quyền, cần nhắc người dùng cho phép.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    private void writeFile(String data) {
        // Thư mục gốc của SD Card.
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Save to: " + path);

        try {
            File myFile = new File(path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();

            Toast.makeText(getApplicationContext(), fileName + " saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String askPermissionAndReadFile() {
        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        String data = "";
        if (canRead) {
            data = this.readFile();
        }
        return data;
    }

    private String readFile() {
        // Thư mục gốc của SD Card.
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Read file: " + path);

        String s = "";
        String fileContent = "";
        try {
            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((s = myReader.readLine()) != null) {
                fileContent += s + "\n";
            }
            myReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
