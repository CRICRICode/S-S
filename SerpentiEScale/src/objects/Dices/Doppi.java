package objects.Dices;

import java.util.Random;

public class Doppi implements Dadi{

    @Override
    public int[] lanciaDadi(){
        int [] lan = new int[2];
        lan[0] = new Random().nextInt(6)+1;
        lan[1] = new Random().nextInt(6)+1;
        return  lan;
    }
}
