package codeshooter.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import codeshooter.model.Sensor.ReadingType;

public class ShooterState implements Serializable {
	private static final long serialVersionUID = 2L;

	private static final int BYTE_BUFFER_SIZE = 1024;

	private double health;
	private double[] readings;
	private ReadingType[] types;

	public ShooterState(double health, double[] readings, ReadingType[] types) {
		this.health = health;
		this.readings = readings.clone();
		this.types = types.clone();
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

	@Override
	public String toString() {
		String res = "";

		res += "Health: " + health + "; Readings: ";

		for ( int i = 0; i < readings.length; i++ ) {
			res += readings[i] + " ";
		}

		res += "; Types: ";

		for ( int i = 0; i < types.length; i++ ) {
			res += types[i] + " ";
		}

		return res;
	}

	private byte[] toByteArray() {
		int size = 8 + (readings.length * 8) + 8 + (types.length * 4);
		ByteBuffer buffer = ByteBuffer.allocate(size);

		buffer.putDouble(health);
		buffer.putInt(readings.length);

		for ( int i = 0; i < readings.length; i++ ) {
			buffer.putDouble(readings[i]);
		}

		for ( int i = 0; i < types.length; i++ ) {
			buffer.putInt(types[i].ordinal());
		}

		return buffer.array();
	}

	private void fromByteArray(byte[] byteArray) {
		ByteBuffer buffer = ByteBuffer.allocate(byteArray.length);
		buffer.put(byteArray);
		buffer.rewind();

		health = buffer.getDouble();

		int length = buffer.getInt();
		readings = new double[length];
		types = new ReadingType[length];

		for ( int i = 0; i < length; i++ ) {
			readings[i] = buffer.getDouble();
		}

		for ( int i = 0; i < length; i++ ) {
			types[i] = ReadingType.values()[buffer.getInt()];
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.write(toByteArray());
	}

	private void readObject(ObjectInputStream in) throws IOException {
		byte[] byteArray = new byte[BYTE_BUFFER_SIZE];
		in.read(byteArray);
		fromByteArray(byteArray);
	}
}
