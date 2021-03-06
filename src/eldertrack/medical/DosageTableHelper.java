package eldertrack.medical;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import eldertrack.db.SQLObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class DosageTableHelper  {
	
	public static DefaultTableModel getElderlyFromQueryDos(String TimeOfDay,String name) throws SQLException {
		SQLObject so = new SQLObject();
		ResultSet rs = null;

		if(TimeOfDay.equalsIgnoreCase("Morning")){
			PreparedStatement stmt  = so.getPreparedStatementWithKey("SELECT morningdosage FROM et_elderly WHERE name = ?");
			stmt.setString(1,name);
			stmt.executeQuery();
			System.out.println(stmt);
			rs = stmt.getResultSet();
		}
		else if(TimeOfDay.equalsIgnoreCase("Afternoon")){
			PreparedStatement stmt  = so.getPreparedStatementWithKey("SELECT afternoondosage FROM et_elderly WHERE name = ?");
			stmt.setString(1,name);
			stmt.executeQuery();
			System.out.println(stmt);
			rs = stmt.getResultSet();
		}
		else if(TimeOfDay.equalsIgnoreCase("Noon")){
			PreparedStatement stmt  = so.getPreparedStatementWithKey("SELECT noondosage FROM et_elderly WHERE name = ?");
			stmt.setString(1,name);
			stmt.executeQuery();
			System.out.println(stmt);
			rs = stmt.getResultSet();
		}
		
		return (DefaultTableModel) buildTableModelForDos(rs);
	}



	@SuppressWarnings("unchecked")
	public static DefaultTableModel buildTableModelForDos(ResultSet rs) throws SQLException {
		ArrayList<DosageObject> DosageList=null;
		// debug statement
		System.out.println(rs);
		try {
			while(rs.next()){
				ByteArrayInputStream in = new ByteArrayInputStream(rs.getBytes(1));
				ObjectInputStream is = new ObjectInputStream(in);
				Object retrieveDosBlob =(Object) is.readObject();
				if(retrieveDosBlob instanceof  ArrayList<?>){
					DosageList=((ArrayList<DosageObject>) retrieveDosBlob);
				}
			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		// storing array list in an array list for future uses

		Vector<String> columnNames = new Vector<String>();

		columnNames.add("Description");
		columnNames.add("Prescription");
		columnNames.add("Medication Type");
		columnNames.add("Dosage");
		columnNames.add("Checked");

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		for(int k=0;k<DosageList.size();k++){
			Vector<Object> vector = new Vector<Object>();
			vector.add(DosageList.get(k).getMedDescrip());
			vector.add(DosageList.get(k).getMedPrescrip());
			vector.add(DosageList.get(k).getMedType());
			vector.add(DosageList.get(k).getMedDosage());
			vector.add("Not Feed");
			data.add(vector);
		}

		DefaultTableModel dtm = new DefaultTableModel(data, columnNames) {
			private static final long serialVersionUID = 4234183862785566645L;

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex < 4) {
					return false;
				} else {
					return true;
				}
			}
		};
		return dtm;
	}

	// For Management
	public static DefaultTableModel getElderlyFromQueryManagement(String search) throws SQLException {
		search = (search.equalsIgnoreCase("")) ? "%" : search;
		SQLObject so = new SQLObject();
		return buildTableModelForManagement(so.getResultSet("SELECT id, name, gender, room, morningdosage,afternoondosage,noondosage FROM et_elderly WHERE name LIKE ?", search));
	}

	public static DefaultTableModel buildTableModelForManagement(ResultSet rs) throws SQLException {
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("ID");
		columnNames.add("NAME");
		columnNames.add("GENDER");
		columnNames.add("ROOM");
		columnNames.add("MORNING");
		columnNames.add("AFTERNOON");
		columnNames.add("NOON");
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <=7; columnIndex++) {
				if(columnIndex==1 ||columnIndex<5){
				vector.add(rs.getObject(columnIndex));
				}
				else if(columnIndex==5){
					if(rs.getObject(columnIndex)!=null){
						vector.add("Added");
					}
					else{
						vector.add("");
					}
				}
				else if(columnIndex==6){
					if(rs.getObject(columnIndex)!=null){
						vector.add("Added");
					}
					else{
						vector.add("");
					}
				}
				else if(columnIndex==7){
					if(rs.getObject(columnIndex)!=null){
						vector.add("Added");
					}
					else{
						vector.add("");
					}
				}
				
			}
			
			
			data.add(vector);
		}
		DefaultTableModel dtm = new DefaultTableModel(data, columnNames) {
			private static final long serialVersionUID = 4234183862785566645L;

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex < 8) {
					return false;
				} else {
					return true;
				}
			}
		};
		return dtm;
	}

	public static DefaultTableModel getElderlyFromQueryManagementDos(String TimeOfDay,String name,SQLObject so) throws SQLException {
		ResultSet rs = null;
		if(TimeOfDay.equalsIgnoreCase("Morning")){
			PreparedStatement stmt  = so.getPreparedStatementWithKey("SELECT morningdosage FROM et_elderly WHERE name = ?");
			stmt.setString(1,name);
			stmt.executeQuery();
			System.out.println(stmt);
			rs = stmt.getResultSet();
		}
		else if(TimeOfDay.equalsIgnoreCase("Afternoon")){
			PreparedStatement stmt  = so.getPreparedStatementWithKey("SELECT afternoondosage FROM et_elderly WHERE name = ?");
			stmt.setString(1,name);
			stmt.executeQuery();
			System.out.println(stmt);
			rs = stmt.getResultSet();
		}
		else if(TimeOfDay.equalsIgnoreCase("Noon")){
			PreparedStatement stmt  = so.getPreparedStatementWithKey("SELECT noondosage FROM et_elderly WHERE name = ?");
			stmt.setString(1,name);
			stmt.executeQuery();
			System.out.println(stmt);
			rs = stmt.getResultSet();
		}
		return buildTableModelManagementDos(rs);
	}



	@SuppressWarnings("unchecked")
	public static DefaultTableModel buildTableModelManagementDos(ResultSet rs) throws SQLException {
		ArrayList<DosageObject> DosageList=null;
		try {
			while(rs.next()){
				ByteArrayInputStream in = new ByteArrayInputStream(rs.getBytes(1));
				ObjectInputStream is = new ObjectInputStream(in);
				Object retrieveDosBlob =(Object) is.readObject();
				if(retrieveDosBlob instanceof  ArrayList<?>){
					DosageList=((ArrayList<DosageObject>) retrieveDosBlob);
				}
			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		Vector<String> columnNames = new Vector<String>();

		columnNames.add("Description");
		columnNames.add("Prescription");
		columnNames.add("Medication Type");
		columnNames.add("Dosage");

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		for(int k=0;k<DosageList.size();k++){
			Vector<Object> vector = new Vector<Object>();
			vector.add(DosageList.get(k).getMedDescrip());
			vector.add(DosageList.get(k).getMedPrescrip());
			vector.add(DosageList.get(k).getMedType());
			vector.add(DosageList.get(k).getMedDosage());

			data.add(vector);
		}

		DefaultTableModel dtm = new DefaultTableModel(data, columnNames) {
			private static final long serialVersionUID = 4234183862785566645L;

		};
		return dtm;
	}
	
	public static void TableListen(JTable Model,SQLObject so){
		Model.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent evt) {
				if(Model.getModel().getRowCount()!=0){
					if(Model.getSelectedColumn()==0){
						if(!Model.getModel().getValueAt( Model.getSelectedRow(), 0).toString().equals("-Selection-")){
							String treatmentChoice=Model.getModel().getValueAt(Model.getSelectedRow(), Model.getSelectedColumn()).toString();
							JComboBox<String> MedicationComboBox = null;
							JComboBox<Double> DosageLimitComboBox = null;
							
								
							TableColumn medicationCol=Model.getColumnModel().getColumn(1);
							MedicationComboBox=DosageObject.GetListOfMedication(so,treatmentChoice);
							medicationCol.setCellEditor(new DefaultCellEditor(MedicationComboBox));
							
							TableColumn dosageLimitCol=Model.getColumnModel().getColumn(3);
							DosageLimitComboBox=DosageObject.GetListOfDosageLimit(so,treatmentChoice);
							dosageLimitCol.setCellEditor(new DefaultCellEditor(DosageLimitComboBox));
						}
					}
				}
			}
		});
	}
	
	public static void AddManagementModel(JTable Model,SQLObject so){
		JComboBox<String> treatmentBox = null;
		TableColumn treatmentCol=Model.getColumnModel().getColumn(0);
		treatmentBox=DosageObject.GetListOfTreatMent(so);
		treatmentCol.setCellEditor(new DefaultCellEditor(treatmentBox));		
	}

}

