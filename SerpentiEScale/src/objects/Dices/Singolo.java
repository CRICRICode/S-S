package objects.Dices;

import java.util.Random;

public class Singolo implements  Dadi{

    @Override
    public int[] lanciaDadi(){
        int[] lan = new int[1];
        lan[0] = new Random().nextInt(6)+1;
        return lan;
    }
}
