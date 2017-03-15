/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.awt.*;
import java.util.ArrayList;

public class GUI 
{
    enum Menu
    {
        None,
        SelectTrader,Haggle,Steal,
    }
    Rectangle[] button;
    Rectangle[] resButton;
    int pressed;
    ArrayList<Player> plyrMenu;
    Menu whichMenu;
    Color selected;
    boolean cardDisplayed;
    Polygon leftCardArrow,rightCardArrow;
    public GUI()
    {
        pressed=-1;
        button=new Rectangle[9];
        resButton=new Rectangle[5];
        plyrMenu=new ArrayList<Player>();
        whichMenu=Menu.None;
        cardDisplayed=false;
        int y=30;
        for(int n=0;n<4;n++)
        {
            y+=70;
            button[n]=new Rectangle(1030,y,150,50);
        }
        button[4]=new Rectangle(1070,760,110,60);
        button[5]=new Rectangle(1070,830,110,60);
        //dev card buttons
        leftCardArrow=new Polygon();
        leftCardArrow.addPoint(917,840);
        leftCardArrow.addPoint(917,850);
        leftCardArrow.addPoint(902,845);
        button[6]=new Rectangle(897,830,25,30);
        rightCardArrow=new Polygon();
        rightCardArrow.addPoint(1031,840);
        rightCardArrow.addPoint(1031,850);
        rightCardArrow.addPoint(1046,845);
        button[7]=new Rectangle(1026,830,25,30);
        
        button[8]=new Rectangle(922,800,104,100);
        //res buttons
        int x=890;
        for (int n=0;n<5;n++)
        {
            resButton[n]=new Rectangle(x,920,50,50);
            x+=60;
        }
    }
    public int getButton(Point p)
    {
        for (int n=0;n<button.length;n++)
        {
            if ((n<6 || n>8) || cardDisplayed) //the buttons 6-8 are on the devCard mini-menu
            {//and shouldnt be clicked if the menus not shown
                if (button[n].contains(p))
                {
                    pressed=n;
                    return n;
                }
            }
        }
        pressed=-1;
        return -1;
    }
    public int getResButton(Point pt)
    {
        for (int n=0;n<resButton.length;n++)
        {
            if (resButton[n].contains(pt))
                return n;
        }
        return -1;
    }
    public void press(int which)
    {
        pressed=which;
    }
    public void clearPress()
    {
        pressed=-1;
        plyrMenu.clear();
        whichMenu=Menu.None;
    }
    public void showStealMenu(ArrayList<Player> victims,Player suspect)
    {
        plyrMenu.clear();
        for (int n=0;n<victims.size();n++)
            plyrMenu.add(victims.get(n));
        selected=suspect.getColor();
        whichMenu=Menu.Steal;
    }
    public Player getStealClick(Point click)
    {
        int front=600-plyrMenu.size()*75;
        for (int n=0;n<plyrMenu.size();n++)
        {
            if (new Rectangle(front,400,120,100).contains(click))
            {
                return plyrMenu.get(n);
            }
            front+=150;
        }
        return null;
    }
    public void showTradeMenu(Player trader,ArrayList<Player> allPlyrs)
    {
        plyrMenu.clear();
        for (int n=0;n<allPlyrs.size();n++)
        {
            if (allPlyrs.get(n).getID()!=trader.getID())
            {
                plyrMenu.add(allPlyrs.get(n));
            }
        }
        selected=trader.getColor();
        whichMenu=Menu.SelectTrader;
    }
    public int getTradeClick(Point click)
    {
        int xSize=(180/plyrMenu.size());

        int px=860;
        for (int n=0;n<plyrMenu.size();n++)
        {
            if (new Rectangle(px+5,835,xSize-10,50).contains(click))
                return plyrMenu.get(n).getID();
            px+=xSize;
        }
        return -1;
    }
    public void showTradeScreen(Player from,Player to) 
    {
        plyrMenu.add(to);
        plyrMenu.add(from);
        plyrMenu.add(new Player());
        plyrMenu.add(new Player());
        whichMenu=Menu.Haggle;
    }
    public boolean clickTradeScreen(Point pt)
    {
        int px=440;
        for (int n=0;n<5;n++)
        {
            if (new Rectangle(px,330,50,50).contains(pt))
            {
                if (plyrMenu.get(0).getResAmt(n)>0)
                {
                    plyrMenu.get(0).takeRes(n);
                    plyrMenu.get(2).donate(n);
                }
                return false;
            }
            else if (new Rectangle(px,415,50,50).contains(pt))
            {
                if (plyrMenu.get(2).getResAmt(n)>0)
                {
                    plyrMenu.get(2).takeRes(n);
                    plyrMenu.get(0).donate(n);
                }
                return false;
            }
            else if (new Rectangle(px,535,50,50).contains(pt))
            {
                if (plyrMenu.get(3).getResAmt(n)>0)
                {
                    plyrMenu.get(3).takeRes(n);
                    plyrMenu.get(1).donate(n);
                }
                return false;
            }
            else if (new Rectangle(px,615,50,50).contains(pt))
            {
                if (plyrMenu.get(1).getResAmt(n)>0)
                {
                    plyrMenu.get(1).takeRes(n);
                    plyrMenu.get(3).donate(n);
                }
                return false;
            }
            px+=66;
        }
        if (new Rectangle(430,480,100,40).contains(pt))
        {//accept trade
            for (int n=0;n<5;n++)
            {
                plyrMenu.get(0).donate(n,plyrMenu.get(3).getResAmt(n));
                plyrMenu.get(1).donate(n,plyrMenu.get(2).getResAmt(n));
            }
            return true;
        }
        else if (new Rectangle(550,480,100,40).contains(pt))
        {//cancel if hit cancel button
            for (int n=0;n<5;n++)
            {
                plyrMenu.get(0).donate(n,plyrMenu.get(2).getResAmt(n));
                plyrMenu.get(1).donate(n,plyrMenu.get(3).getResAmt(n));
            }
            return true;
        }
        else if (pt.x>850||pt.x<350|| pt.y>750 || pt.y<250)
        {//cancel if click outside trade screen
            for (int n=0;n<5;n++)
            {
                plyrMenu.get(0).donate(n,plyrMenu.get(2).getResAmt(n));
                plyrMenu.get(1).donate(n,plyrMenu.get(3).getResAmt(n));
            }
            return true;
        }
        else if (new Rectangle(670,480,100,40).contains(pt))
        {//clear
            for (int n=0;n<5;n++)
            {
                plyrMenu.get(0).donate(n,plyrMenu.get(2).getResAmt(n));
                plyrMenu.get(1).donate(n,plyrMenu.get(3).getResAmt(n));
            }
            plyrMenu.get(2).clearHand();
            plyrMenu.get(3).clearHand();
            return false;
        }
        return false;
    }
    public void draw(Graphics2D g, Player ply)
    {
        //draw player
        if (ply.devCardAmt()>0)
        {
            g.setColor(Util.getPlayerColor(ply.which));
            g.fillRect(898,790,151,109);
            g.setStroke(new BasicStroke(2));
            ply.drawShownCard(g,899,800);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1));
            g.drawRect(898,790,151,109);
            g.setColor(new Color(200,200,200));
            g.fillPolygon(leftCardArrow);
            g.fillPolygon(rightCardArrow);
            g.setColor(Color.BLACK);
            g.drawPolygon(leftCardArrow);
            g.drawPolygon(rightCardArrow);
            cardDisplayed=true;
        }
        else cardDisplayed=false;
        g.setColor(ply.getColor());
        g.fillRect(800,900,400,100);
        int x=890;
