package game.prefabs.unitorderselection;

import game.prefabs.Player;
import lib.Object2D;
import lib.StaticTextures;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnitOrderSelector extends Object2D {
    public List<Player> players = new ArrayList<>();
    public void onSelected(){

    }
    public UnitOrderSelector(float x, float y) {
        super(x, y, 1267, 565, 0);
        texture = StaticTextures.read("src/assets/textures/objects/unitselection/unit-selection-bg.png");
    }

    public Point[] points = new Point[]{
            new Point(72, 208),
            new Point(380, 208),
            new Point(688, 208),
            new Point(996, 208),
    };

    List<UnitCard> cards = new ArrayList<>();

    @Override
    public void onObjectStart() {
        for (int i = 0; i < 4; i++) {
            UnitCard unitCard = new UnitCard(points, i);
            unitCard.color = new Color(new Random().nextInt(0, 255), new Random().nextInt(0, 255), new Random().nextInt(0, 255));
            addChild(unitCard);
            cards.add(unitCard);
        }
        for (UnitCard card : cards){
            card.cards = cards;
        }
    }
}
