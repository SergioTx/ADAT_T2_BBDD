package proyecto;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import beans.Cliente;
import beans.Producto;
import dao.Dao;

public class Ejercicio1 {

	public static void insertarClientes(Connection conn) {
		ArrayList<Cliente> clientes = leerClientes();
		for (Cliente cliente : clientes) {
			System.out.println(cliente.toString());
			Dao.insertarCliente(conn, cliente);
		}
	}

	/**
	 * Inserta los clientes del fichero XML en la base de datos
	 * 
	 * @param conn
	 */
	public static void insertarProductos(Connection conn) {
		ArrayList<Producto> productos = leerProductos();
		for (Producto producto : productos) {
			System.out.println(producto.toString());
			Dao.insertarProducto(conn, producto);
		}
	}

	/////////////////////////////////////////////////////////////////////////

	/**
	 * lee clientes del archivo XML de clientes
	 * 
	 * @return ArrayList de Clientes
	 */
	public static ArrayList<Cliente> leerClientes() {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		SAXBuilder builder = new SAXBuilder();

		File file = new File(Utils.XML_CLIENTES);
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
	public static ArrayList<Producto> leerProductos() {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		SAXBuilder builder = new SAXBuilder();

		File file = new File(Utils.XML_PRODUCTOS);
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