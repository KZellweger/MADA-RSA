import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class Primes {

    public static ArrayList<Integer> eratostenes(int n) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int j = 2; j <= n; j++) {
            primes.add(j);
        }

        int i = 2;
        while (i <= Math.sqrt(n)) {
            int finalI = i ;
            primes.removeIf(integer -> integer.intValue() % finalI == 0 && integer.intValue() != finalI);
            i++;
        }

        return primes;
    }

    public static ArrayList<Integer> sophieGermainPrimes(ArrayList<Integer> primes, int range){
        ArrayList<Integer> sophieGermainPrimes = new ArrayList<>();

        for (int i = 0; i<primes.size(); i++)
        {
            if(primes.contains((primes.get(i)*2) + 1)){sophieGermainPrimes.add(primes.get(i));}
            if(sophieGermainPrimes.size() >= range){return sophieGermainPrimes;}

        }
        return sophieGermainPrimes;
    }

    public static BigInteger genPrime(int bitLength){
        Random rand = new SecureRandom();
        BigInteger prime = new BigInteger(bitLength,rand);
        return prime;
    }

    public static void BigPrimes(){
        BigInteger p = genPrime(1024);
        BigInteger q = genPrime(1024);

        BigInteger n = p.multiply(q);

        System.out.println("p: " + p.toString());
        System.out.println("q: " + q.toString());
        System.out.println("n: " + n.toString());
    }


}
