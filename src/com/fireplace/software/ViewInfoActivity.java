package com.fireplace.software;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewInfoActivity extends Activity implements android.view.View.OnClickListener{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conatininfo);
        setTitle("Application information");
        
        //-------- Buttons -------//
        Button btnDownload = (Button)findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
               
        Intent i = getIntent();
        if(i.getExtras()!=null && i.getExtras().containsKey("passedName")){
        	TextView lbAppTitle = (TextView) findViewById(R.id.lbAppTitle);
        	lbAppTitle.setText(i.getExtras().getString("passedName"));
        }
        	
        	if(i.getExtras()!=null && i.getExtras().containsKey("passedInfo")){
        	TextView lbInfoText = (TextView) findViewById(R.id.lbInfo);
        	lbInfoText.setText(i.getExtras().getString("passedInfo"));
        	}
        	
        	if(i.getExtras()!=null && i.getExtras().containsKey("passedDeveloper")){
            	TextView lbDeveloper = (TextView) findViewById(R.id.lbDeveloper);
            	lbDeveloper.setText("Developer: " + i.getExtras().getString("passedDeveloper"));
            	}
        }

	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		//Button btnDownload = (Button)findViewById(R.id.btnDownload);
		//btnDownload.setText("Hello");
		
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) { 
		
		case R.id.btnDownload:
			Button btnDownload = (Button)findViewById(R.id.btnDownload);
		//Download Item from getPath
			if ("Download".equals(btnDownload.getText()))
			{
			try {				          	
	            					
        		//set the download URL, a url that points to a file on the internet
        		//this is the file to be downloaded
        		URL url = new URL("Path to file");
        		

        		//create the new connection
        		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        		//set up some things on the connection
        		urlConnection.setRequestMethod("GET");
        		urlConnection.setDoOutput(true);

        		//and connect!
        		urlConnection.connect();

        		//set the path where we want to save the file
        		//in this case, going to save it on the root directory of the
        		//sd card.
        		File SDCardRoot = Environment.getExternalStorageDirectory();
        		//create a new file, specifying the path, and the filename
        		//which we want to save the file as.
        		File file = new File(SDCardRoot + "Fireplace/","splited link!");

        		//this will be used to write the downloaded data into the file we created
        		FileOutputStream fileOutput = new FileOutputStream(file);

        		//this will be used in reading the data from the internet
        		InputStream inputStream = urlConnection.getInputStream();

        		//this is the total size of the file
        		int totalSize = urlConnection.getContentLength();
        		//variable to store total downloaded bytes
        		int downloadedSize = 0;

        		//create a buffer...
        		byte[] buffer = new byte[1024];
        		int bufferLength = 0; //used to store a temporary size of the buffer

        		//now, read through the input buffer and write the contents to the file
        		while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
        			//add the data in the buffer to the file in the file output stream (the file on the sd card
        			fileOutput.write(buffer, 0, bufferLength);
        			//add up the size so we know how much is downloaded
        			downloadedSize += bufferLength;
        			//this is where you would do something to report the prgress, like this maybe
        			updateProgress(downloadedSize, totalSize);

        		}
        		//close the output stream when done
        		fileOutput.close();
        		
        		
        	        Intent promptInstall = new Intent(Intent.ACTION_VIEW)
        	        .setData(Uri.parse(SDCardRoot + "Fireplace/filename from splited string!"))
        	        .setType("application/vnd.android.package-archive");
        	    startActivity(promptInstall); 
        	    btnDownload.setText("Open");
				

        	//catch some possible errors...
        	} catch (MalformedURLException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	
			
			Intent i = getIntent();
			if(i.getExtras()!=null && i.getExtras().containsKey("passedName")){
				
			
        	String ns = Context.NOTIFICATION_SERVICE;
        	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
    	    int icon = R.drawable.download;
    	    CharSequence tickerText = "Failed to download " + i.getExtras().getString("passedName");
    	    long when = System.currentTimeMillis();
    	    Notification notification = new Notification(icon, tickerText, when);
    	    Context context = getApplicationContext();
    	    CharSequence contentTitle = "Failed to download " + i.getExtras().getString("passedName");
    	    CharSequence contentText = "Could not find the file specified!";

    	    notification.setLatestEventInfo(context, contentTitle, contentText, null);
    	    final int notify_ID = 1;

    	    mNotificationManager.notify(notify_ID, notification);
				}
			}
			else{
				
				Toast.makeText(this, "Should open installed app", Toast.LENGTH_SHORT).show(); 
			}
			
            break;
			
		}
	}

	private void updateProgress(int downloadedSize, int totalSize) {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		if(i.getExtras()!=null && i.getExtras().containsKey("passedName")){
			
		String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
	    int icon = R.drawable.download;
	    CharSequence tickerText = "Downloading" + i.getExtras().getString("passedName");
	    long when = System.currentTimeMillis();
	    Notification notification = new Notification(icon, tickerText, when);
	    Context context = getApplicationContext();
	    CharSequence contentTitle = "Failed to download + getPath();";
	    CharSequence contentText = "Could find the specified file";
	    Intent notificationIntent = new Intent(this, ViewInfoActivity.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

	    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	    final int notify_ID = 1;

	    mNotificationManager.notify(notify_ID, notification);
		}
		
	}	
}
