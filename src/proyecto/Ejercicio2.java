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
	 * @return -1 - No se ha podido insertar la venta
	 * @return 0 - Existe una venta con ese ID
	 * @return 1 - Insertada la venta con éxito
	 * @return 2 - Insertada la venta con éxito y stock por debajo del mínimo
	 * 
	 */
	public static int insertarVenta(Connection conn, Venta v) {
	
		if (Dao.existeVentaId(conn, v.getIdventa())){
			return 0;
		}
		switch (Dao.insertarVenta(conn, v)){
		case -1:
			return -1;
		case 1:
			return 1;
		case 2:
			return 2;
		}
		return -1;
	}

	/**
	 * Inserta una venta en la base de datos (DB4O)
	 * 
	 * @param cont - Conexión a la base de datos
	 * @param v - Venta a insertar
	 * 
	 * @return -1 - No se ha podido insertar la venta
	 * @return 0 - Existe una venta con ese ID
	 * @return 1 - Insertada la venta con éxito
	 * 
	 */
	public static int insertarVenta(ObjectContainer cont, Venta v) {
		if (Dao.existeVentaId(cont, v.getIdventa())){
			return 0;
		}
		switch (Dao.insertarVenta(cont, v)){
		case -1:
			return -1;
		case 1:
			return 1;
		case 2:
			return 2;
		}
		return -1;
	}
}
