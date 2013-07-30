package LocateWind.Comtois;

public class Wind {

	private int latitude;
	private int longitude;
	private double force;
	private String orientation;
	private String heure;
	private String date;

	public Wind(int latitude, int longitude, double force, String orientation,
			String heure, String date) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.force = force;
		this.orientation = orientation;
		this.heure = heure;
		this.date = date;

	}

	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public double getForce() {
		return force;
	}

	public String getOrientation() {
		return orientation;
	}

	public String getHeure() {
		return heure;
	}

	public String getDate() {
		return date;
	}

}