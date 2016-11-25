package proyecto;

import java.sql.Connection;

import com.db4o.ObjectContainer;

import beans.Venta;
import dao.Dao;

public class Ejercicio2 {

	/**
	 * Inserta una venta en la base de datos (SQL)
	 * 
	 * @param conn - Conexión a la base de datos
	 * @param v - Venta a insertar
	 * 
	 * @return -2 - Ya existe una venta con ese ID
	 * @return -1 - No se ha podido insertar la venta
	 * @return 0 - No hay stock suficiente
	 * @return 1 - Insertada la venta con éxito
	 * @return 2 - Insertada la venta con éxito y stock por debajo del mínimo
	 * 
	 */
	public static int insertarVenta(Connection conn, Venta v) {
	
		if (Dao.existeVentaId(conn, v.getIdventa())){
			return -1;
		}
		return Dao.insertarVenta(conn, v);
	}

	/**
	 * Inserta una venta en la base de datos (DB4O)
	 * 
	 * @param cont - Conexión a la base de datos
	 * @param v - Venta a insertar
	 * 
	 * @return -1 - No se ha podido insertar la venta
	 * @return -2 - Existe una venta con ese ID
	 * @return 1 - Insertada la venta con éxito
	 * 
	 */
	public static int insertarVenta(ObjectContainer cont, Venta v) {
		if (Dao.existeVentaId(cont, v.getIdventa())){
			return -2;
		}
		return Dao.insertarVenta(cont, v);
	}
}
