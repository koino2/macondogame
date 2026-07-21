package game.scripts.level.spawnqueue;

import lib.Script;

import java.util.ArrayList;
import java.util.List;

public class SpawnQueue extends Script {
    public List<SpawnQueueItem> queue = new ArrayList<>();
    @Override
    public void start() {

    }

    public double time;

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        for(int i = 0; i < queue.size(); i++){
            if(time >= queue.get(i).spawnTime){
                object.scene.addObject(queue.get(i).object);
                queue.remove(queue.get(i));
            }
        }
    }

    public void clearQueue(){
        queue.clear();
        time = 0;
    }

    public double getLastQueueTimeAdd(double time){
        return queue.get(queue.size()-1).spawnTime + time;
    }

    public void addItem(SpawnQueueItem item){
        queue.add(item);
    }
}
