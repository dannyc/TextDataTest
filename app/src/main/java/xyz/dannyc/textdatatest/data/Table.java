package xyz.dannyc.textdatatest.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 19-Apr-15.
 */
public class Table {
		private String name;
		private List<Column> columns = new ArrayList<>();

		public Table(String name){
			this.name = name;
        }

		public void addColumn(String name, String modifier) {
			Column column = new Column(name,modifier);
			columns.add(column);
		}

        public String getCreateScript(){
			StringBuilder sb = new StringBuilder()
				.append("create table ")
				.append(name)
				.append("(_id integer primary key autoincrement, "); //there always is an _id column as key
			for(int i = 0; i < columns.size(); i++){
				sb.append(columns.get(i));
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
			Column (String name, String modifier){
				this.name = name;
				this.modifier = modifier;
			}

			@Override
			public String toString() {
				return name + " " + modifier;
			}
		}

}
