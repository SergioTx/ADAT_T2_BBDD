package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.db4o.ObjectContainer;

import beans.Cliente;
import beans.Producto;
import beans.Venta;
import dao.Dao;
import proyecto.Ejercicio1;
import proyecto.Ejercicio2;

public class Proyecto extends JFrame {

	private final String MYSQL = "MySQL", DB4O = "DB4O", SQLITE = "SQLite";

	private static Properties prop;
	private final String propFile = "config.properties";
	// En mayúsculas porque son constantes,
	// pero no deja declararlas como constantes
	// porque las lee en tiempo de ejecución del fichero
	private String XML_PRODUCTOS, XML_CLIENTES, SQLITE_FILE, DB4O_FILE;
	private String[] DBS = { MYSQL, SQLITE, DB4O };

	// auto añadido para quitar warning
	private static final long serialVersionUID = 1L;

	private JFrame oThis;

	private JPanel contentPane;

	private JComboBox<String> comboBoxPrompt;
	private JComboBox<String> comboBox;

	private Connection conn;
	private ObjectContainer cont;

	private String dbElegida;
	private JButton btn_importarProductos;
	private JTable tabla;
	private JTextField txt_idventa;
	private JTextField txt_cantidad;

	private ModeloTablaVentas tablamodel;

	private JComboBox<Cliente> combo_cliente;
	private JComboBox<Producto> combo_producto;
	private DefaultComboBoxModel<Producto> combomodel_producto;
	private DefaultComboBoxModel<Cliente> combomodel_cliente, combomodel_clienteFiltro;

	private JScrollPane scrollPane;

	private int idSeleccionadoVenta = -1;

	private JComboBox<Cliente> combo_filtroClientes;

	private Cliente filtroCliente = null;

	private JButton btn_importarClientes;

	private JButton btn_borrar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Proyecto frame = new Proyecto();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Proyecto() {
		oThis = this;
		setResizable(false);

		cargarPropiedades();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 573, 437);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btn_aniadir = new JButton("A\u00F1adir nueva");
		btn_aniadir.setBounds(318, 270, 229, 55);
		contentPane.add(btn_aniadir);

		btn_borrar = new JButton("Borrar venta seleccionada");
		btn_borrar.setBounds(318, 336, 229, 55);
		contentPane.add(btn_borrar);

		// prompt con un combo para elegir a qué base conectarse al principio
		promptComboBox();

