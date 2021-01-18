package geometries;

import elements.Material;
import primitives.Color;

import static primitives.Util.isZero;

/**
 * @author Eliezer
 * RadialGeometry is ana abstract class that defines
 * all radial geometries.
 */

public abstract class RadialGeometry extends Geometry {
    protected double _radius;

    /*--------------------------------------------------constructors------------------------------------------------------*/

    /**
     * constructor for a new extended  RadialGeometry object.
     *
     * @param emissionLight the emision light color
     * @param radius        the radius of the RadialGeometry
     * @param material      the material of the RadialGeometry
     * @throws IllegalArgumentException in any case of radius less or equal to 0
     */
    public RadialGeometry(Color emissionLight, Material material, double radius) {
        super(emissionLight, material);
        setRadius(radius);
    }

    public RadialGeometry(Color emissionLight, double radius) {
        super(emissionLight);
        setRadius(radius);
    }

    public RadialGeometry(double radius) {
        super();
        setRadius(radius);
    }

    public RadialGeometry(RadialGeometry other) {
        super(other._emissionColor, other._material);
        setRadius(other._radius);
    }

    /*--------------------------------------------------getters and setters------------------------------------------------------*/
    protected double getRadius() {
        return _radius;
    }

    protected void setRadius(double radius) {
        if (isZero(radius) || (radius < 0.0))
            throw new IllegalArgumentException("radius " + radius + " is not valid");
        this._radius = radius;
    }

}
