/** 
 * The code written in this file was intended to serve as an EXAMPLE. You are
 * free to modify and use code in this file. However, the this is also provided 
 * without ANY WARRANTY, EXPRESSED OR IMPLIED, INCLUDING ANY WARRANTIES OR 
 * CONDITIONS OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSES. Users of 
 * the code contained in this file assume the risks and costs of, including but
 * not limited to, any program errors, data loss or interruption of operations.
 *
 * This was written for the Android "Using the SQLite Database with ListView" tutorial at:
 * http://kahdev.wordpress.com/2010/09/27/android-using-the-sqlite-database-with-listview
 */
package org.kah;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * This is the activity that that provides the view. Press the menu key to
 * open the options menu. It has only one item - to bring up a dialog that
 * allows you to add a new entry.
 * 
 * @author Kah
 */
public class SQLiteDemo extends ListActivity {
	private static final int DIALOG_ID = 100;

	private SQLiteDatabase database;

	private CursorAdapter dataSource;

	private View entryView;

	private EditText firstNameEditor;

	private EditText lastNameEditor;

	private static final String fields[] = { "first", "last", BaseColumns._ID };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseHelper helper = new DatabaseHelper(this);
		database = helper.getWritableDatabase();
		Cursor data = database.query("names", fields, null, null, null, null,
				null);

		dataSource = new SimpleCursorAdapter(this, R.layout.row, data, fields,
				new int[] { R.id.first, R.id.last });

		ListView view = getListView();
		view.setHeaderDividersEnabled(true);
		view.addHeaderView(getLayoutInflater().inflate(R.layout.row, null));

		setListAdapter(dataSource);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, DIALOG_ID, 1, R.string.addItem);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == DIALOG_ID) {
			showDialog(DIALOG_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		entryView = getLayoutInflater().inflate(R.layout.entry, null);
		builder.setView(entryView);
		firstNameEditor = (EditText) entryView.findViewById(R.id.firstName);
		lastNameEditor = (EditText) entryView.findViewById(R.id.lastName);
		builder.setTitle(R.string.addDialogTitle);
		builder.setPositiveButton(R.string.addItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ContentValues values = new ContentValues();
						values.put("first", firstNameEditor.getText()
								.toString());
						values.put("last", lastNameEditor.getText().toString());
						database.insert("names", null, values);
						dataSource.getCursor().requery();
					}
				});

		builder.setNegativeButton(R.string.cancelItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return builder.create();
	}
}