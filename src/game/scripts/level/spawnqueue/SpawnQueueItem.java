package game.scripts.level.spawnqueue;

import lib.Object2D;

public abstract class SpawnQueueItem{
    public Object2D object;
    public double spawnTime;

    public abstract Object2D initObject();

    public SpawnQueueItem(double time){
        this.spawnTime = time;
        this.object = initObject();
    }
}