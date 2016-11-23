package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.sqlite.SQLiteDataSource;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import beans.Cliente;
import beans.Producto;
import beans.Venta;
import proyecto.Utils;

public class Dao {

	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public static Connection getMysqlConnection() {

		Connection conn = null;

		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser("ejercicio1");
		dataSource.setPassword("ejercicio1");
		dataSource.setServerName("localhost");
		dataSource.setDatabaseName("ejercicio1");

		try {
			conn = (Connection) dataSource.getConnection();
			System.out.println("Conectado a la base de datos MySQL...");
		} catch (SQLException e) {
			System.err.println("ERROR - al conectar a la base de datos MySQL");
		}

		return conn;
	}

	public static Connection getSqliteConnection(String filePath) {

		Connection conn = null;

		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setDatabaseName("ejercicio1");
		dataSource.setUrl("jdbc:sqlite:" + filePath);

		try {
			conn = (Connection) dataSource.getConnection();
			System.out.println("Conectado a la base de datos SQLite...");
		} catch (SQLException e) {
			System.err.println("ERROR - al conectar a la base de datos SQLite");
		}

		return conn;
	}

	public static ObjectContainer getDB4OContainer(String filePath) {
		ObjectContainer container = null;

		container = Db4oEmbedded.openFile(filePath);

		return container;
	}

	/**
	 * Inserta un cliente en la base de datos
	 */
	public static boolean insertarCliente(Connection conn, Cliente cli) {
		int count = 0;
		// String driver = conn.toString();
		String sql = "INSERT INTO clientes (nombre,direccion,poblacion,telef,nif) VALUES (?,?,?,?,?)";

		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cli.getNombre());
			stmt.setString(2, cli.getDireccion());
			stmt.setString(3, cli.getPoblacion());
			stmt.setString(4, cli.getTelef());
			stmt.setString(5, cli.getNif());

