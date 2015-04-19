package xyz.dannyc.textdatatest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by daniel on 16-Apr-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TORAH_DB";
    private static final int DB_VERSION = 1;

	private static final String M_TEXT_NOT_NULL = " text not null";
	private static final String M_TEXT = " text";
    private static final String M_INTEGER = " integer";

	private Table tanach;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		createTanachTable();
	}

	private void createTanachTable(){
		tanach = new Table("TANACH");
		tanach.addColumn("CATEGORY", M_TEXT_NOT_NULL);
		tanach.addColumn("FORMAT", M_TEXT_NOT_NULL);
		tanach.addColumn("BOOK", M_TEXT_NOT_NULL);
		tanach.addColumn("PARSHA", M_TEXT);
		tanach.addColumn("CHAPTER", M_TEXT_NOT_NULL);
		tanach.addColumn("VERSE", M_TEXT_NOT_NULL);
		tanach.addColumn("TEXT", M_TEXT_NOT_NULL);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(tanach.getCreateScript());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL(tanach.getDropScript());
		onCreate(db);
	}

	private void insertData(Table table, JSONArray data) throws JSONException {

		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(table.getInsertScript());

		for(int i = 0; i < data.length(); i++){
			JSONObject obj = data.getJSONObject(i);
			List<String> columnNames = table.getColumnNames();
			String[] columnsValues = new String[columnNames.size()];
			for(int j = 0; j < columnNames.size(); j++){
				String columnName = columnNames.get(j);
				String colValue = obj.getString(columnName);
				columnsValues[j] = colValue;
			}
			stmt.bindAllArgsAsStrings(columnsValues);
			long entryID = stmt.executeInsert();
			stmt.clearBindings();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

}
