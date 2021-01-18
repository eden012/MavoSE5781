package renderer;

import elements.*;
import geometries.Cylinder;
import geometries.Geometries;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point3D;
import primitives.Vector;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

public class ProjectTest {

    @Test
    public void projectPicture() {

//*************************setting the scene***************************//
        Scene scene = new Scene.SceneBuilder("Project scene")
                .addAmbientLight(new AmbientLight(Color.WHITE, 0.9))
                .addCamera(
                        new Camera(
                                new Point3D(0, 160, 300),
                                //				new Point3D(0, 0, 300),
                                new Vector(0, 1, 0),
                                new Vector(0, 0, -1)))
                .addDistance(900)
                .addBackground(Color.BLACK)
                .build();

        Geometries geometries = new Geometries();
        List<LightSource> lights = new LinkedList<>();
//*************************setting the lights*************************//

        lights.add(new DirectionalLight(
                new Color(250, 230, 100),
                new Vector(2, -0.3, -0.5)));

//*************************setting the floor*************************//

        Point3D FAR_LEFT_CORNER = new Point3D(-250, 0, -250);

        double XsizePanel = 25;
        double ZsizePanel = 15;

        Color firstColor = new Color(30, 30, 30);
        Color secondColor = new Color(70, 70, 70);

        Material material = new Material(0.3, 0.15, 80, 0.3, 0);

        //first black triangle
        Point3D p1B = FAR_LEFT_CORNER,
                p2B = FAR_LEFT_CORNER.add(new Point3D(0, 0, ZsizePanel)),
                p3B = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, 0)),

                //first white triangle
                p1W = FAR_LEFT_CORNER.add(new Point3D(0, 0, ZsizePanel)),
                p2W = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, 0)),
                p3W = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, ZsizePanel));

        boolean flag = true;
        int count = 1;

        for (int j = 0; j < 18; j++) {//j<4
            for (int i = 0; i < 10; i++) {//i<8
                scene.addGeometries(
                        new Triangle(firstColor, material, p1B, p2B, p3B),
                        new Triangle(secondColor, material, p1W, p2W, p3W));

                //first flip black triangle:
                p1B = p1B.add(new Point3D(XsizePanel * 2, 0, 0));
                p2B = p2B.add(new Point3D(XsizePanel * 2, 0, 0));

                //first flip white triangle:
                p1W = p1W.add(new Point3D(XsizePanel * 2, 0, 0));

                scene.addGeometries(
                        new Triangle(p1B, p2B, p3B),
                        new Triangle(secondColor, material, p1W, p2W, p3W));

                //second flip black triangle:
                p3B = p3B.add(new Point3D(XsizePanel * 2, 0, 0));

                //second flip white triangle:
                p2W = p2W.add(new Point3D(XsizePanel * 2, 0, 0));
                p3W = p3W.add(new Point3D(XsizePanel * 2, 0, 0));

            }
            if (flag) {
                p1B = FAR_LEFT_CORNER.add(new Point3D(0, 0, ZsizePanel * 2 * count));
                p2B = FAR_LEFT_CORNER.add(new Point3D(0, 0, ZsizePanel + ZsizePanel * 2 * (count - 1)));
                p3B = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, ZsizePanel * 2 * count));

                p1W = FAR_LEFT_CORNER.add(new Point3D(0, 0, ZsizePanel + ZsizePanel * 2 * (count - 1)));
                p2W = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, ZsizePanel * 2 * count));
                p3W = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, ZsizePanel + ZsizePanel * 2 * (count - 1)));
                flag = !flag;
            } else {
                p1B = FAR_LEFT_CORNER.add(new Point3D(0, 0, ZsizePanel * 2 * count));
                p2B = FAR_LEFT_CORNER.add(new Point3D(0, 0, ZsizePanel + ZsizePanel * 2 * count));
                p3B = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, ZsizePanel * 2 * count));

                p1W = FAR_LEFT_CORNER.add(new Point3D(0, 0, ZsizePanel + ZsizePanel * 2 * count));
                p2W = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, ZsizePanel * 2 * count));
                p3W = FAR_LEFT_CORNER.add(new Point3D(XsizePanel, 0, ZsizePanel + ZsizePanel * 2 * count));
                count++;
                flag = !flag;
            }
        }

