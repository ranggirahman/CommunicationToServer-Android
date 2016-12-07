package com.example.cortex.komunikasiserver;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tvHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHasil = (TextView) findViewById(R.id.tvHasil);
    }

    private class AmbilData extends AsyncTask<String, Integer, String >{
        protected String doInBackground(String... strUrl) {
            Log.v("yw", "mulai ambil data");
            String hasil="";
            // ambil data dari internet
            InputStream inStream = null;
            int len = 15000;  // buffer

            try{
                URL url = new URL(strUrl[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // timeout
                conn.setReadTimeout(1000 /* milliseconds */);
                conn.setConnectTimeout(5000 /* milliseconds */);

                conn.setRequestMethod("GET");
                conn.connect();
                int response = conn.getResponseCode();
                inStream = conn.getInputStream();   // ambil stream data

                // konversi stream ke string
                Reader r = null;
                r = new InputStreamReader(inStream, "UTF-8");
                char[] buffer = new char[len];
                r.read(buffer);
                hasil = new String(buffer);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(inStream != null){
                    try {
                        inStream.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            return hasil;
        }

        protected void onPostExecute(String result) {
            tvHasil.setText(result);
        }
    }

    public void onClick(View v){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            // contoh mengambil ip
            new AmbilData().execute("http://api.ipify.org/");   // url jadi parameter
            // contoh mengambil data buku
            // new AmbilData().execute("https://www.googleapis.com/books/v1/volumes/yZ1APgAACAAJ");   // url jadi parameter
        }else{
            // tampilkan error
            Toast t = Toast.makeText(getApplicationContext(), "Tidak ada Koneksi!", Toast.LENGTH_LONG);
            t.show();
        }
    }

}
