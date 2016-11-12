package interfaz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import beans.Venta;

public class ModeloTablaVentas extends AbstractTableModel {
	private ArrayList<Venta> ventas;
	private String[] columns;

	public ModeloTablaVentas(ArrayList<Venta> ventas) {
		super();
		this.ventas = ventas;
		columns = new String[] { "Id", "Fecha", "Cliente", "Producto", "Cantidad" };
	}

	// Number of column of your table
	public int getColumnCount() {
		return columns.length;
	}

	// Number of row of your table
	public int getRowCount() {
		return ventas.size();
	}

	// The object to render in a cell
	public Object getValueAt(int row, int col) {
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Venta venta = ventas.get(row);
				
		switch (col) {
		case 0:
			return venta.getIdventa();
		case 1:
			return formatter.format(venta.getFechaventa());
		case 2:
			return venta.getCliente().getNombre();
		case 3:
			return venta.getProducto().getDescripcion();
		case 4:
			return venta.getCantidad();
		default:
			return null;
		}
	}

	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
	}

	// Optional, the name of your column
	public String getColumnName(int col) {
		return columns[col];
	}

	public void setVentas(ArrayList<Venta> ventas) {
		this.ventas = ventas;
	}

}