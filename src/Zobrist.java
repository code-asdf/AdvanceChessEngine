import java.security.*;
public class Zobrist {
    public static long random64() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }
    public static long random64Bad() {
        return (long)(Math.random()*1000000000000000000L);
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