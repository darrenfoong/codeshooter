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

	public static double getRangeInsideCircle(double x0, double y0, Heading heading, Circle c) {
		return getRange(x0, y0, heading, c, true);
	}

	public static double getRangeOutsideCircle(double x0, double y0, Heading heading, Circle c) {
		return getRange(x0, y0, heading, c, false);
	}

	public static double getRange(double x0, double y0, Heading heading, Circle c, boolean inside) {
		double deltaX = x0 - c.getCentreX();
		double deltaY = y0 - c.getCentreY();
		double distanceSquared = deltaX * deltaX + deltaY * deltaY;
		double radiusSquared = c.getRadius() * c.getRadius();
		double differenceSquared = distanceSquared - radiusSquared;

		boolean reject;

		if ( inside ) {
			reject = differenceSquared > 0;
		} else {
			reject = differenceSquared <= 0;
		}

		if ( reject ) {
			return -1;
		} else {
			// parameterise line as
			// (x0, y0) + t (cos theta, sin theta), t >= 0
			// then solve for the intersection of
			// (x, y) = (x0 + t cos theta, y0 + t sin theta)
			// and
			// (x - x_c)^2 + (y - y_c)^2 = r^2
			// which reduces to solving the quadratic equation
			// t^2 + 2t(deltaX cos theta + deltaY sin theta) + (L^2 - r^2)
			// where L^2 = (x0 - x_c)^2 + (y0 - y_c)^2
			//       deltaX = x0 - x_c
			//       deltaY = y0 - y_c
			double B = 2 * (deltaX * heading.getX() + deltaY * heading.getY());
			double C = differenceSquared;
			double D = B*B - 4 * C;

			if ( D < 0 ) {
				return -1;
			} else if ( D == 0 ) {
				return -B/2;
			} else {
				double t1 = (-B + Math.sqrt(D))/2;
				double t2 = (-B - Math.sqrt(D))/2;

				return inside ? Math.max(t1, t2) : Math.min(t1, t2);
			}
		}
	}
}
