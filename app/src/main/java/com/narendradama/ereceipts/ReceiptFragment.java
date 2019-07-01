package com.narendradama.ereceipts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.narendradama.ereceipts.Database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptFragment extends Fragment {

    private static final String FRAGMENT_TAG = "ReceiptFragment";

    DBHelper myDB;

    public ListView listView;
    private ReceiptAdapter receiptAdapter;

    public static ReceiptFragment newInstance() {
        ReceiptFragment fragment = new ReceiptFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.receipt_fragment, container, false);
        myDB = new DBHelper(view.getContext());

        final ArrayList<HashMap<String, String>> userList = myDB.GetReceiptData();

        //receiptAdapter = new ReceiptAdapter(view.getContext(), R.layout.receipt_view_row, array_list);

        listView = (ListView) view.findViewById(R.id.receiptList);
        receiptAdapter = new ReceiptAdapter(view.getContext(), R.layout.receipt_view_row, userList);

        if (listView != null){
            listView.setAdapter(receiptAdapter);
        }


        final SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.receiptList);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                // set item background
                deleteItem.setBackground(R.color.colorAccent);
                // set item width
                deleteItem.setWidth(150);
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                MainActivity activity = (MainActivity) getActivity();

                switch (index) {
                    case 0:
                        Map<String, String > map1 = (Map<String, String>) userList.get(position);
                        for (Map.Entry<String, String > entry : map1.entrySet())
                        {
                            if (entry.getKey().equals("ID"))
                                myDB.deleteReceipt(Integer.valueOf(entry.getValue()));
                        }
                        userList.remove(position);
                        receiptAdapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(view.getContext(), ReceiptsDetails.class).putExtra("ReceiptData", userList.get(position)));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final ArrayList<HashMap<String, String>> userList = myDB.GetReceiptData();
        receiptAdapter.updateReceiptsList(userList);
        receiptAdapter.notifyDataSetChanged();
    }

}
