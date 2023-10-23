package com.example.rc_car;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private LayoutInflater m_layoutInflater;
    private int m_resourceView;
    private ArrayList<BluetoothDevice> m_devices = new ArrayList<>();

    public DeviceListAdapter(Context context, int resource, ArrayList<BluetoothDevice> devices) {
        super(context, resource);

        m_layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_resourceView = resource;
        m_devices = devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = m_layoutInflater.inflate(m_resourceView, null);

        BluetoothDevice device = m_devices.get(position);

        TextView tvName = convertView.findViewById(R.id.tvNameDevice);
        TextView tvAddress = convertView.findViewById(R.id.tvAddressDevice);

        tvName.setText(device.getName());
        tvAddress.setText(device.getAddress());

        return convertView;
    }
}
