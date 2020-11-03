package genetic;

public class City {

	private int CityNumber;
	
	private double x;
	
	private double y;

	public City(int cityNumber, double x, double y) {
		super();
		CityNumber = cityNumber;
		this.x = x;
		this.y = y;
	}

	public int getCityNumber() {
		return CityNumber;
	}

	public void setCityNumber(int cityNumber) {
		CityNumber = cityNumber;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "City [CityNumber=" + CityNumber + ", x=" + x + ", y=" + y + "]";
	}

	
}