//******************************walls****************************//


//****************************left wall****************************//

        //Upper left wall
        scene.addGeometries(
                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 170, -250),
                        new Point3D(-250, 170, 20),
                        new Point3D(-250, 250, 20)),

                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 170, -250),
                        new Point3D(-250, 250, 20),
                        new Point3D(-250, 250, -250)),

                //Lower left wall
                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 0, -250),
                        new Point3D(-250, 0, 20),
                        new Point3D(-250, 60, 20)),

                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 0, -250),
                        new Point3D(-250, 60, 20),
                        new Point3D(-250, 60, -250)),

                //Closer left wall
                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 60, -50),
                        new Point3D(-250, 60, 20),
                        new Point3D(-250, 170, 20)),

                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 60, -50),
                        new Point3D(-250, 170, 20),
                        new Point3D(-250, 170, -50)),

                //Farther left wall
                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 60, -250),
                        new Point3D(-250, 60, -180),
                        new Point3D(-250, 170, -180)),

                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 60, -250),
                        new Point3D(-250, 170, -180),
                        new Point3D(-250, 170, -250)),

//****************************window*****************************//


                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.5, 0.33, 35, 0, 0),
                        2,
                        new Point3D(-250, 60, -50),
                        new Point3D(-250, 170, -50)),

                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.5, 0.33, 35, 0, 0),
                        2,
                        new Point3D(-250, 60, -180),
                        new Point3D(-250, 170, -180)),

                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.5, 0.33, 35, 0, 0),
                        2,
                        new Point3D(-250, 60, -50),
                        new Point3D(-250, 60, -180)),

                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.5, 0.33, 35, 0, 0),
                        2,
                        new Point3D(-250, 170, -50),
                        new Point3D(-250, 170, -180))
        );

//************************window shutter*****************************//

        for (int i = 0; i < 13; i++) {
            scene.addGeometries(
                    new Triangle(
                            new Color(100, 100, 100),
                            new Material(0.2, 0.15, 80, 0, 0),
                            new Point3D(-250, 162 - 8 * i, -50),
                            new Point3D(-250, 162 - 8 * i, -180),
                            new Point3D(-250, 166 - 8 * i, -180)),

                    new Triangle(
                            new Color(100, 100, 100),
                            new Material(0.2, 0.15, 80, 0, 0),
                            new Point3D(-250, 166 - 8 * i, -180),
                            new Point3D(-250, 166 - 8 * i, -50),
                            new Point3D(-250, 162 - 8 * i, -50))
            );
        }

//****************************************************************//

        //Right wall
        scene.addGeometries(
                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(250, 0, -250),
                        new Point3D(250, 0, 20),
                        new Point3D(250, 250, 20)),

                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(250, 0, -250),
                        new Point3D(250, 250, 20),
                        new Point3D(250, 250, -250)),

                //Front wall
                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 0, -250),
                        new Point3D(-250, 250, -250),
                        new Point3D(250, 0, -250)),

                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 250, -250),
                        new Point3D(250, 0, -250),
                        new Point3D(250, 250, -250)),

                //Ceiling
                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 250, -250),
                        new Point3D(-250, 250, 20),
                        new Point3D(250, 250, 20)),

                new Triangle(
                        new Color(70, 70, 70),
                        new Material(0.3, 0.15, 80, 0, 0),
                        new Point3D(-250, 250, -250),
                        new Point3D(250, 250, 20),
                        new Point3D(250, 250, -250))
        );

//****************************mirrors*********************************//


//		//Right wall mirror
//		geometries.add(new Triangle(
//				new Point3D(249.5, 60,-200),
//				new Point3D(249.5, 60, -20),
//				new Point3D(249.5, 170, -20),
//				new Color(100, 100, 100),
//				new Material(0.3, 0.15, 80, 0.9, 0)));
//
//		geometries.add(new Triangle(
//				new Point3D(249.5, 60,-200),
//				new Point3D(249.5, 170, -20),
//				new Point3D(249.5, 170, -200),
//				new Color(100, 100, 100),
//				new Material(0.3, 0.15, 80, 0.9, 0)));


