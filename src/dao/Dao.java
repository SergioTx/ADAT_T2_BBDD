package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.sqlite.SQLiteDataSource;

import java.sql.SQLException;

import com.mysql.jdbc.integration.jboss.ExtendedMysqlExceptionSorter;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import beans.Cliente;
import beans.Producto;
import beans.Venta;
import proyecto.Utils;

public class Dao {

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

	public static Connection getSqliteConnection() {

		Connection conn = null;

		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setDatabaseName("ejercicio1");
		dataSource.setUrl("jdbc:sqlite:" + Utils.SQLITE_DB);

		try {
			conn = (Connection) dataSource.getConnection();
			System.out.println("Conectado a la base de datos SQLite...");
		} catch (SQLException e) {
			System.err.println("ERROR - al conectar a la base de datos SQLite");
		}

		return conn;
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

	public static ArrayList<Venta> todasLasVentas(Connection conn) {
		ArrayList<Venta> ventas = new ArrayList<Venta>();

		String sql = "SELECT idventa, fechaventa, cantidad, idcliente, idproducto, "
				+ "clientes.nombre, clientes.direccion, clientes.poblacion, clientes.telef, clientes.nif, productos.id, "
				+ "productos.descripcion, productos.stockactual, productos.stockminimo, productos.pvp " + "FROM ventas, clientes, productos "
				+ "WHERE ventas.idcliente = clientes.id " + "AND ventas.idproducto = productos.id";

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

					v = new Venta(rs.getInt(1), date, c, p, rs.getInt(4));
				} else {
					v = new Venta(rs.getInt(1), new java.util.Date(rs.getDate(2).getTime()), c, p, rs.getInt(4));
				}
				ventas.add(v);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ventas;
	}

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

					v = new Venta(rs.getInt(1), date, c, p, rs.getInt(4));
				} else {
					v = new Venta(rs.getInt(1), new java.util.Date(rs.getDate(2).getTime()), c, p, rs.getInt(4));
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

		String sql = "SELECT id, nombre, direccion, poblacion, telef, nif " 
		+ "FROM clientes "
		+ "ORDER BY nombre";
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

		String sql = "SELECT id, descripcion, stockactual, stockminimo, pvp " + "FROM productos";
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

	public static boolean insertarVenta(Connection conn, Venta v) {

		int count = 0;
		
		if (existeVentaId(conn,v.getIdventa())){
			System.out.println("Ya existe una venta con ese id.");
			return false;
		}
		
		String sql = "INSERT INTO ventas (idventa, fechaventa, idcliente, idproducto, cantidad) VALUES (?,?,?,?,?)";

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, v.getIdventa());
			pstmt.setDate(2, new java.sql.Date(v.getFechaventa().getTime()));
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
	
	public static boolean existeVentaId(Connection conn, int idventa){
		String sql = "SELECT idventa FROM ventas WHERE idventa = " + idventa;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				//si ya existe, vuelve
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return true; //ERROR
		} finally{ //cerrar los statement
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

	public static boolean aniadirProductoAVentaExistente(Venta v) {
		// TODO Auto-generated method stub
		return true;
	}

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
	
}