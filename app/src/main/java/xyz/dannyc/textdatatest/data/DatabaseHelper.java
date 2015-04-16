package xyz.dannyc.textdatatest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by daniel on 16-Apr-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "torahDB";
    private static final int DB_VERSION = 1;
    private static final String M_TEXT_NOT_NULL = " text not null";
    private final String T_TANACH = "TANACH";
    private final Column C_CATEGORY = new Column("CATEGORY", M_TEXT_NOT_NULL);
    private final String C_CATEGORYOOO = "CATEGORY";
    private final String C_FORMAT = "FORMAT";
    private final String C_BOOK = "BOOK";
    private final String C_PARSHA = "PARSHA";
    private final String C_CHAPTER = "CHAPTER";
    private final String C_VERSE = "VERSE";
    private final String C_TEXT = "TEXT";
    private final String M_TEXT = " text";
    private final String M_INTEGER = " integer";

    private class Column{
        String name;
        String modifier;
        Column (String name, String modifier){
            this.name = name;
            this.modifier = modifier;
        }
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(createTableScript(T_TANACH));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String createTableScript(String tableName, String[] columns, String[] columnModifiers){
        StringBuilder sb = new StringBuilder()
                .append("create table ")
                .append(tableName)
                .append("(_id integer primary key autoincrement, "); //there always is an _id column as key
        for(int i = 0; i < columns.length; i++){
            sb.append(columns[i]).append(columnModifiers[i]);
            if(i != columns.length){ //last
                sb.append(",");
            }
        }
        sb.append(");");
        return sb.toString();
    }
}