//*****************************bulbs**********************************//

        scene.addGeometries(
                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.5, 0.33, 35, 0.6, 0),
                        1,
                        new Point3D(0, 250, -90),
                        new Point3D(0, 220, -90)),

                new Sphere(
                        new Color(30, 30, 30),
                        new Material(0.04, 0.15, 85, 0.1, 0.7),
                        10,
                        new Point3D(0, 210, -90))
        );

        lights.add(new PointLight(
                Color.WHITE,
                new Point3D(0, 210, -90),
                0.1, 0.0001, 0.00005));


        scene.addGeometries(
                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.5, 0.33, 35, 0.6, 0),
                        1,
                        new Point3D(-20, 250, -55),
                        new Point3D(-20, 210, -55)),

                new Sphere(
                        new Color(30, 30, 30),
                        new Material(0.2, 0.20, 85, 0.1, 0.5),
                        10,
                        new Point3D(-20, 200, -55))
        );

        lights.add(new PointLight(
                Color.WHITE.scale(0.4),
                new Point3D(-20, 200, -55),
                0.1, 0.0001, 0.00005));


        scene.addGeometries(
                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.5, 0.33, 35, 0.6, 0),
                        1,
                        new Point3D(0, 250, -40),
                        new Point3D(0, 200, -40)),

                new Sphere(
                        new Color(30, 30, 30),
                        new Material(0.2, 0.20, 85, 0.1, 0.5),
                        10,
                        new Point3D(0, 190, -40))
        );

        lights.add(new PointLight(
                Color.WHITE.scale(0.4),
                new Point3D(0, 190, -40),
                0.1, 0.0001, 0.00005));


        scene.addGeometries(
                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.5, 0.33, 35, 0.6, 0),
                        1,
                        new Point3D(20, 250, -55),
                        new Point3D(20, 190, -55)),

                new Sphere(
                        new Color(30, 30, 30),
                        new Material(0.2, 0.20, 85, 0.1, 0.5),
                        10,
                        new Point3D(20, 180, -55)));

        lights.add(new PointLight(
                Color.WHITE.scale(0.4),
                new Point3D(20, 180, -55),
                0.1, 0.0001, 0.00005));

//*************************OBJECTS IN THE ROOM*************************//

//*******************************table********************************//

        scene.addGeometries(
                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.3, 0.33, 35, 0.2, 0),
                        30,
                        new Point3D(0, 0, -115),
                        new Point3D(0, 3, -115)),

                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.3, 0.33, 35, 0, 0),
                        3,
                        new Point3D(0, 2, -115),
                        new Point3D(0, 58, -115)),

                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.2, 0.2, 35, 0.2, 0),
                        60,
                        new Point3D(0, 57, -115),
                        new Point3D(0, 60, -115)),

//*************************Just objects******************************//

                //Undefined object (the object was there when we came, so we
                //did not move it...)
                new Cylinder(
                        new Color(120, 120, 120),
                        new Material(0.4, 0.2, 35, 0, 0),
                        30,
                        new Point3D(220, 30, -250),
                        new Point3D(220, 30, -235)),

//****************************Spheres********************************//

                new Sphere(
                        new Color(30, 30, 30),
                        new Material(0.3, 0.20, 85, 0.85, 0),
                        35,
                        new Point3D(-214, 35.5, -214)),

                new Sphere(
                        new Color(90, 30, 160),
                        new Material(0.3, 0.20, 85, 0.2, 0),
                        25,
                        new Point3D(-224, 26, -155)),

                new Sphere(
                        new Color(130, 110, 30),
                        new Material(0.3, 0.20, 85, 0.3, 0),
                        20,
                        new Point3D(-151, 21, -229)),


                //ON THE TABLE
                new Sphere(
                        new Color(50, 210, 130),
                        new Material(0.3, 0.20, 85, 0.45, 0),
                        8,
                        new Point3D(-16, 68, -117)),

                new Sphere(
                        new Color(130, 170, 70),
                        new Material(0.3, 0.20, 85, 0.45, 0),
                        14,
                        new Point3D(33, 74, -100)),

                new Sphere(
                        new Color(230, 120, 154),
                        new Material(0.3, 0.20, 85, 0.45, 0),
                        6.5,
                        new Point3D(-4, 66.5, -65)),

