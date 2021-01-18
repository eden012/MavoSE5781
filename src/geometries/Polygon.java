package geometries;

import elements.Material;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan Zilberstein
 */
public class Polygon extends FlatGeometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point3D> _vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;

    /*--------------------------------------------------constructors------------------------------------------------------*/

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Color emissionLight, Material material, Point3D... vertices) {
        super(emissionLight, material);
        setPlane(vertices);
        setVertices(vertices);
    }

    public Polygon(Color emissionLight, Point3D... vertices) {
        super(emissionLight);
        setPlane(vertices);
        setVertices(vertices);
    }

    public Polygon(Point3D... vertices) {
        super();
        setPlane(vertices);
        setVertices(vertices);
    }

    /*--------------------------------------------------getters and setters------------------------------------------------------*/

    private void setPlane(Point3D[] vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        _plane = new Plane(vertices[0], vertices[1], vertices[2]);
    }

    private void setVertices(Point3D[] vertices) {
        _vertices = List.of(vertices);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        if (vertices.length == 3) return; // no need for more tests for a Triangle

        Vector n = _plane.getNormal(null);

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }


    @Override
    public Vector getNormal(Point3D point) {
        return _plane.getNormal(null);
    }

    /*--------------------------------------------------functions------------------------------------------------------*/

    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        List<GeoPoint> planeIntersections = _plane.findIntersections(ray, maxDistance);
        if (planeIntersections == null)
            return null;

        Point3D p0 = ray.getOriginPoint();
        Vector v = ray.getDirection();

        Vector v1 = _vertices.get(1).subtract(p0);
        Vector v2 = _vertices.get(0).subtract(p0);
        double sign = v.dotProduct(v1.crossProduct(v2));
        if (isZero(sign))
            return null;

        boolean positive = sign > 0;

        for (int i = _vertices.size() - 1; i > 0; --i) {
            v1 = v2;
            v2 = _vertices.get(i).subtract(p0);
            sign = alignZero(v.dotProduct(v1.crossProduct(v2)));
            if (isZero(sign)) return null;
            if (positive != (sign > 0)) return null;
        }

        planeIntersections.get(0)._geometry = this;
        return planeIntersections;
    }
}
