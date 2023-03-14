package com.example.simonjon;

public class ColorAudio {
    ColorSimon colorSimon;
    int idAudio;

    public ColorAudio (ColorSimon c, int id){
        colorSimon = c;
        idAudio= id;

    }

    public ColorSimon getColorSimon(){
        return  colorSimon;

    }

    public void setColorSimon(ColorSimon colorSimon){

        this.colorSimon = colorSimon;

    }
    public int getIdAudio(){

        return this.idAudio;
    }
}
