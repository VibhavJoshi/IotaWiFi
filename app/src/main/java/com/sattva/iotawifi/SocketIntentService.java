package com.sattva.iotawifi;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SocketIntentService extends IntentService {


    private ServerSocket serverSocket;
    Thread serverThread = null;
    public static final int SERVERPORT = 8080;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-HH-mm-ss");
    String strDate = sdf.format(c.getTime());
    String fileName = "sattva-" +  strDate;
    String[] tempString;
    String tempInput;
    int temp_count = 0;

    public SocketIntentService() {
        super("SocketIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();

    }



    class ServerThread implements Runnable
    {

        public void run()
        {
            Socket socket = null;
            try
            {
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e)
            {
                e.printStackTrace();
            }


            while (!Thread.currentThread().isInterrupted())
            {

                try
                {

                    socket = serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable
    {

        private Socket clientSocket;

        private BufferedReader input;


        public CommunicationThread(Socket clientSocket)
        {

            this.clientSocket = clientSocket;

            try
            {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public void run()
        {

            ApplicationUtils.startMS = System.currentTimeMillis();

            while (!Thread.currentThread().isInterrupted())
            {

                try
                {

                    tempInput = input.readLine();
                    LogData(tempInput);
                    tempString = tempInput.split("\\+");


                    for(int temp_count = 1; temp_count < tempString.length; temp_count++)
                    {
                        ApplicationUtils.dynamicDataStore.add(tempString[temp_count]);
                    }

                    if(ApplicationUtils.dynamicDataStore.size() >=  ApplicationUtils.bufferLength && ApplicationUtils.convert_flag == 1)
                    {

                        Log.e("SocketIntentService", "15000 samples read at: " + (System.currentTimeMillis() - ApplicationUtils.startMS));
                        Intent startConvertingIntent = new Intent(SocketIntentService.this, ConvertIntentService.class);
                        startService(startConvertingIntent);
                        ApplicationUtils.convert_flag = 0;


                    }


                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }


    }

        public void LogData(String input) {
            // Log.e("LogData", input);

            try {
                File root = new File(Environment.getExternalStorageDirectory()
                        .getPath() + "/sattva");
                File ActivityLog = new File(Environment
                        .getExternalStorageDirectory().getPath() + "/sattva",
                        fileName + ".txt");

                if (root.isDirectory()) {
                    // Log.d("root exists","root exists");
                    if (ActivityLog.exists()) {

                        FileWriter outFile = new FileWriter(ActivityLog, true);
                        // Log.d("writing","writing " +
                        // ActivityLog.getAbsolutePath());
                        PrintWriter out = new PrintWriter(outFile);
                        out.print(input);
                        out.flush();
                        out.close();
                    } else {
                        // Log.d("writing2","writing2");
                        ActivityLog.createNewFile();
                        FileWriter outFile = new FileWriter(ActivityLog, true);
                        PrintWriter out = new PrintWriter(outFile);
                        out.print(input);
                        out.flush();
                        out.close();
                    }
                } else {
                    root.mkdir();
                    ActivityLog.createNewFile();

                    FileWriter outFile = new FileWriter(ActivityLog, true);

                    PrintWriter out = new PrintWriter(outFile);
                    out.println(input);
                    out.flush();
                    // Log.d("writing3","writing3");
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }



}