//*********************************STAR******************************//

                new Sphere(
                        new Color(40, 40, 40),
                        new Material(0.3, 0.20, 85, 0.40, 0),
                        8,
                        new Point3D(125, 8, -50)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(125, 8, -50),
                        new Point3D(150, 6, -40)),

                new Sphere(
                        new Color(40, 40, 40),
                        new Material(0.3, 0.20, 85, 0.40, 0),
                        6,
                        new Point3D(150, 6, -40)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(150, 6, -40),
                        new Point3D(110, 7, -25)),

                new Sphere(
                        new Color(40, 40, 40),
                        new Material(0.3, 0.20, 85, 0.40, 0),
                        7,
                        new Point3D(110, 7, -25)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(110, 7, -25),
                        new Point3D(190, 10, -25)),

                new Sphere(
                        new Color(40, 40, 40),
                        new Material(0.3, 0.20, 85, 0.40, 0),
                        10,
                        new Point3D(190, 10, -25)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(190, 10, -25),
                        new Point3D(150, 46, -40)),

                new Sphere(
                        new Color(40, 40, 40),
                        new Material(0.3, 0.20, 85, 0.40, 0),
                        9,
                        new Point3D(150, 46, -40)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(150, 46, -40),
                        new Point3D(125, 9, -5)),

                new Sphere(
                        new Color(40, 40, 40),
                        new Material(0.3, 0.20, 85, 0.40, 0),
                        9,
                        new Point3D(125, 9, -5)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(150, 46, -40),
                        new Point3D(172, 8, -84)),

                new Sphere(
                        new Color(40, 40, 40),
                        new Material(0.3, 0.20, 85, 0.40, 0),
                        8,
                        new Point3D(172, 8, -84)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(172, 8, -84),
                        new Point3D(125, 8, -50)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(150, 46, -40),
                        new Point3D(125, 8, -50)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        1.5,
                        new Point3D(172, 8, -84),
                        new Point3D(150, 6, -40)),


//*************************spot light***************************//

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(10, 2.3, -5),
                        new Point3D(50, 2.3, -15))
        );

        lights.add(new SpotLight(
                Color.WHITE.scale(0.8),
                new Point3D(51, 2.3, -15.5),
                new Vector(new Point3D(50, 2.3, -15).subtract(new Point3D(10, 2.3, -5))),
                0.01, 0.0001, 0.00005));
//*******************************shelf*******************************//

        scene.addGeometries(
                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -185),
                        new Point3D(-40, 85, -185)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -191),
                        new Point3D(-40, 85, -191)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -197),
                        new Point3D(-40, 85, -197)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -203),
                        new Point3D(-40, 85, -203)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -209),
                        new Point3D(-40, 85, -209)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -215),
                        new Point3D(-40, 85, -215)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -221),
                        new Point3D(-40, 85, -221)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -227),
                        new Point3D(-40, 85, -227)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -233),
                        new Point3D(-40, 85, -233)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),

                        2.5,
                        new Point3D(-250, 85, -239),
                        new Point3D(-40, 85, -239)),
                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-250, 85, -245),
                        new Point3D(-40, 85, -245)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-40, 85, -185),
                        new Point3D(-40, 250, -185)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        2.5,
                        new Point3D(-40, 85, -185),
                        new Point3D(-40, 85, -250)),

                new Sphere(
                        new Color(40, 40, 40),
                        new Material(0.3, 0.20, 85, 0.40, 0),
                        4,
                        new Point3D(-40, 85, -185)),

