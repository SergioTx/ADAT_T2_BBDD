package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import dao.Dao;

//https://jonathanmelgoza.com/blog/como-hacer-graficos-con-java/
public class Grafico extends JFrame {

	private JPanel contentPane;

	Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Grafico frame = new Grafico();
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

	public Grafico(Connection conn) {
		this.conn = conn;
		cargarInterfaz();
	}

	// si no recibe conexión, creamos una a MySQL
	public Grafico() {
		this.conn = Dao.getMysqlConnection();
		cargarInterfaz();
	}

	void cargarInterfaz() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel graphic1 = new JPanel();
		tabbedPane.addTab("Producto-Cantidad", null, graphic1, null);
		graphic1.setLayout(new BorderLayout());

		JPanel graphic2 = new JPanel();
		tabbedPane.addTab("Clientes Habituales", null, graphic2, null);
		graphic2.setLayout(new BorderLayout());
		
		JPanel panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnGuardarJpg = new JButton("Guardar JPG");
		panelSouth.add(btnGuardarJpg);

		// gráficos (1)--------------------------------------------------
		// datos
		DefaultPieDataset data1 = new DefaultPieDataset();
		HashMap<String, Integer> hash = Dao.dameObjetoCantidadVendida(conn);
		Iterator it = hash.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry) it.next();
			// System.out.println(pair.getKey() + " = " + pair.getValue()); //
			// Log
			data1.setValue((String) pair.getKey(), (Integer) pair.getValue());
			it.remove(); // evita: ConcurrentModificationException
		}

		// Creando el Grafico
		JFreeChart chart1 = ChartFactory.createPieChart("Ventas por producto", data1, false, true, false);

		ChartPanel chartPanel = new ChartPanel(chart1);
		graphic1.add(chartPanel);

		// gráficos (2)--------------------------------------------------

		// datos
		//DefaultPieDataset data2 = new DefaultPieDataset();
		DefaultCategoryDataset data2 = new DefaultCategoryDataset(); 
		hash = Dao.dameClientesVentas(conn);
		it = hash.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue()); //TODO Log
			data2.setValue((Integer) pair.getValue(),"",(String) pair.getKey());
			it.remove(); // evita: ConcurrentModificationException
		}

		// Creando el Grafico
		//JFreeChart chart2 = ChartFactory.createPieChart("", data2, false, true, false);
		
		JFreeChart chart2 = ChartFactory.createBarChart( 
		         "Cantidad de compras por cliente",        //Título de la gráfica 
		         "",           //leyenda Eje horizontal 
		         "",      //leyenda Eje vertical 
		         data2,                   //datos 
		         PlotOrientation.VERTICAL,  //orientación 
		         false,                      //incluir leyendas 
		         true,                      //mostrar tooltips 
		         true);                   
		//chart2.setBackgroundPaint(Color.LIGHT_GRAY);
		CategoryPlot plot =(CategoryPlot) chart2.getPlot(); 
		//plot.setBackgroundPaint(Color.CYAN); //fondo del grafico 
		plot.setDomainGridlinesVisible(true);//lineas de rangos, visibles 
		plot.setRangeGridlinePaint(Color.DARK_GRAY);//color de las lineas de rangos

		ChartPanel chartPanel2 = new ChartPanel(chart2);
		graphic2.add(chartPanel2);

		// eventos-------------------------------------------------------

		btnGuardarJpg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					JFreeChart chart;
					if (tabbedPane.getSelectedIndex() == 0)
						chart = chart1;
					else
						chart = chart2;

					if (fc.showOpenDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) {
						String filepath = fc.getSelectedFile().getAbsolutePath();
						ChartUtilities.saveChartAsJPEG(new File(filepath + File.separator + "grafico.jpg"), chart, 500,
								500);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
