package com.atinasoft.mucahitkambur.lolping;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.googlecode.jpingy.Ping;
import com.googlecode.jpingy.PingArguments;
import com.googlecode.jpingy.PingResult;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private TextView txMs;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtSunucu;
    private TextView txtDurum;
    private ImageView imTeemo;
    private List<String> arrayList;
    private String[] ipAdress;
    private Spinner spinner;
    private Button btnPingle;
    private int finalPing;
    private PingResult result;
    private Snackbar snacbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        // Font ayarları
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/frizquadrata.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //IP getirme
        ipAdress = getResources().getStringArray(R.array.ip);

        // Spinner ayarları
        arrayList = Arrays.asList(getResources().getStringArray(R.array.server));
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, arrayList);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(customSpinnerAdapter);

        btnPingle = (Button)findViewById(R.id.btnPingle);
        btnPingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = String.valueOf(spinner.getSelectedItemPosition());
                errorSnackbar(selected);

            }
        });
    }

    protected void errorSnackbar(String s){

        snacbar = Snackbar.
                make(coordinatorLayout, getString(R.string.internet), Snackbar.LENGTH_LONG);

        View snackbarView = snacbar.getView();

        TextView txtSnackbar = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        txtSnackbar.setTextColor(getResources().getColor(R.color.kenar));
        snackbarView.setBackgroundColor(getResources().getColor(R.color.mainStatus));

        try{
            if(checkConnection()){
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(ipAdress[Integer.valueOf(s)]);
            }else{

                snacbar.show();
            }
        }catch (Exception e){
            snacbar.show();
        }

    }


    protected boolean checkConnection(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {

            txMs = (TextView)findViewById(R.id.txtMS);
            imTeemo = (ImageView)findViewById(R.id.imgTemo);
            txtSunucu = (TextView)findViewById(R.id.txtSunucu);
            txtDurum = (TextView)findViewById(R.id.txtDurum);

            txMs.setVisibility(View.INVISIBLE);
            imTeemo.setVisibility(View.VISIBLE);
            txtSunucu.setText(getString(R.string.tPinging));
            txtDurum.setVisibility(View.INVISIBLE);

        }


        @Override
        protected void onPostExecute(String s) {

            txMs = (TextView)findViewById(R.id.txtMS);
            imTeemo = (ImageView)findViewById(R.id.imgTemo);
            txtSunucu = (TextView)findViewById(R.id.txtSunucu);

            txMs.setVisibility(View.VISIBLE);
            imTeemo.setVisibility(View.INVISIBLE);
            txtSunucu.setText(getString(R.string.tServer));

            pingDurumu(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {

            txMs = (TextView)findViewById(R.id.txtMS);
            txMs.setText(values[0]);

        }

        @Override
        protected String doInBackground(String... params) {

            try{
                PingArguments arguments = new PingArguments.Builder().url(params[0]).timeout(5000).count(3).bytes(32).build();
                result = Ping.ping(arguments, Ping.Backend.UNIX);

                finalPing = Math.round(result.rtt_avg());

                publishProgress(String.valueOf(finalPing));

            } catch (Exception e){
                finalPing = 0;
                errorSnackbar("null");
            }

            return String.valueOf(finalPing);

        }

        void pingDurumu(String ping){

            int convertPing = Integer.valueOf(ping);

            txtDurum = (TextView)findViewById(R.id.txtDurum);
            txtDurum.setVisibility(View.VISIBLE);

            Log.d("deneme", ping);

            if(convertPing > 1 && convertPing < 100){
                txtDurum.setText(getString(R.string.pingIyi));
                txtDurum.setTextColor(getResources().getColor(R.color.iyi));
            }else if(convertPing >= 100 && convertPing < 180){
                txtDurum.setText(getString(R.string.pingOrta));
                txtDurum.setTextColor(getResources().getColor(R.color.orta));
            }else if(convertPing >= 180){
                txtDurum.setText(getString(R.string.pingKotu));
                txtDurum.setTextColor(getResources().getColor(R.color.kotu));
            }else{
                txtDurum.setText("");
            }

        }
    }
}

