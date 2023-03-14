package com.example.simonjon;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<ColorAudio> machineList;
    int pointUser, selectedColor;
    int numColors;
    int soundBlue, soundRed, soundGreen, soundYellow, soundFail, soundPlay;
    SoundPool soundPool;
    ImageView ibBLUE, ibRED, ibGREEN, ibYELLOW, ibPLAY;
    View.OnClickListener listener;
    GameState gameState;
    TextView labelLevel, level,lose,rondas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSounPool();
        initEmelents();
        gameState = GameState.PLAY;
        //initGame();
        instantiateImages();

        createListener();

        addElementstoListener();


    }

    private void initEmelents() {
        labelLevel = findViewById(R.id.tvPuntuacio);
        level = findViewById(R.id.tvPuntos);
        lose = findViewById(R.id.perdiste);
        rondas = findViewById(R.id.rondas);
        machineList = new ArrayList<ColorAudio>();


    }

    private void addElementstoListener() {
        ibPLAY.setOnClickListener(listener);
        ibBLUE.setOnClickListener(listener);
        ibGREEN.setOnClickListener(listener);
        ibRED.setOnClickListener(listener);
        ibYELLOW.setOnClickListener(listener);

    }

    private void createListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == ibPLAY.getId() && gameState == GameState.PLAY) {
                    ibPLAY.setImageResource(R.drawable.center);
                    ibPLAY.setEnabled(false);
                    labelLevel.setVisibility(View.VISIBLE);
                    level.setVisibility(View.VISIBLE);
                    rondas.setVisibility(View.INVISIBLE);
                    lose.setVisibility(View.INVISIBLE);
                    initGame();
                    playMachineSequence();

                }

                if (gameState == GameState.USER){

                    if (v.getId() == ibBLUE.getId()){

                        soundPool.play(soundBlue, 1, 1, 1, 0, 1);
                        ibBLUE.setImageResource(R.drawable.blueimglight);
                        Pausa(ColorSimon.BLUE);
                        selectedColor = soundBlue;
                        validateShoot();
                    }
                    else if (v.getId() == ibRED.getId()){
                        soundPool.play(soundRed, 1, 1, 1, 0, 1);
                        ibRED.setImageResource(R.drawable.redimglight);
                        Pausa(ColorSimon.RED);
                        selectedColor = soundRed;
                        validateShoot();
                    }
                    else if (v.getId() == ibYELLOW.getId()){
                        soundPool.play(soundYellow, 1, 1, 1, 0, 1);
                        ibYELLOW.setImageResource(R.drawable.yellowimglight);
                        Pausa(ColorSimon.YELLOW);
                        selectedColor = soundYellow;
                        validateShoot();
                    }
                    else if (v.getId() == ibGREEN.getId()){
                        soundPool.play(soundGreen, 1, 1, 1, 0, 1);
                        ibGREEN.setImageResource(R.drawable.greenimglight);
                        Pausa(ColorSimon.GREEN);
                        selectedColor = soundGreen;
                        validateShoot();
                    }

                }
            }
        };

    }

    /**
     * Desactiva los botones durante el turno de la máquina y los reactiva durante
     * el turno del jugador
     * @param gameState
     */
    private void checkButtons(GameState gameState) {
        if (gameState == GameState.MACHINE)
        {
        ibBLUE.setEnabled(false);
        ibRED.setEnabled(false);
        ibYELLOW.setEnabled(false);
        ibGREEN.setEnabled(false);
        }
        else if(gameState == GameState.PLAY)
        {
            ibBLUE.setEnabled(true);
            ibRED.setEnabled(true);
            ibYELLOW.setEnabled(true);
            ibGREEN.setEnabled(true);

        }

    }

    /**
     * Comprueba y controla si el color que ha seleccionado el jugador es el correcto
     */
    private void validateShoot() {
        Handler h = new Handler();
        //OK
        if (machineList.get(pointUser).getIdAudio() == selectedColor) {
            if (machineList.size() == (pointUser+1)){

                level.setText(String.valueOf(machineList.size()+1));

                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initGame();
                    }
                },1000);
            }else{
                pointUser++;
            }
        //Fail
        }else{
            soundPool.play(soundFail, 1, 1, 1, 0, 1);
            lose.setVisibility(View.VISIBLE);
            ibPLAY.setImageResource(R.drawable.play);
            ibPLAY.setEnabled(true);
            labelLevel.setVisibility(View.INVISIBLE);
            level.setVisibility(View.INVISIBLE);
            rondas.setText("Has completado: "+ (machineList.size()-1) +" rondas");
            rondas.setVisibility(View.VISIBLE);
            machineList.clear();
            level.setText("1");
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameState = GameState.PLAY;
                }
            },1000);
        }
    }

    private void instantiateImages() {

        ibBLUE = findViewById(R.id.ibAzul);
        ibRED = findViewById(R.id.ibRojo);
        ibYELLOW = findViewById(R.id.ibAmarillo);
        ibGREEN = findViewById(R.id.ibVerde);
        ibPLAY = findViewById(R.id.ibPlay);
    }

    private void initSounPool() {
        if (Build.VERSION.SDK_INT > 21) { // LOLLIPOP
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .setMaxStreams(6)
                    .build();
        } else {
            //SoundPool(int maxStreams , int streamType , int srcQuality)
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            ;

        }

        initAudios();
    }

    /**
     * Carga un nuevo color en el array y
     */
    private void initGame() {
        numColors = 4;
        gameState = GameState.MACHINE;
        pointUser = 0;



        //TESTS
        machineList.add(getRandomColor());
        //machineList.add(getRandomColor());
        //machineList.add(getRandomColor());
        playMachineSequence();
        //for (int i=0;i<4;i++){
            //machineList.add(getRandomColor());

        //}

    }

    /**
     * Genera un numero aleatorio entre 0 y 4 que se usará para identificar un color
     * del juego
     * @return
     */
    private ColorAudio getRandomColor() {
        ColorAudio colorAudio;
        int rand = new Random().nextInt(numColors);
        //rand = 1; //TEST ONLY BLUE
        if (rand == 0) {
            colorAudio = new ColorAudio(ColorSimon.BLUE, soundBlue);
            //BLUE.setImageResource(R.drawable.blueimglight);
        } else if (rand == 1) {
            colorAudio = new ColorAudio(ColorSimon.RED, soundRed);
            //RED.setImageResource(R.drawable.redimglight);
        } else if (rand == 2) {
            colorAudio = new ColorAudio(ColorSimon.YELLOW, soundYellow);
            //YELLOW.setImageResource(R.drawable.yellowimglight);
        } else {
            colorAudio = new ColorAudio(ColorSimon.GREEN, soundGreen);
            //GREEN.setImageResource(R.drawable.greenimglight);
        }

        return colorAudio;
    }


    private void initAudios() {

        soundBlue = soundPool.load(this, R.raw.sounds_01, 0);
        soundRed = soundPool.load(this, R.raw.sounds_02, 0);
        soundYellow = soundPool.load(this, R.raw.sounds_03, 0);
        soundGreen = soundPool.load(this, R.raw.sounds_04, 0);
        soundFail = soundPool.load(this, R.raw.error, 0);
        soundPlay = soundPool.load(this, R.raw.intro, 0);

    }

    /**
     * Controla la secuencia de colores y sonidos que ha de ejecutar el juego
     * durante el turno de la máquina
     */
    private void playMachineSequence() {
        Handler h = new Handler();
        //ColorAudio colorAudio ;
        //Recorre arrayList
        for (int i = 0; i < machineList.size(); i++) {
            long timeDelay = 1000 * i;
            int id = i;
            //h.postDelayed(new RunnableColor(machineList.get(i)), timeDelay);
            h.postDelayed(new Runnable() {

                @Override
                public void run() {
                   ColorAudio colorAudio = new ColorAudio(machineList.get(id).getColorSimon(),machineList.get(id).getIdAudio());
                    if (machineList.get(id).colorSimon == ColorSimon.BLUE) {
                        soundPool.play(colorAudio.getIdAudio(), 1, 1, 1, 0, 1);
                        ibBLUE.setImageResource(R.drawable.blueimglight);
                        Pausa(machineList.get(id).colorSimon);
                        //returnImage(machineList.get(i).idAudio);
                    } else if (machineList.get(id).colorSimon == ColorSimon.RED) {
                        //RunnableColor(machineList.get());
                        soundPool.play(colorAudio.getIdAudio(), 1, 1, 1, 0, 1);
                        ibRED.setImageResource(R.drawable.redimglight);
                        Pausa(machineList.get(id).colorSimon);
                    } else if (machineList.get(id).getColorSimon() == ColorSimon.YELLOW) {
                        //RunnableColor(machineList.get());
                        soundPool.play(colorAudio.getIdAudio(), 1, 1, 1, 0, 1);
                        ibYELLOW.setImageResource(R.drawable.yellowimglight);
                        Pausa(machineList.get(id).colorSimon);
                    } else {
                        //RunnableColor(machineList.get());
                        soundPool.play(colorAudio.getIdAudio(), 1, 1, 1, 0, 1);
                        ibGREEN.setImageResource(R.drawable.greenimglight);
                        Pausa(machineList.get(id).colorSimon);
                    }

                }
            }, timeDelay);


        }

        //Executor handler and Delay del color que toca ColorAudio
        h.postDelayed(new Runnable() {
            @Override
            public void run() {gameState = GameState.USER;
            /*checkButtons(gameState);*/}
        }, (machineList.size()+100) +250);

    }

    /**
     * Controla el tiempo que dura una imagen iluminada
     * @param c imagen que está iluminada
     */
    private void Pausa(ColorSimon c) {
        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                returnImage(c);
            }
        },300);
    }

    /**
     * Devuelve a la la normalidad la imagen que se le pasa como parámetro
     * @param c
     */
    private void returnImage(ColorSimon c) {

        if (c == ColorSimon.BLUE) {
            ibBLUE.setImageResource(R.drawable.blueimg);
        } else if (c == ColorSimon.RED) {
            ibRED.setImageResource(R.drawable.redimg);
        } else if (c == ColorSimon.YELLOW) {
            ibYELLOW.setImageResource(R.drawable.yellowimg);
        } else {
            ibGREEN.setImageResource(R.drawable.greenimg);
        }

    }

    class RunnableColor implements Runnable {
        ColorAudio colorAudio;

        public RunnableColor(ColorAudio c) {
            colorAudio = c;
        }

        @Override
        public void run() {
            soundPool.play(colorAudio.getIdAudio(), 1, 1, 1, 0, 1);

        }
    }


}