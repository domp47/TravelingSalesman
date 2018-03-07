package City;

public class City {

    private float xCord, yCord;

    public City(float x, float y){
        xCord = x;
        yCord = y;
    }

    /**
     * Calculates the distance between this city and a given city
     * @param city to find distance between
     * @return distance from this city to given city
     */
    public float GetDistance(City city){
        //Calculate distance using pythagorean theorem
        float x = Math.abs(getX() - city.getX());
        float y = Math.abs(getY() - city.getY());
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * Gets the X cord of the city
     * @return X
     */
    public float getX() {
        return xCord;
    }

    /**
     * Gets the Y cord of the city
     * @return Y
     */
    public float getY() {
        return yCord;
    }
}
