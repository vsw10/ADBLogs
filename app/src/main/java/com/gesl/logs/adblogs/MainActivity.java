package com.gesl.logs.adblogs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.gesl.logs.adblogs.R.id.button4;
import static com.gesl.logs.adblogs.R.id.button5;
import static com.gesl.logs.adblogs.R.id.button6;
import static com.gesl.logs.adblogs.R.id.cancel_action;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static  final String TAG = "logs";
    private TextView mTextView;
    private TextView mTextFile;
    Button start,save,clear;
    Intent intent;
    String mTextString = "";
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        start = (Button)findViewById(button4);
        save = (Button)findViewById(R.id.button5);
        clear = (Button)findViewById(R.id.button6);
        mTextView = (TextView)findViewById(R.id.text1);
        mTextFile = (TextView)findViewById(R.id.text2);
        start.setOnClickListener(this);
        save.setOnClickListener(this);
        clear.setOnClickListener(this);
        intent = getIntent();
        intent = new Intent(this,RunningService.class);
        mTextView.setText(mTextString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case button4:
                //intent = new Intent(this,RunningService.class);
                mTextString = "Running action : START" ;
                intent.putExtra("action","start");
                startNewService(intent);
                mTextView.setText(mTextString);
                break;
            case button5:
                mTextString = "Running action : SAVE" ;
                intent.putExtra("action","save");
                mTextView.setText(mTextString);
                startNewService(intent);
                mTextFile.setText(Utility.getInstance().getTextFile());
                break;
            case button6:
                mTextString = "Running action : CLEAR" ;
                intent.putExtra("action","clear");
                mTextView.setText(mTextString);
                startNewService(intent);
                break;
            default:
                Log.d(TAG,"No Button Clicked");

        }
    }

    private void startNewService(Intent intent) {
        getApplicationContext().startService(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putString("value",mTextString);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
        mTextString = savedInstanceState.getString("value");
        Log.d("user","OnRestore InstanceState ");
    }
}
