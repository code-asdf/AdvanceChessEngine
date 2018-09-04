public class PrincipalVariation {
    public static int zWSearch(int beta,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth){
        //fail-hard zero window seach, returns either beta-1 or beta
        int score = Integer.MIN_VALUE;
        //alpha == beta-1
        //this is either a cut- or all-node
        if(depth == Orion.searchDepth){
            score = Rating.evaluate();
            return score;
        }

        String moves;
        if (WhiteToMove) {
            moves=Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        } else {
            moves=Moves.possibleMovesB(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        }
        //sortMoves();
        for (int i=0;i<moves.length();i+=4) {
            long WPt=Moves.makeMove(WP, moves.substring(i,i+4), 'P'), WNt=Moves.makeMove(WN, moves.substring(i,i+4), 'N'),
                    WBt=Moves.makeMove(WB, moves.substring(i,i+4), 'B'), WRt=Moves.makeMove(WR, moves.substring(i,i+4), 'R'),
                    WQt=Moves.makeMove(WQ, moves.substring(i,i+4), 'Q'), WKt=Moves.makeMove(WK, moves.substring(i,i+4), 'K'),
                    BPt=Moves.makeMove(BP, moves.substring(i,i+4), 'p'), BNt=Moves.makeMove(BN, moves.substring(i,i+4), 'n'),
                    BBt=Moves.makeMove(BB, moves.substring(i,i+4), 'b'), BRt=Moves.makeMove(BR, moves.substring(i,i+4), 'r'),
                    BQt=Moves.makeMove(BQ, moves.substring(i,i+4), 'q'), BKt=Moves.makeMove(BK, moves.substring(i,i+4), 'k'),
                    EPt=Moves.makeMoveEP(WP|BP,moves.substring(i,i+4));
            WRt=Moves.makeMoveCastle(WRt, WK|BK, moves.substring(i,i+4), 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, moves.substring(i,i+4), 'r');
            boolean CWKt=CWK,CWQt=CWQ,CBKt=CBK,CBQt=CBQ;
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                int start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
                else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
                else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
                else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
                else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
                else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
            }
            if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                    ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                score = -zWSearch(1 - beta,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
            }
            if (score >= beta)
            {
                return score;//fail-hard beta-cutoff
            }
        }
        return beta - 1;//fail-hard, return alpha
    }
    public static int getFirstLegalMove(String moves,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove) {
        for (int i=0;i<moves.length();i+=4) {
            long WPt=Moves.makeMove(WP, moves.substring(i,i+4), 'P'), WNt=Moves.makeMove(WN, moves.substring(i,i+4), 'N'),
                    WBt=Moves.makeMove(WB, moves.substring(i,i+4), 'B'), WRt=Moves.makeMove(WR, moves.substring(i,i+4), 'R'),
                    WQt=Moves.makeMove(WQ, moves.substring(i,i+4), 'Q'), WKt=Moves.makeMove(WK, moves.substring(i,i+4), 'K'),
                    BPt=Moves.makeMove(BP, moves.substring(i,i+4), 'p'), BNt=Moves.makeMove(BN, moves.substring(i,i+4), 'n'),
                    BBt=Moves.makeMove(BB, moves.substring(i,i+4), 'b'), BRt=Moves.makeMove(BR, moves.substring(i,i+4), 'r'),
                    BQt=Moves.makeMove(BQ, moves.substring(i,i+4), 'q'), BKt=Moves.makeMove(BK, moves.substring(i,i+4), 'k');
            WRt=Moves.makeMoveCastle(WRt, WK|BK, moves.substring(i,i+4), 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, moves.substring(i,i+4), 'r');
            if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                    ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                return i;
            }
        }
        return -1;
    }
    public static int pvSearch(int alpha,int beta,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth) {//using fail soft with negamax
        int bestScore;
        int bestMoveIndex = -1;
        if (depth == Orion.searchDepth)
        {
            bestScore = Rating.evaluate();
            return bestScore;
        }
        String moves;
        if (WhiteToMove) {
            moves=Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        } else {
            moves=Moves.possibleMovesB(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        }
        //sortMoves();
        int firstLegalMove = getFirstLegalMove(moves,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove);
        if (firstLegalMove == -1)
        {
            return WhiteToMove ? Orion.MATE_SCORE : -Orion.MATE_SCORE;
        }
        long WPt=Moves.makeMove(WP, moves.substring(firstLegalMove,firstLegalMove+4), 'P'), WNt=Moves.makeMove(WN, moves.substring(firstLegalMove,firstLegalMove+4), 'N'),
                WBt=Moves.makeMove(WB, moves.substring(firstLegalMove,firstLegalMove+4), 'B'), WRt=Moves.makeMove(WR, moves.substring(firstLegalMove,firstLegalMove+4), 'R'),
                WQt=Moves.makeMove(WQ, moves.substring(firstLegalMove,firstLegalMove+4), 'Q'), WKt=Moves.makeMove(WK, moves.substring(firstLegalMove,firstLegalMove+4), 'K'),
                BPt=Moves.makeMove(BP, moves.substring(firstLegalMove,firstLegalMove+4), 'p'), BNt=Moves.makeMove(BN, moves.substring(firstLegalMove,firstLegalMove+4), 'n'),
                BBt=Moves.makeMove(BB, moves.substring(firstLegalMove,firstLegalMove+4), 'b'), BRt=Moves.makeMove(BR, moves.substring(firstLegalMove,firstLegalMove+4), 'r'),
                BQt=Moves.makeMove(BQ, moves.substring(firstLegalMove,firstLegalMove+4), 'q'), BKt=Moves.makeMove(BK, moves.substring(firstLegalMove,firstLegalMove+4), 'k'),
                EPt=Moves.makeMoveEP(WP|BP,moves.substring(firstLegalMove,firstLegalMove+4));
        WRt=Moves.makeMoveCastle(WRt, WK|BK, moves.substring(firstLegalMove,firstLegalMove+4), 'R');
        BRt=Moves.makeMoveCastle(BRt, WK|BK, moves.substring(firstLegalMove,firstLegalMove+4), 'r');
        boolean CWKt=CWK,CWQt=CWQ,CBKt=CBK,CBQt=CBQ;
        if (Character.isDigit(moves.charAt(firstLegalMove+3))) {//'regular' move
            int start=(Character.getNumericValue(moves.charAt(firstLegalMove))*8)+(Character.getNumericValue(moves.charAt(firstLegalMove+1)));
            if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
            else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
            else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
            else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
            else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
            else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
        }
        bestScore = -pvSearch(-beta,-alpha,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
        Orion.moveCounter++;
        if (Math.abs(bestScore) == Orion.MATE_SCORE)
        {
            return bestScore;
        }
        if (bestScore > alpha)
        {
            if (bestScore >= beta)
            {
                //This is a refutation move
                //It is not a PV move
                //However, it will usually cause a cutoff so it can
                //be considered a best move if no other move is found
                return bestScore;
            }
            alpha = bestScore;
        }
        bestMoveIndex = firstLegalMove;
        for (int i=firstLegalMove;i<moves.length();i+=4) {
            int score;
            Orion.moveCounter++;
            //legal, non-castle move
            WPt=Moves.makeMove(WP, moves.substring(i,i+4), 'P');
            WNt=Moves.makeMove(WN, moves.substring(i,i+4), 'N');
            WBt=Moves.makeMove(WB, moves.substring(i,i+4), 'B');
            WRt=Moves.makeMove(WR, moves.substring(i,i+4), 'R');
            WQt=Moves.makeMove(WQ, moves.substring(i,i+4), 'Q');
            WKt=Moves.makeMove(WK, moves.substring(i,i+4), 'K');
            BPt=Moves.makeMove(BP, moves.substring(i,i+4), 'p');
            BNt=Moves.makeMove(BN, moves.substring(i,i+4), 'n');
            BBt=Moves.makeMove(BB, moves.substring(i,i+4), 'b');
            BRt=Moves.makeMove(BR, moves.substring(i,i+4), 'r');
            BQt=Moves.makeMove(BQ, moves.substring(i,i+4), 'q');
            BKt=Moves.makeMove(BK, moves.substring(i,i+4), 'k');
            EPt=Moves.makeMoveEP(WP|BP,moves.substring(i,i+4));
            WRt=Moves.makeMoveCastle(WRt, WK|BK, moves.substring(i,i+4), 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, moves.substring(i,i+4), 'r');
            CWKt=CWK;
            CWQt=CWQ;
            CBKt=CBK;
            CBQt=CBQ;
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                int start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
                else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
                else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
                else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
                else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
                else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
            }
            score = -zWSearch(-alpha,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
            if ((score > alpha) && (score < beta))
            {
                //research with window [alpha;beta]
                // I think this should be score and not bestScore
                bestScore = -pvSearch(-beta,-alpha,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,!WhiteToMove,depth+1);
                if (score>alpha)
                {
                    bestMoveIndex = i;
                    alpha = score;
                }
            }
            //did not understand
            if ((score != Orion.NULL_INT) && (score > bestScore))
            {
                if (score >= beta)
                {
                    return score;
                }
                bestScore = score;
                if (Math.abs(bestScore) == Orion.MATE_SCORE)
                {
                    return bestScore;
                }
            }
        }
        return bestScore;
    }
}