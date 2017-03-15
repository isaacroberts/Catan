/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Hex
{
    int px,py;
    Polygon hex;
    int num;
    char type; // w=wood,f=farm,s=sheep,r=rock,b=brick,d=desert
    Color color;
    boolean rolled;
    boolean robber;
    static Image[] types;
    static Image robberImage;
    public static void initImages()
    {
        types=new Image[6];
        
        try {
            robberImage=Toolkit.getDefaultToolkit().getImage(Hex.class.getResource("robber.png"));
            for (int n=0;n<=5;n++)
            {
                    types[n]=Toolkit.getDefaultToolkit().getImage(Hex.class.getResource(Util.ixToName(n)+".png"));
            }
        }
        catch(Exception e)
        {
            System.out.println("Hex image not gotten");
            e.printStackTrace();
        }
    }
    public Hex(int pixX,int pixY)
    {
        rolled=false;
        px=pixX;
        py=pixY;
        type='d';
        robber=false;
        color=new Color(0,0,0);
        num=0;
        //init polygon
        hex=new Polygon();
        
        hex.addPoint(px, py);//  top left corner
        hex.addPoint(px, py+100);// bottom left
        hex.addPoint(px+87,py+150); // bottom middle
        hex.addPoint(px+173,py+100);//  bottom right
        hex.addPoint(px+173,py);// top right
        hex.addPoint(px+87,py-50);// top middle
    }
    public int getID() {
        return px*py;
    }
    public void setType(char set) {
        type=set;
        color=Util.getResourceColor(type);
    }
    public void highlight(boolean set) {
        rolled=set;
    }
    public boolean isRolled() {
        return rolled;
    }
    public void setNum(int set) {
        num=set;
    }
    public char getType() {
        return type;
    }
    public int getNum() {
        return num;
    }
    public void addRobber() {
        robber=true;
    }
    public void removeRobber() {
        robber=false;
    }
    public boolean hasRobber() {
        return robber;
    }
    public void draw(Graphics2D g,boolean firstView)
    {
        g.drawImage(types[Util.typeToIx(type)],px,py-50,null);
        if (firstView)
        {
            g.setColor(color);
            g.fillPolygon(hex);
        }
        g.setStroke(new BasicStroke(15));
        g.setColor(new Color(202,160,0));
        g.drawPolygon(hex);
        
        if (!firstView)
        {
            if (num!=0)
            {
                g.setColor(new Color(195,176,145,200));
                if (rolled)
                    g.setColor(new Color(255,50,50,225));
                g.fillOval(px+61,py+30,50,50);
                g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,30));
    //            g.setColor(Color.BLACK);
                g.setColor(Util.getProbColor(num));
                if (Integer.toString(num).length()==2)
                    g.drawString(""+num, px+70, py+66);
                else g.drawString(" "+num, px+70, py+66);
            }
        }
        if (robber)
        {
//                g.setColor(new Color(105,105,105,220));
//                g.fillOval(px+51,py+20,70,70);
            g.drawImage(robberImage,px+36,py,null);
        }
    }
    public Polygon getPolygon()
    {
        return hex;
    }
}
