package elements;

import primitives.Color;

public class AmbientLight extends Light {

    public AmbientLight(Color ia, double ka) {
        this._intensity = ia.scale(ka);
    }

    /**
     * @return new Color(_intensity)
     */
    public Color getIntensity() {
        return new Color(_intensity);
    }
}
