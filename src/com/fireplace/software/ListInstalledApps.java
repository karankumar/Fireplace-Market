package com.fireplace.software;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListInstalledApps extends Activity implements OnItemClickListener, OnClickListener {
   
   /* whether or not to include system apps */
   private static final boolean INCLUDE_SYSTEM_APPS = false;
   
   private ListView mAppsList;
   private AppListAdapter mAdapter;
   private List<App> mApps;
   
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.browse);
      setTitle("Installed applications");
            
      mAppsList = (ListView) findViewById(R.id.listView1);
      mAppsList.setOnItemClickListener(this);
   
      mApps = loadInstalledApps(INCLUDE_SYSTEM_APPS);
      
      mAdapter = new AppListAdapter(getApplicationContext());
      mAdapter.setListItems(mApps);
      mAppsList.setAdapter(mAdapter);
      
      new LoadIconsTask().execute(mApps.toArray(new App[]{}));
   }
  
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      
      final App app = (App) parent.getItemAtPosition(position);

      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      
      String msg = app.getTitle() + "\n\n" + 
         "Version " + app.getVersionName() + " (" +
         app.getVersionCode() + ")" +
         (app.getDescription() != null ? ("\n\n" + app.getDescription()) : "");
      
      builder.setMessage(msg)
      .setCancelable(true)
      .setTitle(app.getTitle())
      .setIcon(mAdapter.getIcons().get(app.getPackageName()))
      .setPositiveButton("Launch", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {
            // start the app by invoking its launch intent
            Intent i = getPackageManager().getLaunchIntentForPackage(app.getPackageName());
            try {
               if (i != null) {
                  startActivity(i);
               } else {
                  i = new Intent(app.getPackageName());
                  startActivity(i);
               }
            } catch (ActivityNotFoundException err) {
               Toast.makeText(ListInstalledApps.this, "Error launching app", Toast.LENGTH_SHORT).show();
            }
         }
      })
      .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {
        	 
        	 Uri packageURI = Uri.parse("package:" + app.getPackageName());
        	 Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        	 startActivity(uninstallIntent);
            dialog.cancel();
         }
      });
      AlertDialog dialog = builder.create();
      dialog.show();
      
   }
   
   /**
    * Uses the package manager to query for all currently installed apps which are put into beans and returned
    * in form of a list.
    * 
    * @param includeSysApps whether or not to include system applications
    * @return a list containing an {@code App} bean for each installed application 
    */
   private List<App> loadInstalledApps(boolean includeSysApps) {
      List<App> apps = new ArrayList<App>();
      
      // the package manager contains the information about all installed apps
      PackageManager packageManager = getPackageManager();
      
      List<PackageInfo> packs = packageManager.getInstalledPackages(0); //PackageManager.GET_META_DATA 
      
      for(int i=0; i < packs.size(); i++) {
         PackageInfo p = packs.get(i);
         ApplicationInfo a = p.applicationInfo;
         // skip system apps if they shall not be included
         if ((!includeSysApps) && ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
            continue;
         }
         App app = new App();
         app.setTitle(p.applicationInfo.loadLabel(packageManager).toString());
         app.setPackageName(p.packageName);
         app.setVersionName(p.versionName);
         app.setVersionCode(p.versionCode);
         CharSequence description = p.applicationInfo.loadDescription(packageManager);
         app.setDescription(description != null ? description.toString() : "");
         apps.add(app);
      }
      return apps;
   }
   
   /**
    * An asynchronous task to load the icons of the installed applications.
    */
   private class LoadIconsTask extends AsyncTask<App, Void, Void> {
      @Override
      protected Void doInBackground(App... apps) {
         
         Map<String, Drawable> icons = new HashMap<String, Drawable>();
         PackageManager manager = getApplicationContext().getPackageManager();
         
         for (App app : apps) {
            String pkgName = app.getPackageName();
            Drawable ico = null;
            try {
               Intent i = manager.getLaunchIntentForPackage(pkgName);
               if (i != null) {
                  ico = manager.getActivityIcon(i);
               }
            } catch (NameNotFoundException e) {
               Log.e("ERROR", "Unable to find icon for package '" + pkgName + "': " + e.getMessage());
            }
            icons.put(app.getPackageName(), ico);
         }
         mAdapter.setIcons(icons);
         
         return null;
      }
      
      @Override
      protected void onPostExecute(Void result) {
         mAdapter.notifyDataSetChanged();
         
      }
  }

public void onClick(View arg0) {
	// TODO Auto-generated method stub
	
}

}

