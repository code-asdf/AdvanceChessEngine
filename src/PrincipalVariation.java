public class PrincipalVariation {


    public static int pvSearch(int alpha,int beta,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth){
        //using fail soft with negamax
        int bestScore;
        int bestMoveIndex = -1;
        if(depth==Orion.searchDepth){
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
        if(firstLegalMove == -1){
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
    }
}
