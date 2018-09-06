import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class UserInterface extends JPanel {
    static long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L,EP=0L;
    static boolean CWK=true,CWQ=true,CBK=true,CBQ=true,WhiteToMove=true;//true=castle is possible
    static long UniversalWP=0L,UniversalWN=0L,UniversalWB=0L,UniversalWR=0L,UniversalWQ=0L,UniversalWK=0L,UniversalBP=0L,UniversalBN=0L,UniversalBB=0L,UniversalBR=0L,UniversalBQ=0L,UniversalBK=0L,UniversalEP=0L;
    static boolean UniversalCastleWK=true,UniversalCastleWQ=true,UniversalCastleBK=true,UniversalCastleBQ=true;//true=castle is possible
    static int humanIsWhite=1;
    static int rating=0;
    static int border=10;//the amount of empty space around the frame
    static double squareSize=64;//the size of a chess board square
    static JFrame javaF=new JFrame("Advance Chess Engine ");//must be declared as static so that other class' can repaint
    static UserInterface javaUI=new UserInterface();//must be declared as static so that other class' can repaint
    public static void main(String[] args) {
//        UCI.uciCommunication();
        javaF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        javaF.add(javaUI);
        javaF.setSize(757, 570);
        javaF.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-javaF.getWidth())/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height-javaF.getHeight())/2);
        javaF.setVisible(true);
        newGame();
//        BoardGeneration.importFEN("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1");
        //BoardGeneration.initiateStandardChess();
//        BoardGeneration.drawArray(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
//        Perft.perftRoot(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove,0);
//        System.out.println("Nodes: "+Perft.perftTotalMoveCounter);


        javaF.repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(200, 100, 0));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                squareSize=(double)(Math.min(getHeight(), getWidth()-200-border)-2*border)/8;
            }
        });
        drawBorders(g);
        drawBoard(g);
        drawPieces(g);
    }
    public void drawBoard(Graphics g) {
        for (int i=0;i<64;i+=2) {//draw chess board
            g.setColor(new Color(255, 200, 100));
            g.fillRect((int)((i%8+(i/8)%2)*squareSize)+border, (int)((i/8)*squareSize)+border, (int)squareSize, (int)squareSize);
            g.setColor(new Color(150, 50, 30));
            g.fillRect((int)(((i+1)%8-((i+1)/8)%2)*squareSize)+border, (int)(((i+1)/8)*squareSize)+border, (int)squareSize, (int)squareSize);
        }
    }
    public void drawPieces(Graphics g) {
        Image chessPieceImage;
        chessPieceImage=new ImageIcon("ChessPiecesHD.png").getImage();
        for (int i=0;i<64;i++) {
            int j=-1,k=-1;
            if (((WP>>i)&1)==1) {j=5; k=1-humanIsWhite;}
            else if (((BP>>i)&1)==1) {j=5; k=humanIsWhite;}
            else if (((WB>>i)&1)==1) {j=2;k=1-humanIsWhite;}
            else if (((BB>>i)&1)==1) {j=2;k=humanIsWhite;}
            else if (((WN>>i)&1)==1) {j=3;k=1-humanIsWhite;}
            else if (((BN>>i)&1)==1) {j=3;k=humanIsWhite;}
            else if (((WQ>>i)&1)==1) {j=1;k=1-humanIsWhite;}
            else if (((BQ>>i)&1)==1) {j=1;k=humanIsWhite;}
            else if (((WR>>i)&1)==1) {j=4;k=1-humanIsWhite;}
            else if (((BR>>i)&1)==1) {j=4;k=humanIsWhite;}
            else if (((WK>>i)&1)==1) {j=0;k=1-humanIsWhite;}
            else if (((BK>>i)&1)==1) {j=0;k=humanIsWhite;}
            if (j!=-1 && k!=-1) {
                g.drawImage(chessPieceImage, (int)((i%8)*squareSize)+border, (int)((i/8)*squareSize)+border, (int)((i%8+1)*squareSize)+border, (int)((i/8+1)*squareSize)+border, j * chessPieceImage.getWidth(this)/6, k * chessPieceImage.getHeight(this)/2, (j + 1) * chessPieceImage.getWidth(this)/6, (k + 1) * chessPieceImage.getHeight(this)/2, this);
            }
        }
    }
    public void drawBorders(Graphics g) {
        g.setColor(new Color(100, 0, 0));
        g.fill3DRect(0, border, border, (int)(8*squareSize), true);
        g.fill3DRect((int)(8*squareSize)+border, border, border, (int)(8*squareSize), true);
        g.fill3DRect(border, 0, (int)(8*squareSize), border, true);
        g.fill3DRect(border, (int)(8*squareSize)+border, (int)(8*squareSize), border, true);

        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, border, border, true);
        g.fill3DRect((int)(8*squareSize)+border, 0, border, border, true);
        g.fill3DRect(0, (int)(8*squareSize)+border, border, border, true);
        g.fill3DRect((int)(8*squareSize)+border, (int)(8*squareSize)+border, border, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, 0, border, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, (int)(8*squareSize)+border, border, border, true);

        g.setColor(new Color(0,100,0));
        g.fill3DRect((int)(8*squareSize)+2*border, 0, 200, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, border, border, (int)(8*squareSize), true);
        g.fill3DRect((int)(8*squareSize)+2*border, (int)(8*squareSize)+border, 200, border, true);
    }
    public static void newGame() {
        BoardGeneration.initiateStandardChess();
        CWK=true; CWQ = true; CBK=true; CBQ=true;
        EP=0;
        Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
    }
}