		JLabel lblElegirSgbd = new JLabel("Cambiar SGBD:");
		lblElegirSgbd.setBounds(10, 11, 92, 14);
		contentPane.add(lblElegirSgbd);

		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(DBS));
		comboBox.setBounds(107, 8, 82, 20);
		contentPane.add(comboBox);
		comboBox.setSelectedItem(new String(dbElegida));

		btn_importarClientes = new JButton("Importar Clientes");
		btn_importarClientes.setBounds(277, 7, 130, 23);
		contentPane.add(btn_importarClientes);

		btn_importarProductos = new JButton("Importar Productos");
		btn_importarProductos.setBounds(417, 7, 130, 23);
		contentPane.add(btn_importarProductos);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 537, 93);
		contentPane.add(scrollPane);
		tabla = new JTable(tablamodel);
		scrollPane.setViewportView(tabla);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Venta", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 168, 275, 223);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblIdventa = new JLabel("IdVenta");
		lblIdventa.setBounds(10, 24, 255, 14);
		panel.add(lblIdventa);

		txt_idventa = new JTextField();
		txt_idventa.setBounds(10, 49, 255, 20);
		panel.add(txt_idventa);
		txt_idventa.setColumns(10);

		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setBounds(10, 80, 255, 14);
		panel.add(lblCliente);

		JLabel lblProducto = new JLabel("Producto");
		lblProducto.setBounds(10, 126, 255, 14);
		panel.add(lblProducto);

		txt_cantidad = new JTextField();
		txt_cantidad.setColumns(10);
		txt_cantidad.setBounds(10, 187, 255, 20);
		panel.add(txt_cantidad);

		JLabel lblCantidad = new JLabel("Cantidad");
		lblCantidad.setBounds(10, 172, 255, 14);
		panel.add(lblCantidad);

		combo_cliente = new JComboBox<Cliente>();
		combo_cliente.setModel(combomodel_cliente); // el modelo se carga al elegir la base de datos
		combo_cliente.setBounds(10, 95, 255, 20);
		panel.add(combo_cliente);

		combo_producto = new JComboBox<Producto>();
		combo_producto.setBounds(10, 141, 255, 20);
		combo_producto.setModel(combomodel_producto); // el modelo se carga al elegir la base de datos
		panel.add(combo_producto);

		combo_filtroClientes = new JComboBox<Cliente>();
		combo_filtroClientes.setModel(combomodel_clienteFiltro); // el modelo se carga al elegir la base de datos
		combo_filtroClientes.setBounds(417, 137, 130, 20);
		contentPane.add(combo_filtroClientes);

		JLabel lblFiltrarVentas = new JLabel("Filtrar ventas:");
		lblFiltrarVentas.setBounds(306, 140, 101, 14);
		contentPane.add(lblFiltrarVentas);

		// LISTENERS
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dbElegida = comboBox.getSelectedItem().toString();
				conectarSGBD();
			}
		});

		// ejercicio 3 - filtrar clientes por nombre
		combo_filtroClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtroCliente = (Cliente) combomodel_clienteFiltro.getSelectedItem();
				cargarTabla(filtroCliente);
			}
		});

		btn_importarProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (conn == null && cont != null)
					Ejercicio1.insertarProductos(cont, XML_PRODUCTOS);
				else
					Ejercicio1.insertarProductos(conn, XML_PRODUCTOS);
				//después de importar, cargamos el combo de nuevo
				comboProductos();
			}
		});
		btn_importarClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (conn == null && cont != null)
					Ejercicio1.insertarClientes(cont, XML_CLIENTES);
				else
					Ejercicio1.insertarClientes(conn, XML_CLIENTES);
				//después de importar, cargamos el combo de nuevo
				comboClientes();
			}
		});
		btn_aniadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Venta v = new Venta();
				try {
					v.setIdventa(Integer.parseInt(txt_idventa.getText().toString()));
					v.setCantidad(Integer.parseInt(txt_cantidad.getText().toString()));
					v.setFechaventa(new java.util.Date());
					Cliente c = (Cliente) combomodel_cliente.getSelectedItem();
					v.setCliente(c);
					Producto p = (Producto) combomodel_producto.getSelectedItem();
					v.setProducto(p);

					if (v.getIdventa() <= 0)
						JOptionPane.showMessageDialog(oThis, "La cantidad no puede ser negativa");
					else {
						int resultado;
						if (conn == null && cont != null){
							resultado = Ejercicio2.insertarVenta(cont, v);
						}else{
							resultado = Ejercicio2.insertarVenta(conn, v);
						}
						
						
						String mensaje = "";
						switch (resultado) {
						case -2:
							mensaje = "Ha ocurrido un error al insertar la venta.";
							break;
						case -1:
							mensaje = "Ya existe una venta con ese ID.";
							break;
						case 0:
							mensaje = "No hay stock suficiente para la venta";
							break;
						case 1:
							mensaje = "Insertado con éxito.";
							break;
						case 2:
							mensaje = "Insertado con éxito. Stock debajo del mínimo";
							break;
						}
						JOptionPane.showMessageDialog(oThis, mensaje);
					}

				} catch (NumberFormatException ex) {
					// si no ha introducido algún dato bien
					JOptionPane.showMessageDialog(oThis, "Por favor, rellene bien los campos");
				}
				cargarTabla(filtroCliente);
			}
		});
		btn_borrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (idSeleccionadoVenta != -1) {// valor por defecto cuando no hay ninguna seleccionada
					if (conn == null && cont != null)
						Dao.borrarVenta(cont, idSeleccionadoVenta);
					else
						Dao.borrarVenta(conn, idSeleccionadoVenta);
				}
				tablamodel.fireTableRowsDeleted(idSeleccionadoVenta, idSeleccionadoVenta);
				cargarTabla(filtroCliente);
			}
		});
		tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				// el evento también saltaba al borrar filas y la fila borrada era -1
				// saltaba nullPointerException
				if (tabla.getSelectedRow() != -1)
					idSeleccionadoVenta = (int) tabla.getValueAt(tabla.getSelectedRow(), 0);
			}
		});
	}

	private void cargarPropiedades() {
		// carga las variables desde el archivo de propiedades
		prop = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(propFile);
			prop.load(in);
			in.close();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		XML_PRODUCTOS = prop.getProperty("XML_PRODUCTOS");
		XML_CLIENTES = prop.getProperty("XML_CLIENTES");
		SQLITE_FILE = prop.getProperty("SQLITE_FILE");
		DB4O_FILE = prop.getProperty("DB4O_FILE");
	}

	/**
	 * carga el modelo de la tabla
	 */
	private void cargarTabla(Cliente filtroCliente) {
		ArrayList<Venta> ventas = null;
		if (conn == null && cont != null)
			ventas = Dao.todasLasVentas(cont, filtroCliente);
		else
			ventas = Dao.todasLasVentas(conn, filtroCliente);

		// si el botón existe, se dehabilita si no hay elementos en la tabla
		if (btn_borrar != null) {
			if (ventas != null && ventas.size() <= 0) {
				btn_borrar.setEnabled(false);
			} else {
				btn_borrar.setEnabled(true);
			}
		}

		if (tablamodel != null)
			tablamodel.setVentas(ventas);
		else
			tablamodel = new ModeloTablaVentas(ventas);
		// el modelo avisa a la tabla de que ha cambiado
		// sin esto no cambia el número de filas de la tabla, aunque sí cambiaba los valores
		tablamodel.fireTableDataChanged();
	}

	/**
	 * carga el combo de clientes si ya existía, lo borra y recarga
	 */
	private void comboClientes() {
		ArrayList<Cliente> clientes = null;
		if (conn == null && cont != null)
			clientes = Dao.todosLosClientes(cont);
		else
			clientes = Dao.todosLosClientes(conn);

		if (combomodel_cliente == null)
			combomodel_cliente = new DefaultComboBoxModel<Cliente>();

		if (combomodel_clienteFiltro == null)
			combomodel_clienteFiltro = new DefaultComboBoxModel<Cliente>();

		combomodel_cliente.removeAllElements();
		combomodel_clienteFiltro.removeAllElements();

		combomodel_clienteFiltro.addElement(null);// elemento en blanco para que aparezcan todos los clientes

		if (clientes != null) {
			for (Cliente c : clientes) {
				combomodel_clienteFiltro.addElement(c);
				combomodel_cliente.addElement(c);
			}
		}
	}

	/**
	 * carga el combo de productos si ya existía, lo borra y recarga
	 */
	private void comboProductos() {
		ArrayList<Producto> productos = null;
		if (conn == null && cont != null)
			productos = Dao.todosLosProductos(cont);
		else
			productos = Dao.todosLosProductos(conn);

		if (combomodel_producto == null)
			combomodel_producto = new DefaultComboBoxModel<Producto>();

		if (productos != null) {
			for (Producto p : productos) {
				combomodel_producto.addElement(p);
			}
		}

	}

	/**
	 * combo box inicial para elegir un SGBD
	 */
	private void promptComboBox() {
		// Se mantiene el elegido aunque se cierre la ventana en lugar de dar a OK
		comboBoxPrompt = new JComboBox<String>();
		comboBoxPrompt.setModel(new DefaultComboBoxModel<String>(DBS));
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Elige un SGBD:"), BorderLayout.NORTH);
		panel.add(comboBoxPrompt);
		JOptionPane.showMessageDialog(null, panel);
		dbElegida = comboBoxPrompt.getSelectedItem().toString();
		conectarSGBD();
	}

	private void conectarSGBD() {

		// si existía una conexión, la cerramos antes de abrir otra
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (cont != null) {
			cont.close();
			cont=null;
		}

		if (dbElegida.equals(MYSQL)) {
			conn = Dao.getMysqlConnection();
		}
		if (dbElegida.equals(SQLITE)) {
			conn = Dao.getSqliteConnection(SQLITE_FILE);
		}
		if (dbElegida.equals(DB4O)) {
			cont = Dao.getDB4OContainer(DB4O_FILE);
		}

		comboClientes();
		comboProductos();
		cargarTabla(filtroCliente);
	}
}
