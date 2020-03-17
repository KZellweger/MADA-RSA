import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Kevin Zellweger
 * @Date 17.03.20
 *
 * This Class implements all Mathematical needs for RSA and provides them as static methods
 */

public class RSA {

    /**
     * Encrypts the given message and returns the result as an BigInter Array where each Character is an Element of the Array
     * @param message: Message to be encrypted
     * @param pk: Public RSA Key
     * @return BigInter[] encrypted Message
     */
    public static BigInteger[] encryptMessage(String message, Tuple<BigInteger, BigInteger> pk) {
        char[] charWise = message.toCharArray();
        BigInteger[] chiffre = new BigInteger[charWise.length];

        for (int i = 0; i < charWise.length; i++) {
            System.out.println("Encrypt x: " + BigInteger.valueOf(charWise[i]));
            chiffre[i] = fastExponentiation(BigInteger.valueOf(charWise[i]), pk);
        }
        return chiffre;
    }

    /**
     * Decrypts the provided Message and Returns the original String
     * @param encryptedMessage: BigInteger[] character wise encrypted Message as BigInteger Values
     * @param sk: Private RSA Key
     * @return Original String
     */
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

    /**
     * Implementation of the fast exponentiation algorithm
     * @param k: base
     * @param key: Tuple with exponent and modulo factor
     *           key.x = m (modulo)
     *           key.y = e (exponent)
     * @return x^e mod m
     */
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
     * Generate a valid RSA Key Pair of the given Size
     *
     * @param bitLength: bit length of the Keypairs
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
     * @param bitLength: size of the prime in Bit
     *                 will be dived by 2 to reach the size in the product
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

    /**
     * Internal Class which represents the RSA Key-Pairs as tuples
     */
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
