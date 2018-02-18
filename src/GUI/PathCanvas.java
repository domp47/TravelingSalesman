package GUI;

import City.City;

import javax.swing.*;
import java.awt.*;

public class PathCanvas extends JPanel{

    private int[] path = null;
    private City[] cities = null;
    private float maxW, maxH;

    public void SetPath(int[] path, City[] cities){

        this.path = path;
        this.cities = cities;


        float maxW = Float.MIN_VALUE;
        float maxH = Float.MIN_VALUE;

        for (City city : cities) {
            if (city.getX() > maxW)
                maxW = city.getX();
            if (city.getY() > maxH)
                maxH = city.getY();
        }
        this.maxW = maxW;
        this.maxH = maxH;
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(600,600);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(path != null && cities != null){
            float ratio = FindRatio();

            for (int i = 0; i < path.length; i++) {
                int x1 = (int) (cities[path[i]].getX() * ratio);
                int x2 = (int) (cities[path[(i+1)%path.length]].getX() * ratio);

                int y1 = (int) (cities[path[i]].getY() * ratio);
                int y2 = (int) (cities[path[(i+1)%path.length]].getY() * ratio);

                g.drawLine(x1,y1,x2,y2);
            }

        }
    }

    private float FindRatio(){
        float heightRatio = getHeight() / maxH;
        float widthRatio = getWidth() / maxW;

        if(widthRatio < heightRatio)
            return widthRatio;
        return heightRatio;
    }

}
