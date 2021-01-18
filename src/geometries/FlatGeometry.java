package geometries;

import elements.Material;
import primitives.Color;

/**
 * Flat Geometry is a Marker abstract class extending Geometry
 * to differentiate it from RadialGeometry
 * we did not decalre it as an interface
 */
public abstract class FlatGeometry extends Geometry {
    /**
     * Associated plane in which the flat geometry lays
     */
    protected Plane _plane = null;

    /*--------------------------------------------------constructors------------------------------------------------------*/

    public FlatGeometry(Color _emission, Material _material) {
        super(_emission, _material);
    }

    public FlatGeometry(Color _emission) {
        super(_emission);
    }

    public FlatGeometry() {
        super();
    }

    /*--------------------------------------------------getters and setters------------------------------------------------------*/

    protected Plane getPlane() {
        return _plane;
    }


}