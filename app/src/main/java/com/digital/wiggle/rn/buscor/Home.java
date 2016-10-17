package com.digital.wiggle.rn.buscor;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.FragmentActivity;


import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class Home extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static TextView balance;
    final Context context = this;
    private static Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        balance = (TextView) findViewById(R.id.balance);
        test = (Button)findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, BusStations.class);
                startActivity(i);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new GetBalance().execute();
//        printKeyHash(Home.this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.makeDepo) {
            showDialog(context);
            // Handle the camera action
        } else if (id == R.id.logout) {
            logout();
        } else if (id == R.id.bus_station) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("wiggle_buscor_user_id", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("memID").commit();
        finish();
    }

    private void showDialog(Context context){
        // custom dialog
        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        dialog.setContentView(R.layout.recharge_layout_custom);
//        dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        final EditText recharge = (EditText)dialog.findViewById(R.id.enterVoucherPin);
        Button submit = (Button)dialog.findViewById(R.id.btnRecharge);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = recharge.getText().toString();
                new LoadAccount().execute(pin);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public String getUserID(){
        SharedPreferences sharedPreferences = getSharedPreferences("wiggle_buscor_user_id", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("memID", "");
        return userID;
    }

    class LoadAccount extends AsyncTask<String, String, JSONObject>{
        JSONParser jsonParser = new JSONParser();
        ProgressDialog progressDialog;
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            try{
                HashMap<String, String>map = new HashMap<>();
                map.put("method", "recharge");
                map.put("user_id", getUserID());
                map.put("voucher_pin", params[0]);

                JSONObject jsonObject = jsonParser.makeHttpRequest(Constants.URL, "POST", map);

                if (jsonObject != null){
                    Log.e("JSON result", jsonObject.toString());
                    return jsonObject;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Home.this);
            progressDialog.setMessage("Authenticating...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param jsonObject The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (progressDialog != null){
                progressDialog.dismiss();
            }

            try {
                String message = jsonObject.getString("message");
                Toast.makeText(Home.this, message, Toast.LENGTH_SHORT).show();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    class GetBalance extends AsyncTask<String, String, JSONObject>{
        JSONParser jsonParser = new JSONParser();
        ProgressDialog progressDialog;

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Home.this);
            progressDialog.setMessage("Authenticating...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param jsonObject The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (progressDialog != null){
                progressDialog.dismiss();
            }

            try{

                if (jsonObject != null){
                    String balance = jsonObject.getString("balance");
                    Home.balance.setText("Your current Buscor balance is "+balance);
                }else{
                    Toast.makeText(Home.this, "could not get relevant data", Toast.LENGTH_SHORT).show();
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

        @Override
        protected JSONObject doInBackground(String... params) {
            try{
                HashMap<String, String>map = new HashMap<>();
                map.put("method", "getAccountDetails");
                map.put("user_id", getUserID());
                JSONObject jsonObject = jsonParser.makeHttpRequest(Constants.URL, "POST", map);
                if (jsonObject != null){
                    Log.e("JSON result", jsonObject.toString());
                    return jsonObject;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
