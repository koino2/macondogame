package game.levels;

import game.prefabs.Enemy;
import game.prefabs.Ghost;
import game.prefabs.Player;
import game.scripts.player.CameraController;
import game.scripts.player.recording.Recording;
import lib.Camera;
import lib.Object2D;
import lib.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Level extends Scene {

    public List<Enemy> levelEnemies = new ArrayList<>();
    public List<Enemy> enemies = new ArrayList<>();

    public List<Recording> recordings = new ArrayList<>();
    public List<Ghost> ghosts = new ArrayList<>();

    public List<SpawnQueueItem> spawnQueue = new ArrayList<>();

    public abstract void buildObjects();
    public abstract void initEnemies();

    public abstract void onWin();

    public double spawnInterval = 1d;

    public int runNumber = 0;

    public Player player;

    public Camera sceneCamera;
    public CameraController cameraController;
    public Object2D cameraFallbackObject;

    public void clearEnemies(){
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).destroy();
        }
        enemies.clear();
        levelEnemies.clear();
    }

    public void addNewRecording(){

        ghostTextures.add(player.texture);
        ghostColors.add(player.color);

        recordings.add(player.playerRecorder.recording);
        player.playerRecorder.stopRecording();
    }

    public void clearGhosts(){
        for (int i = 0; i < ghosts.size(); i++) {
            ghosts.get(i).destroy();
        }
        ghosts.clear();
    }

    public abstract Player initPlayer();

    public void onPlayerDeath(){
        addNewRecording();
        player.destroy();
    }

    public void updateCamera(){
        if(!(player != null && !player.destroyed && objects.contains(player))){
            cameraController.target = cameraFallbackObject;
        } else{
            cameraController.target = player;
        }
    }

    public boolean isPlayerAlive(){
        if(player == null || player.destroyed){
            return false;
        }
        return true;
    }

    public boolean areGhostsAlive(){
        for (int i = 0; i < ghosts.size(); i++) {
            if(!ghosts.get(i).destroyed){
                return true;
            }
        }
        return false;
    }

    public boolean areEnemiesAlive(){
        for(Enemy enemy : enemies){
            if(!enemy.destroyed){
                return true;
            }
        }
        return false;
    }

    double initialWinTimer = 2;
    double winTimer = initialWinTimer;
    public void checkRunState(double deltaTime){
        if(!isPlayerAlive() && !areGhostsAlive()){
            clearEnemies();
            clearGhosts();
            spawnQueue.clear();
            winTimer = initialWinTimer;
            startNewRun();
        }
        if(isPlayerAlive() || areGhostsAlive()){
            if(!areEnemiesAlive()){
                winTimer -= deltaTime;
                if(winTimer <= 0) {
                    onWin();
                }
            }
        }
    }

    public List<BufferedImage> ghostTextures = new ArrayList<>();
    public List<Color> ghostColors = new ArrayList<>();

    public void startNewRun(){
        time = 0;

        if(player != null) {
            player.destroy();
            player = null;
        }

        player = initPlayer();

        initEnemies();

        for (int i = 0; i < levelEnemies.size(); i++) {
            final int finalI = i;
            spawnQueue.add(new SpawnQueueItem(0) {
                @Override
                public Object2D initObject() {
                    enemies.add(levelEnemies.get(finalI));
                    return levelEnemies.get(finalI);
                }
            });
        }

        for (int i = 0; i < recordings.size(); i++){
            final int finalI = i;
            spawnQueue.add(new SpawnQueueItem(spawnQueue.get(spawnQueue.size()-1).spawnTime + spawnInterval) {
                @Override
                public Object2D initObject() {
                    Ghost ghost = new Ghost(recordings.get(finalI));
                    ghost.texture = ghostTextures.get(finalI);
                    ghost.color = ghostColors.get(finalI);
                    ghosts.add(ghost);
                    return ghost;
                }
            });
        }
        spawnQueue.add(new SpawnQueueItem(spawnQueue.get(spawnQueue.size()-1).spawnTime + spawnInterval) {
            @Override
            public Object2D initObject() {
                return player;
            }
        });

        runNumber++;
    }

    @Override
    public void start() {
        //this.player = initPlayer();
        //addObject(player);
        buildObjects();
        startNewRun();

        sceneCamera = new Camera(0, 0, 0);
        cameraController = new CameraController(cameraFallbackObject);
        sceneCamera.addScript(cameraController);
        addObject(sceneCamera);
        this.camera = sceneCamera;
    }

    double time = 0;

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        updateCamera();

        checkRunState(deltaTime);

        for(int i = 0; i < spawnQueue.size(); i++){
            if(time >= spawnQueue.get(i).spawnTime){
                addObject(spawnQueue.get(i).object);
                spawnQueue.remove(spawnQueue.get(i));
            }
        }
    }

    @Override
    public void renderUI(Graphics g){

    }


}
abstract class SpawnQueueItem{
    Object2D object;
    double spawnTime;

    public abstract Object2D initObject();

    public SpawnQueueItem(double time){
        this.spawnTime = time;
        this.object = initObject();
    }
}
