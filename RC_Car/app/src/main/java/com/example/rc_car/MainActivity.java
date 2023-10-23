package com.example.rc_car;

import static android.R.color.holo_blue_dark;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    private Button m_backButton;
    private Button m_forwardButton;
    private Button m_leftButton;
    private Button m_rightButton;
    private BluetoothAdapter m_bluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private ProgressDialog m_progressDialog;
    private ArrayList<BluetoothDevice> m_devices = new ArrayList<>();
    private ListView listDevices;
    private DeviceListAdapter m_deviceListAdapter;
    private BluetoothSocket m_bluetoothSocket;
    private OutputStream m_outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_backButton = findViewById(R.id.backButton);
        m_forwardButton = findViewById(R.id.forwardButton);
        m_leftButton = findViewById(R.id.leftButton);
        m_rightButton = findViewById(R.id.rightButton);

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (m_bluetoothAdapter == null) {
            Log.d(TAG, "onCreate: device does not support bluetooth");
            finish();
        }
        enableBluetooth();

        m_deviceListAdapter = new DeviceListAdapter(this, R.layout.device_item, m_devices);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemSearch) {
            searchDevices();
        } else if (item.getItemId() == R.id.itemExit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void searchDevices() {
        enableBluetooth();

        checkPermissionLocation();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (!m_bluetoothAdapter.isDiscovering()) {
                m_bluetoothAdapter.startDiscovery();
            }

            if (m_bluetoothAdapter.isDiscovering()) {
                m_bluetoothAdapter.cancelDiscovery();
                m_bluetoothAdapter.startDiscovery();
            }

            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            registerReceiver(m_receiver, filter);
        }
    }

    private void showListDeices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Found devices");

        View view = getLayoutInflater().inflate(R.layout.list_devices_view, null);
        listDevices = view.findViewById(R.id.list_devices);
        listDevices.setAdapter(m_deviceListAdapter);
        listDevices.setOnItemClickListener(itemClickListener);

        builder.setView(view);
        builder.setNegativeButton("OK", null);
        builder.create();
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionLocation() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int check = checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            check += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");

            if (check != 0) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1002);
            }
        }
    }

    private void enableBluetooth() {
        Log.d(TAG, "enableBluetooth()");
        if (!m_bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableBluetooth: bluetooth off");
            Intent intent = new Intent(m_bluetoothAdapter.ACTION_REQUEST_ENABLE);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            }
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }
    }

    private void showTestMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (!m_bluetoothAdapter.isEnabled()) {
                enableBluetooth();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    public void onClickBack(View view) {
        m_backButton.setBackgroundColor(holo_blue_dark);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(m_backButton.isPressed()) {
                    String command = "3";
                }
                if(!m_backButton.isPressed()) {
                    String command = "5";
                }
            }
        }).start();
    }

    @SuppressLint("ResourceAsColor")
    public void onClickLeft(View view) {
        m_leftButton.setBackgroundColor(holo_blue_dark);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(m_backButton.isPressed()) {
                    String command = "2";
                }
                if(!m_backButton.isPressed()) {
                    String command = "6";
                }
            }
        }).start();
    }

    @SuppressLint("ResourceAsColor")
    public void onClickForward(View view) {
        m_forwardButton.setBackgroundColor(holo_blue_dark);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(m_backButton.isPressed()) {
                    String command = "1";
                }
                if(!m_backButton.isPressed()) {
                    String command = "5";
                }
            }
        }).start();
    }
    @SuppressLint("ResourceAsColor")
    public void onClickRight(View view) {
        m_rightButton.setBackgroundColor(holo_blue_dark);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(m_backButton.isPressed()) {
                    String command = "4";
                }
                if(!m_backButton.isPressed()) {
                    String command = "6";
                }
            }
        }).start();
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice device = m_devices.get(position);

            startConnection(device);
        }
    };

    private void startConnection(BluetoothDevice device) {
        if (device != null) {
            try {
                Method method = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                m_bluetoothSocket = (BluetoothSocket) method.invoke(device, 1);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                }
                m_bluetoothSocket.connect();

                m_outputStream = m_bluetoothSocket.getOutputStream();

                showTestMessage("connection successful");
            }catch (Exception e) {
                showTestMessage("connection failed");
                e.printStackTrace();
            }

        }
    }

    private void setMessage(String command) {
        byte[] buffer = command.getBytes();

        if(m_outputStream != null) {
            try {
                m_outputStream.write(buffer);
                m_outputStream.flush();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BroadcastReceiver m_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Log.d(TAG, "onReceive: ACTION_DISCOVERY_STARTED");

                showTestMessage("Device search started");
                m_progressDialog = ProgressDialog.show(MainActivity.this, "Device search", "Please wait...");

            }

            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Log.d(TAG, "onReceive: ACTION_DISCOVERY_FINISHED");
                showTestMessage("Finished searching for devices");

                m_progressDialog.dismiss();

                showListDeices();
            }

            if(action.equals(BluetoothDevice.ACTION_FOUND)) {
                Log.d(TAG, "onReceive: ACTION_FOUND");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device != null) {
                    if(!m_devices.contains(device))
                        m_deviceListAdapter.add(device);
                }
            }
        }
    };
}