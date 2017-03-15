

package catan;

import java.awt.*;
//import java.Util.*;

public class Harbor 
{
    int x,y;
    char type;
    Image boat;
    Settlement land[];
    public Harbor(int px,int py)
    {
        x=px;
        y=py;
        
        boat=Toolkit.getDefaultToolkit().getImage(getClass().getResource("sailboat.png"));
        
        land=new Settlement[2];
        land[0]=land[1]=null;
        
        type='-';
    }
    public void addSettlement(Settlement add)
    {
        if (land[0]==null)
            land[0]=add;
        else if (land[1]==null)
            land[1]=add;
        else return;
    }
    public void setType(char set)
    {
        type=set;
    }
    public char getType()
    {
        return type;
    }
    public boolean contactWith(int who)
    {
        return (land[0].ownedBy()==who || land[1].ownedBy()==who);
    }
    public Point getCenter() {
        return new Point(x+90,y+60);
    }
    public boolean contains(Point p) 
    {//radius=50
        return (getCenter().distanceSq(p)<=2500);
    }
    public void draw(Graphics2D g,boolean firstView)
    {
        g.setStroke(new BasicStroke(15));
        g.setColor(Util.getResourceColor(type));
        g.drawLine(x+90,y+60,land[1].getPoint().x,land[1].getPoint().y);
        g.drawLine(x+90,y+60,land[0].getPoint().x,land[0].getPoint().y);
        g.fillOval(x+40,y+10,100,100);
        g.drawImage(boat,x+50,y+15,null);
    }
}
