/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.awt.*;
import java.util.*;

public class Road 
{
    int player;
    Rectangle rect;
    Settlement from;
    Settlement to;
    public Road(Settlement start,Settlement end)
    {
        from=start;
        to=end;
        int xStart,yStart;
        int xDist,yDist;
        xStart=(from.getPoint().x<to.getPoint().x?from.getPoint().x:to.getPoint().x);
        yStart=(from.getPoint().y<to.getPoint().y?from.getPoint().y:to.getPoint().y);
        xDist=(from.getPoint().x>to.getPoint().x?from.getPoint().x-to.getPoint().x:to.getPoint().x-from.getPoint().x);
        yDist=(from.getPoint().y>to.getPoint().y?from.getPoint().y-to.getPoint().y:to.getPoint().y-from.getPoint().y);
        int offset=(xDist<5?15:-15);
        rect=new Rectangle(xStart-offset,yStart-15,xDist+offset*2,yDist+30);
        player=-1;
    }
    public void tellNeighbors()
    {
        from.addRoad(this);
        to.addRoad(this);
    }
    public int ownedBy() {
        return player;
    }
    public ArrayList<Road> getLongest()
    {
        ArrayList<Road> chkd=new ArrayList<Road>();
        chkd.add(this);
        return getLongest(chkd);
    }
    public ArrayList<Road> getLongest(ArrayList<Road> checked) {
        ArrayList<Road> longest[]=new ArrayList[2];
        for (int side=0;side<=1;side++)
        {
            longest[side]=new ArrayList<Road>();
            int owner=(side==0?from:to).ownedBy();
            if (owner==-1 || owner==player)
            {
                final ArrayList<Road> touching=(side==0?from:to).road;
                for (int n=0;n<touching.size();n++)
                {
                    if (touching.get(n).player==player)
                    {
                        boolean notChecked=true;
                        for (int c=0;c<checked.size();c++)
                        {
                            if (checked.get(c).equals(touching.get(n)))
                            {
                                notChecked=false;
                                break;
                            }
                        }
                        if (notChecked)
                        {
                            checked.add(touching.get(n));
                            ArrayList<Road> road=touching.get(n).getLongest(checked);
                            if (road.size()>longest[side].size())
                            {
                                longest[side]=road;
                            }
                        }
                    }
                }
            }
        }
        ArrayList<Road> road=new ArrayList<Road>();
        road.addAll(longest[0]);
        road.add(this);
        road.addAll(longest[1]);
        return road;
    }
    public boolean touchingOtherSettlement(Settlement otherThan)
    {
        if (!from.equals(otherThan))
        {
            if (from.ownedBy()!=-1)
                return true;
        }
        if (!to.equals(otherThan))
            if (to.ownedBy()!=-1)
                return true;
        return false;
        
    }
    public boolean contactWith(int who)
    {
        if (from.ownedBy()==who || to.ownedBy()==who)
            return true;
        if (from.isConnected(who))
            return true;
        if (to.isConnected(who))
            return true;
        return false;
    }
    public Rectangle getRect() {
        return rect;
    }
    public void draw(Graphics2D g)
    {
        if (player!=-1)
        {
            g.setColor(Util.getPlayerColor(player));
            g.setStroke(new BasicStroke(10));
            g.drawLine(from.getPoint().x,from.getPoint().y,to.getPoint().x,to.getPoint().y);
            g.fillOval(from.getPoint().x-6,from.getPoint().y-6,12,12);
            g.fillOval( to.getPoint().x -6, to.getPoint().y -6,12,12);
        }
    }
}
