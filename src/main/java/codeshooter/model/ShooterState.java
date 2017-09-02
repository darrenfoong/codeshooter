package codeshooter.model;

import codeshooter.model.Sensor.ReadingType;
import java.io.*;
import java.nio.ByteBuffer;

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
    return readings.clone();
  }

  public ReadingType[] getTypes() {
    return types.clone();
  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();

    res.append("Health: " + health + "; Readings: ");

    for (int i = 0; i < readings.length; i++) {
      res.append(readings[i] + " ");
    }

    res.append("; Types: ");

    for (int i = 0; i < types.length; i++) {
      res.append(types[i] + " ");
    }

    return res.toString();
  }

  private byte[] toByteArray() {
    int size = 8 + (readings.length * 8) + 8 + (types.length * 4);
    ByteBuffer buffer = ByteBuffer.allocate(size);

    buffer.putDouble(health);
    buffer.putInt(readings.length);

    for (int i = 0; i < readings.length; i++) {
      buffer.putDouble(readings[i]);
    }

    for (int i = 0; i < types.length; i++) {
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

    for (int i = 0; i < length; i++) {
      readings[i] = buffer.getDouble();
    }

    for (int i = 0; i < length; i++) {
      types[i] = ReadingType.values()[buffer.getInt()];
    }
  }

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.write(toByteArray());
  }

  private void readObject(ObjectInputStream in) throws IOException {
    byte[] byteArray = new byte[BYTE_BUFFER_SIZE];
    if (in.read(byteArray) != -1) {
      fromByteArray(byteArray);
    } else {
      throw new EOFException();
    }
  }
}
