package com.fireplace.software;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


import com.fireplace.software.R;

public class StorageActivity extends Activity implements OnClickListener
{
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage);
        setTitle("Storage information");
        
        //--------------- TEXTVIEW INFORMATION LOAD ---------------//
        TextView infoStorage = (TextView) findViewById(R.id.infoStorage);
        TextView infoOS = (TextView) findViewById(R.id.infoOS);
        TextView infoDevice = (TextView) findViewById(R.id.infoDevice);
        TextView infoModel = (TextView) findViewById(R.id.infoModel);
        TextView infoFireplace = (TextView) findViewById(R.id.infoFireplace);
        
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        long megAvailable = bytesAvailable / (1024 * 1024);
        Log.e("","Available MB : "+megAvailable);
        
        infoStorage.setText("" + megAvailable + " MB");
        // infoOS.setText(System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")");
        infoDevice.setText(android.os.Build.DEVICE);
        infoModel.setText(android.os.Build.MODEL);
        infoOS.setText(android.os.Build.VERSION.RELEASE);
        String versionString = getString(R.string.version);
        infoFireplace.setText(versionString);

        
        
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	}
