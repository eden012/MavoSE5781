package scene;

import elements.*;
import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

public class Scene {
    private final String _name;
    private final Geometries _geometries = new Geometries();

    private Color _background;
    private Camera _camera;
    private double _distance;
    private AmbientLight _ambientLight;
    private List<LightSource> _lights = null;

    /*-----------------constructors-------------------------------------*/

    public Scene(String _sceneName) {
        this._name = _sceneName;
    }

/*-------------------------getters and setters------------------------*/
    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    public Camera getCamera() {
        return _camera;
    }

    public Geometries getGeometries() {
        return _geometries;
    }

    public double getDistance() {
        return _distance;
    }



    public Color getBackground() {
        return this._background;
    }


    public List<LightSource> getLightSources() {
        return _lights;
    }
    public Scene setCamera(Camera camera) {
        this._camera = camera;
        return this;
    }

    public void setDistance(int i) {
        this._distance=i;
        //לעשות return?
    }

    public void setBackground(Color color) {
        this._background=color;
        //לעשות return?
    }

    public void setAmbientLight(AmbientLight ambientLight) {
        this._ambientLight=ambientLight;
        //לעשות return?
    }
/*---------------------------functions---------------------------------*/
    public void addGeometries(Geometry... geometries) {
        for (Geometry geo : geometries) {
            _geometries.add(geo);
        }
    }

    public void removeAllGeometries() {
        _geometries.removeAll();
    }

    public void addLight(LightSource light) {
        if (_lights == null) {
            _lights = new LinkedList<>();
        }
        _lights.add(light);
    }

    public void addLights(LightSource... lights) {
        for (LightSource l : lights) {
            addLight(l);
        }

    }


    public static class SceneBuilder {
        private final String name;
        private Color background;
        private Camera camera;
        private double distance;
        private AmbientLight ambientLight;

        public SceneBuilder(String name) {
            this.name = name;
        }

        public SceneBuilder addBackground(Color background) {
            this.background = background;
            return this;
        }

        public SceneBuilder addCamera(Camera camera) {
            this.camera = camera;
            return this;
        }

        public SceneBuilder addDistance(double distance) {
            this.distance = distance;
            return this;
        }

        public SceneBuilder addAmbientLight(AmbientLight ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        public Scene build() {
            Scene scene = new Scene(this.name);
            scene._camera = this.camera;
            scene._distance = this.distance;
            scene._background = this.background;
            scene._ambientLight = this.ambientLight;
            validateUserObject(scene);
            return scene;
        }

        private void validateUserObject(Scene scene) {
            //Do some basic validations to check
            //if user object does not break any assumption of system
        }
    }
}
