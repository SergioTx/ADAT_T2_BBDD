package beans;

public class Producto {
	
	private int id;
	private String descripcion;
	private int stockactual, stockminimo;
	private double pvp;
	
	public Producto() {
		
	}
	public Producto(int id, String descripcion, int stockactual, int stockminimo, double pvp) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.stockactual = stockactual;
		this.stockminimo = stockminimo;
		this.pvp = pvp;
	}
	//getters/setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getStockactual() {
		return stockactual;
	}
	public void setStockactual(int stockactual) {
		this.stockactual = stockactual;
	}
	public int getStockminimo() {
		return stockminimo;
	}
	public void setStockminimo(int stockminimo) {
		this.stockminimo = stockminimo;
	}
	public double getPvp() {
		return pvp;
	}
	public void setPvp(double pvp) {
		this.pvp = pvp;
	}
	@Override
	public String toString() {
		return this.descripcion;
	}
	
	
}
