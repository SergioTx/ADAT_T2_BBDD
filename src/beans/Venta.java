package beans;

import java.util.Date;

public class Venta {

	private int idventa;
	private Date fechaventa;
	private Cliente cliente;
	private Producto producto;
	private int cantidad;

	public Venta() {
	}
	
	

	public Venta(int idventa, Date fechaventa, Cliente cliente, Producto producto, int cantidad) {
		super();
		this.idventa = idventa;
		this.fechaventa = fechaventa;
		this.cliente = cliente;
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public int getIdventa() {
		return idventa;
	}

	public void setIdventa(int idventa) {
		this.idventa = idventa;
	}

	public Date getFechaventa() {
		return fechaventa;
	}

	public void setFechaventa(Date fechaventa) {
		this.fechaventa = fechaventa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}



	@Override
	public String toString() {
		return "Venta [idventa=" + idventa + ", fechaventa=" + fechaventa + ", cliente=" + cliente + ", producto=" + producto + ", cantidad=" + cantidad + "]";
	}

	//TODO
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Venta other = (Venta) obj;
		if (idventa != other.idventa)
			return false;
		return true;
	}


/* Comparar objetos ventas
	public static void main(String[] args) {
		Venta v1 = new Venta();
		Venta v2 = new Venta();
		Venta v3 = new Venta();
		
		v1.setIdventa(1);
		v2.setIdventa(2);
		v3.setIdventa(1);
		
		System.out.println(v1.equals(v2));
		System.out.println(v1.equals(v3));
	}*/
}
