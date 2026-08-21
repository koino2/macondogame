package game.prefabs.unitorderselection;

import game.levels.Level;
import game.prefabs.Player;
import lib.Object2D;
import lib.StaticTextures;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnitOrderSelector extends Object2D {

    public List<Player> players = new ArrayList<>();

    List<Player> cardPlayers;

    public void onSelected(List<Player> playerList){

    }

    public UnitOrderSelector(float x, float y, List<Player> cardPlayers) {
        super(x, y, 1267, 565, 0);
        texture = StaticTextures.read("src/assets/textures/objects/unitselection/unit-selection-bg.png");
        this.cardPlayers = cardPlayers;
    }

    List<UnitCard> cards = new ArrayList<>();

    @Override
    public void onObjectStart() {

        Point[] points = new Point[]{
                new Point((int)(72+100 - xSize/2), (int)(208+100-ySize/2)),
                new Point((int)(380+100 - xSize/2), (int)(208+100-ySize/2)),
                new Point((int)(688+100 - xSize/2), (int)(208+100-ySize/2)),
                new Point((int)(996+100 - xSize/2), (int)(208+100-ySize/2)),
        };

        for (int i = 0; i < 4; i++) {
            UnitCard unitCard = new UnitCard(points, i);
            unitCard.color = new Color(new Random().nextInt(0, 255), new Random().nextInt(0, 255), new Random().nextInt(0, 255));

            unitCard.cardPlayer = cardPlayers.get(i);

            addChild(unitCard);
            cards.add(unitCard);
        }
        for (UnitCard card : cards){
            card.cards = cards;
        }

        UnitOrderSelectorNextButton nextButton = new UnitOrderSelectorNextButton(this, cards) {
            @Override
            public void onSelected(List<Player> playerList) {
                UnitOrderSelector.this.onSelected(playerList);

                ((Level)(scene)).playerOrder = playerList;
                ((Level)(scene)).startNewRun();
                UnitOrderSelector.this.destroy();
            }
        };
        addChild(nextButton);
    }
}
