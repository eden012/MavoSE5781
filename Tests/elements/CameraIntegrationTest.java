package elements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import primitives.*;
import geometries.*;

import java.util.List;

class CameraIntegrationTest {
    Camera cam1 = new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0));
    Camera cam2 = new Camera(new Point3D(0, 0, -0.5), new Vector(0, 0, 1), new Vector(0, -1, 0));

    @Test
    void constructRayThroughPixelWithSphere1() {
        //TO DO
        Sphere sph =  new Sphere(1, new Point3D(0, 0, 3));

        int count = 0;
        int Nx =3;
        int Ny =3;
        for (int i = 0; i < Nx; ++i) {
            for (int j = 0; j < Ny; ++j) {
                Ray ray = cam1.constructRayThroughPixel(3, 3, 3, 3, j, i, 1);
                List<Intersectable.GeoPoint> results = sph.findIntersections(ray);
                if (results != null)
                    count += results.size();
            }
        }

        assertEquals(2,count,"too bad");
        System.out.println("count: "+count);

    }
    @Test
    void constructRayThroughPixelWithSphere2() {
        Sphere sph =  new Sphere(2.5, new Point3D(0, 0, 2.5));

        List<Intersectable.GeoPoint> results;
        int count = 0;
        // TODO explanations here
        int Nx =3;
        int Ny =3;

        // TODO explanations here
        for (int i = 0; i < Nx; ++i) {
            for (int j = 0; j < Ny; ++j) {
                results = sph.findIntersections(cam2.constructRayThroughPixel(Nx, Ny, 3, 3, j, i, 1));
                if (results != null)
                    count += results.size();
            }
        }

        assertEquals(18,count,"too bad");
        System.out.println("count: "+count);
    }
}