			count = stmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("ERROR - al hacer la consulta sql: " + sql);
			e.printStackTrace();
		}
		return (count == 0) ? false : true;
	}

	/**
	 * Inserta un producto en la base de datos
	 */
	public static boolean insertarProducto(Connection conn, Producto prod) {

		int count = 0;
		String sql = "INSERT INTO productos (descripcion,stockactual,stockminimo,pvp) VALUES (?,?,?,?)";

		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, prod.getDescripcion());
			stmt.setInt(2, prod.getStockactual());
			stmt.setInt(3, prod.getStockminimo());
			stmt.setDouble(4, prod.getPvp());

			count = stmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("ERROR - al hacer la consulta sql: " + sql);
			e.printStackTrace();
		}
		return (count == 0) ? false : true;
	}

	/**
	 * 
	 * @param conn
	 * @param filtroCliente
	 *            Si no se quiere aplicar filtro, pasar null
	 * @return
	 */
	public static ArrayList<Venta> todasLasVentas(Connection conn, Cliente filtroCliente) {
		ArrayList<Venta> ventas = new ArrayList<Venta>();

		String sql = "SELECT idventa, fechaventa, cantidad, idcliente, idproducto, "
				+ "clientes.nombre, clientes.direccion, clientes.poblacion, clientes.telef, clientes.nif, productos.id, "
				+ "productos.descripcion, productos.stockactual, productos.stockminimo, productos.pvp " + "FROM ventas, clientes, productos "
				+ "WHERE ventas.idcliente = clientes.id " + "AND ventas.idproducto = productos.id";

		if (filtroCliente != null)
			sql += " AND ventas.idcliente = " + filtroCliente.getId();

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				Venta v;
				Cliente c = new Cliente();
				Producto p = new Producto();
				c.setId(rs.getInt("idcliente"));
				c.setNombre(rs.getString("nombre"));
				c.setDireccion(rs.getString("direccion"));
				c.setPoblacion(rs.getString("poblacion"));
				c.setTelef(rs.getString("telef"));
				c.setNif(rs.getString("nif"));

				p.setId(rs.getInt("idproducto"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setStockactual(rs.getInt("stockactual"));
				p.setStockminimo(rs.getInt("stockminimo"));
				p.setPvp(rs.getDouble("pvp"));

				if (conn.toString().toLowerCase().contains("sqlite")) {
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date date = null;
					try {
						date = formatter.parse(rs.getString(2));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					v = new Venta(rs.getInt("idventa"), date, c, p, rs.getInt("cantidad"));
				} else {
					v = new Venta(rs.getInt("idventa"), new java.util.Date(rs.getDate(2).getTime()), c, p, rs.getInt("cantidad"));
				}
				ventas.add(v);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ventas;
	}

	public static ArrayList<Cliente> todosLosClientes(Connection conn) {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sql = "SELECT id, nombre, direccion, poblacion, telef, nif " + "FROM clientes " + "ORDER BY nombre";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Cliente c = new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
				clientes.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return clientes;
	}

	public static ArrayList<Producto> todosLosProductos(Connection conn) {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT id, descripcion, stockactual, stockminimo, pvp FROM productos";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Producto p = new Producto(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5));
				productos.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return productos;
	}

	/**
	 * Inserta una venta NO COMPRUEBA que exista (sql por medio de claves sí).
	 * Para comprobar usar existeVentaId()
	 * 
	 * @param conn
	 * @param v
	 * @return
	 */
	public static boolean insertarVenta(Connection conn, Venta v) {

		int count = 0;
		if (!hayStockSuficiente(conn, v.getCantidad(), v.getProducto().getId()))
			return false;

		actualizarStock(conn, v.getCantidad(), v.getProducto().getId());

		String sql = "INSERT INTO ventas (idventa, fechaventa, idcliente, idproducto, cantidad) VALUES (?,?,?,?,?)";

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, v.getIdventa());
			if (conn.toString().toLowerCase().contains("sqlite")) {
				pstmt.setString(2, formatter.format(new Date()));
			} else {
				pstmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
			}

			pstmt.setInt(3, v.getCliente().getId());
			pstmt.setInt(4, v.getProducto().getId());
			pstmt.setInt(5, v.getCantidad());

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("ERROR - al hacer la consulta sql: " + sql);
			e.printStackTrace();
			return false;
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count == 1;
	}

	private static void actualizarStock(Connection conn, int cantidad, int id) {
		String sql = "UPDATE productos SET stockactual = stockactual - ? WHERE id = ?";

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cantidad);
			pstmt.setInt(2, id);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("ERROR - al hacer la consulta sql: " + sql);
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private static boolean hayStockSuficiente(Connection conn, int cantidad, int idproducto) {

		String sql = "SELECT stockactual FROM productos WHERE id = ?";
		int stock = -1;

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idproducto);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				stock = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.err.println("ERROR - al hacer la consulta sql: " + sql);
			e.printStackTrace();
			return false;
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return stock > cantidad;
	}

	public static boolean existeVentaId(Connection conn, int idventa) {
		String sql = "SELECT idventa FROM ventas WHERE idventa = " + idventa;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				// si ya existe, vuelve
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return true; // ERROR
		} finally { // cerrar los statement
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// TODO falta actualizar el stock si se borra una venta
	public static boolean borrarVenta(Connection conn, int idVenta) {
		int count = 0;
		String sql = "DELETE FROM ventas WHERE idventa = ?";

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idVenta);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("ERROR - al hacer la consulta sql: " + sql);
			e.printStackTrace();
			return false;
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (count == 0)
			return false;
		return true;
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * DB4O Mismas funciones pero reciben el ObjectContainer en lugar de Connection
	 */

	// TODO falta actualizar el stock si se borra una venta
	public static boolean borrarVenta(ObjectContainer cont, int idVenta) {
		
		Venta v = new Venta();
		v.setIdventa(idVenta);
		cont.delete(v);
		//mantengo el return boolean para la compatibilidad con las otras funciones
		return true;
	}

	public static ArrayList<Venta> todasLasVentas(ObjectContainer cont, Cliente filtroCliente) {
		ArrayList<Venta> ventas = new ArrayList<Venta>();
		Venta v = new Venta();
		v.setCliente(filtroCliente);
		ObjectSet<Venta> set = cont.queryByExample(v);
		while (set.hasNext()) {
			ventas.add(set.next());
		}
		return ventas;
	}

	public static ArrayList<Cliente> todosLosClientes(ObjectContainer cont) {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		ObjectSet<Cliente> set = cont.queryByExample(new Cliente());
		while (set.hasNext()) {
			clientes.add(set.next());
		}
		return clientes;
	}

	public static ArrayList<Producto> todosLosProductos(ObjectContainer cont) {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		ObjectSet<Producto> set = cont.queryByExample(new Producto());
		while (set.hasNext()) {
			productos.add(set.next());
		}
		return productos;
	}

	public static boolean existeVentaId(ObjectContainer cont, int idventa) {
		Venta v = new Venta();
		v.setIdventa(idventa);
		ObjectSet<Venta> set = cont.queryByExample(new Venta());
		while (set.hasNext()) {
			return true;
		}
		return false;
	}

	public static boolean insertarVenta(ObjectContainer cont, Venta v) {
		cont.store(v);
		return true;
	}
}
