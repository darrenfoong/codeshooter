package codeshooter.utils;

import codeshooter.model.Circle;

public class Geometry {
	public static boolean isColliding(Circle c1, Circle c2) {
		double r1 = c1.getRadius();
		double r2 = c2.getRadius();

		double x1 = c1.getCentreX();
		double y1 = c1.getCentreY();
		double x2 = c2.getCentreX();
		double y2 = c2.getCentreY();

		return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) <= (r1+r2)*(r1+r2);
	}

	public static boolean isColliding(Circle c1, Circle c2, double dx, double dy) {
		double r1 = c1.getRadius();
		double r2 = c2.getRadius();

		double x1 = c1.getCentreX();
		double y1 = c1.getCentreY();
		double x2 = c2.getCentreX() + dx;
		double y2 = c2.getCentreY() + dy;

		return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) <= (r1+r2)*(r1+r2);
	}

	public static boolean containsEntirely(Circle c1, Circle c2) {
		double r1 = c1.getRadius();
		double r2 = c2.getRadius();

		double x1 = c1.getCentreX();
		double y1 = c1.getCentreY();
		double x2 = c2.getCentreX();
		double y2 = c2.getCentreY();

		if ( r2 > r1 ) {
			return false;
		} else {
			return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) <= (r1-r2)*(r1-r2);
		}
	}

	public static boolean containsEntirely(Circle c1, Circle c2, double dx, double dy) {
		double r1 = c1.getRadius();
		double r2 = c2.getRadius();

		double x1 = c1.getCentreX();
		double y1 = c1.getCentreY();
		double x2 = c2.getCentreX() + dx;
		double y2 = c2.getCentreY() + dy;

		if ( r2 > r1 ) {
			return false;
		} else {
			return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) <= (r1-r2)*(r1-r2);
		}
	}
}