//        g.setColor(Color.CYAN);
//        g.fillOval(820,920,50,50);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,20));
        g.drawString("VP:"+ply.getVP(),825,950);
        if (ply.equals(Player.largestArmy))
        {
            g.drawString("Largest Army: "+Player.largeArmySize,810,995);
        }
        else if (ply.knightAmt>0)
        {
            g.drawString("Army: "+ply.knightAmt,883,995);
        }
        if (ply.equals(Player.longestPlayer))
        {
            g.drawString("Longest Road: "+Player.longestRoad.size(),1020,995);
        }
        for (int n=0;n<5;n++)
        {
            g.setColor(Util.getResourceColor(n));
            g.fillOval(x,920,50,50);
            g.setColor(Color.BLACK);
            g.drawString(""+ply.getResAmt(n),x+15,950);
            x+=60;
        }
        
        int y=30;
        for(int n=0;n<4;n++)
        {
            g.setColor(Color.GRAY);
            if (pressed==n)
                g.setColor(Color.DARK_GRAY);
            y+=70;
            g.fillRect(1030,y,150,50);
        }
        g.setColor(ply.getColor());
        g.fillRect(1050,740,200,160);//the box for the trade buttons
        g.setColor(Color.GRAY);
        if (pressed==4)
            g.setColor(Color.DARK_GRAY);
        g.fillRect(1070,760,110,60);
        g.setColor(Color.GRAY);
        if (pressed==5)
            g.setColor(Color.DARK_GRAY);
        g.fillRect(1070,830,110,60);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,25));
        g.drawString("Build Road",1040,135);
        g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,17));
        g.drawString("Build Settlement",1040,200);
        g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,20));
        g.drawString("Upgrade to City",1034,275);
        g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,17));
        g.drawString("Buy Development",1037,340);
        g.drawString("Trade with ",1080,784);
        g.drawString("bank (4:1)",1085,807);
        g.drawString("Trade with ",1085,854);
        g.drawString("other players",1074,877);
        
        if (whichMenu==Menu.Steal)
        {
            g.setColor(selected);
            g.fillRect(500,350,180,40);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawRect(500,350,180,40);
            g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,30));
            g.drawString("Steal from",515,380);
            int front=600-plyrMenu.size()*75;
            for (int n=0;n<plyrMenu.size();n++)
            {
                g.setColor(plyrMenu.get(n).getColor());
                g.fillRect(front,400,120,100);
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(5));
                g.drawRect(front,400,120,100);
                
                g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,17));
                g.drawString(" Owns "+plyrMenu.get(n).getTotalRes(),front+15,440);
                g.drawString("resources",front+15,460);
                front+=150;
            }
        }
        else if (whichMenu==Menu.SelectTrader)
        {
            g.setColor(selected);
            g.fillRect(850,820,200,80);
//            g.setColor(new Color(0,0,0,100));
//            g.fillRect(850,740,200,160);
            int xSize=(180/plyrMenu.size());
            
            int px=860;
            for (int n=0;n<plyrMenu.size();n++)
            {
                g.setColor(plyrMenu.get(n).getColor());
                g.fillRect(px+3,835,xSize-6,50);
                px+=xSize;
            }
        }
        else if (whichMenu==Menu.Haggle)
        {
            g.setColor(Color.WHITE);
            g.fillRect(400,300,400,400);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(7));
            g.drawRect(400,300,400,400);
            g.setColor(plyrMenu.get(0).getColor());
            g.fillRect(420,315,360,80);
            g.setColor(plyrMenu.get(1).getColor());
            g.fillRect(420,605,360,80);
            g.setColor(new Color(0,0,0,100));
            g.fillRect(420,410,360,180);
            int px=440;
            g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,20));
            for (int n=0;n<5;n++)
            {
                g.setColor(Util.getResourceColor(n));
                g.fillOval(px,330,50,50);
                g.fillOval(px,415,50,50);
                g.fillOval(px,535,50,50);
                g.fillOval(px,615,50,50);
                g.setColor(Color.BLACK);
                g.drawString(""+plyrMenu.get(0).getResAmt(n),px+17,355);
                g.drawString(""+plyrMenu.get(2).getResAmt(n),px+17,450);
                g.drawString(""+plyrMenu.get(3).getResAmt(n),px+17,570);
                g.drawString(""+plyrMenu.get(1).getResAmt(n),px+17,645);
                px+=66;
            }
            g.setColor(Color.DARK_GRAY);
            g.fillRect(430,480,100,40);
            g.fillRect(550,480,100,40);
            g.fillRect(670,480,100,40);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawRect(430,480,100,40);
            g.drawRect(550,480,100,40);
            g.drawRect(670,480,100,40);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,25));
            g.drawString("Trade",450,510);
            g.drawString("Cancel",560,510);
            g.drawString("Clear",690,510);
        }
    }
}
