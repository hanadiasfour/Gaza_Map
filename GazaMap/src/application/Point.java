package application;

import javafx.scene.shape.Circle;

public class Point implements Comparable<Point> {

	private String name;
	private double latitude, longitude, x, y;// y,x
	private boolean isStreet;
	private Circle circle;
	private double distance = Double.MAX_VALUE;
	private Point pre;

	public Point(String name, double latitude, double longitude, boolean isStreet) {

		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isStreet = isStreet;
		this.x = calculateX();
		this.y = calculateY();

	}

	public Point(double distance) {

		this.distance = distance;
	}

	private double calculateX() {

		return ((this.longitude - 34.17592) * (857 - 5) / (34.66164 - 34.17592)) + 5;
		// ((longitude - longitudeMin)*(xMax - xMin)/(longitudeMax - longitudeMin))+xMin

	}

	private double calculateY() {

		return ((this.latitude - 31.59655) * (780 - 1) / (31.22225 - 31.59655)) + 1;

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

	public void setName(String name) {
		this.name = name;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setStreet(boolean isStreet) {
		this.isStreet = isStreet;
	}

	public String getName() {
		return name;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public boolean isStreet() {
		return isStreet;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;

	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Point getPre() {
		return pre;
	}

	public void setPre(Point pre) {
		this.pre = pre;
	}

	@Override
	public int compareTo(Point n) {

		if (n == null)
			return -1;

		else if (this.distance < n.distance)
			return -1;

		else if (this.distance > n.distance)
			return 1;
		else
			return 0;

	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub

		if (obj == null)
			return false;
		return this.name == ((Point) obj).getName();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

}
