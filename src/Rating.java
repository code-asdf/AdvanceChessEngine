import com.sun.org.apache.xpath.internal.operations.Or;

public class Rating {

    static int pawnBoard[][]={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
            { 0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            { 5,  5, 10, 25, 25, 10,  5,  5},
            { 0,  0,  0, 20, 20,  0,  0,  0},
            { 5, -5,-10,  0,  0,-10, -5,  5},
            { 5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][]={
            { 0,  0,  0,  0,  0,  0,  0,  0},
            { 5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][]={
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][]={
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][]={
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][]={
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            { 20, 20,  0,  0,  0,  0, 20, 20},
            { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int kingEndBoard[][]={
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}};
    private final static int CHECK_MATE_BONUS = Orion.MATE_SCORE;
    private final static int CHECK_BONUS = 50;
    private final static int CASTLE_BONUS = 60;
    private final static int MOBILITY_MULTIPLIER = 2;
    private final static int ATTACK_MULTIPLIER = 2;
    private final static int TWO_BISHOPS_BONUS = 50;
    private final static int king_val = 20000;
    private final static int queen_val = 900;
    private final static int rook_val = 500;
    private final static int bishop_val = 350;
    private final static int knight_val = 320;
    private final static int pawn_val = 100;

    public static int evaluate(final int depth,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ){
        return score(Orion.WhiteToMove,depth,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ)
        - score(!Orion.WhiteToMove,depth,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
    }
    private static int score(boolean player,int depth,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ){
        String move;
        if(player)
            move = Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        else
            move= Moves.possibleMovesB(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);

        return  mobility(player,move) +
                kingThreats(player,depth) +
                attacks(player,move,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ) +
                castle(player) +
                pieceEvaluations(player,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK) +
                pawnStructure(player);
    }

    private static int attacks(final boolean player,String moves,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ){
        //white or black should matter
        int attackScore = 0;
        for (int i=0;i<moves.length();i+=4) {
            long WPt = Moves.makeMove(WP, moves.substring(i, i + 4), 'P'), WNt = Moves.makeMove(WN, moves.substring(i, i + 4), 'N'),
                    WBt = Moves.makeMove(WB, moves.substring(i, i + 4), 'B'), WRt = Moves.makeMove(WR, moves.substring(i, i + 4), 'R'),
                    WQt = Moves.makeMove(WQ, moves.substring(i, i + 4), 'Q'), WKt = Moves.makeMove(WK, moves.substring(i, i + 4), 'K'),
                    BPt = Moves.makeMove(BP, moves.substring(i, i + 4), 'p'), BNt = Moves.makeMove(BN, moves.substring(i, i + 4), 'n'),
                    BBt = Moves.makeMove(BB, moves.substring(i, i + 4), 'b'), BRt = Moves.makeMove(BR, moves.substring(i, i + 4), 'r'),
                    BQt = Moves.makeMove(BQ, moves.substring(i, i + 4), 'q'), BKt = Moves.makeMove(BK, moves.substring(i, i + 4), 'k'),
                    EPt = Moves.makeMoveEP(WP | BP, moves.substring(i, i + 4));
            WRt = Moves.makeMoveCastle(WRt, WK | BK, moves.substring(i, i + 4), 'R');
            BRt = Moves.makeMoveCastle(BRt, WK | BK, moves.substring(i, i + 4), 'r');
            boolean CWKt = CWK, CWQt = CWQ, CBKt = CBK, CBQt = CBQ;
            if (Character.isDigit(moves.charAt(i + 3))) {//'regular' move
                int start = (Character.getNumericValue(moves.charAt(i)) * 8) + (Character.getNumericValue(moves.charAt(i + 1)));
                if (((1L << start) & WK) != 0) {
                    CWKt = false;
                    CWQt = false;
                } else if (((1L << start) & BK) != 0) {
                    CBKt = false;
                    CBQt = false;
                } else if (((1L << start) & WR & (1L << 63)) != 0) {
                    CWKt = false;
                } else if (((1L << start) & WR & (1L << 56)) != 0) {
                    CWQt = false;
                } else if (((1L << start) & BR & (1L << 7)) != 0) {
                    CBKt = false;
                } else if (((1L << start) & BR & 1L) != 0) {
                    CBQt = false;
                }
            }
            if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && player) ||
                    ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !player)) {
                //legal move
                int moved = getMovedPiece(WP,WN,WB,WR,WQ,WK,WPt,WNt,WBt,WRt,WQt,WKt);
                int attacked = getMovedPiece(BP,BN,BB,BR,BQ,BK,BPt,BNt,BBt,BRt,BQt,BKt);
                if(moved<=attacked)
                    attackScore++;
            }

        }

        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int getMovedPiece(long WP,long WN,long WB,long WR,long WQ,long WK,long WPt,long WNt,long WBt,long WRt,long WQt,long WKt){
        if(WP!=WPt){
            return pawn_val;
        }else if(WN!=WNt){
            return knight_val;
        }else if(WB!=WBt){
            return bishop_val;
        }else if(WR!=WRt){
            return rook_val;
        }else if(WQ!=WQt){
            return queen_val;
        }else if(WK!=WKt){
            return king_val;
        }
        return 0;
    }

    private static int pieceEvaluations(boolean player,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        long P,N,B,R,Q,K;
        if (player) {
            P=WP;N=WN;B=WB;R=WR;Q=WQ;K=WK;
        }else{
            P=BP;N=BN;B=BB;R=BR;Q=BQ;K=BK;
        }

        int pieceValuationScore = 0;
        int material = 0;
        int numBishops = 0;
        //pawn
        long piece = P&~(P-1);
        while(piece!=0){
            material+=pawn_val;
            int index = Long.numberOfTrailingZeros(piece);
            pieceValuationScore+=pawn_val + pawnBoard[index/8][index%8];
            P&=~piece;
            piece = P&~(P-1);
        }
        //knight

        piece = N&~(N-1);
        while(piece!=0){
            material+=knight_val;
            int index = Long.numberOfTrailingZeros(piece);
            pieceValuationScore+=knight_val + knightBoard[index/8][index%8];
            N&=~piece;
            piece = N&~(N-1);
        }
        //Bishop

        piece = B&~(B-1);
        while(piece!=0){
            material+=bishop_val;
            int index = Long.numberOfTrailingZeros(piece);
            pieceValuationScore+=bishop_val+bishopBoard[index/8][index%8];
            numBishops++;
            B&=~piece;
            piece = B&~(B-1);
        }
        // Rook
        piece = R&~(R-1);
        while(piece!=0){
            material+=rook_val;
            int index = Long.numberOfTrailingZeros(piece);
            pieceValuationScore+=rook_val+rookBoard[index/8][index%8];
            R&=~piece;
            piece = R&~(R-1);
        }

        //Queen
        piece = Q&~(Q-1);
        while(piece!=0){
            material+=queen_val;
            int index = Long.numberOfTrailingZeros(piece);
            pieceValuationScore+=queen_val+queenBoard[index/8][index%8];
            Q&=~piece;
            piece = Q&~(Q-1);
        }
        //King
        piece = K&~(K-1);
        while(piece!=0){
            int index = Long.numberOfTrailingZeros(piece);
            pieceValuationScore+=king_val;
            if(material>=1750){
                pieceValuationScore+=kingMidBoard[index/8][index%8];
            }else{
                pieceValuationScore+=kingEndBoard[index/8][index%8];
            }
            K&=~piece;
            piece = K&~(K-1);
        }

        return pieceValuationScore + (numBishops>=2 ? TWO_BISHOPS_BONUS : 0);
    }

    private static int mobility(boolean player,String moves){
        return MOBILITY_MULTIPLIER * mobilityRatio();
    }

    public static int rating(int list, int depth) {
        int counter=0, material=rateMaterial();
        counter+=rateAttack();
        counter+=material;
        counter+=rateMoveablitly(list, depth, material);
        counter+=ratePositional(material);
        alphaBetaChess.flipBoard();
        material=rateMaterial();
        counter-=rateAttack();
        counter-=material;
        counter-=rateMoveablitly(list, depth, material);
        counter-=ratePositional(material);
        alphaBetaChess.flipBoard();
        return -(counter+depth*50);
    }
    public static int rateAttack() {
        int counter=0;
        int tempPositionC=alphaBetaChess.kingPositionC;
        for (int i=0;i<64;i++) {
            switch (alphaBetaChess.chessBoard[i/8][i%8]) {
                case "P": {alphaBetaChess.kingPositionC=i; if (!alphaBetaChess.kingSafe()) {counter-=64;}}
                break;
                case "R": {alphaBetaChess.kingPositionC=i; if (!alphaBetaChess.kingSafe()) {counter-=500;}}
                break;
                case "K": {alphaBetaChess.kingPositionC=i; if (!alphaBetaChess.kingSafe()) {counter-=300;}}
                break;
                case "B": {alphaBetaChess.kingPositionC=i; if (!alphaBetaChess.kingSafe()) {counter-=300;}}
                break;
                case "Q": {alphaBetaChess.kingPositionC=i; if (!alphaBetaChess.kingSafe()) {counter-=900;}}
                break;
            }
        }
        alphaBetaChess.kingPositionC=tempPositionC;
        if (!alphaBetaChess.kingSafe()) {counter-=200;}
        return counter/2;
    }
    public static int rateMaterial() {
        int counter=0, bishopCounter=0;
        for (int i=0;i<64;i++) {
            switch (alphaBetaChess.chessBoard[i/8][i%8]) {
                case "P": counter+=100;
                    break;
                case "R": counter+=500;
                    break;
                case "K": counter+=300;
                    break;
                case "B": bishopCounter+=1;
                    break;
                case "Q": counter+=900;
                    break;
            }
        }
        if (bishopCounter>=2) {
            counter+=300*bishopCounter;
        } else {
            if (bishopCounter==1) {counter+=250;}
        }
        return counter;
    }
    public static int rateMoveablitly(int listLength, int depth, int material) {
        int counter=0;
        counter+=listLength;//5 pointer per valid move
        if (listLength==0) {//current side is in checkmate or stalemate
            if (!alphaBetaChess.kingSafe()) {//if checkmate
                counter+=-200000*depth;
            } else {//if stalemate
                counter+=-150000*depth;
            }
        }
        return 0;
    }
    public static int ratePositional(int material) {
        int counter=0;
        for (int i=0;i<64;i++) {
            switch (alphaBetaChess.chessBoard[i/8][i%8]) {
                case "P": counter+=pawnBoard[i/8][i%8];
                    break;
                case "R": counter+=rookBoard[i/8][i%8];
                    break;
                case "K": counter+=knightBoard[i/8][i%8];
                    break;
                case "B": counter+=bishopBoard[i/8][i%8];
                    break;
                case "Q": counter+=queenBoard[i/8][i%8];
                    break;
                case "A": if (material>=1750) {counter+=kingMidBoard[i/8][i%8]; counter+=alphaBetaChess.posibleA(alphaBetaChess.kingPositionC).length()*10;} else
                {counter+=kingEndBoard[i/8][i%8]; counter+=alphaBetaChess.posibleA(alphaBetaChess.kingPositionC).length()*30;}
                    break;
            }
        }
        return counter;
    }
}
