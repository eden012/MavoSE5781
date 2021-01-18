package renderer;

import elements.*;
import geometries.Cylinder;
import geometries.Plane;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

public class NewTest {
    Scene scene;

    public NewTest() {
        scene = new Scene.SceneBuilder("Test scene")
                .addAmbientLight(new AmbientLight(Color.BLACK, 0.15))
                .addCamera(
                        new Camera(
                                new Point3D(-500, -250, 125),
                                new Vector(2, 1, 0),
                                new Vector(1, -2, 0)))
                .addDistance(1000)
                .addBackground(Color.BLACK)
                .build();


        scene.addGeometries(
                new Plane(
                        new Color(10, 10, 10),
                        new Material(0.2, 0.2, 30, 0, 0.2),
                        new Point3D(1, 0, 1),
                        new Vector(0, -1, 0)));

        //the objects on the plane
        scene.addGeometries(
                //four spheres
                new Sphere(
                        new Color(0, 102, 204),
                        new Material(0.3, 0.3, 30, 0, 0), // )
                        30,
                        new Point3D(60, -30, 100)),
                new Sphere(
                        new Color(0, 40, 110),
                        new Material(0.3, 0.3, 30, 0, 0), // )
                        23,
                        new Point3D(-30, -23, 100)),
                new Sphere(
                        new Color(159, 62, 178),
                        new Material(0.3, 0.3, 30, 0, 0), // )
                        17,
                        new Point3D(-110, -17, 100)),
                new Sphere(
                        new Color(146, 107, 67),
                        new Material(0.3, 0.3, 30, 0, 0), // )
                        10,
                        new Point3D(-170, -10, 100)),

                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, 0, 200), new Vector(0, -1, 0)),
                        50),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, 0, 250), new Vector(0, -1, 0)),
                        50),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(150, 0, 200), new Vector(0, -1, 0)),
                        50),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(150, 0, 250), new Vector(0, -1, 0)),
                        50),

                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, 0, 200), new Vector(1, -1, 1)),
                        86.6),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, 0, 250), new Vector(1, -1, -1)),
                        86.6),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(150, 0, 200), new Vector(-1, -1, 1)),
                        86.6),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(150, 0, 250), new Vector(-1, -1, -1)),
                        86.6),

                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, -50, 200), new Vector(0, 0, 1)),
                        50),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(150, -50, 200), new Vector(0, 0, 1)),
                        50),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, -50, 200), new Vector(1, 0, 0)),
                        50),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, -50, 250), new Vector(1, 0, 0)),
                        50),

                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, -50, 200), new Vector(1, -1, 1)),
                        43.3),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(100, -50, 250), new Vector(1, -1, -1)),
                        43.3),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(150, -50, 200), new Vector(-1, -1, 1)),
                        43.3),
                new Cylinder(
                        new Color(210, 81, 242),
                        new Material(0.3, 0.3, 30, 0, 0),
                        1,
                        new Ray(new Point3D(150, -50, 250), new Vector(-1, -1, -1)),
                        43.3));
        //3 Light Sources
        // behind the spheres(spotlight) , behind the cylinders (spotlight) and above the scene (pointLight)
        scene.addLights(
                new SpotLight(
                        new Color(255, 255, 255), //
                        new Point3D(-100, -100, -50),
                        new Vector(0, 2, 3),
                        1, 0.00001, 0.000001, 10),
                new SpotLight(
                        new Color(255, 255, 255), //
                        new Point3D(200, -100, 230),
                        new Vector(-2, 1, 0),
                        1, 0.0001, 0.00001, 5),
                new PointLight(
                        new Color(255, 209, 163),
                        new Point3D(0, -700, 150),
                        1d, 0.0001, 0.00001)
        );
    }

    @Test
    public void SoftShadowsOnTwoAngles() {
        ImageWriter imageWriter = new ImageWriter("SoftShadowsOnCam1", 648, 432, 1080, 720);
        Render render = new Render(imageWriter, scene)
                .setMultithreading(4)
                .setRayCounter(80)
                .setBeamRadius(20)
                .setAntialiasing(true)
                .setDebugPrint()
                .setSoftshadows(true);

        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void SoftShadowsOffTwoAngles() {

        ImageWriter imageWriter = new ImageWriter("SoftShadowsOffCam1", 648, 432, 1080, 720);
        Render render = new Render(imageWriter, scene)
                .setMultithreading(4)
                .setRayCounter(200)
                .setBeamRadius(10)
                .setDebugPrint()
                .setSoftshadows(false);

        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void SoftShadowsOnTwoAnglesCam2() {
        Camera cam2 = new Camera(new Point3D(-40, -1800, 150), new Vector(0, 1, 0), new Vector(0, 0, 1));
        scene.setCamera(cam2);
        ImageWriter imageWriter = new ImageWriter("SoftShadowsOnCam2", 432, 288, 1080, 720);
        Render render = new Render(imageWriter, scene)
                .setMultithreading(4)
                .setRayCounter(200)
                .setBeamRadius(20)
                .setSoftshadows(true);

        render.renderImage();
        render.writeToImage();

    }

    @Test
    public void SoftShadowsOffTwoAnglesCam2() {
        ImageWriter imageWriter = new ImageWriter("SoftShadowsOffCam2", 432, 288, 1080, 720);
        Render render = new Render(imageWriter, scene)
                .setMultithreading(4)
                .setRayCounter(200)
                .setSoftshadows(false);

        render.renderImage();
        render.writeToImage();
    }
}

