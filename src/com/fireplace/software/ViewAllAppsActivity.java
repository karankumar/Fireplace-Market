package com.fireplace.software;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ViewAllAppsActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listplaceholder);
        
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
      
       
        String xml = XMLfunctions.getXML();
        Document doc = XMLfunctions.XMLfromString(xml);
                
        int numResults = XMLfunctions.numResults(doc);
        
        if((numResults <= 0)){
        	Toast.makeText(ViewAllAppsActivity.this, "Could not connect to server!", Toast.LENGTH_LONG).show();  
        	finish();
        }
                
		NodeList nodes = doc.getElementsByTagName("result");
					
		for (int i = 0; i < nodes.getLength(); i++) {							
			HashMap<String, String> map = new HashMap<String, String>();	
			
			Element e = (Element)nodes.item(i);
			map.put("id", XMLfunctions.getValue(e, "id"));
        	map.put("name", XMLfunctions.getValue(e, "name"));
        	map.put("Score", "" + XMLfunctions.getValue(e, "score"));
        	mylist.add(map);			
		}		
       
        ListAdapter adapter = new SimpleAdapter(this, mylist , R.layout.placeholder, 
                        new String[] { "name", "Score" }, 
                        new int[] { R.id.item_title, R.id.item_subtitle });
        
        setListAdapter(adapter);
        
        final ListView lv = getListView();
        lv.setTextFilterEnabled(true);	
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
        		@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        		
        	
        		
        		try {
        			
        			String ns = Context.NOTIFICATION_SERVICE;
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
                    
                    int icon = R.drawable.icon;
                    CharSequence tickerText = "Downloading " + o.get("name");
                    long when = System.currentTimeMillis();
                    Notification notification = new Notification(icon, tickerText, when);
                    
                    Context context = getApplicationContext();
                    CharSequence contentTitle = o.get("name");
                    CharSequence contentText = "Downloading and installing";
                    //Intent notificationIntent = new Intent(this, App.class);
                    //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                    notification.setLatestEventInfo(context, contentTitle, contentText, null);
                    
                    final int HELLO_ID = 0;

                    mNotificationManager.notify(HELLO_ID, notification);
        			
                    //set the download URL, a url that points to a file on the internet
                    //this is the file to be downloaded
                	
                	
                    URL url = new URL("http://fireplacemarked.x90x.net/uploads/" + o.get("Score"));

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
                    File file = new File(SDCardRoot + "/Fireplace/", o.get("Score"));

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

            //catch some possible errors...
            } catch (MalformedURLException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }
        		
        		File appFile = new File("/sdcard/Fireplace/" + File.separator + o.get("Score"));
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(Uri.fromFile(appFile),"application/vnd.android.package-archive");
                startActivity(installIntent);
                             
        		
			}
        	
        	

        	 public void updateProgress(int currentSize, int totalSize){
        	    	//Toast.makeText(this, "Packages: " + Long.toString((currentSize/totalSize)*100)+"% Complete", Toast.LENGTH_SHORT).show();
        	    	}
		});
    }
}