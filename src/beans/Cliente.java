package beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Cliente {

	private int id;
	private String nombre, direccion, poblacion, telef, nif;

	public Cliente() {

	}

	public Cliente(int id, String nombre, String direccion, String poblacion, String telef, String nif) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.poblacion = poblacion;
		this.telef = telef;
		this.nif = nif;
	}

	public int getId() {
		return id;
	}
	@XmlElement
	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	@XmlElement
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}
	@XmlElement
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPoblacion() {
		return poblacion;
	}
	@XmlElement
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getTelef() {
		return telef;
	}
	@XmlElement
	public void setTelef(String telef) {
		this.telef = telef;
	}

	public String getNif() {
		return nif;
	}
	@XmlElement
	public void setNif(String nif) {
		this.nif = nif;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
