package xyz.dannyc.textdatatest.data;

import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 19-Apr-15.
 */
public class Table {
		private String name;
		private List<Column> columns = new ArrayList<>();
		public enum DataType {TEXT, LONG, DOUBLE, BLOB, NULL }

		public Table(String name){
			this.name = name;
        }

		public void addColumn(String name, DataType dataType, String modifier) {
			Column column = new Column(name, dataType, modifier);
			columns.add(column);
		}

        public String getCreateScript(){
			StringBuilder sb = new StringBuilder()
				.append("create table ")
				.append(name)
				.append("(_id integer primary key autoincrement, "); //there always is an _id column as key
			for(int i = 0; i < columns.size(); i++){
				sb.append(columns.get(i).toString());
				if(i != columns.size()){ //last
					sb.append(",");
				}
			}
			sb.append(");");
			return sb.toString();
		}

		public String getDropScript(){
			return "DROP TABLE IF EXISTS " + name;
		}

		public void bindColumns(SQLiteStatement stmt, JSONObject obj) throws JSONException {
			int colIndex = 1;
			for(Column column : columns){
				switch (column.dataType){
					case TEXT:
						String strValue = obj.getString(column.name);
						stmt.bindString(colIndex, strValue);
					break;
					case LONG:
						long longValue = obj.getLong(column.name);
						stmt.bindLong(colIndex, longValue);
					break;
					case DOUBLE:
						double doubleValue = obj.getDouble(column.name);
						stmt.bindDouble(colIndex, doubleValue);
					break;
					case BLOB:
						Object objValue = obj.get(column.name);
						stmt.bindBlob(colIndex, objValue.toString().getBytes());
					break;
				}
				colIndex++;
			}
		}

		public List<String> getColumnNames(){
			List<String> columnNames = new ArrayList<>();
			for(Column column : columns){
				columnNames.add(column.name);
			}
			return columnNames;
		}

		public String getInsertScript(){
			StringBuilder sb = new StringBuilder()
			.append("insert into ").append(name).append(" (");
			String delim = "";
			for(Column column : columns){
				sb.append(delim).append(column.name);
				delim = ",";
			}
			sb.append(") values (");
			delim = "";
			for(Column column : columns){
				sb.append(delim).append("?");
				delim = ",";
			}
			sb.append(")");
			return sb.toString();
		}

		private class Column{
			String name;
			String modifier;
			DataType dataType;
			Column (String name, DataType dataType, String modifier){
				this.name = name;
				this.dataType = dataType;
				this.modifier = modifier;
			}

			@Override
			public String toString() {
				return name + " " + dataType + " " + (modifier == null ? "" : modifier);
			}
		}

}
