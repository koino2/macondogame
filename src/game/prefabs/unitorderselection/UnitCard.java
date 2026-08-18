package game.prefabs.unitorderselection;

import game.scripts.objects.DragScript;
import lib.Object2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnitCard extends Object2D {

    public Point[] snapPositions;

    public int position;

    public int snapSize = 50;

    DragScript script;

    public List<UnitCard> cards = new ArrayList<>();

    public void whatTheFuckAmIWitnessing(int whyIsThisAThing){ // I understand why people dislike java now.
        position = whyIsThisAThing;
    }

    public UnitCard(Point[] snapPositions, int position) {
        super(snapPositions[position].x, snapPositions[position].y, 180, 180, 0);
        this.snapPositions = snapPositions;
        this.position = position;

        script = (new DragScript(){
            @Override
            public void onDragStart(){
                object.xSize = 200;
                object.ySize = 200;

                for (UnitCard card : cards){
                    if (card != UnitCard.this){
                        card.script.canDrag = false;
                    }
                }

                zIndex += 10;
            }
            @Override
            public void onDragEnd(){
                object.xSize = 180;
                object.ySize = 180;

                boolean snapped = false;

                for (int i = 0; i < snapPositions.length; i++) {
                    if (Math.abs(object.xPos - snapPositions[i].x) <= snapSize && Math.abs(object.yPos - snapPositions[i].y) <= snapSize){
                        whatTheFuckAmIWitnessing(i);
                        snapped = true;
                        System.out.println("snap!");
                        break;
                    }
                }
                if (!snapped) {
                    object.xPos = snapPositions[position].x;
                    object.yPos = snapPositions[position].y;
                }

                for (UnitCard card : cards) {
                    card.script.canDrag = true;
                }

                zIndex -= 10;
            }
        });

        addScript(((((((((((((script))))))))))))); // idk i got carried away
    }
}
