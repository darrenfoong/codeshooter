package codeshooter.utils;

public class Heading {
	private static final double TWO_PI = 2 * Math.PI;
	private double heading;

	public Heading() {
		this.heading = 0;
	}

	public Heading(double heading) {
		this.heading = heading;
	}

	public double get() {
		return heading;
	}

	public double getSwingDegrees() {
		return (450 - Math.toDegrees(heading)) % 360;
	}

	public void change(double delta) {
		heading += delta;

		if ( heading < 0 ) {
			heading += TWO_PI;
		} else {
			heading %= TWO_PI;
		}
	}

	public double getX() {
		return Math.sin(heading);
	}

	public double getY() {
		return -Math.cos(heading);
	}
}
