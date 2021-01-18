package renderer;

import elements.Camera;
import elements.LightSource;
import elements.Material;
import geometries.FlatGeometry;
import geometries.Geometry;
import org.junit.jupiter.api.Disabled;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 *
 */
public class Render {
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final int SPARE_THREADS = 2;

    private final ImageWriter _imageWriter;
    private final Scene _scene;
    private boolean _antialiasing = false;
    private boolean _softshadows = false;
    private int _rayCounter = 25;
    private int _threads = 1;
    private boolean _print = false;
    private double _beamRadius = 20d;

    //constructor
    public Render(ImageWriter imageWriter, Scene scene) {
        this._imageWriter = imageWriter;
        this._scene = scene;
        this._antialiasing = false;
    }

    public Render setBeamRadius(double beamRadius) {
        _beamRadius = beamRadius;
        return this;
    }

    public boolean getSoftshadows() {
        return _softshadows;
    }

    public Render setSoftshadows(boolean softshadows) {
        _softshadows = softshadows;
        return this;
    }

    //getters and setters
    public boolean getAntialiasing() {
        return _antialiasing;
    }

    public Render setAntialiasing(boolean val) {
        _antialiasing = val;
        return this;
    }

    public int getRayCounter() {
        return _rayCounter;
    }

    public Render setRayCounter(int rayCounter) {
        _rayCounter = rayCounter;
        return this;
    }

    /**
     * Set multithreading <br>
     * - if the parameter is 0 - number of cores less 2 is taken
     *
     * @param threads number of threads
     * @return the Render object itself
     */
    public Render setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
        if (threads != 0)
            _threads = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            _threads = cores <= 2 ? 1 : cores;
        }
        return this;
    }

    /**
     * Set debug printing on
     *
     * @return the Render object itself
     */
    public Render setDebugPrint() {
        _print = true;
        return this;
    }

    /**
     * Print a grid over the image
     *
     * @param interval gap from square to square
     * @param color
     */
    public void printGrid(int interval, java.awt.Color color) {
        int Nx = _imageWriter.getNx();
        int Ny = _imageWriter.getNy();
        for (int i = 0; i < Ny; i++) {
            for (int j = 0; j < Nx; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    _imageWriter.writePixel(j, i, color);
                }
            }
        }
    }

    public void writeToImage() {
        _imageWriter.writeToImage();
    }

    public void renderImage() {
        Camera camera = _scene.getCamera();
        double distance = _scene.getDistance();
        Color backgroundColor = _scene.getBackground();
        Color ambientLightColor = _scene.getAmbientLight().getIntensity();

        int Nx = _imageWriter.getNx();
        int Ny = _imageWriter.getNy();
        double width = _imageWriter.getWidth();
        double height = _imageWriter.getHeight();

        final Pixel thePixel = new Pixel(Ny, Nx);

        Thread[] threads = new Thread[_threads];
        for (int i = _threads - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();
                Color resultingColor;
                while (thePixel.nextPixel(pixel)) {
                    if (_antialiasing) {//         with supersampling
                        resultingColor = getPixelRaysGridColor(camera, Nx, Ny, width, height, pixel, distance);
                    } else {
                        resultingColor = getPixelRayColor(camera, Nx, Ny, width, height, pixel, distance);
                    }
                    _imageWriter.writePixel(pixel.col, pixel.row, resultingColor.getColor());
                }
            });
        }
        // Start threads
        for (Thread thread : threads) thread.start();
        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception e) {
                boolean dummy = true;
            }
        }
        if (_print) {
            printMessage("100%\n");
        }
    }

    private synchronized void printMessage(String msg) {
        synchronized (System.out) {
            System.out.println(msg);
        }
    }

    private Color getPixelRaysGridColor(Camera camera, int nx, int ny, double width, double height, Pixel pixel, double distance) {
        Color resultColor = Color.BLACK;
        Color backgroundColor = _scene.getBackground();
        Color ambientLightColor = _scene.getAmbientLight().getIntensity();
        Color rayColor;
        GeoPoint closestPoint;
//
        List<Ray> rays = camera.constructGridRaysThroughPixel(nx, ny, width, height, pixel.col, pixel.row, distance, _rayCounter);
//        List<Ray> rays = camera.constructRandomRaysBeamThroughPixel(nx, ny, width, height, pixel.col, pixel.row, distance, _beamRadius, _rayCounter);
//        Ray mainray = camera.constructRayThroughPixel(nx, ny, width, height, pixel.col, pixel.row, distance);
//        rays.add(mainray);
        for (Ray ray : rays) {
            closestPoint = findClosestIntersection(ray);
            if (closestPoint != null) {
                resultColor = resultColor.add(calcColor(closestPoint, ray, MAX_CALC_COLOR_LEVEL, 1d));
            } else {
                resultColor = resultColor.add(backgroundColor);
            }
        }
        resultColor = resultColor.reduce(rays.size());

        if (!resultColor.equals(backgroundColor)) {
            resultColor.add(ambientLightColor);
        }
        return resultColor;
    }


    private Color getPixelRayColor(Camera camera, int nx, int ny, double width, double height, Pixel pixel, double distance) {
        Ray ray = camera.constructRayThroughPixel(nx, ny, width, height, pixel.col, pixel.row, distance);
        GeoPoint closestPoint = findClosestIntersection(ray);

        if (closestPoint != null) {
            Color resultingColor = calcColor(closestPoint, ray, MAX_CALC_COLOR_LEVEL, 1d);
            resultingColor = resultingColor.add(_scene.getAmbientLight().getIntensity());

            return resultingColor;
        }
        return _scene.getBackground();
    }


    private GeoPoint closestPoint(List<GeoPoint> intersectionPoints) {

        if (intersectionPoints == null) {
            return null;
        }

        GeoPoint result = null;

        Point3D p0 = _scene.getCamera().getP0();
        double minDist = Double.MAX_VALUE;
        double currentDistance;

        for (GeoPoint geoPoint : intersectionPoints) {
            currentDistance = p0.distance(geoPoint.getPoint());
            if (currentDistance < minDist) {
                minDist = currentDistance;
                result = geoPoint;
            }
        }
        return result;
    }

    /**
     * Find intersections of a ray with the scene geometries and get the
     * intersection point that is closest to the ray head. If there are no
     * intersections, null will be returned.
     *
     * @param ray intersecting the scene
     * @return the closest point
     */
    private GeoPoint findClosestIntersection(Ray ray) {

        if (ray == null) {
            return null;
        }

        GeoPoint theClosestPoint = null;
        double closestDistance = Double.MAX_VALUE;
        Point3D ray_p0 = ray.getOriginPoint();

        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(ray);
        if (intersections == null)
            return null;

        for (GeoPoint geoPoint : intersections) {
            double distance = ray_p0.distance(geoPoint.getPoint());
            if (distance < closestDistance) {
                closestDistance = distance;
                theClosestPoint = geoPoint;
            }
        }
        return theClosestPoint;
    }

