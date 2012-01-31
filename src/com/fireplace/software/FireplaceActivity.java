package com.fireplace.software;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.fireplace.software.R;

public class FireplaceActivity extends ListActivity implements OnClickListener
{
	
	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MUCH TIMES BUTTON WAS CLICKED
    int clickCounter=1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
            setListAdapter(adapter);
            listItems.add("Network Tools");
            listItems.add("Root utilities");
            listItems.add("System Tools");
            listItems.add("Security");
            listItems.add("Tweaks");
            listItems.add("Themes");
        
        File folder = new File("/sdcard/Fireplace/");

        if (folder.exists()) {
            String deleteCmd = "rm -r " + "/sdcard/Fireplace/";
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }

        
        TabHost th = (TabHost) findViewById (R.id.tabhost);
        th.setup();
        
        // Tab 1
        TabSpec ts = th.newTabSpec("tag1"); //ts = TabSpec
        ts.setContent(R.id.tab1);
        ts.setIndicator("Home");
        th.addTab(ts);
        
        // Tab 2
        ts = th.newTabSpec("tag2"); //ts = TabSpec
        ts.setContent(R.id.tab2);
        ts.setIndicator("Manage");
        th.addTab(ts);
        
        // Tab 3
        ts = th.newTabSpec("tag3"); //ts = TabSpec
        ts.setContent(R.id.tab3);
        ts.setIndicator("Browse");
        th.addTab(ts);
        
        // Tab 4
        ts = th.newTabSpec("tag4"); //ts = TabSpec
        ts.setContent(R.id.tab4);
        ts.setIndicator("Search");
        th.addTab(ts);
        
        TextView txtLoading = (TextView) findViewById (R.id.txtLoading);
        txtLoading.setText("Setting up components"); //Initial loading
        
        Button btnRepo = (Button) findViewById (R.id.btnRepo);
        btnRepo.setOnClickListener(this);
        
        Button btnPack = (Button) findViewById (R.id.btnPack);
        btnPack.setOnClickListener(this);
        
        Button btnStorage = (Button) findViewById (R.id.btnStorage);
        btnStorage.setOnClickListener(this);
        
        Button btnViewAll = (Button) findViewById (R.id.btnViewAll);
        btnViewAll.setOnClickListener(this);
        
        Button btnFacebook = (Button) findViewById (R.id.btnFacebook);
        btnFacebook.setOnClickListener(this);
        
        Button btnTwitter = (Button) findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(this);
        
        TextView txtDeviceInfo = (TextView) findViewById (R.id.txtDeviceInfo);
        txtDeviceInfo.setText("Android: " + android.os.Build.VERSION.RELEASE + "/ Device: " + android.os.Build.DEVICE);

        txtLoading.setText("Runnning network check");
        
        startupNetworkCheck();
        
      //Should be firstTimeRun in string in strings.xml
        String firstTimeRun = "true";
        
