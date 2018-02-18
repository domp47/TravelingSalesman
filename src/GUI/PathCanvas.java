package GUI;

import javax.swing.*;
import java.awt.*;

public class PathCanvas extends JPanel{

    private int[] path = null;
    private float[][] vertices = null;
    private float maxW, maxH;

    public void SetPath(int[] path, float[][] vertices){

        this.path = path;
        this.vertices = vertices;


        float maxW = Float.MIN_VALUE;
        float maxH = Float.MIN_VALUE;

        for (float[] vertex : vertices) {
            if (vertex[0] > maxW)
                maxW = vertex[0];
            if (vertex[1] > maxH)
                maxH = vertex[1];
        }
        this.maxW = maxW;
        this.maxH = maxH;    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(600,600);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(path != null && vertices != null){
            float ratio = FindRatio();

            for (int i = 0; i < path.length; i++) {
                int x1 = (int) (vertices[path[i]][0] * ratio);
                int x2 = (int) (vertices[path[(i+1)%path.length]][0] * ratio);

                int y1 = (int) (vertices[path[i]][1] * ratio);
                int y2 = (int) (vertices[path[(i+1)%path.length]][1] * ratio);

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
