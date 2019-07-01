package com.narendradama.ereceipts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.text.InputType;

import com.narendradama.ereceipts.Database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    // Variable creation.
    public ListView receiptsList;
    private String toDoList = "";

    TextView actionBar;
    Boolean isFirstTab = true;

    Realm realm;

    ListView listView ;

    private ArrayList toDoListArray;

    private ArrayAdapter arrayAdapter;

    public static ArrayList<String> toDoListArrayToDisplay = new ArrayList<String>();

    private DBHelper mydb ;

    private Toolbar mTopToolbar;

    float dX, dY;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_bills:
                    actionBar = (TextView)mTopToolbar.findViewById(R.id.toolbarTextView);
                    actionBar.setText("Bills");
                    isFirstTab = true;
                    selectedFragment = ReceiptFragment.newInstance();
                    break;
                case R.id.navigation_todo:
                    actionBar = (TextView)mTopToolbar.findViewById(R.id.toolbarTextView);
                    actionBar.setText("To Do");
                    isFirstTab = false;
                    selectedFragment = ToDoListFragment.newInstance();
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toDoListArrayToDisplay = readToDoListRecord();

        mydb = new DBHelper(MainActivity.this);

        final ArrayList<HashMap<String, String>> userList = mydb.GetReceiptData();

        mTopToolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(mTopToolbar);
        actionBar = (TextView)mTopToolbar.findViewById(R.id.toolbarTextView);
        actionBar.setText("Bills");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Fragment selectedFragment = null;
        selectedFragment = ReceiptFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFirstTab) {
                    startActivity(new Intent(MainActivity.this, AddBills.class));

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("To Do List");

                    final EditText input = new EditText(v.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toDoList = input.getText().toString();
                            addToDoListRecord(input.getText().toString());
                            readToDoListRecord();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            }
        });


    }

    public void addToDoListRecord(String data){
        realm.beginTransaction();

        ToDoList list = realm.createObject(ToDoList.class);

        list.setToDoListName(data);

        realm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();

    }

    public ArrayList readToDoListRecord(){

        if (toDoListArrayToDisplay != null)
            toDoListArrayToDisplay.clear();

        RealmResults<ToDoList> results = realm.where(ToDoList.class).findAll();

        for(ToDoList toDo : results){
            toDoListArrayToDisplay.add(toDo.getToDoListName());
        }
        return toDoListArrayToDisplay;
    }

    public void deleteRecord(String name){
        RealmResults<ToDoList> results = realm.where(ToDoList.class).equalTo("ToDoListName", name).findAll();

        realm.beginTransaction();

        results.deleteAllFromRealm();

        realm.commitTransaction();
    }


}