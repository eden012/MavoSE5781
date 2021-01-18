package renderer;

import org.junit.jupiter.api.Test;

import elements.*;
import geometries.*;
import primitives.*;
import scene.*;

public class AntialiasingTest {
    @Test
    public void trianglesSphereAA() {
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

}
