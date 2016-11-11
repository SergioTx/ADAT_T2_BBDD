package proyecto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import beans.Cliente;
import beans.Producto;
import dao.Dao;

public class Proyecto {

	//estructura antigua sin interfaz gráfica
	
	/*public static void main(String[] args) {

		Connection conn = null;
		boolean seguir = true;
		int opcion = -1;

		conn = elegirSGBD(conn);
		
		while (seguir) {
			mostrarMenu(conn.toString());
			do {
				try {
					opcion = Utils.leerInt();
				} catch (IOException e) {
					System.err.println("ERROR - vuelve a introducir la opción elegida");
				}
			} while (opcion != 0 && opcion != 1 && opcion != 2 && opcion != 3 && opcion != 4 && opcion != 5);

			switch (opcion) {
			case 1:
				conn = elegirSGBD(conn);
				break;
			case 2:
				Ejercicio1.insertarClientes(conn);
				break;
			case 3:
				Ejercicio1.insertarProductos(conn);
				break;
			case 4:
				Ejercicio2.insertarVenta();
				break;
			case 5:
				Ejercicio3.leerVentas();
				break;
			case 0:
				seguir = false;
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}

			// elegir bbdd

			// insertar desde archivo

			// clientes

			// productos

		}
	}

	/*private static Connection elegirSGBD(Connection conn) {

		int opcion = -1;

		// cerrar la conexión si estaba abierta
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		do {
			try {
				System.out.println("Elige un SGBD(1,2,3):");
				System.out.println("1.- MySQL");
				System.out.println("2.- SQLite");
				System.out.println("3.- DB4O");
				opcion = Utils.leerInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (opcion != 1 && opcion != 2 && opcion != 3);

		switch (opcion) {
		case 1:
			conn = Dao.getMysqlConnection();
			break;
		case 2:
			conn = Dao.getSqliteConnection();
			break;
		case 3:
			conn = Dao.getMysqlConnection(); // TODO change
			break;
		}
		return conn;
	}

	private static void mostrarMenu(String driver) {
		if (driver.contains("mysql")) {
			driver = "MySQL";
		}
		if (driver.contains("sqlite")){
			driver = "SQLite";
		}
		// TODO otros drivers

		System.out.println("\n------------------------------------------");
		System.out.println("MENU - " + driver);
		System.out.println("------------------------------------------");
		System.out.println("1.- Cambiar de sistema gestor de base de datos");
		System.out.println("2.- Insertar desde archivo clientes");
		System.out.println("3.- Insertar desde archivo productos");
		System.out.println("4.- Insertar una venta");
		System.out.println("5.- Leer ventas");
		System.out.println("0.- SALIR");
		System.out.println("------------------------------------------");
	}*/
}
