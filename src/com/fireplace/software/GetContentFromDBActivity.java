package com.fireplace.software;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class GetContentFromDBActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofapps);
        setTitle("All applications");
        
        LoadData();
    }
    
    
    void GenData(){
    	ArrayList<ItemSkel> list = new ArrayList<ItemSkel>();
    	for(int i = 0; i< 10; ++i) {
    		list.add(new ItemSkel("id " + i, "label " + i, "path " + i));
    	}

    	Gson gson = new Gson();
    	String json = gson.toJson(list);

    	Toast.makeText(GetContentFromDBActivity.this, json, Toast.LENGTH_LONG).show();
    }
    
    public void btnLoadData(View v) {
    	LoadData();
    }
    
    
    void LoadData(){

        //TextView v = (TextView)findViewById(R.id.txtStatusError);
    	
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("http://www.universuse.com/xda/getdata.php");
            HttpResponse response = httpClient.execute(httpGet, localContext);
            String result = "";
             
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                  response.getEntity().getContent()
                )
              );
             
            String line = null;
            while ((line = reader.readLine()) != null)
              result += line;

    		Type type = new TypeToken<ArrayList<ItemSkel>>(){}.getType();
            Gson g = new Gson();
            final ArrayList<ItemSkel> list = g.fromJson(result, type);
            final ArrayList<String> stringArray = new ArrayList<String>();
            
            for (ItemSkel item : list)
            	stringArray.add("Name : " + item.getLabel() + "\n "); //last 'space' for line space between app names
                        
            ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
            ListView lv = (ListView)findViewById(R.id.lwApps); 
            lv.setAdapter(modeAdapter);
            final Context contextPage = this;
            lv.setOnItemClickListener(new OnItemClickListener() {
            	
              public void onItemClick(AdapterView<?> parent, View view,
                  int position, long id) {
            	  
            	ItemSkel currentItem = list.get(position);
            	
                // Open download page  ¨
            	
                startActivity(new Intent(contextPage, ViewInfoActivity.class).putExtra("passedName", currentItem.getLabel())
                		.putExtra("passedInfo", currentItem.getPath())
                			.putExtra("passedLink", currentItem.getPath())
                				.putExtra("passedDeveloper", "Spxc").addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
              }
            });
        }
        catch (Exception ex) {
        	//v.setText(ex.getMessage());
        	Toast.makeText(GetContentFromDBActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}