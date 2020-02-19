import java.awt.*;
import java.awt.geom.Line2D;
import MyPoint.java;
import MyPointSet.java;


public class MyGraphics {

    public boolean isFill;
    public Color paintColor, bkColor, hullColor;
    public double scale;
    public int pointRadius;
    public double worldX;
    public double worldY;
    public int screenX;
    public int screenY;
    //    public MyPolygon  thePoly;
    public MyPointSet thePoints;
    public int mode;

    public MyGraphics(Color paint, Color bk)
    {
        scale = 1.0D;
        pointRadius = 4;
        //	thePoly = new MyPolygon();
        thePoints = new MyPointSet();
        paintColor = hullColor = paint;
        bkColor = bk;
    }


    public void clear(Component component, Color paramColor)
    {
        //	thePoly   = new MyPolygon();
        thePoints = new MyPointSet();
        component.repaint();
    }

    private void drawPoint(Graphics g, int x, int y)
    {
        int i = x;
            int j = y;
            int k = pointRadius;
        Color color = g.getColor();
        g.setColor(paintColor);
        //        System.out.println("...drawPoint..."+paintColor.toString());
        g.fillOval(i - k, j - k, k + k, k + k);
        g.setColor(color);
    }

    public void addPolyPoint(Graphics g, int x, int y)
    {
        thePoints.addPoint(x,y);
        drawPoint(g, x, y);

	//	System.out.println("...addPoly..."+thePoly.npoints);
    }

    private void drawPolyPoints(Graphics g) {
        MyPoint p;
        //System.out.println("...drawPoly..."+thePoly.npoints);
        for (int i=0; i<thePoints.size(); i++) {
            //System.out.println("i="+i+" x="+thePoly.xpoints[i]+" y="+thePoly.ypoints[i]);
            //p = (MyPoint)thePoints.elementAt(i);
            p = thePoints.elementAt(i);
            drawPoint(g, p.x, p.y);
        }
    }

    public void drawThePolygon(Graphics g)
    {
        Color color = g.getColor();
        g.setColor(paintColor);
        drawPolyPoints(g);
        g.setColor(color);
    }


    
    public void hullThePolygon(Graphics g) 
    {
        Polygon chp = thePoints.hullThePolygon();
        Color color = g.getColor();
        g.setColor(hullColor);
        g.drawPolygon(chp);
        g.setColor(color);
    }
}
