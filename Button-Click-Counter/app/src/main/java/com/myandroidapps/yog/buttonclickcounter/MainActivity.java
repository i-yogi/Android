package com.myandroidapps.yog.buttonclickcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Declaring 'views' to use 'em
    private Button myButton;
    private TextView myText;
    private TextView counter;
    private int count1=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking Button/Textview with views in 'content_main.xml'
        myButton = (Button)findViewById(R.id.button);
        myText = (TextView)findViewById(R.id.textView);
        counter = (TextView)findViewById(R.id.counter);

        View.OnClickListener myOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myText.setText("You Pressed the Button!");
                String countMsg = (++count1) + "  time";
                if(count1 != 1){
                    countMsg += "s";
                }
                counter.setText(countMsg);
            }
        };

        myButton.setOnClickListener(myOnClick);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
            Toast.makeText(MainActivity.this, "Setting menu option Selected! Button Pressed "+ counter.getText(), Toast.LENGTH_LONG).show();
            count1 = 0;
            Toast.makeText(this, "Counter Reseted", Toast.LENGTH_SHORT).show();
           // Toast ytoast = Toast.makeText(this, "Short Toast !", Toast.LENGTH_SHORT);
            //ytoast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
