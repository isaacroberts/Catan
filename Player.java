/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.util.*;
import java.awt.*;


public class Player 
{
    static ArrayList<Road> longestRoad;
    static Player longestPlayer=null;
    static Player largestArmy=null;
    static int largeArmySize=2;
    static ArrayList<Player> players;
    public static void onStart(int amt)
    {
        longestRoad=new ArrayList<Road>();
        players=new ArrayList<Player>();
        for (int n=0;n<=amt;n++)
            players.add(new Player(players.size()));
    }
    public static Player get(int n) {
        return players.get(n);
    }
    public static int amt() {
        return players.size();
    }
    public static boolean checkLargestArmy(Player who) {
        if (who.knightAmt>largeArmySize)
        {
            largeArmySize=who.knightAmt;
            if (!who.equals(largestArmy))
            {
                if (largestArmy!=null)
                    largestArmy.vicPt-=2;
                largestArmy=who;
                largestArmy.vicPt+=2;
                return true;
            }
        }
        return false;
    }
    static class CardAmt {
        int amt;
        Catan.Card card;
        public CardAmt(Catan.Card setCard,int setAmt) {
            card=setCard;
            amt=setAmt;
        }
    }
    ArrayList<Settlement> cities=new ArrayList<Settlement>();
    int[] hand;
    int which;
    Color color;
    int vicPt;
    private ArrayList<CardAmt> devCards;
    private int cardShown=0;
    int knightAmt=0;
    public Player()
    {
        hand=new int[5];
        devCards=new ArrayList<CardAmt>();
        which=-1;
        color=Color.BLACK;
    }
    public Player(int id)
    {
        hand=new int[5];
        hand[0]=hand[3]=4;
        hand[1]=hand[2]=2;
        devCards=new ArrayList<CardAmt>();
        which=id;
        color=Util.getPlayerColor(id);
        vicPt=0;
    }
    public int getVP() {
        return vicPt;
    }
    public int getID() {
        return which;
    }
    public boolean subtractRes(int[] res)
    {
        for (int n=0;n<hand.length;n++)
        {
            if (res[n]>hand[n])
                return false;
        }
        for (int n=0;n<hand.length;n++)
        {
            hand[n]-=res[n];
        }
        return true;
    }
    public boolean addRoad(Road road)
    {
        road.player=which;
        ArrayList<Road> longest=road.getLongest();
        if (longest.size()>=5)
        {
            if (longest.size()>longestRoad.size())
            {
                longestRoad=longest;
                if (!equals(longestPlayer))
                {
                    if (longestPlayer!=null)
                        longestPlayer.vicPt-=2;
                    vicPt+=2;
                    longestPlayer=this;
                    return true;
                }
            }
        }
        return false;
    }
    public void giveSettlmt(Settlement given)
    {
        cities.add(given);
        given.giveToPlayer(which);
        vicPt++;
        for (int n=0;n<longestRoad.size();n++)
        {
            if (longestRoad.get(n).from.equals(given) || longestRoad.get(n).to.equals(given))
            {
                System.out.println("breaking longest road");
                findLongestRoad();
                break;
            }
        }
    }
    public static void findLongestRoad()
    {
        System.out.println("ogod findLongestRoad() called");
        longestRoad=new ArrayList<Road>();
        boolean tie=false;
        int player=-1;
        for (int n=0;n<Util.brd.road.length;n++)
        {
            if (Util.brd.road[n].ownedBy()!=-1)
            {
                ArrayList<Road> road=Util.brd.road[n].getLongest();
                if (road.size()>=5)
                {
                    if (road.size()>longestRoad.size())
                    {
                        longestRoad=road;
                        tie=false;
                        player=road.get(0).player;
                    }
                    else if (road.size()==longestRoad.size())
                    {
                        if (player!=road.get(0).player)
                            tie=true;
                    }
                }
            }
            
        }
        longestPlayer=null;
        if (!tie && longestRoad.size()>0)
        {
            for (int n=0;n<Player.amt();n++)
            {
                if (Player.get(n).which==player)
                {
                    if (longestPlayer!=null)
                        longestPlayer.vicPt-=2;
                    longestPlayer=Player.get(n);
                    longestPlayer.vicPt+=2;
                    break;
                }
            }
        }
    }
    public void removeDevCard(int which)
    {
        devCards.get(which).amt--;
        if (devCards.get(which).amt<=0)
        {
            if (cardShown>which)
                cardShown--;
            devCards.remove(which);
        }
    }
    public void addDevCard(Catan.Card what) {
        for (int n=0;n<devCards.size();n++)
        {
            if (devCards.get(n).card.equals(what))
            {
                devCards.get(n).amt++;
                return;
            }
        }
        devCards.add(new CardAmt(what,1));
    }
    public void drawShownCard(Graphics2D g,int x,int y)
    {
        devCards.get(cardShown).card.draw(g, x, y,devCards.get(cardShown).amt);
    }
    public int devCardAmt() {
        return devCards.size();
    }
    public int cardShown() {
        return cardShown;
    }
    public void cycleCard(boolean forward)
    {
        if (forward)
        {
            cardShown++;
            if (cardShown>=devCards.size())
                cardShown=0;
        }
        else
        {
            cardShown--;
            if (cardShown<0)
                cardShown=devCards.size()-1;
        }
    }
    public Catan.Card getShownCard() {
        return devCards.get(cardShown).card;
    }
    public void removeShownCard() {
        removeDevCard(cardShown);
        cardShown=0;
    }
    public void addCity()
    {
        vicPt++;
    }
    public ArrayList<Settlement> getSettlements() {
        return cities;
    }
    public int getSettltmtAmt() {
        return cities.size();
    }
    public void update()
    {
        for (int n=0;n<cities.size();n++)
        {
            int[] get=cities.get(n).getResources();
            for (int m=0;m<5;m++)
                hand[m]+=get[m];
        }
    }
    public Color getColor()
    {
        return color;
    }
    public void donate(int type,int amt)
    {
        hand[type]+=amt;
    }
    public void donate(int type) {
        hand[type]++;
    }
    public void donate(char type) {
        hand[Util.typeToIx(type)]++;
    }
    public void takeRes(char which)
    {
        takeRes(Util.typeToIx(which));
    }
    public void takeRes(int which)
    {
        hand[which]--;
    }
    public boolean takeRes(int which,int amt)
    {
        if (amt<=hand[which])
        {
            hand[which]-=amt;
            return true;
        }
        else return false;
    }
    public char takeRes()
    {
        if (getTotalRes()==0)
        {
            return '0';
        }
        Random rand=new Random();
        int r=rand.nextInt(5);
        while (hand[r]==0)
        {
            r=rand.nextInt(5);
        }
        hand[r]--;
        return Util.ixToType(r);
    }
    public void clearHand()
    {
        for (int n=0;n<hand.length;n++)
        {
            hand[n]=0;
        }
    }
    public void clearHand(int type)
    {
        hand[type]=0;
    }
    public int getResAmt(char type)
    {
        return hand[Util.typeToIx(type)];
    }
    public int getResAmt(int which)
    {
        return hand[which];
    }
    public int getTotalRes()
    {
        int res=0;
        for (int r=0;r<5;r++)
        {
            res+=hand[r];
        }
        return res;
    }
}
