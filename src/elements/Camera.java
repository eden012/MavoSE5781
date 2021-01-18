package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static primitives.Util.isZero;

public class  Camera {
    private static final Random rnd = new Random();
    Point3D _p0; /*location*/
    Vector _vTo; /*3 orientation vectors*/
    Vector _vUp;
    Vector _vRight;

    /*-----------------------------constructors--------------------------------------*/
    /**
     * constructor with parameters for the position values
     * and two vectors of directions, forward and up (Vto ,Vup)
     * the constructor make sure that the Vup Vto are vertical, and create the Vright vector.
     * @params p0-location. Vto and Vup for the direction*/
    public Camera(Point3D p0, Vector vTo, Vector vUp) {

        //if the vectors are not orthogonal, throw exception.
        if (vUp.dotProduct(vTo) != 0)//בדיקה אם מאונכים
            throw new IllegalArgumentException("the vectors must be orthogonal");

        this._p0 = new Point3D(p0);
        this._vTo = vTo.normalized();
        this._vUp = vUp.normalized();

        _vRight = this._vTo.crossProduct(this._vUp).normalize();

    }

    /*---------------------------------------getters and setters--------------------------------*/

    public Point3D getP0() {
        return new Point3D(_p0);
    }

    public Vector getVTo() {
        return new Vector(_vTo);
    }

    public Vector getVUp() {
        return new Vector(_vUp);
    }

    public Vector getVRight() {
        return new Vector(_vRight);
    }

    /*--------------------------------------functions--------------------------------------------*/

    public Ray constructRayThroughPixel(int nX, int nY, double screenWidth, double screenHeight,
                                        int j, int i, double screenDistance) {
        if (isZero(screenDistance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }

        Point3D Pij = getPij(nX, nY, j, i, screenWidth, screenHeight, screenDistance);

        Vector Vij = Pij.subtract(_p0);

        return new Ray(_p0, Vij);

    }

    private Point3D getPij(int nX, int nY, int j, int i, double screenWidth, double screenHeight, double screenDistance) {
        Point3D Pc = _p0.add(_vTo.scale(screenDistance));

        double Ry = screenHeight / nY;
        double Rx = screenWidth / nX;

        double yi = ((i - nY / 2d) * Ry + Ry / 2d);
        double xj = ((j - nX / 2d) * Rx + Rx / 2d);

        Point3D Pij = Pc;

        if (!isZero(xj)) {
            Pij = Pij.add(_vRight.scale(xj));
        }
        if (!isZero(yi)) {
            Pij = Pij.subtract(_vUp.scale(yi)); // Pij.add(_vUp.scale(-yi))
        }
        return Pij;
    }

//     NOT IN USE
//
//    /**
//     * creating beam of rays for supersampling
//     *
//     * @param beamradius factor for the beam radius
//     * @param raysAmount number of random rays
//     * @return list of rays
//     */
//    public List<Ray> constructRandomRaysBeamThroughPixel(int nX, int nY, double screenWidth, double screenHeight,
//                                                         int j, int i, double screenDistance,
//                                                         double beamradius, int raysAmount) {
//        if (isZero(screenDistance)) {
//            throw new IllegalArgumentException("distance cannot be 0");
//        }
//
//        List<Ray> rays = new LinkedList<>();
//
//        double Ry = screenHeight / nY;
//        double Rx = screenWidth / nX;
//
//        Point3D Pij = getPij(nX, nY, j, i, screenWidth, screenHeight, screenDistance);
//
//        //antialiasing density >= 1
//        double density = beamradius / screenDistance;
//
//        double radius = (Rx + Ry) / 2d;
//
////        double[] ds = rnd.doubles(-1,1).toArray();
//        for (int counter = 0; counter < raysAmount; counter++) {
//            Point3D point = new Point3D(Pij);
//            double cosTheta = 2 * rnd.nextDouble() - 1;
//            double sinTheta = Math.sqrt(1d - cosTheta * cosTheta);
//
//            double d = radius;
//            double x = d * cosTheta;
//            double y = d * sinTheta;
//
//            if (isZero(x) && isZero(y)) {
//                counter--;
//                continue;
//            }
//            if (!isZero(x)) {
//                point = point.add(_vRight.scale(x));
//            }
//            if (!isZero(y)) {
//                point = point.add(_vUp.scale(y));
//            }
//
//            rays.add(new Ray(_p0, point.subtract(_p0)));
//        }
//        return rays;
////        return rays.stream()
////                .unordered()
////                .distinct()
////                .collect(Collectors.toCollection(LinkedList::new));
//    }


    public List<Ray> constructGridRaysThroughPixel(int nX, int nY, double screenWidth, double screenHeight,
                                                   int j, int i, double screenDist, int size) {

        double Rx = screenWidth / nX;//the length of pixel in X axis
        double Ry = screenHeight / nY;//the length of pixel in Y axis

        Point3D Pij = getPij(nX, nY, j, i, screenWidth, screenHeight, screenDist);
        Point3D tmp;
        //-----SuperSampling-----
        List<Ray> rays = new LinkedList<>();//the return list, construct Rays Through Pixels


        double n = Math.floor(Math.sqrt(size));
        int delta = (int) (n / 2d);

        double gapX = Rx / n;
        double gapY = Ry / n;

/* ***********************************************************************
             |(-3,-3)|(-3,-2)|(-3,-1)|(-3, 0)|(-3, 1)|(-3, 2)||(-3, 3)
             |(-2,-3)|(-2,-2)|(-2,-1)|(-2, 0)|(-2, 1)|(-2, 2)||(-2, 3)
             |(-1,-3)|(-1,-2)|(-1,-1)|(-1, 0)|(-1, 1)|(-1, 2)||(-1, 3)
             |( 0,-3)|( 0,-2)|( 0,-1)|( 0, 0)|( 0, 1)|( 0, 2)||( 0, 3)
             |( 1,-3)|( 1,-2)|( 1,-1)|( 1, 0)|( 1, 1)|( 1, 2)||( 1, 3)
             |( 2,-3)|( 2,-2)|( 2,-1)|( 2, 0)|( 2, 1)|( 2, 2)||( 2, 3)
             |( 3,-3)|( 3,-2)|( 3,-1)|( 3, 0)|( 3, 1)|( 3, 2)||( 3, 3)
*************************************************************************** */
        for (int row = -delta; row <= delta; row++) {
            for (int col = -delta; col <= delta; col++) {
                tmp = new Point3D(Pij);
                if (!isZero(row)) {
                    tmp = tmp.add(_vRight.scale(row * gapX));
                }
                if (!isZero(col)) {
                    tmp = tmp.add(_vRight.scale(col * gapY));
                }
                rays.add(new Ray(_p0, tmp.subtract(_p0).normalize()));
            }
        }
        return rays;
    }
}

