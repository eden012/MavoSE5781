package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

public class DirectionalLight extends Light implements LightSource {
    private final Vector _direction;

    /**
     * Initialize directional light with it's intensity and direction, direction
     * vector will be normalized.
     *
     * @param colorintensity intensity of the light
     * @param direction      direction vector
     */

    public DirectionalLight(Color colorintensity, Vector direction) {
        _intensity = colorintensity;
        _direction = direction.normalized();
    }

    /**
     * @param p the lighted point is not used and is mentioned
     *          only for compatibility with LightSource
     * @return fixed intensity of the directionLight
     */
    @Override
    public Color getIntensity(Point3D p) {
//        return super.getIntensity();
        return new Color(_intensity);
    }

    //instead of getDirection()
    @Override
    public Vector getL(Point3D p) {
        return new Vector(_direction);
    }

    @Override
    public List<Vector> getBeamL(Point3D dummyPoint3D, double dummyRadius, int dummyInt) {
        return List.of(new Vector(_direction));
    }

    @Override
    public double getDistance(Point3D point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Point3D getPoint() {
        return new Point3D(0, 0, Double.POSITIVE_INFINITY);
    }
}
