package com.fireplace.software;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class RepoActivity extends ListActivity implements OnClickListener{
    /** Called when the activity is first created. */
    
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MUCH TIMES BUTTON WAS CLICKED
    int clickCounter=1;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repo);
        setTitle("Repositories");
        
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
            setListAdapter(adapter);
            
        Button btnAddRepo = (Button) findViewById(R.id.addBtn);
        btnAddRepo.setOnClickListener(this);
        listItems.add("http://apt.fireplacemarked.com");
        
        
        
    }
    
    public void onClick(View v) {
    	
    	switch (v.getId()){
    	
    	case R.id.addBtn:
    		EditText txtAddRepo= (EditText) findViewById(R.id.txtAddRepo);
    		EditText text = (EditText) findViewById(R.id.txtAddRepo);
    		String textstring = text.getText().toString();
    		listItems.add(textstring);
    		adapter.notifyDataSetChanged();
    		txtAddRepo.setText("");
    		break;
    
    	}
    }
}