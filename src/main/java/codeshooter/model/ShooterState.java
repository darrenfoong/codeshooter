package codeshooter.model;

import java.io.Serializable;

import codeshooter.model.Sensor.ReadingType;

public class ShooterState implements Serializable {
	private static final long serialVersionUID = 1L;

	private double health;
	private double[] readings;
	private ReadingType[] types;

	public ShooterState(double health, double[] readings, ReadingType[] types) {
		this.health = health;
		this.readings = readings;
		this.types = types;
	}

	public double getHealth() {
		return health;
	}

	public double[] getReadings() {
		return readings;
	}

	public ReadingType[] getTypes() {
		return types;
	}
}
