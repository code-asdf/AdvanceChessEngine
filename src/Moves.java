import java.util.Arrays;
import java.util.Scanner;

public class Moves {
    static long FILE_A=72340172838076673L;
    static long FILE_H=-9187201950435737472L;
    static long FILE_AB=217020518514230019L;
    static long FILE_GH=-4557430888798830400L;
    static long RANK_1=-72057594037927936L;
    static long RANK_4=1095216660480L;
    static long RANK_5=4278190080L;
    static long RANK_8=255L;
    static long CENTRE=103481868288L;
    static long EXTENDED_CENTRE=66229406269440L;
    static long KING_SIDE=-1085102592571150096L;
    static long QUEEN_SIDE=1085102592571150095L;
    static long KING_B7=460039L;
    static long KNIGHT_C6=43234889994L;
    static long NOT_WHITE_PIECES;
    static long BLACK_PIECES;
    static long EMPTY;
    public static String posibleMovesW(String history,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        NOT_WHITE_PIECES=~(WP|WN|WB|WR|WQ|WK|BK);//added BK to avoid illegal capture
        BLACK_PIECES=BP|BN|BB|BR|BQ;//omitted BK to avoid illegal capture
        EMPTY=~(WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK);
        timeExperiment(WP);
        String list = possiblePW(history,WP);
        // have to add funlist of other mmoves as well
        return list;
    }
    public static String possiblePW(String history,long WP){
        String list = "";
        //x1,y1,x2,y2
        long PAWN_MOVES = (WP>>7)&BLACK_PIECES&~RANK_8&~FILE_H;//capture right
        for(int i=Long.numberOfTrailingZeros(PAWN_MOVES);i<64-Long.numberOfLeadingZeros(PAWN_MOVES);i++){
            if(((PAWN_MOVES>>i)&1)==1){
                list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
            }
        }
        PAWN_MOVES = (WP>>9)&BLACK_PIECES&~RANK_8&~FILE_A;//capture left
        for (int i=Long.numberOfTrailingZeros(PAWN_MOVES);i<64-Long.numberOfLeadingZeros(PAWN_MOVES);i++){
            if(((PAWN_MOVES>>i)&1)==1){
                list+=""+(i/8+1)+(i%8+1)+(i/8)+(i%8);
            }
        }
        PAWN_MOVES=(WP>>8)&EMPTY&~RANK_8;//move 1 forward
        for(int i=Long.numberOfTrailingZeros(PAWN_MOVES);i<64-Long.numberOfLeadingZeros(PAWN_MOVES);i++){
            if(((PAWN_MOVES>>i)&1)==1){
                list+=""+(i/8+1)+(i%8)+(i/8)+(i%8);
            }
        }
        PAWN_MOVES = (WP>>16)&EMPTY&(EMPTY>>8)&RANK_4;//move 2 forward
        for(int i=Long.numberOfTrailingZeros(PAWN_MOVES);i<64-Long.numberOfLeadingZeros(PAWN_MOVES);i++){
            if(((PAWN_MOVES>>i)&1)==1){
                list+=""+(i/8+2)+(i%8)+(i/8)+(i%8);
            }
        }
        //y1,y2.Promotion Type,"P"
        PAWN_MOVES=(WP>>7)&BLACK_PIECES&RANK_8&~FILE_A;//Pawn promotion by right capture
        for (int i=Long.numberOfTrailingZeros(PAWN_MOVES);i<64-Long.numberOfLeadingZeros(PAWN_MOVES);i++) {
            if (((PAWN_MOVES>>i)&1)==1) {
                list+=""+(i%8-1)+(i%8)+"QP"+(i%8-1)+(i%8)+"RP"+(i%8-1)+(i%8)+"BP"+(i%8-1)+(i%8)+"NP";
            }
        }
        PAWN_MOVES=(WP>>9)&BLACK_PIECES&RANK_8&~FILE_H;//pawn promotion by capture left
        for (int i=Long.numberOfTrailingZeros(PAWN_MOVES);i<64-Long.numberOfLeadingZeros(PAWN_MOVES);i++) {
            if (((PAWN_MOVES>>i)&1)==1) {
                list+=""+(i%8+1)+(i%8)+"QP"+(i%8+1)+(i%8)+"RP"+(i%8+1)+(i%8)+"BP"+(i%8+1)+(i%8)+"NP";
            }
        }
        PAWN_MOVES=(WP>>8)&EMPTY&RANK_8;//pawn promotion by move 1 forward
        for (int i=Long.numberOfTrailingZeros(PAWN_MOVES);i<64-Long.numberOfLeadingZeros(PAWN_MOVES);i++) {
            if (((PAWN_MOVES>>i)&1)==1) {list+=""+(i%8)+(i%8)+"QP"+(i%8)+(i%8)+"RP"+(i%8)+(i%8)+"BP"+(i%8)+(i%8)+"NP";}
        }
        //y1,y2,Space,"E
        return list;
    }
    public static void drawBitboard(long bitBoard){
        String chessBoard[][] = new String[8][8];
        for(int i=0;i<64;i++){
            if(((bitBoard>>i)&1)==1){
                chessBoard[i/8][i%8]="P";
            }
            if("".equals(chessBoard[i/8][i%8])){
                chessBoard[i/8][i%8]=" ";
            }
        }
        for(int i=0;i<8;i++){
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
    public static void timeExperiment(long WP){
        int loopLength = 1000;
        long startTime = System.currentTimeMillis();
        tEMethodA(loopLength,WP);
        long endTime = System.currentTimeMillis();
        System.out.println("That took "+(endTime-startTime)+" milliseconds");
        startTime = System.currentTimeMillis();
        tEMethodB(loopLength,WP);
        endTime = System.currentTimeMillis();
        System.out.println("That took "+(endTime-startTime)+" milliseconds");
    }
    public static void tEMethodA(int loopLength,long WP){
        for(int loop=0;loop<loopLength;loop++){
            long PAWN_MOVES = (WP>>7)&BLACK_PIECES&~RANK_8&~FILE_A;//capture right
            String list="";
            for(int i=0;i<64;i++){
                if(((PAWN_MOVES>>i)&1)==1){
                    list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
                }
            }
        }
    }
    public static void tEMethodB(int loopLength,long WP){
        for(int loop=0;loop<loopLength;loop++){
            long PAWN_MOVES = (WP>>7)&BLACK_PIECES&~RANK_8&~FILE_A;//capture right
            String list="";
            long possibility = PAWN_MOVES&~(PAWN_MOVES-1);// gets the first pawn from the top or we can say the LS bit of pawn in bitboard
            while(possibility!=0){
                int index = Long.numberOfTrailingZeros(possibility);
                list+=""+(index/8+1)+(index%8-1)+(index/8)+(index%8);
                PAWN_MOVES&=~(possibility);
                possibility=PAWN_MOVES&~(PAWN_MOVES-1);
            }
        }
    }
}