        if ("true".equals(firstTimeRun))
        {
          
        	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        	alertbox.setTitle("Changelog");
            alertbox.setMessage(R.string.info_changelog);
            alertbox.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //Edit string firstTimeRun in strings.xml
                }
            });
 
            // show it
            alertbox.show();
        }
        
     // create a dir
        txtLoading.setText("Loading folders");
        
        File fireplaceDir = new File("/sdcard/Fireplace/");
        fireplaceDir.mkdirs();
        
        txtLoading.setText("All done!");
        
        
    }
        
    
    public void updateProgress(int currentSize, int totalSize){
    	//Toast.makeText(this, "Packages: " + Long.toString((currentSize/totalSize)*100)+"% Complete", Toast.LENGTH_SHORT).show();
    	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.textDonate:
        	Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=DC4WKW2AJFE4S&lc=US&item_name=Stian%20W%20Insteb%c3%b8%20%28Spxc%29&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted") );
			startActivity( browse );
			break;
        
            case R.id.textAbout:     
            	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            	alertbox.setTitle("About Fireplace Market");
                alertbox.setMessage("Fireplace Market is a 3rd party app store which contain apps and tweaks which didn't get into Android Market" +
                		"\n\nThis software comes without any kind of warranty!" +
                		"\n\nProject started by Spxc" +
                		"\n\nCopyright 2012" +
                		"\nRooted Dev Team");
                alertbox.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Edit string firstTimeRun in strings.xml
                    }
                });
     
                // show it
                alertbox.show();
            	
            	
            break;
            case R.id.textCheckUpdate:             	
            	try {
            		//set the download URL, a url that points to a file on the internet
            		//this is the file to be downloaded
            		URL url = new URL("http://fireplacemarket.x90x.net/uploads/fireplaceUpdate.v" + getResources().getString(R.string.updateTo)+ ".apk");

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
            		File file = new File(SDCardRoot + "Fireplace/","update.apk");

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
            	        .setData(Uri.parse(SDCardRoot + "Fireplace/update.apk"))
            	        .setType("application/vnd.android.package-archive");
            	    startActivity(promptInstall); 

            	//catch some possible errors...
            	} catch (MalformedURLException e) {
            		e.printStackTrace();
            	} catch (IOException e) {
            		e.printStackTrace();
            	}
            	// see http://androidsnippets.com/download-an-http-file-to-sdcard-with-progress-notification
            	
            	Toast.makeText(this, "No new updates available!", Toast.LENGTH_LONG).show();
                                break;
        }
       
    
        return true;
        
    }

    public void onClick(View v) {
    	switch(v.getId()) {    	
    	
    	case R.id.btnTwitter: //Twitter button
    		Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("http://twitter.com/#!/Spxc") );
			startActivity( browse );
			break;
			
    	case R.id.btnFacebook: //Facebook Buttin
    		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        	alertbox.setTitle("Sorry");
            alertbox.setMessage("We don't have a support group on facebook, yet! Check back later.");
            alertbox.setNeutralButton("I'll wait", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //Edit string firstTimeRun in strings.xml
                }
            });
 
            // show it
            alertbox.show();
    		break;
    	
    	case R.id.btnRepo: //Repository button
    		//Show repo's
    		final Context contextRepo = this;
    		
    		
    		Intent intentRepo = new Intent(contextRepo, RepoActivity.class);
    		startActivityForResult(intentRepo, 0);    		
    		break;
    		
    	case R.id.btnPack: //Packages button
    		//Show packages installed
    		final Context contextPack = this;
    		
    		
    		Intent intentPack = new Intent(contextPack, ListInstalledApps.class);
    		startActivityForResult(intentPack, 0);    		
    		break;
    		
    	case R.id.btnStorage: //Storage button
    		//Show storage left on phone and SD card
    		final Context contextStorage = this;
    		
    		
    		Intent intentStorage = new Intent(contextStorage, StorageActivity.class);
    		startActivityForResult(intentStorage, 0);    		
    		break;    	
    		
    	case R.id.btnViewAll:
    		//Show all apps
    		isOnline();    		
    		break;
    	}
    }
    
   
    
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        	Toast.makeText(FireplaceActivity.this, "Loading apps...", Toast.LENGTH_LONG).show();
        	final Context contextStorage2 = this;
    		
    		
    		Intent intentStorage2 = new Intent(contextStorage2, GetContentFromDBActivity.class);
    		startActivityForResult(intentStorage2, 0);
            return true;
        }
        Toast.makeText(FireplaceActivity.this, "You need network connection!", Toast.LENGTH_LONG).show();
        return false;
    }
    
    public boolean startupNetworkCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        	//Toast.makeText(FireplaceActivity.this, "Network enabled!", Toast.LENGTH_LONG).show();
            return true;
        }
        Toast.makeText(FireplaceActivity.this, "No network connection detected!", Toast.LENGTH_LONG).show();
        Button btnViewAll = (Button)findViewById(R.id.btnViewAll);
        btnViewAll.setEnabled(false);
        return false;
    }
    
    public boolean updateChecketwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
try {
    			
    			String ns = Context.NOTIFICATION_SERVICE;
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
                
                int icon = R.drawable.icon;
                CharSequence tickerText = "Downloading update";
                long when = System.currentTimeMillis();
                Notification notification = new Notification(icon, tickerText, when);
                
                Context context = getApplicationContext();
                CharSequence contentTitle = "Downloading update";
                CharSequence contentText = "Fireplace_update.apk";
                //Intent notificationIntent = new Intent(this, App.class);
                //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                notification.setLatestEventInfo(context, contentTitle, contentText, null);
                
                final int HELLO_ID = 1;

                mNotificationManager.notify(HELLO_ID, notification);
    			
                //set the download URL, a url that points to a file on the internet
                //this is the file to be downloaded
            	//Toast.makeText(this, "Preparing: Packages", Toast.LENGTH_SHORT).show();
                String updateString = getString(R.string.updateTo);
                URL url = new URL("http://fireplacemarked.x90x.net/uploads/Fireplace_update" + updateString + ".apk");

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
                File file = new File(SDCardRoot + "/Fireplace/Fireplace_update" + updateString + ".apk");

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

			String updateString = getString(R.string.updateTo);
    		
    		File appFile = new File("/sdcard/Fireplace/Fireplace_update" + updateString + ".apk");
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(Uri.fromFile(appFile),"application/vnd.android.package-archive");
            startActivity(installIntent);
            
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
            
            int icon = R.drawable.icon;
            CharSequence tickerText = "Update";
            long when = System.currentTimeMillis();
            Notification notification = new Notification(icon, tickerText, when);
            
            Context context = getApplicationContext();
            CharSequence contentTitle = "Fireplace is updated";
            CharSequence contentText = "Install complete";
            //Intent notificationIntent = new Intent(this, App.class);
            //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            notification.setLatestEventInfo(context, contentTitle, contentText, null);
            
            final int HELLO_ID = 1;

            mNotificationManager.notify(HELLO_ID, notification);
            return true;
        }
        Toast.makeText(FireplaceActivity.this, "No update available", Toast.LENGTH_LONG).show();
        return false;
    }

}