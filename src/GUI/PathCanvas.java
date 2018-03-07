package GUI;

import City.City;

import javax.swing.*;
import java.awt.*;

/**
 * Class for actually drawing the path
 */
public class PathCanvas extends JPanel{

    //path to draw
    private City[] path = null;
    private float maxW, maxH;

    /**
     * Settings the path to draw
     * @param path to draw
     */
    public void SetPath(City[] path){

        this.path = path;

        //get max values to find the ratio to scale by
        float maxW = Float.MIN_VALUE;
        float maxH = Float.MIN_VALUE;

        for (City city : path) {
            if (city.getX() > maxW)
                maxW = city.getX();
            if (city.getY() > maxH)
                maxH = city.getY();
        }
        this.maxW = maxW;
        this.maxH = maxH;
    }

    /**
     * Set the preferred dimension of the canvas to 600x600
     * @return Dimension of 600x600
     */
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(600,600);
    }

    /**
     * paints the path for the path saved to the object
     * @param g graphics used to draw
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(path != null){
            //get ratio for scaling drawing
            float ratio = FindRatio();

            //draw a line from city to the next city for every city
            for (int i = 0; i < path.length; i++) {
                int x1 = (int) (path[i].getX() * ratio);
                int x2 = (int) (path[(i+1)%path.length].getX() * ratio);

                int y1 = (int) (path[i].getY() * ratio);
                int y2 = (int) (path[(i+1)%path.length].getY() * ratio);

                g.drawLine(x1,y1,x2,y2);
            }

        }
    }

    /**
     * Calculates the ratio for scaling
     * @return ratio to scale
     */
    private float FindRatio(){
        float heightRatio = getHeight() / maxH;
        float widthRatio = getWidth() / maxW;

        if(widthRatio < heightRatio)
            return widthRatio;
        return heightRatio;
    }

}
