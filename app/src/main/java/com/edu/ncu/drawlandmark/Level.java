package com.edu.ncu.drawlandmark;

import java.util.*;

public class Level{

    int playerXP;
    private int playerLV;
    private double levelupXP; //需要升級的xp
    private double maxXP;
    private int maxLV;
    private String achievementTitle;
    private Boolean checkLVup;

    int correct_count=0; //default 0

    private String title[] = new String[]{ "Bubble開拓者","Bubble勇士","Bubble領主","Bubble締造者",
            "Bubble守護者","Bubble先鋒","Bubble大師","Bubble菁英","Bubble王者","Bubble至尊","無所不知" };

    public Level(){

    }


    public Level(int playerXP, int playerLV, int maxXP, int maxLV, String achievementTitle,
                 Boolean checkLVup, String title[]){

        this.playerXP = playerXP;
        this.playerLV = playerLV;
        this.maxXP = maxXP;
        this.maxLV = maxLV;
        this.achievementTitle= achievementTitle;
        this.checkLVup = false;//???Boolean這裡要幹嘛
        this.title = title;
    }

    public void accumulateXP(){

        for (playerLV=1; maxLV<=50; playerLV++){

            maxXP = (Math.pow(playerLV-1, 2)+60)/5*(playerLV+19);
        }
    }

    public void setLevelupXP(int playerLV){
        this.levelupXP = (Math.pow(playerLV-1, 2)+60)/5*(playerLV+19);
    }

    public int levelUp(){

      //  Scanner gainXP = new Scanner(input.nextInt());
        // 猜謎
        playerLV += 1;
        playerXP -= levelupXP;

        return  this.playerLV;

    }

    public double getgainXP(int correct_count) {
        // TODO Auto-generated method stub

        this.correct_count = correct_count;
        double gainXP = this.correct_count*30;

        if(this.correct_count == 2){
            playerXP += 1.25*gainXP;
        }

        else if(this.correct_count== 3){
            playerXP += 1.5*gainXP;
        }

        else if(this.correct_count == 4){
            playerXP += 1.75*gainXP;
        }

        else if(this.correct_count == 5){
            playerXP += 2*gainXP;
        }

        if (playerXP >= levelupXP){
            levelUp();
        }

        return gainXP;
    }

    public int getPlayerXP(){
        return playerXP;
    }

    public int getPlayerLV(){
        return playerLV;
    }

    public void setAchTitle(String achievementTitle){

        this.achievementTitle=achievementTitle;
    }

    public String getAchTitle(int playerLV){

        this.playerLV = playerLV;

        if(playerLV <10){
            return title[0]; //1~9開拓者
        }

        else if(playerLV <20){
            return title[1]; //10~19勇士
        }

        else if(playerLV < 25){
            return title[2]; //20~24締造者
        }

        else if(playerLV < 30){
            return title[3]; //25~29守護者
        }

        else if( playerLV < 34){
            return title[4]; //30~33守護者
        }

        else if( playerLV < 38){
            return title[5]; //34~37先鋒
        }

        else if( playerLV < 41){
            return title[6]; //38~40大師
        }

        else if( playerLV < 44){
            return title[7]; //41~43菁英
        }

        else if( playerLV < 47){
            return title[8]; //44~46王者
        }

        else if( playerLV < 50){
            return title[9]; //47~49至尊
        }

        if( playerLV == 50){
            return title[10]; //50無所不知
        }
        return achievementTitle;
    }

    public Boolean setMaxXP(int maxXP){

        boolean x = false;

        if(playerXP == maxXP){
            return true;
        }

        else{
            return false;
        }

    }

    public void setMaxLV(int maxLV){
        maxLV = 50;
    }

}
