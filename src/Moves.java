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
    static long OCCUPIED;
    static long EMPTY;
    static long RankMasks8[] =/*from rank1 to rank8*/
            {
                    0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L
            };
    static long FileMasks8[] =/*from fileA to FileH*/
            {
                    0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
                    0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
            };
    static long DiagonalMasks8[] =/*from top left to bottom right*/
            {
                    0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
                    0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
                    0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
            };
    static long AntiDiagonalMasks8[] =/*from top right to bottom left*/
            {
                    0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
                    0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
                    0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
            };
    static long HAndVMoves(int s){
        long binaryS=1L<<s;
        long possibilitiesHorizontal = (OCCUPIED-2*binaryS)^Long.reverse(Long.reverse(OCCUPIED)-2*Long.reverse(binaryS));
        long possibilitiesVertical = ((OCCUPIED&FileMasks8[s%8])-(2*binaryS))^Long.reverse(Long.reverse(OCCUPIED&FileMasks8[s%8])-(2*Long.reverse(binaryS)));
        return (possibilitiesHorizontal&RankMasks8[s/8]) |  (possibilitiesVertical&FileMasks8[s%8]);
    }
    static long DAndAntiDMoves(int s){
        long binaryS = 1L<<s;
        long possibilitiesDiagonal = ((OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * Long.reverse(binaryS)));
        long possibilitiesAntiDiagonal = ((OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesDiagonal&DiagonalMasks8[(s / 8) + (s % 8)]) | (possibilitiesAntiDiagonal&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]);
    }
    public static String posibleMovesW(String history,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        NOT_WHITE_PIECES=~(WP|WN|WB|WR|WQ|WK|BK);//added BK to avoid illegal capture
        BLACK_PIECES=BP|BN|BB|BR|BQ;//omitted BK to avoid illegal capture
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        EMPTY=~OCCUPIED;
//        timeExperiment(WP);
        String list = possibleWP(history,WP,BP)/*+
                possibleWN(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK)*/+
                possibleWB(OCCUPIED,WB)+
                possibleWR(OCCUPIED,WR)+
                possibleWQ(OCCUPIED,WQ) /*+
                possibleWK(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK)*/;
        return list;
    }
    public static String possibleWP(String history,long WP,long BP){
        String list = "";
        //x1,y1,x2,y2
        long PAWN_MOVES = (WP>>7)&BLACK_PIECES&~RANK_8&~FILE_A;//capture right
        long possibility = PAWN_MOVES&~(PAWN_MOVES-1);
        while(possibility!=0){
            int index = Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1) +(index%8-1) + (index/8) + (index%8);
            PAWN_MOVES &=~possibility;
            possibility = PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES = (WP>>9)&BLACK_PIECES&~RANK_8&~FILE_H;//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8+1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>8)&EMPTY&~RANK_8;//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES = (WP>>16)&EMPTY&(EMPTY>>8)&RANK_4;//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+2)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2.Promotion Type,"P"
        PAWN_MOVES=(WP>>7)&BLACK_PIECES&RANK_8&~FILE_A;//Pawn promotion by right capture
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"QP"+(index%8-1)+(index%8)+"RP"+(index%8-1)+(index%8)+"BP"+(index%8-1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>9)&BLACK_PIECES&RANK_8&~FILE_H;//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"QP"+(index%8+1)+(index%8)+"RP"+(index%8+1)+(index%8)+"BP"+(index%8+1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>8)&EMPTY&RANK_8;//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"QP"+(index%8)+(index%8)+"RP"+(index%8)+(index%8)+"BP"+(index%8)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Space,"E
        //en passant
        if(history.length()>=4){    //1636
            if((history.charAt(history.length()-1)==history.charAt(history.length()-3)) && Math.abs(history.charAt(history.length()-2)-history.charAt(history.length()-4))==2){
                int eFile = history.charAt(history.length()-1)-'0';
                //en passant right
                possibility = (WP<<1)&BP&RANK_5&~FILE_A&FileMasks8[eFile];//shows piece to remove, not destination
                if(possibility!=0){
                    int index =Long.numberOfTrailingZeros(possibility);
                    list+=""+(index%8-1)+(index%8)+" E";
                }
                //en passant left
                possibility=(WP>>1)&BP&RANK_5&~FILE_H&FileMasks8[eFile];//shows piece to remove, not the destination
                if(possibility!=0){
                    int index = Long.numberOfTrailingZeros(possibility);
                    list+=""+(index%8+1) +(index%8)+" E";
                }
            }
        }
        return list;
    }
    public static String possibleWB(long OCCUPIED,long WB){
        String list = "";
        long i=WB&~(WB-1);
        long possibility;
        while(i!=0){
            int iLocation = Long.numberOfTrailingZeros(i);
            possibility=DAndAntiDMoves(iLocation)&NOT_WHITE_PIECES;
            long j=possibility&~(possibility-1);
            while(j!=0){
                int index = Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            WB&=~i;
            i=WB&~(WB-1);
        }
        return list;
    }
    public static String possibleWR(long OCCUPIED,long WR){
        String list="";
        long i=WR&~(WR-1);
        long possibility;
        while(i!=0){
            int iLocation = Long.numberOfTrailingZeros(i);
            possibility=HAndVMoves(iLocation)&NOT_WHITE_PIECES;
            long j=possibility&~(possibility-1);
            while(j!=0){
                int index = Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~possibility;
                j=possibility&~(possibility-1);
            }
            WR&=~i;
            i=WR&~(WR-1);
        }
        return list;
    }
    public static String possibleWQ(long OCCUPIED,long WQ){
        String list ="";
        long i = WQ&~(WQ-1);
        long possibility;
        while(i!=0){
            int iLocation = Long.numberOfTrailingZeros(i);
            possibility = (HAndVMoves(iLocation)|DAndAntiDMoves(iLocation)) & NOT_WHITE_PIECES;
            long j = possibility&~(possibility-1);
            while(j!=0){
                int index = Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            WQ&=~i;
            i=WQ&~(WQ-1);
        }
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
        for(int loop=0;loop<loopLength;loop++) {

        }
    }
    public static void tEMethodB(int loopLength,long WP){
        for(int loop=0;loop<loopLength;loop++) {

        }
    }
}
