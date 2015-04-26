package xyz.dannyc.textdatatest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daniel on 16-Apr-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TORAH_DB";
    private static final int DB_VERSION = 1;
	private static final String NOT_NULL = "not null";

	private Table tanach;
	private Context context;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
		createTanachTable();
	}

	private void createTanachTable(){
		tanach = new Table("TANACH");
		tanach.addColumn("CATEGORY", Table.DataType.TEXT, NOT_NULL);
		tanach.addColumn("FORMAT", Table.DataType.TEXT, NOT_NULL);
		tanach.addColumn("BOOK", Table.DataType.TEXT, NOT_NULL);
		tanach.addColumn("PARSHA", Table.DataType.TEXT, null);
		tanach.addColumn("CHAPTER", Table.DataType.TEXT, NOT_NULL);
		tanach.addColumn("VERSE", Table.DataType.TEXT, NOT_NULL);
		tanach.addColumn("TEXT", Table.DataType.TEXT, NOT_NULL);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w("DatabaseHelper", "creating DB with this script:    "  + tanach.getCreateScript());
		db.execSQL(tanach.getCreateScript());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL(tanach.getDropScript());
		onCreate(db);
	}

	public void insertData(Table table, JSONArray data) throws JSONException {

		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(table.getInsertScript());

		for(int i = 0; i < data.length(); i++){
			JSONObject dataObj = data.getJSONObject(i);
			table.bindColumns(stmt, dataObj);
			long entryID = stmt.executeInsert();
			stmt.clearBindings();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	SimpleCursorAdapter getSimpleCursorAdapter(){

		SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, );
		simpleCursorAdapter.changeCursor();
	}

}
