package com.myandroidapps.yogi.sqliteintro;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView myLsitView;

    myDB mydb;

    EditText textedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydb = new myDB(this);
        textedItem = (EditText) findViewById(R.id.itemText);
        listview();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });


    }

    public void addButtonClicked(View view){
        ShoppingKart shoppingKart = new ShoppingKart(textedItem.getText().toString());
        mydb.addItem(shoppingKart);
        textedItem.setText("");
        listview();
    }

    public void deleteButtonClicked(View view){
        String input = textedItem.getText().toString();
        mydb.deleteItem(input);
        textedItem.setText("");
        listview();
    }

    public void listview(){

        //SHOPPING KART ListView

        Cursor cursor = mydb.getData();
        List<String> items = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    items.add(cursor.getString(cursor.getColumnIndex("itemName")));
                } while (cursor.moveToNext());
            }
        }
        myLsitView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, items);

        myLsitView.setAdapter(myAdapter);

        myLsitView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textedItem.setText(myLsitView.getItemAtPosition(position).toString());
            }
        });
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
}
