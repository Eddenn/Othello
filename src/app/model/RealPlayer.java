package app.model;

import java.awt.*;

public class RealPlayer implements Player {

    private boolean hasPlayed;
    private Point play;

    public RealPlayer() {
        hasPlayed = false;
        play = new Point(-1,-1);
    }

    @Override
    public Point askToPlay() {
        do {
            //Wait
        }while( !hasPlayed );
        this.hasPlayed = false;
        return play;
    }

    public void setPlay(Point play) {
        this.play = play;
        this.hasPlayed = true;
    }
}
