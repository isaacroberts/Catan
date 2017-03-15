/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Catan extends JFrame implements Runnable, MouseListener, KeyListener
{
    public enum Card
    {
        VicPoint(4,false),Monopoly(2,false),Roader(2,true),Resource(2,false),Knight(15,false);
        int amount;
        boolean immediate;
        Card(int amt,boolean now) {
            amount=amt;
            immediate=now;
        }
        static Image[] images=new Image[Card.values().length];
        public static void loadImages() {
            for (int n=0;n<values().length;n++)
            {
                try {
                    images[n]=Toolkit.getDefaultToolkit().getImage(Card.class.getResource(values()[n].name()+".png"));
                }
                catch (Exception e)
                {
                    System.out.println("Card image not gotten: "+values()[n].name());
                    e.printStackTrace();
                }
            }
        }
        public void draw(Graphics2D g,int x,int y)
        {
            g.drawImage(images[ordinal()],x,y,null);
        }
        public void draw(Graphics2D g,int x,int y,int amt)
        {
            draw(g,x,y);
            g.setColor(new Color(202,160,0,125));
            g.fillRect(x+128,y+78,18,14);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,10));
            g.drawString("x "+amt,x+130,y+90);
        }
    }
    Container con = getContentPane();
    Thread t = new Thread(this);
    Board brd;
    int dice[];
    Random rand;
    int nextClick;
    //    Win=-2
//        BeginScreen,//=-1
//        Normal,//0
//        PlaceRobber,//=1
//        BuildRoad,//=2
//        BuildHouse,//=3
//        Citify,//=4
//        SelectPlayerToTrade,//=7
//        Trading,//=8
//        SelectWhoToStealFrom,//=10
//        Select Monopoly //=12
//        GetFreeRoad//=13 & 14
//        SelectRes,// 100+Amount left to trade away
//        GetResource,//  =101-Amount left to get
    ArrayList<Card> deck;
    int curPlayer;
    int robbedPlayer;
    GUI gui;
    boolean starting;//starting is at first used for whether to make board random or beginner. afterwards
    //is used to tell whether this is the opening setup
    ArrayList<String> text;
    public Catan()
    {
        gui=new GUI();
        shuffleDeck();
        curPlayer=1;
        robbedPlayer=-1;
        rand=new Random();
        dice=new int[2];
        dice[0]=dice[1]=7;
        starting=true;
        nextClick=-1;
        text=new ArrayList<String>();
        brd=Util.brd=new Board();
        Hex.initImages();
        Card.loadImages();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(this);
        addKeyListener(this);
        t.start();
        repaint();
    }
    public void shuffleDeck()
    {
        deck=new ArrayList<Card>();
        Random rand=new Random();
        for (int type=0;type<Card.values().length;type++)
        {
            for (int n=0;n<Card.values()[type].amount;n++)
            {
                int pos=rand.nextInt(deck.size()+1);
                deck.add(pos,Card.values()[type]);
            }
        }
    }
    public void run()
    {
//        try{
//            while(true)
//            {
//                t.sleep(10);
//                repaint();
//            }
//        }
//        catch(Exception e){}
    }
    public void update()
    {
        
        curPlayer++;
        if (curPlayer==Player.amt())
            curPlayer=0;
        for (int n=0;n<Player.amt();n++)
        {
            Player.get(n).update();
        }
        if (Player.get(curPlayer).vicPt>=10)
        {
            nextClick=-2;
        }
        repaint();
    }
    public void update(Graphics g)
    {
        paint(g);
    }
    public void paint(Graphics gr)
    {
//        super.paint(gr);
        Image i=createImage(getSize().width, getSize().height);
        Graphics2D g2 = (Graphics2D)i.getGraphics();
        
        if (nextClick==-1)
        {
            for (int n=0;n<Catan.Card.values().length;n++)
            {
                try {
                    g2.drawImage(Card.images[n],500,400,null);
                }
                catch (Exception e)
                {
                    System.out.println("couldnt display "+Card.values()[n].name());
                    e.printStackTrace();
                }
            }
            g2.setColor(Color.BLUE);
            g2.fillRect(0,0,1200,1000);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,40));
            g2.drawString("Player Amount: "+(curPlayer+1),465,480);
            if   (starting)
                g2.drawString("Beginner Setup",475,570);
            else
                g2.drawString("Randomized Setup",450,570);
        }
        else if (nextClick==-2)
        {
            Color winner=Util.getPlayerColor(curPlayer);
            g2.setColor(winner);
            g2.fillRect(0,0,1200,1000);
            g2.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,100));
            if (curPlayer==8)
                g2.setColor(Color.BLACK);
            else
                g2.setColor(Color.WHITE);