//*************************objects on the shelf**********************//

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        25,
                        new Point3D(-220, 87.5, -220),
                        new Point3D(-220, 140, -220)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        18,
                        new Point3D(-160, 87.5, -210),
                        new Point3D(-160, 130, -210)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        13,
                        new Point3D(-110, 87.5, -203),
                        new Point3D(-110, 120, -203)),

                new Cylinder(
                        new Color(40, 40, 40),
                        new Material(0.4, 0.2, 35, 0, 0),
                        10,
                        new Point3D(-80, 87.5, -200),
                        new Point3D(-80, 108, -200))
        );
        scene.addLights(lights.toArray(LightSource[]::new));

//*************************rendering the image***********************//

//		ImageWriter imageWriter2 = new ImageWriter("p2", 2000, 2000, 4,4);
//		ImageWriter imageWriter3 = new ImageWriter("p3", 2000, 2000, 8,8);
//		ImageWriter imageWriter4 = new ImageWriter("p4", 2000, 2000, 10,10);
//		ImageWriter imageWriter5 = new ImageWriter("p5", 2000, 2000, 16,16);
//		ImageWriter imageWriter6 = new ImageWriter("p6", 2000, 2000, 20,20);
//		ImageWriter imageWriter7 = new ImageWriter("p7", 2000, 2000, 25,25);
//		ImageWriter imageWriter8 = new ImageWriter("p8", 2000, 2000, 40,40);
//		ImageWriter imageWriter9 = new ImageWriter("p9", 2000, 2000, 50,50);
//		ImageWriter imageWriter10 = new ImageWriter("p10", 2000, 2000, 100,100);
//		ImageWriter imageWriter11 = new ImageWriter("p11", 2000, 2000, 125,125);
//		ImageWriter imageWriter12 = new ImageWriter("p12", 2000, 2000, 200,200);
//		ImageWriter imageWriter13 = new ImageWriter("p13", 2000, 2000, 500,500);
        ImageWriter imageWriter14 = new ImageWriter("p14sd", 2000, 2000, 1000, 1000);
//		ImageWriter imageWriter15 = new ImageWriter("p15", 2000, 2000, 2000,2000);
//		ImageWriter imageWriter16 = new ImageWriter("p16", 2000, 2000, 4000,4000);

//		Renderer render2 = new Renderer(imageWriter2, scene);
//		Renderer render3 = new Renderer(imageWriter3, scene);
//		Renderer render4 = new Renderer(imageWriter4, scene);
//		Renderer render5 = new Renderer(imageWriter5, scene);
//		Renderer render6 = new Renderer(imageWriter6, scene);
//		Renderer render7 = new Renderer(imageWriter7, scene);
//		Renderer render8 = new Renderer(imageWriter8, scene);
//		Renderer render9 = new Renderer(imageWriter9, scene);
//		Renderer render10 = new Renderer(imageWriter10, scene);
//		Renderer render11 = new Renderer(imageWriter11, scene);
//		Renderer render12 = new Renderer(imageWriter12, scene);
//		Renderer render13 = new Renderer(imageWriter13, scene);
        Render render14 = new Render(imageWriter14, scene)
                .setMultithreading(4);
//		Renderer render15 = new Renderer(imageWriter15, scene);
//		Renderer render16 = new Renderer(imageWriter16, scene);

//		render2.renderImage();
//		render3.renderImage();
//		render4.renderImage();
//		render5.renderImage();
//		render6.renderImage();
//		render7.renderImage();
//		render8.renderImage();
//		render9.renderImage();
//		render10.renderImage();
//		render11.renderImage();
//		render12.renderImage();
//		render13.renderImage();
        render14.renderImage();
//		render15.renderImage();
//		render16.renderImage();

//		render2.writeToImage();
//		render3.writeToImage();
//		render4.writeToImage();
//		render5.writeToImage();
//		render6.writeToImage();
//		render7.writeToImage();
//		render8.writeToImage();
//		render9.writeToImage();
//		render10.writeToImage();
//		render11.writeToImage();
//		render12.writeToImage();
//		render13.writeToImage();
        render14.writeToImage();
//		render15.writeToImage();
//		render16.writeToImage();

    }
}
