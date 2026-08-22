package game.prefabs.unitorderselection;

import game.prefabs.Player;
import game.scripts.misc.ZIndexOffsetScript;
import game.scripts.npc.LookAtMouseScript;
import game.scripts.objects.DragScript;
import lib.Object2D;
import lib.StaticTextures;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnitCard extends Object2D {

    public Point[] snapPositions;

    public int position;

    public int snapSize = 100;

    DragScript script;

    public List<UnitCard> cards = new ArrayList<>();

    public boolean isSnapped = true;

    public Player cardPlayer;

    public UnitCard(Point[] snapPositions, int position) {
        super(snapPositions[position].x, snapPositions[position].y, 180, 180, 0);
        this.snapPositions = snapPositions;
        this.position = position;

        this.texture = StaticTextures.read("src/assets/textures/objects/unitselection/card.png");

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

                isSnapped = false;

                zIndex += 10;
            }
            @Override
            public void onDragEnd(){
                object.xSize = 180;
                object.ySize = 180;

                boolean snapped = false;

                for (int i = 0; i < snapPositions.length; i++) {
                    if (Math.abs(object.xPos - snapPositions[i].x) <= snapSize && Math.abs(object.yPos - snapPositions[i].y) <= snapSize){
                        UnitCard.this.position = i;
                        snapped = true;
                        isSnapped = true;
                        break;
                    }
                }
                if (snapped) {
                    object.xPos = snapPositions[UnitCard.this.position].x;
                    object.yPos = snapPositions[UnitCard.this.position].y;
                }

                for (UnitCard card : cards) {
                    card.script.canDrag = true;

                    if (card.position == UnitCard.this.position && card != UnitCard.this && card.isSnapped){
                        card.yPos = snapPositions[position].y + 200;
                    }
                }

                zIndex -= 10;
            }
        });

        addScript(((((((((((((script))))))))))))); // idk i got carried away
    }

    @Override
    public void onObjectStart() {
        try {
            Object2D visual = new Object2D(0, 0, 150, 150, 0);
            visual.texture = cardPlayer.texture;
            visual.addScript(new ZIndexOffsetScript(10));
            visual.addScript(new LookAtMouseScript());
            addChild(visual);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
