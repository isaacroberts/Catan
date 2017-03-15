/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.awt.*;

public class Util 
{
    static Board brd;
    public static int typeToIx(char type)
    {
        // w=wood,f=farm,s=sheep,r=rock,b=brick,d=desert
        if (type=='w')
            return 0;
        else if (type=='f')
            return 1;
        else if (type=='s')
            return 2;
        else if (type=='b')
            return 3;
        else if (type=='r')
            return 4;
        else if (type=='d')
            return 5;
        else if (type=='?')
            return 6;
        else 
        {
            System.out.println("typeToIx error:"+type+" is not a type");
            return -1;
        }
    }
    public static char ixToType(int ix)
    {
        switch (ix)
        {
            case 0:
                return 'w';
            case 1:
                return 'f';
            case 2:
                return 's';
            case 3:
                return 'b';
            case 4:
                return 'r';
            case 5:
                return 'd';
            case 6:
                return '?';
        }
        System.out.println("ixToType error: "+ix+" is not an index");
        return '!';
    }
    public static String ixToName(int ix)
    {
        switch (ix)
        {
            case 0:
                return "wood";
            case 1:
                return "food";
            case 2:
                return "sheep";
            case 3:
                return "brick";
            case 4:
                return "rock";
            case 5:
                return "desert";
            case 6:
                return "?";
        }
        System.out.println("ixToType error: "+ix+" is not an index");
        return "!";
    }
    public static Color getResourceColor(int ix)
    {
        return getResourceColor(ixToType(ix));
    }
    public static Color getResourceColor(char type)
    {
        Color color;
        if (type=='w')
            color=new Color(0,125,0);
        else if (type=='f')
            color=new Color(255, 252, 0);
        else if (type=='s')
            color=new Color(255,255,255);
        else if (type=='b')
            color=new Color(132,66,0);
        else if (type=='r')
            color=new Color(100,100,100);
        else if (type=='d')
            color=new Color(255,193,71);
        else if (type=='?')
            color=new Color(125,0,125);
        else if (type=='-')
            color=new Color(0,0,0,0);
        else
        {
            System.out.println("error in getResourceColor: "+type+" is not a type");
            color=new Color(0,0,0,0);
        }
        return color;
    }
    public static String getResourceName(char type)
    {
        if (type=='w')
            return "wood";
        else if (type=='f')
            return "food";
        else if (type=='s')
            return "sheep";
        else if (type=='b')
            return "brick";
        else if (type=='r')
            return "rock";
        else
            return "--error--";
    }
    public static Color getPlayerColor(int who)
    {
        switch(who)
        {
            case -1:
                return new Color(150,150,150); 
            case 0:
                return new Color(200,0,0);
            case 1:
                return new Color(0,0,150);
            case 2:
                return new Color(255,120,0);
            case 3:
                return Color.ORANGE;
            case 4:
                return new Color(0,180,0);
            case 5:
                return Color.CYAN;
            case 6:
                return new Color(250,0,250);
            case 7:
                return new Color(230,230,230);
            case 8:
                return new Color(20,20,20);
            case 9:
                return new Color(120,250,0);
            case 10:
                return new Color(150,0,0);
            case 11:
                return new Color(120,0,120);
            case 12:
                return new Color(0,100,0);
            case 13:
                return new Color(100,0,0);
            case 14:
                return new Color(200,200,120);
            default:
                System.out.println("add to getPlayerColor("+who+")");
                return new Color(255,0,255,100);
        }
    }
    public static Color getProbColor(int num)
    {
        switch(num)
        {
            case 2:
            case 12:
                return new Color(0,0,0);
            case 3:
            case 11:
                return new Color(80,0,0);
            case 4:
            case 10:
                return new Color(130,0,0);
            case 5:
            case 9:
                return new Color(190,0,0);
            case 6:
            case 8:
                return new Color(255,0,0);
            default:
                return new Color(0,0,255);
        }
    }
}
