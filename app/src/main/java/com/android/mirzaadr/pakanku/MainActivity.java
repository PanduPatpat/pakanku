package com.android.mirzaadr.pakanku;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.mirzaadr.pakanku.Adapter.ListBahanAdapter;
import com.android.mirzaadr.pakanku.Dao.BahanDAO;
import com.android.mirzaadr.pakanku.Dao.DBHelper;
import com.android.mirzaadr.pakanku.Dao.HewanDAO;
import com.android.mirzaadr.pakanku.Dao.VersionDAO;
import com.android.mirzaadr.pakanku.Internet.NetworkUtils;
import com.android.mirzaadr.pakanku.Manager.AlertDialogManager;
import com.android.mirzaadr.pakanku.Model.Bahan;
import com.android.mirzaadr.pakanku.Model.Hewan;
import com.android.mirzaadr.pakanku.Model.Version;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Bahan> mListBahan;
    private BahanDAO mBahanDao;
    private HewanDAO mHewanDao;
    private VersionDAO mVersionDao;

    Boolean internet = false;
    Boolean internet_error = false;

    AlertDialogManager alert = new AlertDialogManager();

    String version;
    String tanggal;

    private Dialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mBahanDao = new BahanDAO(this);
        mVersionDao = new VersionDAO(this);
        mHewanDao = new HewanDAO(this);
        // fill the listView
        mListBahan = mBahanDao.getAllBahan();

        NetworkUtils utils = new NetworkUtils(MainActivity.this);
        if(utils.isConnectingToInternet())
        {
            internet = true;

            if(mListBahan != null && !mListBahan.isEmpty() && mVersionDao.getTopVersion() !=null) {

                Version versi = mVersionDao.getTopVersion();

                version = versi.getVersiBahan();
                tanggal = versi.getTanggal();

                new InputHewan().execute();
                cekUpdate(version, tanggal);

            }
            else {

                new DataFetcherTask().execute();
                new InputHewan().execute();

            }

        }
        else
        {

            if(mListBahan != null && !mListBahan.isEmpty()) {

                Toast.makeText(getBaseContext(), "No Internet Connection, no update data", Toast.LENGTH_SHORT).show();

            } else {

                alert.showAlertDialog(MainActivity.this, "No update data..", "Require to connect internet for first use", false);

            }

        }


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


 /*       Button buat = (Button) findViewById(R.id.BuatPakan);
        buat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuatPakan.class);
                startActivity(intent);
            }
        });

        Button info = (Button) findViewById(R.id.Informasi);
        info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });*/
    }

    class DataFetcherTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Silahkan tunggu");
        }

        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server
            // Http Request Code start

            if(!internet_error && internet) {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://pakanku.patpatstudio.com/android/cekall.php");
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    serverData = EntityUtils.toString(httpEntity);
                    Log.d("response", serverData);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                }
            }
            else {
                internet_error = true;
                this.cancel(true);
            }

// Http Request Code end
// Json Parsing Code Start
            //success = jsonObj.getInt(TAG_SUCCESS);

            if(!internet_error && internet){

                try {
                    //bahanArrayList = new ArrayList<Bahan>();
                    JSONObject jsonObject = new JSONObject(serverData);
                    JSONArray jsonArray = jsonObject.getJSONArray("bahan");

                    Version version = new Version();

                    String versi = jsonObject.getString("versi");
                    String tanggal = jsonObject.getString("tanggal");

                    version.setVersiBahan(versi);
                    version.setTanggal(tanggal);

                    mVersionDao.addVersionJson(version);

                    //mVersionDao.createVersion(versi, tanggal);

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        if(internet) {

                            JSONObject jsonObjectBahan = jsonArray.getJSONObject(i);
                            int id_bahan = jsonObjectBahan.getInt(DBHelper.BAHAN_ID);
                            String nama_bahan = jsonObjectBahan.getString(DBHelper.NAMA_BAHAN);
                            double bk_prs = jsonObjectBahan.getDouble(DBHelper.BK_PRS);
                            double pk_prs = jsonObjectBahan.getDouble(DBHelper.PK_PRS);
                            String kategori = jsonObjectBahan.getString(DBHelper.KATEGORI);
                            int harga = jsonObjectBahan.getInt(DBHelper.HARGA);

                            Bahan bahan = new Bahan();
                            bahan.setIdBahan(id_bahan);
                            bahan.setNamaBahan(nama_bahan);
                            bahan.setBk_prs(bk_prs);
                            bahan.setPk_prs(pk_prs);
                            bahan.setKategori(kategori);
                            bahan.setHarga(harga);

                            int id = id_bahan;

                            //if (mBahanDao.getBahanById(id).getNama_bahan() == null)
                            //{
                            mBahanDao.addBahanJson(bahan);// Inserting into DB
                            //}
                            //else
                            //{
                            //mBahanDao.updateBahanJSON(bahan);
                            //}

                        }
                        else {
                            internet_error = true;
                            this.cancel(true);
                        }


                    }


                } catch (JSONException e) {
                    internet_error = true;
                    e.printStackTrace();
                }


            }
            else {

                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pDialog.dismiss();
            Toast.makeText(getBaseContext(), "Internet connection error", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();

            if(internet_error) {

                Toast.makeText(getBaseContext(), "Internet connection error", Toast.LENGTH_SHORT).show();

            }



        }
    }

    class InputHewan extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server
            // Http Request Code start

            if(!internet_error && internet) {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://pakanku.patpatstudio.com/android/cekhewan.php");
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    serverData = EntityUtils.toString(httpEntity);
                    Log.d("response", serverData);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                }
            }
            else {
                internet_error = true;
                this.cancel(true);
            }
