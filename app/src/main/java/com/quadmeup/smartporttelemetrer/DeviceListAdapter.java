/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter {

    final private Context context;
    private ArrayList<BluetoothDevice> data;

    public DeviceListAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {

        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setData(ArrayList<BluetoothDevice> newData) {
        data = newData;
    }

    private class ViewHolderPattern {
        TextView name;
        TextView mac;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolderPattern viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.device_list, parent, false);

            viewHolder = new ViewHolderPattern();

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.mac = (TextView) convertView.findViewById(R.id.mac);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderPattern) convertView.getTag();
        }

        final BluetoothDevice current = data.get(position);

        viewHolder.name.setText(current.getName());
        viewHolder.mac.setText(current.getAddress());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataProvider dataProvider = new DataProvider(context);
                dataProvider.put(DataProvider.KEY_BT_MAC, current.getAddress());
                dataProvider.put(DataProvider.KEY_BT_NAME, current.getName());

                Toast.makeText(context, context.getResources().getString(R.string.bt_device_selected), Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask((Activity) context);
            }
        });

        return convertView;
    }


}
