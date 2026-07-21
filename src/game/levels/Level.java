package game.levels;

import game.prefabs.enemies.Enemy;
import game.prefabs.Ghost;
import game.prefabs.Player;
import game.scripts.level.Reset;
import game.scripts.level.spawnqueue.SpawnQueue;
import game.scripts.level.spawnqueue.SpawnQueueItem;
import game.scripts.misc.DebugFunctions;
import game.scripts.misc.pause.Pause;
import game.scripts.player.CameraController;
import game.scripts.player.recording.Recording;
import game.scripts.weapons.WeaponScript;
import lib.Camera;
import lib.Input;
import lib.Object2D;
import lib.Scene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Level extends Scene {

    public List<Enemy> levelEnemies = new ArrayList<>();
    public List<Enemy> enemies = new ArrayList<>();

    public List<Recording> recordings = new ArrayList<>();
    public List<Ghost> ghosts = new ArrayList<>();

    public SpawnQueue spawnQueue = new SpawnQueue();

    public abstract void buildObjects();
    public abstract void initEnemies();

    public abstract void onWin();
    public abstract void onLose();

    public double spawnInterval = 1d;

    public int runNumber = 0;

    public Player player;

    public Object2D scriptObject;

    public Camera sceneCamera;
    public CameraController cameraController;
    public Object2D cameraFallbackObject;

    public Object2D trash;

    public List<Player> playerOrder = new ArrayList<>();

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
            spawnQueue.clearQueue();
            winTimer = initialWinTimer;
            startNewRun();
        }
        if(isPlayerAlive() || areGhostsAlive()){
            if(!areEnemiesAlive()){
                winTimer -= deltaTime;
                if(winTimer <= 0) {
                    onWin();
                }
            } else {
                winTimer = initialWinTimer;
            }
        }
    }

    public List<BufferedImage> ghostTextures = new ArrayList<>();
    public List<Color> ghostColors = new ArrayList<>();

    public void startNewRun(){
        trash.destroy();
        trash = new Object2D(0, 0, 0, 0, 0);
        addObject(trash);

        winTimer = initialWinTimer;
        time = 0;

        if(player != null) {
            player.destroy();
            player = null;
        }

        //player = initPlayer();

        if (runNumber >= playerOrder.size()){
            onLose();
        } else {
            player = playerOrder.get(runNumber);
        }

        initEnemies();

        for (int i = 0; i < levelEnemies.size(); i++) {
            final int finalI = i;
            spawnQueue.addItem(new SpawnQueueItem(0) {
                @Override
                public Object2D initObject() {
                    enemies.add(levelEnemies.get(finalI));
                    return levelEnemies.get(finalI);
                }
            });
        }

        for (int i = 0; i < recordings.size(); i++){
            final int finalI = i;
            spawnQueue.addItem(new SpawnQueueItem(spawnQueue.getLastQueueTimeAdd(spawnInterval)) {
                @Override
                public Object2D initObject() {
                    Ghost ghost = new Ghost(recordings.get(finalI));
                    ghost.texture = ghostTextures.get(finalI);
                    ghost.setColor(ghostColors.get(finalI));
                    ghosts.add(ghost);
                    return ghost;
                }
            });
        }
        Player queuedPlayer = player;
        spawnQueue.addItem(new SpawnQueueItem(spawnQueue.getLastQueueTimeAdd(spawnInterval)) {
            @Override
            public Object2D initObject() {
                return queuedPlayer;
            }
        });

        runNumber++;
    }

    @Override
    public void start() {
        trash = new Object2D(0, 0, 0, 0, 0);
        addObject(trash);

        buildObjects();
        startNewRun();

        scriptObject = new Object2D(0, 0, 0, 0, 0);
        addObject(scriptObject);
        scriptObject.addScript(spawnQueue);
        scriptObject.addScript(new Pause());

        sceneCamera = new Camera(0, 0, 0);
        cameraController = new CameraController(cameraFallbackObject);
        sceneCamera.addScript(cameraController);
        addObject(sceneCamera);
        this.camera = sceneCamera;

        scriptObject.addScript(new Reset());
        scriptObject.addScript(new DebugFunctions());
    }

    double time = 0;

    double selfDestructTime = 0;

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        updateCamera();
        checkRunState(deltaTime);

        selfDestructTime += deltaTime;
        if(Input.isKeyDown(KeyEvent.VK_X) && selfDestructTime > 1f){
            clearEnemies();
            clearGhosts();
            spawnQueue.clearQueue();
            winTimer = initialWinTimer;
            startNewRun();
            selfDestructTime = 0;
        }
    }

    @Override
    public void renderUI(Graphics g){

    }


}
