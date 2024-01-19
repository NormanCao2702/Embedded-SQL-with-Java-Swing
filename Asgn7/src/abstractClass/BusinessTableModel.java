package abstractClass;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import resultSet.BusinessRS;

public class BusinessTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final ArrayList<BusinessRS> businesses;
    private final String[] columnNames = {"ID", "Name", "Address", "City", "Stars"};

    public BusinessTableModel(ArrayList<BusinessRS> businesses) {
        this.businesses = businesses;
    }

    @Override
    public int getRowCount() {
        return businesses.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BusinessRS business = businesses.get(rowIndex);
        switch (columnIndex) {
            case 0: return business.getId();
            case 1: return business.getName();
            case 2: return business.getAddress();
            case 3: return business.getCity();
            case 4: return business.getStars();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }
}
