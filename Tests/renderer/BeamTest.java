package renderer;

import elements.AmbientLight;
import elements.Camera;
import elements.Material;
import elements.SpotLight;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point3D;
import primitives.Vector;
import scene.Scene;

public class BeamTest {
    @Test
    public void trianglesSphereBeam() {
        Scene scene = new Scene.SceneBuilder("Test scene")
                .addAmbientLight(new AmbientLight(Color.WHITE, 0.2))
                .addCamera(
                        new Camera(
                                new Point3D(0, 0, -1000),
                                new Vector(0, 0, 1),
                                new Vector(0, -1, 0)))
                .addDistance(1000)
                .addBackground(Color.BLACK)
                .build();

        scene.addGeometries( //
                new Triangle(
                        new Color(100, 10, 80),
                        new Material(0, 0.8, 80), //
                        new Point3D(-150, 150, 115),
                        new Point3D(150, 150, 135),
                        new Point3D(75, -75, 150)), //
                new Triangle(
                        new Color(50, 50, 80),
                        new Material(0, 0.8, 80), //
                        new Point3D(-150, 150, 115),
                        new Point3D(-70, -70, 140),
                        new Point3D(75, -75, 150)), //
                new Sphere(
                        new Color(java.awt.Color.BLUE),
                        new Material(0.5, 0.5, 60), // )
                        30,
                        new Point3D(0, 0, 115)));

        scene.addLight(
                new SpotLight(
                        new Color(255, 100, 255).scale(0.85), //
                        new Point3D(40, -40, -150),
                        new Vector(-1, 1, 4),
                        1, 4E-4, 2E-5));

        ImageWriter imageWriter = new ImageWriter("trianglesSphereBeam", 200, 200, 600, 600);
        Render render = new Render(imageWriter, scene)
                .setAntialiasing(true)
                .setRayCounter(80)
                .setMultithreading(4)
                .setDebugPrint();

        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void trianglesSphereBeamSoft() {
        Scene scene;
        scene = new Scene.SceneBuilder("Test scene")
                .addAmbientLight(new AmbientLight(Color.WHITE, 0.5))
                .addCamera(
                        new Camera(
                                new Point3D(0, 0, -1000),
                                new Vector(0, 0, 1),
                                new Vector(0, -1, 0)))
                .addDistance(1000)
                .addBackground(Color.BLACK)
                .build();

        scene.addGeometries( //
                new Triangle(
                        new Color(java.awt.Color.darkGray),
//                        Color.BLACK,
                        new Material(0, 0.8, 80), //
                        new Point3D(-150, 150, 115),
                        new Point3D(150, 150, 135),
                        new Point3D(75, -75, 150)), //
                new Triangle(
                        new Color(java.awt.Color.orange),
//                        Color.BLACK,
                        new Material(0, 0.8, 80), //
                        new Point3D(-150, 150, 115),
                        new Point3D(-70, -70, 140),
                        new Point3D(75, -75, 150)), //
                new Sphere(
                        new Color(java.awt.Color.blue),
                        new Material(0.5, 0.5, 60), // )
                        30,
                        new Point3D(0, 0, 115)));

        scene.addLight(
                new SpotLight(
                        new Color(255, 100, 255).scale(0.85), //
                        new Point3D(40, -40, -150),
                        new Vector(-1, 1, 4),
                        1, 4E-4, 2E-5));


        ImageWriter imageWriterRegular = new ImageWriter("trianglesSphereRegular", 200, 200, 600, 600);
        Render renderRegular = new Render(imageWriterRegular, scene)
                .setMultithreading(4);
        renderRegular.renderImage();
        renderRegular.writeToImage();


        ImageWriter imageWriterAA = new ImageWriter("trianglesSphereAAGrid", 200, 200, 600, 600);
        Render renderAA = new Render(imageWriterAA, scene)
                .setMultithreading(4)
                .setAntialiasing(true)
                .setRayCounter(80);

        renderAA.renderImage();
        renderAA.writeToImage();

    }

    @Test
    void testTwoTrianglesSphere() {
        Scene scene;
        scene = new Scene.SceneBuilder("Test scene")
                .addAmbientLight(new AmbientLight(Color.WHITE, 0.2))
                .addCamera(
                        new Camera(
                                new Point3D(0, 0, -1000),
                                new Vector(0, 0, 1),
                                new Vector(0, -1, 0)))
                .addDistance(1000)
                .addBackground(Color.BLACK)
                .build();

        scene.addGeometries( //
                new Triangle(
                        Color.BLACK,
                        new Material(0, 0.8, 60), //
                        new Point3D(-150, 150, 115),
                        new Point3D(150, 150, 135),
                        new Point3D(75, -75, 150)), //
                new Triangle(
                        Color.BLACK, new Material(0, 0.8, 60), //
                        new Point3D(-150, 150, 115),
                        new Point3D(-70, -70, 140),
                        new Point3D(75, -75, 150)), //
                new Sphere(
                        new Color(java.awt.Color.BLUE),
                        new Material(0.5, 0.5, 30), // )
                        30,
                        new Point3D(0, 0, 115)));

        scene.addLight(
                new SpotLight(
                        new Color(700, 400, 400), //
                        new Point3D(40, -40, -115),
                        new Vector(-1, 1, 4),
                        1, 4E-4, 2E-5));

        ImageWriter imageWriter = new ImageWriter("trianglesSphereBB", 200, 200, 600, 600);
        Render render = new Render(imageWriter, scene)
//                .setSupersampling(true)
                .setSoftshadows(true)
                .setBeamRadius(50)
                .setRayCounter(1000)
                .setMultithreading(4)
                .setDebugPrint();

        render.renderImage();
        render.writeToImage();
    }
}
