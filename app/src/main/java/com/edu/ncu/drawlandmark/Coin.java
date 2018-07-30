//import java.util.*;

public class Coin {
    //public static void main(String[] args){
    private int total_coin = 0;
    private double m;
    private int building_coin;
    private int earning_coin;
    private int correct = 0;
    private boolean answerCheck;
    private int conti = 0;
    private int[] problem = new int[5];

    public Coin() {

    }

    public Coin(int total_coin, double m, int building_coin,int correct,int earning_coin,int conti ){

        this.total_coin = total_coin;
        this.m = m;
        this.building_coin = building_coin;
        this.correct = correct;
        this.earning_coin = earning_coin;
        this.conti = conti;

    }


    public void countProblemGain(){
        for(int i=0;i<5;i++) {
            if(answerCheck == true) {
                problem[i]=1;
                correct++;
            }
            else problem[i]=0;
        }

        if(problem[4]!=0) {
            for(int j=3;j>-1;j--) {
                if(problem[j]!=0) conti++;
            }
        }

        if(conti==0)m=1;
        else if(conti==1)m=1.25;
        else if(conti==2)m=1.5;
        else if(conti==3)m=1.75;
        else if(conti==4)m=2;

        earning_coin = (int) ((200*correct)*m);
        System.out.printf("恭喜你答對%d題，你共獲得%d coins",correct,earning_coin);
    }


    public int getEarning_coin(){

        return 	earning_coin;

    }

    public void setBuildingCost(int building_coin){
        this.building_coin = building_coin;

    }

    public int getTotalCoin(){
        return total_coin;
    }
//	public int getBuildingCost(){}
    //}
}

