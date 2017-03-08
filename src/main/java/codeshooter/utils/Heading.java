package codeshooter.utils;

public class Heading {
	private static final double TWO_PI = 2 * Math.PI;
	private double heading;

	public Heading() {
		this.heading = 0;
	}

	public double get() {
		return heading;
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
