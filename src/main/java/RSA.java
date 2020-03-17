import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSA {


    public static BigInteger[] encryptMessage(String message, Tuple<BigInteger, BigInteger> pk) {
        char[] charWise = message.toCharArray();
        BigInteger[] chiffre = new BigInteger[charWise.length];

        for (int i = 0; i < charWise.length; i++) {
            System.out.println("Encrypt x: " + BigInteger.valueOf(charWise[i]));
            chiffre[i] = fastExponentiation(BigInteger.valueOf(charWise[i]), pk);
        }
        return chiffre;
    }

    public static String decrypttMessage(BigInteger[] encryptedMessage, Tuple<BigInteger, BigInteger> sk) {
        char[] decryptedChars = new char[encryptedMessage.length];
        StringBuilder strb = new StringBuilder();
        String decryptedMessage;
        for (int i = 0; i < encryptedMessage.length; i++) {
            BigInteger x = fastExponentiation(encryptedMessage[i], sk);
            System.out.println("Decrypted x: " + x);
            decryptedChars[i] = (char) x.intValue();
        }
        strb.append(decryptedChars);
        decryptedMessage = strb.toString();
        return decryptedMessage;
    }

    public static BigInteger fastExponentiation(BigInteger k, Tuple<BigInteger, BigInteger> key) {
        int l = key.y.bitLength() - 1;
        BigInteger h = BigInteger.ONE;

        for (int i = 0; i <= l; i++) {
            if (key.y.testBit(i)) {
                h = h.multiply(k);
                h = h.mod(key.x);
            }
            k = k.pow(2);
            k = k.mod(key.x);
        }
        return h;
    }


    /**
     * Generate a valid RSA Key Pair of the given Bitlength
     *
     * @param bitLength
     * @return Tuple wit PK (n,e) and SK (n,d)
     */
    public static RSAKeyPair genRSAKeyPair(int bitLength) {
        Random rand = new SecureRandom();
        BigInteger p = genPrime(bitLength);
        BigInteger q = genPrime(bitLength);
        while (p.equals(q)) {
            q = genPrime(bitLength);
        }
        BigInteger n = p.multiply(q);
        System.out.println("p:" + p);
        System.out.println("q:" + q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(65537);
        BigInteger d = euklid(phiN, e);
        System.out.println("phiN:" + phiN);
        RSAKeyPair keyPair = new RSAKeyPair(e, d, n);
        System.out.println("(" + keyPair.pk.x + "," + keyPair.pk.y + ")");
        System.out.println("(" + keyPair.sk.x + "," + keyPair.sk.y + ")");
        return keyPair;
    }

    /**
     * Generate a random Prime Number in the given Size
     *
     * @param bitLength size of the prime in Bit
     * @return the generated Prime number
     */
    public static BigInteger genPrime(int bitLength) {
        Random rand = new SecureRandom();
        BigInteger prime = BigInteger.probablePrime(bitLength / 2, rand);
        return prime;
    }

    /**
     * Find a Number "d" element of Z*m to a given "e" element of Z*m
     * with e * d = 1 (mod m)
     *
     * @param phiN represents m
     * @param e    given "e"
     * @return d
     */

    public static BigInteger euklid(BigInteger phiN, BigInteger e) {
        BigInteger a, b, x0, x1, y0, y1, q, r, tempX0, tempY0;
        a = phiN;
        b = e;
        x0 = BigInteger.ONE;
        y0 = BigInteger.ZERO;
        x1 = BigInteger.ZERO;
        y1 = BigInteger.ONE;
        //TODO: Define criteria if not possible to rech e * d =m 1
        while (!b.equals(BigInteger.ZERO)) {
            tempX0 = x0;
            tempY0 = y0;
            q = a.divide(b);
            r = a.mod(b);
            a = b;
            b = r;
            x0 = x1;
            y0 = y1;
            x1 = tempX0.subtract(q.multiply(x1));
            y1 = tempY0.subtract(q.multiply(y1));
        }
        if (y0.compareTo(BigInteger.ZERO) < 0) {
            return y0.add(phiN);
        }
        return y0;
    }

    public static class RSAKeyPair {
        private Tuple<BigInteger, BigInteger> pk;
        private Tuple<BigInteger, BigInteger> sk;


        RSAKeyPair(BigInteger e, BigInteger d, BigInteger n) {
            pk = new Tuple<>(n, e);
            sk = new Tuple<>(n, d);
        }

        public Tuple<BigInteger, BigInteger> getPk() {
            return pk;
        }

        public Tuple<BigInteger, BigInteger> getSk() {
            return sk;
        }

        public void setPk(Tuple<BigInteger, BigInteger> pk) {
            this.pk = pk;
        }

        public void setSk(Tuple<BigInteger, BigInteger> sk) {
            this.sk = sk;
        }
    }

}
