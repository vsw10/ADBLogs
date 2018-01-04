package com.gesl.logs.adblogs;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by vinod on 24/11/17.
 */

public class RunningService extends Service {

    public static  final String TAG = "logs";

    java.lang.Process process;

    String command = " ";
    String mAction;
    StringBuilder stringBuilder;


    boolean save;
    boolean start;
    boolean clear;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        mAction = intent.getStringExtra("action");
        startRunning(mAction);
        return START_REDELIVER_INTENT;
    }


    /**
     * Perform the action, on basis of input
     * @param action Start/Save/Clear
     */
    private void startRunning(String action) {

        StringBuffer buffer = new StringBuffer();
        if((action != null) && (action.equalsIgnoreCase("start"))) {
            start = true;
            save = false;
            clear = false;
            command = "logcat";
            //executeCommand();
        } else if ((action != null) && action.equalsIgnoreCase("save")) {
            start = false;
            save = true;
            clear = false;
            destroyProcess();
        } else if ((action != null) && action.equalsIgnoreCase("clear")) {
            command = "logcat -c";
            save = false;
            start = false;
            clear = true;
            //new AdbShell().execute(command);
            //executeCommand();
        }

    }

    /**
     * Executes the adb command
     */
    void executeCommand() {
        Log.d(TAG,"Command to be exceuted " + command + " With Action "+ mAction);

        try {
            String []commands = {"sh","-c","sh /sdcard/clear.sh start"};
            if(mAction != null && mAction.equalsIgnoreCase("clear")) {
                Log.d(TAG,"CLEAR Command to be executed");
                String clear[] = {"sh","-c","sh /sdcard/clear.sh clear"};
                process = Runtime.getRuntime().exec(clear);
                //process.waitFor();
            } else if(mAction != null && mAction.equalsIgnoreCase("start")) {
                Log.d(TAG,"START Command to be executed");
                process = Runtime.getRuntime().exec(commands);
                //process.waitFor();
            } else if(mAction != null && mAction.equalsIgnoreCase("save")) {
                destroyProcess();
            }

            //process.waitFor();
        } catch (Exception e) {
            Log.d(TAG,"Exception Occurred "+ e);
        }
        if((mAction != null) && (mAction.equalsIgnoreCase("start"))) {
            Thread t1 = new Thread(runnable);
            //t1.start();
        }


    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(start) {
                File file = createLogFile();
                Looper.prepare();
                //readStreams(file);
                Looper.loop();
            }
        }
    };

    /**
     * To Stop process
     */
    void destroyProcess() {
       // Log.d(TAG,"destroy process");
        try {
            if(process != null) {
                process.destroy();
            }
        } catch (Exception e) {
           Log.e(TAG,"Exception Occurred "+ e);
        }
    }

    /**
     * Function used to read streams and stored in file
     * @param createFile Streams to be stored in particular file
     */
    void readStreams(File createFile)  {
        try {
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(createFile));
             String line = " ";
            stringBuilder = new StringBuilder();
             while ((line = bufferedReader.readLine()) != null) {
                 stringBuilder.append(line);
                 //Log.d(TAG," "+ line);
                 bufferedWriter.write(line);
                 bufferedWriter.flush();
                if(save) {
                    break;
                }
             }

            bufferedWriter.close();

        } catch (Exception e) {
            Log.e(TAG,"Exception Occurred "+ e);
        }

    }

    /**
     * Function used to create log file
     * @return File created file in storage
     */
    File createLogFile() {
        File createFile = new File("logs.txt");
        try {
            File dir = new File(Environment.getExternalStorageDirectory(),"Logs");
            if(!dir.exists()) {
                //Log.d(TAG,"Create New Directory");
                dir.mkdir();

            }
            long time = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            String dateFormat = "dd-MM-yyyy hh:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            String currentTime = simpleDateFormat.format(calendar.getTime());
            createFile = new File(dir,currentTime+"logs.txt");
            if(!createFile.exists()) {
                //Log.d(TAG,"Create New File");
                createFile.createNewFile();
            }
            if(createFile.exists()) {
                String fileCreated = createFile.getName();
                Utility.getInstance().setTextFile("Created File Name is "+fileCreated);
            } else {
                Utility.getInstance().setTextFile(" No file Created ");
            }
        } catch (Exception e) {
            Utility.getInstance().setTextFile(" No file Created ");
            Log.d(TAG,"Exception Occurred is "+ e);
        }
        return  createFile;
    }

    IRunningService.Stub mBinder = new IRunningService.Stub() {

        @Override
        public int add(int a, int b) throws RemoteException {
            Log.d("user","a is "+ a + " b is "+ b);
            return a+b;
        }
    };
}
