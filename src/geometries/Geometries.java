package geometries;

import primitives.Ray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    private  List<Geometry> _geometries =new ArrayList<>() ;


    /*--------------------------------------------------constructors------------------------------------------------------*/
   /**
    * empty constructor
    * */
    public Geometries() {

    }
    public Geometries(Intersectable ... _geometries)
    {
        add(_geometries);
    }

    public Geometries(Geometry... _geometries) {
        add(_geometries);
    }
    public Geometries(Geometries... _geometries) {
        add(_geometries);
    }

    private void add(Geometries[] geometries) {
    }

    public Geometries(Geometries geos2, Geometries geos3, Geometries geos4, Sphere e, Plane p) {
    }

    /*--------------------------------------------------functions------------------------------------------------------*/

    public void add(Geometry... geometries) {
        _geometries.addAll(List.of(geometries));
    }

    public void add (Intersectable ... geometries)
    {
        for (Intersectable g : geometries)
        {
            _geometries.add(g);
        }
    }

    public void removeAll() {
        for (Geometry geo : _geometries) {
            _geometries.remove(geo);
        }
    }

    /**
     * @param ray the ray that intersect the geometries
     * @return list of Point3D that intersect the osef
     */
    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = null;

        for (Intersectable geo : _geometries) {
            List<GeoPoint> tempIntersections = geo.findIntersections(ray, maxDistance);
            if (tempIntersections != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(tempIntersections);
            }
        }
        return intersections;
    }

    public int getSize() {
        return _geometries.size();
    }
}
