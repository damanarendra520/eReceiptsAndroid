package com.narendradama.ereceipts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptAdapter extends ArrayAdapter<Receipts> {

    Context mContext;
    int mLayourResID;
    private ArrayList receiptsList;

    Bitmap bitmap = null;
    public ReceiptAdapter(Context context, int resource, ArrayList list) {
        super(context, resource, list);
        this.mContext = context;
        this.mLayourResID = resource;
        this.receiptsList = list;
    }

    @Override
    public Receipts getItem(int position) {
        return super.getItem(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        PlaceHolder holder = null;

        if (row == null){
            //create a new View
            LayoutInflater inflate = LayoutInflater.from(mContext);
            row = inflate.inflate(mLayourResID,parent,false);

            holder = new PlaceHolder();

            holder.receiptView = (TextView) row.findViewById(R.id.receiptNumber);
            holder.placeView = (TextView) row.findViewById(R.id.Place);
            holder.dateView = (TextView) row.findViewById(R.id.date);

            row.setTag(holder);
        }else{
            holder = (PlaceHolder) row.getTag();
        }

        Map<String, Integer> map1 = (Map<String, Integer>) receiptsList.get(position);
        for (Map.Entry<String, Integer> entry : map1.entrySet())
        {
            if (entry.getKey().equals("receiptNumber")){
                String receiptAsString = String.valueOf(entry.getValue());
                holder.receiptView.setText(receiptAsString);
            }

            if (entry.getKey().equals("place")){
                String placeAsString = String.valueOf(entry.getValue());
                holder.placeView.setText(placeAsString);
            }

            if (entry.getKey().equals("date")){
                String dateAsString = String.valueOf(entry.getValue());
                holder.dateView.setText(dateAsString);
            }

        }

        return row;
    }

    private static class PlaceHolder{
        TextView receiptView;
        TextView placeView;
        TextView dateView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void updateReceiptsList(ArrayList<HashMap<String, String>> newlist) {
        receiptsList.clear();
        receiptsList.addAll(newlist);
        this.notifyDataSetChanged();
    }
}
