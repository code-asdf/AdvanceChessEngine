import java.security.*;
public class Zobrist {
    static long zArray[][][] = new long[2][6][64];
    static long zEnPassant[] = new long[8];
    static long zCastle[] = new long[4];
    static long zBlackMove;
    public static long random64() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }
    public static long random64Bad() {
        return (long)(Math.random()*1000000000000000000L);
    }

    public static void zobristFillArray() {
        for (int color = 0; color < 2; color++)
        {
            for (int pieceType = 0; pieceType < 6; pieceType++)
            {
                for (int square = 0; square < 64; square++)
                {
                    zArray[color][pieceType][square] = random64();
                }
            }
        }
        for (int column = 0; column < 8; column++)
        {
            zEnPassant[column] = random64();
        }
        for (int i = 0; i < 4; i++)
        {
            zCastle[i] = random64();
        }
        zBlackMove = random64();
    }

    public static long getZobristHash(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove) {
        long returnZKey = 0;
        for (int square = 0; square < 64; square++)
        {
            if (((WP >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][0][square];
            }
            else if (((BP >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][0][square];
            }
            else if (((WN >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][1][square];
            }
            else if (((BN >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][1][square];
            }
            else if (((WB >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][2][square];
            }

            else if (((BB >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][2][square];
            }
            else if (((WR >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][3][square];
            }
            else if (((BR >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][3][square];
            }
            else if (((WQ >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][4][square];
            }
            else if (((BQ >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][4][square];
            }
            else if (((WK >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][5][square];
            }
            else if (((BK >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][5][square];
            }
        }
        for (int column = 0; column < 8; column++)
        {
            if (EP == Moves.FileMasks8[column])
            {
                returnZKey ^= zEnPassant[column];
            }
        }
        if (CWK)
            returnZKey ^= zCastle[0];
        if (CWQ)
            returnZKey ^= zCastle[1];
        if (CBK)
            returnZKey ^= zCastle[2];
        if (CBQ)
            returnZKey ^= zCastle[3];
        if (!WhiteToMove)
            returnZKey ^= zBlackMove;
        return returnZKey;
    }

    public static void testDistribution() {
        int sampleSize = 2000;
        int sampleSeconds = 10;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (sampleSeconds * 1000);
        int[] distArray;
        distArray = new int[sampleSize];
        while (System.currentTimeMillis() < endTime)
        {
            for (int i = 0; i < 10000; i++)
            {
                distArray[(int)(random64Bad()% (sampleSize / 2)) + (sampleSize / 2)]++;
            }
        }
        for (int i = 0; i < sampleSize; i++)
        {
            System.out.println(distArray[i]);
        }
    }
}