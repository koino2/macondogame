package game.prefabs.unitorderselection;

import game.prefabs.Player;
import lib.Input;
import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class UnitOrderSelectorNextButton extends Object2D {

    List<UnitCard> cards;

    public abstract void onSelected(List<Player> playerList);

    boolean fired = false;

    public Point getMouseWorldPosition(){
        Point point = Input.getMousePosition();
        float mouseWorldX;
        float mouseWorldY;
        if (point != null) {
            mouseWorldX = (float)((point.x-scene.engine.getWidth()/2.0)/scene.camera.scale+scene.camera.globalX);
            mouseWorldY = (float)((point.y-scene.engine.getHeight()/2.0)/scene.camera.scale+scene.camera.globalY);
            return new Point((int) mouseWorldX, (int) mouseWorldY);
        }
        return null;
    }

    public UnitOrderSelectorNextButton(Object2D parent, List<UnitCard> cards) {
        super((parent.xSize/2), (parent.ySize/2), 100, 25, 0);
        this.cards = cards;

        addScript(new Script() {
            @Override
            public void start() {

            }

            @Override
            public void update(double deltaTime) {
                Point mouse = getMouseWorldPosition();
                if (
                        mouse.x > globalX-object.xSize/2 && mouse.x < globalX+object.xSize/2 &&
                        mouse.y > globalY-object.ySize/2 && mouse.y < globalY+object.ySize/2 &&
                        Input.isMouseReleased(MouseEvent.BUTTON1) && !fired
                ) {

                    fired = true;

                    List<Player> playerList = new ArrayList<>();
                    for (UnitCard card : cards) {
                        if (!card.isSnapped){
                            return;
                        }
                    }
                    cards.sort(Comparator.comparingInt(card -> card.position));
                    for (UnitCard card : cards){
                        playerList.add(card.cardPlayer);
                    }

                    onSelected(playerList);
                }
            }
        });
    }
}
