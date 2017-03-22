package codeshooter.model;

public class Sensor {
	public enum ReadingType { EMPTY, WALL, SELF, SHOOTER }

	private double angleInDegrees;
	private double range;
	private int numReadings;
	private double readingIntervalInDegrees;

	private double[] readings;
	private ReadingType[] types;

	public Sensor(double angleInDegrees, double range, int numReadings) {
		this.angleInDegrees = angleInDegrees;
		this.range = range;
		this.numReadings = numReadings;
		this.readingIntervalInDegrees = angleInDegrees/(numReadings-1);

		readings = new double[numReadings];
		types = new ReadingType[numReadings];
	}

	public double getAngle() {
		return angleInDegrees;
	}

	public double getRange() {
		return range;
	}

	public int getNumReadings() {
		return numReadings;
	}

	public double getReadingInterval() {
		return readingIntervalInDegrees;
	}

	public double getReading(int i) {
		return readings[i];
	}

	public ReadingType getType(int i) {
		return types[i];
	}

	public void updateReading(int i, double reading, ReadingType type) {
		readings[i] = reading;
		types[i] = type;
	}
}
