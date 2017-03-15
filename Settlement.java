
package catan;

import java.util.*;
import java.awt.*;

public class Settlement 
{
    
    Hex[] area;
    int player;
    boolean isCity;
    int px,py;
    Rectangle rect;
    ArrayList<Road> road;
    Polygon shape;
    public Settlement(int x,int y,Hex top,Hex left,Hex right)
    {
        road=new ArrayList<Road>();
        isCity=false;
        player=-1;
        px=x;
        py=y;
        rect=new Rectangle(px-35,py-35,70,70);
        int areaAmt=3;
        if (top==null)
            areaAmt--;
        if (left==null)
            areaAmt--;
        if (right==null)
            areaAmt--;
        area=new Hex[areaAmt];
        
        areaAmt=0;
        if (top!=null)
        {
            area[areaAmt]=top;
            areaAmt++;
        }
        if (left!=null)
        {
            area[areaAmt]=left;
            areaAmt++;
        }
        if (right!=null)
        {
            area[areaAmt]=right;
            areaAmt++;
        }
        makeSettlementPolygon();
    }
    public void makeSettlementPolygon()
    {
        shape=new Polygon();
        shape.addPoint(px-10,py);
        shape.addPoint(px-15, py);
        shape.addPoint(px, py-10);
        shape.addPoint(px+15, py);
        shape.addPoint(px+10,py);
        shape.addPoint(px+10,py+10);
        shape.addPoint(px-10, py+10);
    }
    public void makeCityPolygon()
    {
        shape=new Polygon();
        shape.addPoint(px-19, py-4);
        shape.addPoint(px-4, py-4);
        shape.addPoint(px-4,py-14);
        shape.addPoint(px+3,py-21);
        shape.addPoint(px+10, py-14);
        shape.addPoint(px+10,py+10);
        shape.addPoint(px-19,py+10);
    }
    public void addRoad(Road roadAdd)
    {
//        for (int n=0;n<road.size();n++)
//        {
//            if (add.equals(road.get(n)))
//                return;
//        }
        road.add(roadAdd);
    }
    public void citify() {
        makeCityPolygon();
        isCity=true;
    }
    public void giveToPlayer(int who) {
        player=who;
    }
    public int ownedBy() {
        return player;
    }
    public boolean oneAway()
    {
        for (int n=0;n<road.size();n++)
        {
            if (road.get(n).touchingOtherSettlement(this))
                return true;
        }
        return false;
    }
    public boolean isConnected(int who)
    {
        if (player!=-1 && who!=player)
            return false;
        for (int n=0;n<road.size();n++)
        {
            if (road.get(n).ownedBy()==who)
                return true;
        }
        return false;
    }
    public int[] getResources()
    {
        int[] resource=new int[5];
        for (int n=0;n<area.length;n++)
        {
            if (area[n].isRolled())
                if (!area[n].hasRobber())
                    resource[Util.typeToIx(area[n].getType())]+=(isCity?2:1);
        }
        return resource;
    }
    public Rectangle getBox()
    {
        return rect;
    }
    public Point getPoint()
    {
        return new Point(px,py);
    }
    public Hex[] getArea()
    {
        return area;
    }
    public void draw(Graphics2D g)
    {
        if (ownedBy()!=-1)
        {
            g.setColor(Util.getPlayerColor(player));
            g.fillPolygon(shape);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1));
            g.drawPolygon(shape);
        }
    }
}
