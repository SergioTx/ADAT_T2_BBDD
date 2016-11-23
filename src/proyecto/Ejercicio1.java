package proyecto;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.db4o.ObjectContainer;

import beans.Cliente;
import beans.Producto;
import dao.Dao;

public class Ejercicio1 {

	public static void insertarClientes(Connection conn, String xml) {
		ArrayList<Cliente> clientes = leerClientes(xml);
		for (Cliente cliente : clientes) {
			System.out.println(cliente.toString());
			Dao.insertarCliente(conn, cliente);
		}
	}

	public static void insertarProductos(Connection conn, String xml) {
		ArrayList<Producto> productos = leerProductos(xml);
		for (Producto producto : productos) {
			System.out.println(producto.toString());
			Dao.insertarProducto(conn, producto);
		}
	}

	// DB4O
	public static void insertarProductos(ObjectContainer cont, String xml) {
		ArrayList<Producto> productos = leerProductos(xml);
		for (Producto producto : productos) {
			System.out.println(producto.toString());
			cont.store(producto);
		}
	}

	public static void insertarClientes(ObjectContainer cont, String xml) {
		ArrayList<Cliente> clientes = leerClientes(xml);
		for (Cliente cliente : clientes) {
			System.out.println(cliente.toString());
			cont.store(cliente);
		}
	}

	/**
	 * lee clientes del archivo XML de clientes
	 * 
	 * @param xml
	 * @return ArrayList de Clientes
	 */
	public static ArrayList<Cliente> leerClientes(String xmlPath) {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		SAXBuilder builder = new SAXBuilder();

		File file = new File(xmlPath);
		Document document;
		try {
			document = (Document) builder.build(file);

			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("cliente");

			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				Cliente c = new Cliente();
				c.setDireccion(node.getChildText("direccion"));
				c.setNif(node.getChildText("nif"));
				c.setNombre(node.getChildText("nombre"));
				c.setPoblacion(node.getChildText("poblacion"));
				c.setTelef(node.getChildText("telef"));

				clientes.add(c);
			}
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}

		return clientes;
	}

	/**
	 * lee productos del archivo XML de productos
	 * 
	 * @return ArrayList de Productos
	 */
	public static ArrayList<Producto> leerProductos(String xmlPath) {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		SAXBuilder builder = new SAXBuilder();

		File file = new File(xmlPath);
		Document document;
		try {
			document = (Document) builder.build(file);

			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("producto");

			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				Producto p = new Producto();
				p.setDescripcion(node.getChildText("descripcion"));
				p.setPvp(Double.parseDouble(node.getChildText("pvp")));
				p.setStockactual(Integer.parseInt(node.getChildText("stockactual")));
				p.setStockminimo(Integer.parseInt(node.getChildText("stockminimo")));

				productos.add(p);
			}
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}

		return productos;
	}
}