//    /**
//     * @param inRays List of surrounding rays
//     * @return average color
//     * @author Reuven Smadja
//     */
//    private Color calcColor(List<Ray> inRays, int level, double k) {
//        Color bkg = _scene.getBackground();
//        Color color = Color.BLACK;
//        for (Ray ray : inRays) {
//            GeoPoint gp = findClosestIntersection(ray);
//            color = bkg;
//            if (gp!= null){
//                color = color.add(calcColor(gp, ray, level, k));
//            }
//        }
//        int size = inRays.size();
//        return (size == 1) ? color : color.reduce(size);
//    }

    private Color calcColor(GeoPoint geoPoint, Ray inRay, int level, double k) {
        if (level == 1) {
            return Color.BLACK;
        }

        Point3D pointGeo = geoPoint.getPoint();
        Geometry geometryGeo = geoPoint.getGeometry();
        Color color = geometryGeo.getEmissionLight();

        Material material = geometryGeo.getMaterial();
        int nShininess = material.getnShininess();
        double kd = material.getkD();
        double ks = material.getkS();

        Vector v = pointGeo.subtract(_scene.getCamera().getP0()).normalize();
        Vector n = geometryGeo.getNormal(pointGeo);
        double nv = n.dotProduct(v);

        color = color.add(lightSourcesColor(geoPoint, k, v, n, nv, nShininess, kd, ks));

        double kr = geometryGeo.getMaterial().getKr();
        double kkr = k * kr;

        if (kkr > MIN_CALC_COLOR_K) {
            Ray reflectedRay = constructReflectedRay(pointGeo, inRay, n);
            color = color.add(secondaryRayColor(level - 1, kr, kkr, reflectedRay));
        }

        double kt = geometryGeo.getMaterial().getKt();
        double kkt = k * kt;

        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = constructRefractedRay(pointGeo, inRay, n);
            color = color.add(secondaryRayColor(level - 1, kt, kkt, refractedRay));
        }
        return color;
    }

    private Color secondaryRayColor(int level, double scalefactor, double kkrt, Ray secondaryRay) {
        GeoPoint secondaryGeoPoint = findClosestIntersection(secondaryRay);
        Color color = Color.BLACK;
        if (secondaryGeoPoint != null) {
            color = calcColor(secondaryGeoPoint, secondaryRay, level, kkrt).scale(scalefactor);
        }
        return color;
    }

    private Color lightSourcesColor(GeoPoint geoPoint, double k, Vector v, Vector n, double nv, int nShininess, double kd, double ks) {
        Point3D pointGeo = geoPoint.getPoint();
        Color color = Color.BLACK;
        if (_scene.getLightSources() != null) {
            double ktr = 0;
            for (LightSource lightSource : _scene.getLightSources()) {
                double lightDistance = lightSource.getDistance(pointGeo);
                Vector l = lightSource.getL(pointGeo);
                double nl = n.dotProduct(l);
                if (nl * nv > 0) {
                    if (_softshadows) { //we want8  softshadows
                        ktr = transparencyBeam(lightDistance, lightSource, n, geoPoint);
                    } else {
                        ktr = transparency(lightDistance, l, n, geoPoint);
                    }
                    if (ktr * k > MIN_CALC_COLOR_K) {
                        Color lightIntensity = lightSource.getIntensity(pointGeo).scale(ktr);
                        color = color.add(
                                calcDiffusive(kd, nl, lightIntensity),
                                calcSpecular(ks, l, n, nl, v, nShininess, lightIntensity));

                    }
                }
            }
        }
        return color;
    }

    private double transparencyBeam(double lightDistance, LightSource lightSource, Vector n, GeoPoint geoPoint) {
        double ktr;
        List<Vector> beamL = lightSource.getBeamL(geoPoint.getPoint(), _beamRadius, _rayCounter);
        double tempKtr = 0;
        for (Vector vl : beamL) {
            tempKtr += transparency(lightDistance, vl, n, geoPoint);
        }
        ktr = tempKtr / beamL.size();

        return ktr;
    }

    private Ray constructRefractedRay(Point3D pointGeo, Ray inRay, Vector n) {
        return new Ray(pointGeo, inRay.getDirection(), n);
    }

    private Ray constructReflectedRay(Point3D pointGeo, Ray inRay, Vector n) {
        //r = v - 2.(v.n).n
        Vector v = inRay.getDirection();
        double vn = v.dotProduct(n);

        if (vn == 0) {
            return null;
        }

        Vector r = v.subtract(n.scale(2 * vn));
        return new Ray(pointGeo, r, n);
    }

    /**
     * Calculate Specular component of light reflection.
     *
     * @param ks         specular component coef
     * @param l          direction from light to point
     * @param n          normal to surface at the point
     * @param nl         dot-product n*l
     * @param V          direction from point of view to point
     * @param nShininess shininess level
     * @param ip         light intensity at the point
     * @return specular component light effect at the point
     * @author Dan Zilberstein ( modified by E.G.)
     * <p>
     * Specular light is light from a point light source which will be
     * reflected specularly.
     * <p>
     * Specularly reflected light is light which is reflected in a
     * mirror-like fashion, as from a shiny surface. As shown in Figure IV.3,
     * specularly reflected light leaves a surface with its angle of reflection
     * approximately equal to its angle of incidence. This is the main part of
     * the reflected light from a polished or glossy surface. Specular reflections
     * are the cause of “specular highlights,” i.e., bright spots on curved surfaces
     * where intense specular reflection occurs.
     * <p>
     * Finally, the Phong model has a provision for a highlight, or specular, component, which reflects light in a
     * shiny way. This is defined by [rs,gs,bs](-V.R)^p, where R is the mirror reflection direction vector we discussed
     * in class (and also used for ray tracing), and where p is a specular power. The higher the value of p, the shinier
     * the surface.
     */
    private Color calcSpecular(double ks, Vector l, Vector n, double nl, Vector V, int nShininess, Color ip) {
        if (isZero(nl)) {
            throw new IllegalArgumentException("nl cannot be Zero for scaling the normal vector");
        }
        Vector R = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double VR = alignZero(V.dotProduct(R));
        if (VR >= 0) {
            return Color.BLACK; // view from direction opposite to r vector
        }
        // [rs,gs,bs]ks(-V.R)^p
        return ip.scale(ks * Math.pow(-1d * VR, nShininess));
    }

    /**
     * Calculate Diffusive component of light reflection.
     * <p>
     * Diffuse light is light from a point light source which will be
     * reflected diffusely
     *
     * @param kd diffusive component coef
     * @param nl dot-product n*l
     * @param ip light intensity at the point
     * @return diffusive component of light reflection
     * @author Dan Zilberstein (modified by E.G.)
     * <p>
     * Diffusely reflected light is light which is reflected evenly
     * in all directions away from the surface. This is the predominant mode of
     * reflection for non-shiny surfaces
     * <p>
     * The diffuse component is that dot product n•L. It approximates light, originally from light source L,
     * reflecting from a surface which is diffuse, or non-glossy. One example of a non-glossysurface is paper.
     * In general, you'll also want this to have a non-gray color value,
     * so this term would in general be a color defined as: [rd,gd,bd](n•L)
     */
    private Color calcDiffusive(double kd, double nl, Color ip) {
        return ip.scale(Math.abs(nl) * kd);
    }

    private boolean sign(double val) {
        return (val > 0d);
    }

    private double transparency(double lightDistance, Vector l, Vector n, GeoPoint geopoint) {
        // from point to light source
        Ray lightRay = new Ray(geopoint.getPoint(), l.scale(-1), n);

        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay, lightDistance);
        if (intersections == null) {
            return 1d;
        }

        double shadowK = 1d;
        for (GeoPoint gp : intersections) {
            shadowK *= gp.getGeometry().getMaterial().getKt();
            if (shadowK < MIN_CALC_COLOR_K) {
                return 0.0;
            }
        }
        return shadowK;
    }

    @Deprecated
    @Disabled("for demonstration purposes")
    private boolean unshaded(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.getPoint(), lightDirection, n);
        Point3D pointGeo = geopoint.getPoint();

        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);
        if (intersections == null) {
            return true;
        }
        double lightDistance = light.getDistance(pointGeo);
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.getPoint().distance(pointGeo) - lightDistance) <= 0
                    && gp.getGeometry().getMaterial().getKt() == 0) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    @Disabled("for demonstration purposes")
    private boolean unshaded_0(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.getPoint(), lightDirection, n);
        Point3D pointGeo = geopoint.getPoint();

        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);
        if (intersections == null) {
            return true;
        }
        // Flat geometry cannot self intersect
        if (geopoint.getGeometry() instanceof FlatGeometry) {
            intersections.remove(geopoint);
        }

        double lightDistance = light.getDistance(pointGeo);
        for (GeoPoint gp : intersections) {
            double temp = gp.getPoint().distance(pointGeo) - lightDistance;
            if (alignZero(temp) <= 0)
                return false;
        }
        return true;
    }

    @Deprecated
    @Disabled("for demonstration purposes")
    private boolean occluded(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        Point3D geometryPoint = geopoint.getPoint();
        Vector lightDirection = light.getL(geometryPoint);
        lightDirection.scale(-1);

        Vector epsVector = geopoint.getGeometry().getNormal(geometryPoint);
        epsVector.scale(epsVector.dotProduct(lightDirection) > 0 ? 2 : -2);
        geometryPoint.add(epsVector);
        Ray lightRay = new Ray(geometryPoint, lightDirection);
        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);

        // Flat geometry cannot self intersect
        if (geopoint.getGeometry() instanceof FlatGeometry) {
            intersections.remove(geopoint);
        }

        for (GeoPoint entry : intersections)
            if (entry.getGeometry().getMaterial().getKt() == 0)
                return true;
        return false;
    }

    /**
     * Pixel is an internal helper class whose objects are associated with a Render object that
     * they are generated in scope of. It is used for multithreading in the Renderer and for follow up
     * its progress.<br/>
     * There is a main follow up object and several secondary objects - one in each thread.
     *
     * @author Dan
     */
    private class Pixel {
        public volatile int row = 0;
        public volatile int col = -1;
        private long _maxRows = 0;
        private long _maxCols = 0;
        private long _pixels = 0;
        private long _counter = 0;
        private int _percents = 0;
        private long _nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         *
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            _maxRows = maxRows;
            _maxCols = maxCols;
            _pixels = maxRows * maxCols;
            _nextCounter = _pixels / 100;
            if (Render.this._print) {
                printMessage(String.format(" %02d%%", _percents));
            }
        }

        /**
         * Default constructor for secondary Pixel objects
         */
        public Pixel() {
        }

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object - this function is
         * critical section for all the threads, and main Pixel object data is the shared data of this critical
         * section.<br/>
         * The function provides next pixel number each call.
         *
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print, if it is -1 - the task is
         * finished, any other value - the progress percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            ++col;
            ++_counter;
            if (col < _maxCols) {
                target.row = this.row;
                target.col = this.col;
                if (_print && _counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            ++row;
            if (row < _maxRows) {
                col = 0;
                target.row = this.row;
                target.col = this.col;
                if (_print && _counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            return -1;
        }

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         *
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percents = nextP(target);
            if (_print && percents > 0) {
                printMessage(String.format(" %02d%%", _percents));
            }
            if (percents >= 0)
                return true;
            if (_print) {
                printMessage(String.format(" %02d%%", 100));
            }
            return false;
        }
    }
}
