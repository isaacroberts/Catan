/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Board 
{   
    Hex[] hex;
    Harbor[] harbor;
    Settlement[] corners;
    Road[] road;
    Image background;
    public Board()
    {
        hex=new Hex[19];
        harbor=new Harbor[9];
        background=Toolkit.getDefaultToolkit().getImage(getClass().getResource("darker ocean.jpg"));
        int topCrnX=80,topCrnY=150;
        int y=topCrnY;
        int x=topCrnX+173;
        harbor[0]=new Harbor(166,-3);
        harbor[1]=new Harbor(512,-3);
        for (int r=0;r<3;r++)
        {
            hex[r]=new Hex(x,y);
            x+=173;
        }
        harbor[2]=new Harbor(x,y);
        x=topCrnX-87;
        y+=150;
        harbor[3]=new Harbor(x,y);
        x+=173;
        for (int r=0;r<4;r++)
        {
            hex[r+3]=new Hex(x,y);
            x+=173;
        }
        x=topCrnX;
        y+=150;
        for (int r=0;r<5;r++)
        {
            hex[r+7]=new Hex(x,y);
            x+=173;
        }
        harbor[4]=new Harbor(x,y);
        x=topCrnX-87;
        y+=150;
        harbor[5]=new Harbor(x,y);
        x+=173;
        for (int r=0;r<4;r++)
        {
            hex[r+12]=new Hex(x,y);
            x+=173;
        }
        x=topCrnX+173;
        y+=150;
        for (int r=0;r<3;r++)
        {
            hex[r+16]=new Hex(x,y);
            x+=173;
        }
        harbor[6]=new Harbor(x,y);
        y+=150;
        x=topCrnX+87;
        harbor[7]=new Harbor(x,y);
        x+=346;
        harbor[8]=new Harbor(x,y);
        
        //settlements
        corners=new Settlement[54];
        int crnIx=0;
        for (int h=0;h<19;h++)
        {
            for (int p=0;p<6;p++)
            {
                Point pt=new Point(hex[h].getPolygon().xpoints[p],hex[h].getPolygon().ypoints[p]);
                if (getCorner(pt)==-1)
                {
                    Point leftpt=new Point(pt.x+30, pt.y-90);
                    Point rightpt=new Point(pt.x+30, pt.y+90);
                    Point uppt=new Point(pt.x-90, pt.y+10);
                    
                    Hex left=null,right=null,up=null;
                    if (getHex(leftpt)!=-1)
                        left=hex[getHex(leftpt)];
                    if (getHex(rightpt)!=-1)
                        right=hex[getHex(rightpt)];
                    if (getHex(uppt)!=-1)
                        up=hex[getHex(uppt)];
                    corners[crnIx]=new Settlement(pt.x,pt.y,up,left,right);
                    crnIx++;
                }
            }
        }
        
        harbor[0].addSettlement(corners[0]);
        harbor[0].addSettlement(corners[5]);
        harbor[1].addSettlement(corners[8]);
        harbor[1].addSettlement(corners[9]);
        harbor[2].addSettlement(corners[11]);
        harbor[2].addSettlement(corners[24]);
        harbor[3].addSettlement(corners[14]);
        harbor[3].addSettlement(corners[15]);
        harbor[4].addSettlement(corners[36]);
        harbor[4].addSettlement(corners[37]);
        harbor[5].addSettlement(corners[27]);
        harbor[5].addSettlement(corners[38]);
        harbor[6].addSettlement(corners[45]);
        harbor[6].addSettlement(corners[46]);
        harbor[7].addSettlement(corners[47]);
        harbor[7].addSettlement(corners[48]);
        harbor[8].addSettlement(corners[50]);
        harbor[8].addSettlement(corners[51]);
        
        //road setup
        road=new Road[72];
        int rdIx=0;
        for (int n=0;n<corners.length;n++)
        {
            for (int m=0;m<corners.length;m++)
            {
                if (n!=m)
                {
                    Hex[] nArea=corners[n].getArea();
                    Hex[] mArea=corners[m].getArea();
                    int touching=0;
                    for (int ns=0;ns<nArea.length;ns++)
                    {
                        for (int ms=0;ms<mArea.length;ms++)
                        {
                            if (nArea[ns].getID()==mArea[ms].getID())
                                touching++;
                        }
                    }
                    if (touching==2)
                    {
                        Point pt=new Point((corners[n].getPoint().x+corners[m].getPoint().x)/2,(corners[n].getPoint().y+corners[m].getPoint().y)/2);
                        if (getRoad(pt)==-1)
                        {
                            road[rdIx]=new Road(corners[n],corners[m]);
                            rdIx++;
                        }
                    }
                    else if ((nArea.length<=2&&mArea.length<=1&& touching>=1))
                    {
                        double xx=Math.pow((corners[n].getPoint().x-corners[m].getPoint().x),2);
                        double yy=Math.pow((corners[n].getPoint().y-corners[m].getPoint().y),2);
                        double dist=Math.sqrt(xx+yy);
                        if (dist<=104)
                        {
                            Point pt=new Point((corners[n].getPoint().x+corners[m].getPoint().x)/2,(corners[n].getPoint().y+corners[m].getPoint().y)/2);
                            if (getRoad(pt)==-1)
                            {
                                road[rdIx]=new Road(corners[n],corners[m]);
                                rdIx++;
                            }
                        }
                    }
                }
            }
        }
        for (int n=0;n<road.length;n++)
        {
            road[n].tellNeighbors();
        }
    }
    public void setup(boolean random)
    {
        if (random)
            randomSetup();
        else
            beginnerSetup();
    }
    public void randomSetup()
    {
        int[] used={4,4,4,3,3,1};
        Random rand=new Random();
        for (int n=0;n<19;n++)
        {
            boolean assnd=false;
            while (!assnd)
            {
                int ix=rand.nextInt(6);
                if (used[ix]>0)
                {
                    hex[n].setType(Util.ixToType(ix));
                    used[ix]--;
                    assnd=true;
                }
            }
        }
        int[] nUsed={0,0,1,2,2,2,2,0,2,2,2,2,1};
        for (int n=0;n<19;n++)
        {
            boolean assnd=false;
            while (!assnd)
            {
                if (hex[n].getType()=='d')
                    assnd=true;
                else
                {
                    int ix=rand.nextInt(13);
                    if (nUsed[ix]>0)
                    {
                        hex[n].setNum(ix);
                        nUsed[ix]--;
                        assnd=true;
                    }
                }
            }
        }
        int[] hUsed={1,1,1,1,1,0,4};
        for (int n=0;n<9;n++)
        {
            boolean assnd=false;
            while (!assnd)
            {
                int ix=rand.nextInt(7);
                if (hUsed[ix]>0)
                {
                    harbor[n].setType(Util.ixToType(ix));
                    hUsed[ix]--;
                    assnd=true;
                }
            }
        }
    }
    public void beginnerSetup()
    {
        int n=0;
        hex[n].setType('w');
        hex[n].setNum(11);
        n++;
        hex[n].setType('s');
        hex[n].setNum(12);
        n++;
        hex[n].setType('f');
        hex[n].setNum(9);
        n++;
        hex[n].setType('b');
        hex[n].setNum(4);
        n++;
        hex[n].setType('r');
        hex[n].setNum(6);
        n++;
        hex[n].setType('b');
        hex[n].setNum(5);
        n++;
        hex[n].setType('s');
        hex[n].setNum(10);
        n++;
        hex[n].setType('d');
        hex[n].setNum(0);
        hex[n].addRobber();
        n++;
        hex[n].setType('w');
        hex[n].setNum(3);
        n++;
        hex[n].setType('f');
        hex[n].setNum(11);
        n++;
        hex[n].setType('w');
        hex[n].setNum(4);
        n++;
        hex[n].setType('f');
        hex[n].setNum(8);
        n++;
        hex[n].setType('b');
        hex[n].setNum(8);
        n++;
        hex[n].setType('s');
        hex[n].setNum(10);
        n++;
        hex[n].setType('s');
        hex[n].setNum(9);
        n++;
        hex[n].setType('r');
        hex[n].setNum(3);
        n++;
        hex[n].setType('r');
        hex[n].setNum(5);
        n++;
        hex[n].setType('f');
        hex[n].setNum(2);
        n++;
        hex[n].setType('w');
        hex[n].setNum(6);
        n++;
        harbor[0].setType('?');
        harbor[1].setType('s');
        harbor[2].setType('?');
        harbor[3].setType('r');
        harbor[4].setType('?');
        harbor[5].setType('f');
        harbor[6].setType('b');
        harbor[7].setType('?');
        harbor[8].setType('w');
    }
    public void highlight(int num)
    {
        for (int n=0;n<19;n++)
        {
            if (hex[n].getNum()==num)
            {
                hex[n].highlight(true);
            }
            else
                hex[n].highlight(false);
        }
    }
    public boolean moveRobber(int to)
    {
        for (int n=0;n<19;n++)
        {
            if (n==to)
                if (hex[n].hasRobber())
                    return false;
            hex[n].removeRobber();
        }
        hex[to].addRobber();
        return true;
    }
    public int getHex(Point p)
    {
        for (int n=0;n<19;n++)
        {
            if (hex[n].getPolygon().contains(p))
                return n;
        }
        return -1;
    }
    public int getCorner(Point p)
    { 
        for (int n=0;n<corners.length;n++)
        {
            if (corners[n]!=null)
            if (corners[n].getBox().contains(p))
            {
                return n;
            }
        }
        return -1;
    }
    public int getRoad(Point p)
    {
        for (int n=0;n<road.length;n++)
        {
            if (road[n]!=null)
            if (road[n].getRect().contains(p))
            {
                return n;
            }
        }
        return -1;
    }
    public int getHarbor(Point p)
    {
        for (int n=0;n<harbor.length;n++)
        {
            if (harbor[n].contains(p))
                return n;
        }
        return -1;
    }
    public Hex getHex(int id) {
        return hex[id];
    }
    public Settlement getSettlement(int id) {
        return corners[id];
    }
    public Road getRoad(int id) {
        return road[id];
    }
    public Harbor getHarbor(int id) {
        return harbor[id];
    }
    public void drawHarbors(Graphics2D g)
    {
        for (int n=0;n<harbor.length;n++)
            harbor[n].draw(g,false);
    }
    public void draw(Graphics2D g,boolean firstView)
    {
        g.drawImage(background,-100,20,null);
        if (firstView)
        {
            g.setColor(Color.BLUE);
            g.fillRect(0,0,1200,1000);
        }
        for (int n=0;n<harbor.length;n++)
            harbor[n].draw(g,firstView);
        for (int n=0;n<19;n++)
        {
            hex[n].draw(g,firstView);
        }
        if (!firstView)
        {
            for (int n=0;n<road.length;n++)
            {
                road[n].draw(g);
            }
            for (int n=0;n<corners.length;n++)
            {
                corners[n].draw(g);
            }
        }
    }
}