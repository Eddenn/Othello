package app.view.components;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public enum TileStatus {
    WHITE("/white_pawn.png"),
    BLACK("/black_pawn.png"),
    EMPTY(),
    EMPTY_PLAYABLE("/playable_place.png"),
    EMPTY_PLAYABLE_HOVERED("/playable_place_hover.png");

    private Image img;

    TileStatus() {
        img=null;
    }
    TileStatus(String imgPath) {
        try {
            img = ImageIO.read(getClass().getResource(imgPath));
        } catch(Exception e) {
            System.err.println("Les images correspondant aux poins n'ont pas pu être chargées.");
            System.exit(0);
        }
    }

    public Image getImage() {
        return img;
    }
}
