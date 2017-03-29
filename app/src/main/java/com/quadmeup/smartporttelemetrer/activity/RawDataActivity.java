/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.quadmeup.smartporttelemetrer.activity;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.quadmeup.smartporttelemetrer.ConnectionState;
import com.quadmeup.smartporttelemetrer.DataProvider;
import com.quadmeup.smartporttelemetrer.DataService;
import com.quadmeup.smartporttelemetrer.DeviceListAdapter;
import com.quadmeup.smartporttelemetrer.R;
import com.quadmeup.smartporttelemetrer.RawDataListAdapter;
import com.quadmeup.smartporttelemetrer.smartport.DataValueObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RawDataActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final private String TAG = "SPBT";

    private DataService dataService;
    boolean dataServiceBound = false;

    private ListView sensorList;
    private RawDataListAdapter listAdapter;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection dataServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataService.DataServiceBinder binder = (DataService.DataServiceBinder) service;
            dataService = binder.getService();
            Log.i(TAG, "DataService connected in ConnectActivity");
            dataServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            dataServiceBound = false;
            Log.i(TAG, "DataService unbinded in ConnectActivity");
        }
    };

    private class PopulateUI extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String result) {
            if (dataService != null) {
                Map<Integer, DataValueObject> raw = dataService.getRawDataValues();

                ArrayList<DataValueObject> list = new ArrayList<>();
                list.addAll(raw.values());

                listAdapter.setData(list);
                listAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    private void updateUI() {
        new PopulateUI().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        sensorList = (ListView) findViewById(R.id.sensor_list);
        listAdapter = new RawDataListAdapter(this);
        sensorList.setAdapter(listAdapter);

        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                updateUI();
            }
        };
        timer.schedule(doAsynchronousTask, 0, 200); //execute in every 50000 ms
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
        getMenuInflater().inflate(R.menu.raw_data, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, DataService.class);
        bindService(intent, dataServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unbind from the service
        if (dataServiceBound) {
            Log.i(TAG, "RawDataActivity destroyed");
            unbindService(dataServiceConnection);
            dataServiceBound = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();
    }
}
