package proyecto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
	
	static final String XML_CLIENTES = "ficheros/clientes.xml";
	static final String XML_PRODUCTOS = "ficheros/productos.xml";
	
	public static final String SQLITE_DB = "sqlite/ejercicio1.db";
	
	protected static int leerInt() throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = String.valueOf(in.readLine());
		return Integer.parseInt(str);
	}
	
	static String leerString() throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = String.valueOf(in.readLine());
		return str;
	}
}
