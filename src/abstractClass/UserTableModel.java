package abstractClass;
import resultSet.UserRS;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private final ArrayList<UserRS> users;
	private final String[] columnNames = {"ID", "Name", "Review Count", "Useful", "Funny", "Cool", "Average Stars", "Date"};
	
	public UserTableModel(ArrayList<UserRS> users) {
		this.users = users;
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		UserRS user = users.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getId();
            case 1: return user.getName();
            case 2: return user.getReviewCount();
            case 3: return user.getUseful();
            case 4: return user.getFunny();
            case 5: return user.getCool();
            case 6: return user.getAvgStars();
            case 7: return user.getDate();
            default: return null;
        }
	}
	
	@Override
    public String getColumnName(int index) {
        return columnNames[index];
    }

}

