package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static primitives.Util.isZero;

/**
 * The PointLight object specifies an attenuated light source at a fixed point in space that radiates light equally
 * in all directions away from the light source. PointLight has the same attributes as a Light node,
 * with the addition of location and attenuation parameters.
 * <p>
 * A point light contributes to diffuse and specular reflections,
 * which in turn depend on the orientation and position of a surface.
 * A point light does not contribute to ambient reflections.
 * <p>
 * A PointLight is attenuated by multiplying the contribution of the light by an attenuation factor.
 * The attenuation factor causes the the PointLight's brightness to decrease
 * as distance from the light source increases.
 * A PointLight's attenuation factor contains three values:
 * <p>
 * Constant attenuation
 * Linear attenuation
 * Quadratic attenuation
 * <p>
 * A PointLight is attenuated by the reciprocal of the sum of:
 * <p>
 * The constant attenuation factor
 * The Linear attenuation factor times the distance between the light and the vertex being illuminated
 * The quadratic attenuation factor times the square of the distance between the light and the vertex
 * <p>
 * By default, the constant attenuation value is 1 and the other two values are 0,
 * resulting in no attenuation.
 */
public class PointLight extends Light implements LightSource {
    private static final Random RND = new Random();
    Point3D _position;
    double _kC; // Constant attenuation
    double _kL; // Linear attenuation
    double _kQ; // Quadratic attenuation

    public PointLight(Color colorIntensity, Point3D position, double kC, double kL, double kQ) {
        this._intensity = colorIntensity;
        this._position = position;
        this._kC = kC;
        this._kL = kL;
        this._kQ = kQ;
    }

    // by default, the constant attenuation value is 1 and the other two values are 0
    public PointLight(Color colorIntensity, Point3D position) {
        this(colorIntensity, position, 1d, 0d, 0d);
    }


    //overriding LightSource getIntensity(Point3D)
    @Override
    public Color getIntensity(Point3D p) {
        double dsquared = p.distanceSquared(_position);
        double d = p.distance(_position);

        return (_intensity.reduce(_kC + _kL * d + _kQ * dsquared));
    }

    // Light vector
    @Override
    public Vector getL(Point3D p) {
        if (p.equals(_position)) {
            return null;
        }
        return p.subtract(_position).normalize();
    }

    //Thanks to Maor Sarussi
    @Override
    public List<Vector> getBeamL(Point3D p, double radius, int amount) {
        if (p.equals(_position)) {
            return null;
        }
        LinkedList<Vector> beam = new LinkedList<>();

        //from pointlight position to p point
        Vector v = this.getL(p);
        beam.add(v);
        if (amount <= 1) {
            return beam;
        }

        double distance = this._position.distance(p);

        if (isZero(distance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }
        //Thanks to Raphael Knoll
        Point3D lightHead = v.getHead();
        Vector normX;

        // if v._head == (0,0,w) then normX.head ==(-w,0,0)
        // otherwise normX.head == (-y,x,0)
        if (isZero(lightHead.getX().get()) && isZero(lightHead.getY().get())) {
            normX = new Vector(lightHead.getZ().get() * -1, 0, 0).normalize();
        } else {
            normX = new Vector(lightHead.getY().get() * -1, lightHead.getX().get(), 0).normalize();
        }

        Vector normY = v.crossProduct(normX).normalize();
        double cosTheta;
        double sinTheta;

        double d;
        double x;
        double y;

        for (int counter = 0; counter < amount; counter++) {
            Point3D newPoint = new Point3D(this._position);
            // randomly coose cosTheta and sinTheta in the range (-1,1)
            cosTheta = 2 * RND.nextDouble() - 1;
            sinTheta = Math.sqrt(1d - cosTheta * cosTheta);

            //d ranged between -radius and  +radius
            d = radius * (2 * RND.nextDouble() - 1);
            //d ranged between -radius and  +radius
            if (isZero(d)) { //Thanks to Michael Shachor
                counter--;
                continue;
            }
            x = d * cosTheta;
            y = d * sinTheta;

            if (!isZero(x)) {
                newPoint = newPoint.add(normX.scale(x));
            }
            if (!isZero(y)) {
                newPoint = newPoint.add(normY.scale(y));
            }
            beam.add(p.subtract(newPoint).normalize());
        }
        return beam;
    }

    @Override
    public double getDistance(Point3D point) {
        return _position.distance(point);
    }

    @Override
    public Point3D getPoint() {
        return new Point3D(_position);
    }
}