//            g2.setColor(new Color(255-winner.getRed(),255-winner.getBlue(),255-winner.getGreen()));
            g2.drawString("Player "+(curPlayer+1)+" wins!",300,500);
        }
        else
        {
            brd.draw(g2,(starting && nextClick==0));
            if (!(starting && nextClick==0))
            {
                g2.setColor(Util.getPlayerColor(curPlayer));
                g2.fillRect(1010,90,1190,300);
                if (dice[0]==0)
                    g2.setColor(Util.getPlayerColor(nextPlayer()));
                g2.fillRect(0,0,150,150);

                if (nextClick==1)
                {
                    g2.setColor(new Color(105,105,105));
                    g2.fillRect(10,40,130,70);
                }
                g2.setColor(Color.WHITE);
                g2.fillRect(20,50,50,50);
                g2.fillRect(80,50,50,50);
                if (dice[0]!=0)
                {
                    g2.setColor(Color.BLACK);
                    g2.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,35));
                    g2.drawString(""+dice[0],32,85);
                    g2.drawString(""+dice[1],92,85);
                    if (robbedPlayer!=-1)
                        gui.draw(g2,Player.get(robbedPlayer));
                    else if (curPlayer!=-1)
                        gui.draw(g2,Player.get(curPlayer));
                }
            }
            if (text.size()>0)
            {
                g2.setFont(new Font("Kozuka Gothic Pro",Font.BOLD,25));
                int y=995-text.size()*30;
                int maxLen=0;
                for (int n=0;n<text.size();n++)
                {
                    if (text.get(n).length()>maxLen)
                        maxLen=text.get(n).length();
                }
                g2.setColor(new Color(200,200,200,200));
                g2.fillRect(0,y-35,maxLen*15+15,1035-y);
                g2.setColor(Color.BLACK);
                for (int n=0;n<text.size();n++)
                {
                    g2.drawString(text.get(n),15,y);
                    y+=30;
                }
                text.clear();
            }
        }
        g2.dispose();
        gr.drawImage(i, 0, 0, this);
    }
    public void keyPressed(KeyEvent k)
    {
        if (k.getKeyCode()==KeyEvent.VK_ESCAPE)
            System.exit(0);
        if (nextClick==-1)
        {
            if (k.getKeyCode()==KeyEvent.VK_RIGHT)
            {
                curPlayer++;
                if (curPlayer>8)
                    curPlayer=8;
                repaint();
            }
            else if (k.getKeyCode()==KeyEvent.VK_LEFT)
            {
                curPlayer--;
                if (curPlayer<1)
                    curPlayer=1;
                repaint();
            }
            else if (k.getKeyCode()==KeyEvent.VK_UP || k.getKeyCode()==KeyEvent.VK_DOWN)
            {
                starting=!starting;
                repaint();
            }
            else if (k.getKeyCode()==KeyEvent.VK_ENTER)
            {
                Player.onStart(curPlayer);
                curPlayer=0;
                brd.setup(!starting);
                nextClick=0;
                text.add("Click the screen.");
                starting=true;
                repaint();
            }
            return;
        }
        else if (!starting&& nextClick!=1)
        {
//            if (!(nextClick>=100))
//            {
//                if (k.getKeyCode()==KeyEvent.VK_UP)
//                {
//                    selPlayer--;
//                    if (selPlayer<0)
//                        selPlayer=Player.amt()-1;
//                    nextClick=0;
//                    gui.clearPress();
//                    repaint();
//                }
//                else if (k.getKeyCode()==KeyEvent.VK_DOWN)
//                {
//                    selPlayer++;
//                    if (selPlayer>=Player.amt())
//                        selPlayer=0;
//                    nextClick=0;
//                    gui.clearPress();
//                    repaint();
//                }
//            }
        }
    }
    public void mousePressed(MouseEvent mo)
    {
//        System.out.println("clicked "+nextClick);
        if (nextClick==-1)
        {
            if ((new Rectangle(450,525,350,80)).contains(mo.getPoint()))
            {
                starting=!starting;
                repaint();
            }
        }
        else if (robbedPlayer!=-1)
        {
            if (nextClick>100&&nextClick<=200)
            {//trading with bank (removing resources)
                int selRes=gui.getResButton(mo.getPoint());
                if (selRes!=-1)
                {
                    if (Player.get(robbedPlayer).getResAmt(selRes)>0)
                    {
                        Player.get(robbedPlayer).takeRes(selRes);
                        nextClick--;
                        if (nextClick==100)
                        {
                            setRobbedPlayer();
                        }
                        else
                            text.add("Select "+(nextClick-100)+" resources to lose to the missionaries.");
                        repaint();
                    }
                }
            }
            else
            {
                setRobbedPlayer();
            }
        }
        else if (!starting &&
                nextClick==0 && curPlayer!=-1)
//                nextClick!=1 && nextClick!=8)
        {
            if (mo.getButton()==MouseEvent.BUTTON3)
            {
                nextClick=0;
                gui.clearPress();
                repaint();
                return;
            }
            int button=gui.getButton(mo.getPoint());
            boolean ret=true;
            if ((nextClick==gui.getButton(mo.getPoint())+2 )
               ||(nextClick==104&&gui.getButton(mo.getPoint())==4))
            {
                nextClick=0;
                gui.clearPress();
                repaint();
                return;
            }
            switch (button)
            {
                case -1:
                    ret=false;
                    break;
                case 0:
                    nextClick=2;
                    break;
                case 1:
                    nextClick=3;
                    break;
                case 2:
                    nextClick=4;
                    break;
                case 3:
                    if (deck.size()>0)
                    {
                        int[] res={0,1,1,0,1};
                        if (Player.get(curPlayer).subtractRes(res))
                        {
                            handleCard(deck.get(0));
                            deck.remove(0);
                        }
                        else
                        {
                            text.add("You don't have enough resources.");
                            nextClick=0;
                        }
                    }
                    else
                    {
                        text.add("There are no more development cards.");
                        nextClick=0;
                    }
                    gui.clearPress();
                    break;
                case 4://bank trading
                    if (Player.get(curPlayer).getTotalRes()>=4)
                    {
                        text.add("Select 4 resources to trade.");
                        nextClick=104;
                    }
                    else
                    {
                        nextClick=0;
                        gui.clearPress();
                        text.add("You don't have 4 resources to trade!");
                        repaint();
                    }
                    break;
                case 5:
                    //trade with others
                    nextClick=7;
                    gui.showTradeMenu(Player.get(curPlayer), Player.players);
                    repaint();
                    break;
                case 6:     case 7:
                    Player.get(curPlayer).cycleCard(button==7);
                    nextClick=0;
                    gui.clearPress();
                    repaint();
                    break;
                case 8:
                    Card clicked=Player.get(curPlayer).getShownCard();
                    Player.get(curPlayer).removeShownCard();
                    doCard(clicked,curPlayer);
                    break;
                default:
                    System.out.println("Error in getButton(Point) :returned unexpected "+button);
                    break;
            }
            if (ret)
            {
                repaint();
                return;
            }
        }
        else if (starting && nextClick==0)
        {
            nextClick=3;
            gui.press(1);
            text.add("Build a settlement.");
            repaint();
            return;
        }
        if (nextClick==1)
        {//place robber
            int clkHex=brd.getHex(mo.getPoint());
            if (clkHex!=-1)
            { 
                if (brd.moveRobber(clkHex))
                {
                    ArrayList<Player> victims=new ArrayList<Player>();
                    for (int n=0;n<Player.amt();n++)
                    {
                        if (n!=curPlayer)
                        {
                            for (int m=0;m<Player.get(n).getSettltmtAmt();m++)
                            {
                                Hex[] area=Player.get(n).getSettlements().get(m).getArea();
                                for (int a=0;a<area.length;a++)
                                {
                                    if (brd.getHex(clkHex).equals(area[a]))
                                    {
                                        victims.add(Player.get(n));
                                        a=Player.get(n).getSettlements().get(m).getArea().length;
                                        m=Player.get(n).getSettltmtAmt();
                                    }
                                }
                            }
                        }
                    }
                    if (victims.size()>0)
                    {
                        gui.showStealMenu(victims,Player.get(curPlayer));
                        nextClick=10;
                    }
                    else
                        nextClick=0;
                    repaint();
                }
            }
        }
        else if (nextClick==10)
        {
            Player victim=gui.getStealClick(mo.getPoint());
            if (victim!=null)
            {
                char res=victim.takeRes();
                if (res!='0')
                {
                    Player.get(curPlayer).donate(res);
                    text.add("Got one "+Util.getResourceName(res));
                }
                nextClick=0;
                gui.clearPress();
            }
            else
                text.add("Select a real person.");
            repaint();
        }
        else if (nextClick==2)
        {//build road
            if (!starting && (mo.getButton()==MouseEvent.BUTTON3 || gui.getButton(mo.getPoint())==0))
            {
                nextClick=0;
                gui.clearPress();
                repaint();
                return;
            }
            int rd=brd.getRoad(mo.getPoint());
            if (rd!=-1)
            {
                if (brd.getRoad(rd).ownedBy()==-1)
                {
                    if (brd.getRoad(rd).contactWith(curPlayer))
                    {
                        int[] res={1,0,0,1,0};
                        if (true)
                        if (Player.get(curPlayer).subtractRes(res))
                        {
                            if (Player.get(curPlayer).addRoad(brd.getRoad(rd)))
                                text.add("You get longest road");
                            nextClick=0;
                            gui.clearPress();
                            if (starting)
                            {
                                nextClick=3;
                                gui.press(1);
                                if (Player.get(Player.amt()-1).getSettltmtAmt()>1)
                                    curPlayer--;
                                else
                                    curPlayer++;
                                if (curPlayer==Player.amt() || curPlayer<0)
                                {
                                    if (Player.get(0).getSettltmtAmt()==2)
                                    {
                                        nextClick=0;
                                        starting=false;
                                        gui.clearPress();
                                        curPlayer=-1;
                                        text.add("Roll the dice.");
                                    }
                                    else
                                    {
                                        text.add("Build a settlement.");
                                        curPlayer=Player.amt()-1;
                                    }
                                }
                                else
                                    text.add("Build a settlement.");
                            }
                        }
                        else
                        {
                            text.add("Not enough resources.");
                            nextClick=0;
                            gui.clearPress();
                        }
                        repaint();
                    }
                }
            }
        }
        else if (nextClick==13 || nextClick==14)
        {//free road
            if (mo.getButton()==MouseEvent.BUTTON3)
            {
                nextClick=0;
                gui.clearPress();
                repaint();
                return;
            }
            int rd=brd.getRoad(mo.getPoint());
            if (rd!=-1)
            {
                if (brd.getRoad(rd).ownedBy()==-1)
                {
                    if (brd.getRoad(rd).contactWith(curPlayer))
                    {
                        if (Player.get(curPlayer).addRoad(brd.getRoad(rd)))
                            text.add("You get longest road.");
                        if (nextClick==13)
                        {
                            text.add("Build a second free road.");
                            nextClick=14;
                        }
                        else
                        {
                            nextClick=0;
                            gui.clearPress();
                        }
                        repaint();
                    }
                }
            }
        }
        else if (nextClick==3)
        {//build settlement
            if (!starting && mo.getButton()==MouseEvent.BUTTON3 || gui.getButton(mo.getPoint())==1)
            {
                nextClick=0;
                gui.clearPress();
                repaint();
                return;
            }
            int setlm=brd.getCorner(mo.getPoint());
            if (setlm!=-1)
            {
                if (brd.getSettlement(setlm).ownedBy()==-1)
                {
                    if (starting || brd.getSettlement(setlm).isConnected(curPlayer))
                    {
                        if (!brd.getSettlement(setlm).oneAway())
                        {
                            int[] res={1,1,1,1,0};
                            if (Player.get(curPlayer).subtractRes(res))
                            {
                                Player.get(curPlayer).giveSettlmt(brd.getSettlement(setlm));
                                nextClick=0;
                                gui.clearPress();
                                if (starting)
                                {
                                    Hex[] area=Player.get(curPlayer).getSettlements().get(Player.get(curPlayer).getSettltmtAmt()-1).getArea();
                                    for (int n=0;n<area.length;n++)
                                    {
                                        if (area[n].getType()!='d')
                                            Player.get(curPlayer).donate(area[n].getType());
                                    }
                                    nextClick=2;
                                    text.add("Now build a road.");
                                    gui.press(0);
                                }
                            }
                            else
                            {
                                text.add("Not enough resources.");
                                nextClick=0;
                                gui.clearPress();
                            }
                        }
                        else
                        {
                            text.add("Too close to other settlement.");
                        }
                    }
                    else
                        text.add("Corner not connected by road.");
                    repaint();
                }
            }
        }
        else if (nextClick==4)
        {//citify
            if (mo.getButton()==MouseEvent.BUTTON3 || gui.getButton(mo.getPoint())==2)
            {
                nextClick=0;
                gui.clearPress();
                repaint();
                return;
            }
            int setlm=brd.getCorner(mo.getPoint());
            if (setlm!=-1)
            {
                if (brd.getSettlement(setlm).ownedBy()==curPlayer)
                {
                    int[] res={0,2,0,0,3};
                    if (Player.get(curPlayer).subtractRes(res))
                    {
                        brd.getSettlement(setlm).citify();
                        Player.get(curPlayer).addCity();
                        gui.clearPress();
                        nextClick=0;
                    }
                    else 
                        text.add("Not enough resources.");
                    repaint();
                }
            }
        }
        else if (nextClick>100&&nextClick<=200)
        {//trading with bank (removing resources)
            if (robbedPlayer==-1)
            {
                if (mo.getButton()==MouseEvent.BUTTON3 || gui.getButton(mo.getPoint())==4)
                {
                    nextClick=0;
                    gui.clearPress();
                    repaint();
                    return;
                }
                int selRes=gui.getResButton(mo.getPoint());
                if (selRes!=-1)
                {
                    if (Player.get(curPlayer).getResAmt(selRes)>0)
                    {
                        Player.get(curPlayer).takeRes(selRes);
                        nextClick--;
                        if (nextClick==100)
                            text.add("Select one resource to gain.");
                        else
                            text.add("Select "+(nextClick-100)+" resources to trade.");
                        repaint();
                    }
                }
            }
        }
        else if (nextClick<=100 && nextClick>90)
        {//getting resource from bank
            if (robbedPlayer==-1)
            {
                int selRes=gui.getResButton(mo.getPoint());
                if (selRes!=-1)
                {
                    Player.get(curPlayer).donate(selRes);
                    if (nextClick==100)
                    {
                        nextClick=0;
                        gui.clearPress();
                    }
                    else
                    {
                        nextClick++;
                        text.add("Select "+(101-nextClick)+" resources to gain.");
                    }
                    repaint();
                }
            }
        }
        else if (nextClick==7)
        {//decide who to trade with
            if (mo.getButton()==MouseEvent.BUTTON3 || gui.getButton(mo.getPoint())==5)
            {
                nextClick=0;
                gui.clearPress();
                repaint();
                return;
            }
            int selTrader=gui.getTradeClick(mo.getPoint());
            if (selTrader!=-1)
            {
                gui.clearPress();
                gui.showTradeScreen(Player.get(curPlayer),Player.get(selTrader));
                repaint();
                nextClick=8;
            }
        }
        else if (nextClick==8)
        {//Trade Screen
            if (gui.clickTradeScreen(mo.getPoint()))
            {
                nextClick=0;
                gui.clearPress();
            }
            repaint();
        }
        else if (nextClick==12)
        {//Monopolize
            int selRes=gui.getResButton(mo.getPoint());
            int total=0;
            if (selRes!=-1)
            {
                for (int n=0;n<Player.amt();n++)
                {
                    if (n!=curPlayer)
                    {
                        int donation=Player.get(n).getResAmt(selRes);
                        total+=donation;
                        Player.get(n).clearHand(selRes);
                        Player.get(curPlayer).donate(selRes,donation);
                    }
                }
                nextClick=0;
                gui.clearPress();
                text.add("You gain "+total+" "+Util.ixToName(selRes)+".");
                if (total==0)
                    text.add("lol nerd");
                else
                    text.add("You have all the "+Util.ixToName(selRes)+". Do you feel satisfied?");
                repaint();
            }
        }
        else if (mo.getPoint().x<=150&&mo.getPoint().y<=150)//the dice rectangle
        {
            dice[0]=dice[1]=0;
            repaint();
        }
        else if (brd.getHarbor(mo.getPoint())!=-1)
        {//trade with harbors
            int hrbr=brd.getHarbor(mo.getPoint());
            if (brd.getHarbor(hrbr).contactWith(curPlayer))
            {
                char type=brd.getHarbor(hrbr).getType();
                if (type=='?')
                {
                    text.add("Select any 3 resources to trade for one resource.");
                    nextClick=103;
                }
                else
                {
                    if (Player.get(curPlayer).getResAmt(type)>=2)
                    {
                        Player.get(curPlayer).takeRes(type);
                        Player.get(curPlayer).takeRes(type);
                        text.add("You pay 2 "+Util.getResourceName(type)+". Select one of any resource to gain.");
                        nextClick=100;
                    }
                    else
                    {
                        text.add("You don't have enough of that resource to trade away.");
                        nextClick=0;
                        gui.clearPress();
                    }
                }
            }
            else
                text.add("You don't have contact with that harbor.");
            repaint();
        }
    }
    public void handleCard(Card card)
    {
        if (card.immediate)
        {
            doCard(card,curPlayer);
        }
        else
        {
            Player.get(curPlayer).addDevCard(card);
            text.add("You get a "+card.name());
            repaint();
        }
    }
    public void doCard(Card card,int who)
    {
        gui.clearPress();
        if (card.equals(Card.VicPoint))
        {
            Player.get(who).vicPt++;
            text.add("You get one victory point.");
        }
        else if (card.equals(Card.Monopoly))
        {
            nextClick=12;
            text.add("Select a resource to monopolize on your resource bar.");
        }
        else if (card.equals(Card.Resource))
        {
            nextClick=99;
            text.add("Select two resources to gain.");
        }
        else if (card.equals(Card.Roader))
        {
            nextClick=13;
            text.add("Build two free roads.");
        }
        else if (card.equals(Card.Knight))
        {
            nextClick=1;
            Player.get(who).knightAmt++;
            if (Player.checkLargestArmy(Player.get(who)))
                text.add("You get largest army.");
            text.add("Relocate the missionaries.");
        }
        else return;
        repaint();
    }
    
    public void setRobbedPlayer() {
        int initPlayer=curPlayer;
        if (curPlayer==-1)
            initPlayer=0;
        do {//do while so that when it comes in with curPlayer=robbedPlayer to start the process, it wont end it immediately
            robbedPlayer++;
            if (robbedPlayer>=Player.amt())
                robbedPlayer=0;
        } while (initPlayer!=robbedPlayer && Player.get(robbedPlayer).getTotalRes()<=7);
        if (robbedPlayer==initPlayer)
        {
            text.add("Relocate the missionaries");
            nextClick=1;
            robbedPlayer=-1;
        }
        else
        {
            nextClick=100+Player.get(robbedPlayer).getTotalRes()/2;
            text.add("Select "+(nextClick-100)+" resources to be requisitioned by the missionaries.");
        }
        repaint();
    }
    public void mouseReleased(MouseEvent mo)
    {
        if (dice[0]==0)
        {
            dice[0]=rand.nextInt(6)+1;
            dice[1]=rand.nextInt(6)+1;
            int tot=dice[0]+dice[1];
            brd.highlight(tot);
            if (tot==7)
            {
                for (int n=0;n<Player.amt();n++)
                {
                    if (Player.get(n).getTotalRes()>=7)
                    {
                        nextClick=10000+n*1000+(Player.get(n).getTotalRes()/2);
                    }
                }
                robbedPlayer=curPlayer;
                setRobbedPlayer();
                repaint();
            }
            update();
        }
    }
    public int nextPlayer() {
        int next=curPlayer+1;
        if (next>=Player.amt())
            next=0;
        return next;
    }
    public static void main(String[] args)
    {
        Catan frame = new Catan();
        frame.setSize(1200,1000);
        frame.setVisible(true);
    }
    
    public void keyReleased(KeyEvent k) {}
    public void keyTyped(KeyEvent k) {}
    public void mouseClicked(MouseEvent mo)
    {}
    public void mouseEntered(MouseEvent mo)
    {}
    public void mouseExited(MouseEvent mo)
    {}
}