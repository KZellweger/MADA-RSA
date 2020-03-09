import java.util.ArrayList;

public class main {

    public static void main (String[]args){
        ArrayList<Integer> primes = Primes.eratostenes(1000);
        //ArrayList<Integer> sophieGermain = Primes.sophieGermainPrimes(primes,100);
        prettyPrint(primes);
        //prettyPrint(sophieGermain);
        Primes.BigPrimes();
    }

    public static void prettyPrint(ArrayList<Integer> list)
    {
        System.out.println("Num. of elements: " + list.size());
        int i;
        for(i = 0; i<list.size(); i +=4){
            if(i + 4 < list.size())
            {
                System.out.print(list.get(i) + "\t" + list.get(i + 1) + "\t" + list.get(i + 2) + "\t" + list.get(i + 3));
                System.out.println();
            }
            else{
                while (i < list.size()){
                    System.out.print(list.get(i) + "\t");
                    i ++;
                }
                System.out.println();

            }
        }
    }


}
