package proyecto;

import java.sql.Connection;

import javax.swing.JOptionPane;

import beans.Venta;
import dao.Dao;

public class Ejercicio2 {

	/**
	 * Inserta una venta en la base de datos
	 * En caso de existir, da la posibilidad de añadir otro producto a la venta
	 * 
	 * 
	 * @param conn - Conexión a la base de datos
	 * @param v - Venta
	 * @return -1 - No se ha podido insertar la venta
	 * @return 0 - Existe una venta con ese ID y no quiere insertar un nuevo producto
	 * @return 1 - Insertada la venta con éxito
	 * @return 2 - Insertado otro producto en una venta existente
	 * 
	 */
	
	public static int insertarVenta(Connection conn, Venta v) {
		int result;
		
		if (Dao.existeVentaId(conn, v.getIdventa())){
			result = JOptionPane.showConfirmDialog(null, "La venta ya existe. ¿Quiere insertar otro producto en la misma venta?",
			        "Insertar venta", JOptionPane.OK_CANCEL_OPTION);
			//result = 1 -> aceptar
			//result = 2 -> cancelar
			if (result == 2)
				return 0;
			if (result == 1){
				if(Dao.aniadirProductoAVentaExistente(v))
					return 2;
				else
					return -1;
			}
		}
		
		
		
		
		return Integer.MIN_VALUE; //TODO
	}

}
