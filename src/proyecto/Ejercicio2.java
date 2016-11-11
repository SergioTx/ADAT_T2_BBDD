package proyecto;

import java.sql.Connection;

import javax.swing.JOptionPane;

import beans.Venta;
import dao.Dao;

public class Ejercicio2 {

	/**
	 * Inserta una venta en la base de datos
	 * 
	 * @param conn - Conexión a la base de datos
	 * @param v - Venta
	 * 
	 * @return -1 - No se ha podido insertar la venta
	 * @return 0 - Existe una venta con ese ID
	 * @return 1 - Insertada la venta con éxito
	 * 
	 */
	public static int insertarVenta(Connection conn, Venta v) {
		int result;
		boolean existe;
		
		if (Dao.existeVentaId(conn, v.getIdventa())){
			return 0;
		}
		if (Dao.insertarVenta(conn, v))
			return 1;
		return 0;
	}

}
