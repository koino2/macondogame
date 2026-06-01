package game.levels;

import game.prefabs.Ghost;
import game.prefabs.Player;
import game.scripts.player.recording.Recording;
import game.scripts.weapons.Pistol;
import lib.Scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Level extends Scene {

    public List<Ghost> ghosts = new ArrayList<>();

    public List<Recording> recordings = new ArrayList<>();

    public Player player;

    public int runNumber = 1;

    public boolean runActive = false;
    public boolean firstWaveSpawned = false;

    public double firstRunTimer = 10;

    public abstract void buildObjects();
    public abstract void spawnEnemies();
    public abstract Player createPlayer();

    public void clearGhosts(){
        for (int i = 0; i < ghosts.size(); i++) {
            ghosts.get(i).destroy();
        }
        ghosts.clear();
    }

    public void spawnPlayer(){
        player = createPlayer();
        addObject(player);
        runActive = true;
    }

    public void spawnGhosts(){
        clearGhosts();
        for (int i = 0; i < recordings.size(); i++) {
            Ghost ghost = new Ghost(recordings.get(i));
            addObject(ghost);
            ghosts.add(ghost);
        }
    }

    public void onPlayerDeath(){
        recordings.add(player.playerRecorder.recording);
        player.destroy();
        player = null;
    }

    public void checkRunState(){
        boolean ghostsAlive = false;

        for (int i = 0; i < ghosts.size(); i++) {
            if(!ghosts.get(i).destroyed){
                ghostsAlive = true;
                break;
            }
        }

        boolean playerAlive = false;
        if(player != null && !player.destroyed){
            playerAlive = true;
        }

        if(!playerAlive && !ghostsAlive){
            startNextRun();
        }
    }

    public void startNextRun(){
        runNumber += 1;

        spawnGhosts();

        spawnEnemies();

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                spawnPlayer();
            }
        }, 10000);
    }

    @Override
    public void start() {
        buildObjects();
        spawnPlayer();
    }

    @Override
    public void update(double deltaTime) {
        checkRunState();
        if(runNumber == 1){
            firstRunTimer -= deltaTime;

            if(firstRunTimer <= 0 && !firstWaveSpawned){
                spawnEnemies();
                firstWaveSpawned = true;
            }
        }
    }

    @Override
    public void renderUI(Graphics g){

    }
}
