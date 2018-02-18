package City;

public class City {

    private float xCord, yCord;

    public City(float x, float y){
        xCord = x;
        yCord = y;
    }

    public float GetDistance(City city){
        float x = Math.abs(getX() - city.getX());
        float y = Math.abs(getY() - city.getY());
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public float getX() {
        return xCord;
    }

    public float getY() {
        return yCord;
    }
}