// Http Request Code end
// Json Parsing Code Start
            //success = jsonObj.getInt(TAG_SUCCESS);
            if(!internet_error && internet) {
                try {
                    //bahanArrayList = new ArrayList<Bahan>();
                    JSONObject jsonObject = new JSONObject(serverData);
                    JSONArray jsonArray = jsonObject.getJSONArray("hewan");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectBahan = jsonArray.getJSONObject(i);
                        int idhewan = jsonObjectBahan.getInt("idhewan");
                        String hewan = jsonObjectBahan.getString("nama_hewan");
                        String tujuan = jsonObjectBahan.getString("tujuan");
                        double hijauan = jsonObjectBahan.getDouble("hijauan");
                        double konsentrat = jsonObjectBahan.getDouble("konsentrat");
                        double bk_hewan = jsonObjectBahan.getDouble("keb_bk");
                        double pk_hewan = jsonObjectBahan.getDouble("keb_pk");
                        int harga_jual = jsonObjectBahan.getInt("harga_jual");

                        Hewan hewanxx = new Hewan();
                        hewanxx.setIdhewan(idhewan);
                        hewanxx.setHewan(hewan);
                        hewanxx.setTujuan(tujuan);
                        hewanxx.setHijau(hijauan);
                        hewanxx.setKonsentrat(konsentrat);
                        hewanxx.setBk_hewan(bk_hewan);
                        hewanxx.setPk_hewan(pk_hewan);
                        hewanxx.setHargajual(harga_jual);

                        mHewanDao.addHewanJson(hewanxx);// Inserting into DB


                    }


                } catch (JSONException e) {
                    this.cancel(true);
                    e.printStackTrace();
                }
            }
            else {
                this.cancel(true);
            }
//Json Parsing code end
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            internet_error = true;
            Toast.makeText(getBaseContext(), "Internet connection error", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(internet_error) {
                Toast.makeText(getBaseContext(), "Internet connection error", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void cekUpdate(final String version, final String tanggal) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Silahkan tunggu...");
            }

            @Override
            protected String doInBackground(String... params) {

                String versi = params[0];
                String tgl = params[1];

                InputStream is = null;
                String result = null;

                if(!internet_error && internet) {

                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet("http://pakanku.patpatstudio.com/android/update.php?versi=" + versi + "&tanggal=" + tgl);

                        HttpResponse response = httpClient.execute(httpGet);

                        //response = httpClient.execute("patpatstudio.com", "/pakanku/update.php?versi=" + versi + "&tanggal=" + tgl);
                        HttpEntity entity = response.getEntity();
                        String htmlResponse = EntityUtils.toString(entity);
                        result = htmlResponse;
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                        internet_error = true;
                        this.cancel(true);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        internet_error = true;
                        this.cancel(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        internet_error = true;
                        this.cancel(true);
                    }
                }
                else {
                    internet_error = true;
                    this.cancel(true);
                }



                return result;


            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                Toast.makeText(getBaseContext(), "Internet connection error", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }

            @Override
            protected void onPostExecute(String result){

                if(internet_error || !internet) {

                    loadingDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Internet connection error", Toast.LENGTH_SHORT).show();

                }
                else {

                    loadingDialog.dismiss();

                    String hasil = result.trim();

                    Toast.makeText(getBaseContext(), hasil, Toast.LENGTH_SHORT).show();

                    if(result!=null)
                    {
                        if (hasil.equals("update")){

                            showUpdateDialogConfirmation();

                        }
                        else if (hasil.equals("tidak"))
                        {
                            alert.showAlertDialog(MainActivity.this, "Checking complete..", "No data update", false);
                        }

                    }
                    else
                    {
                        alert.showAlertDialog(MainActivity.this, "Checking Failed..", "Check your internet connection", false);
                    }

                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(version, tanggal);

    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
                //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                internet = true;
            }else{
                //Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
                internet = false;
            }
        }
    };

    @Override
    protected void onResume() {
        this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mConnReceiver);
        super.onPause();
    }

    private void showUpdateDialogConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Update");
        alertDialogBuilder.setMessage("Are you sure you want to update data?");

        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete the company and refresh the list
                if(mBahanDao != null) {

                    new DataFetcherTask().execute();

                    //refresh the listView

                    Version versixx = new Version();

                    versixx.setVersiBahan(version);
                    versixx.setTanggal(tanggal);

                    mVersionDao.deleteVersion(versixx);

                }

                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Update succesfull", Toast.LENGTH_SHORT).show();
            }
        });

        // set neutral button OK
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }



    public void BuatClick(View v) {
        Intent intent = new Intent(MainActivity.this, BuatPakan.class);
        startActivity(intent);
    }

    public void InfoClick(View v) {
        Intent intent = new Intent(MainActivity.this, InformationActivity.class);
        startActivity(intent);
    }

    public void UntungClick(View v) {
        Intent intent = new Intent(MainActivity.this, CekUntung.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mConnReceiver);
        mBahanDao.close();
        mHewanDao.close();
        mVersionDao.close();
    }


}
