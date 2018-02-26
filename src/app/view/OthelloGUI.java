package app.view;

import app.model.Game;
import app.model.player.Player;
import app.model.player.RealPlayer;
import app.view.components.OthelloBoard;
import app.view.components.OthelloTile;
import app.view.components.TileStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Interface graphique de l'application OthelloGUI
 */
public class OthelloGUI {

    private Game game;

    private Font font;
    private JFrame mainFrame;
    private Border paddedBorderRight, paddedBorderLeft;
    private OthelloBoard board;
    private JLabel player1,player2;
    private JLabel player1score, player2score;

    /**
     * Constructeur appellant createModel(), createComponents(), placeComponents() et createControllers()
     */
    public OthelloGUI() {
        createModel();
        createComponents();
        placeComponents();
        createControllers();
    }

    /**
     * Affiche la fenêtre
     */
    public void display() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * Initialise le model
     */
    private void createModel() {
        game = new Game(new RealPlayer(),new RealPlayer());
        try {
            board = new OthelloBoard(8,8);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame,
                    e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        board.refreshModel(game);
    }

    /**
     * Initialise les composants de la fenêtre
     */
    private void createComponents() {
        //---- Chargement de la police de texte ----//
        try {
            InputStream is = OthelloGUI.class.getResourceAsStream("/coolvetica.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("coolvetica.ttf ne s'est pas chargée.  Utilisation de la font de base.");
            font = new Font("serif", Font.PLAIN, 24);
        }

        //---- La fenêtre ----//
        mainFrame = new JFrame("OthelloGUI");
        mainFrame.setSize(800,600);
        mainFrame.setMinimumSize(new Dimension(800,600));
        mainFrame.setLayout(new BorderLayout());
        //mainFrame.setUndecorated(true);
        mainFrame.setBackground(new Color(130, 204, 221));

        //jpHead = new JPanel(new BorderLayout());

        //---- Player à droite et à gauche ----//
        paddedBorderRight = BorderFactory.createCompoundBorder(new MatteBorder(0,0,0,3,Color.BLACK),new EmptyBorder(10, 10, 10, 10));
        paddedBorderLeft = BorderFactory.createCompoundBorder(new MatteBorder(0,3,0,0,Color.BLACK),new EmptyBorder(10, 10, 10, 10));

        player1 = new JLabel("Joueur 1",new ImageIcon(getClass().getResource("/player_black.png")),SwingConstants.CENTER);
        player1.setVerticalTextPosition(SwingConstants.BOTTOM);
        player1.setHorizontalTextPosition(SwingConstants.CENTER);
        player1.setFont(font.deriveFont(Font.PLAIN,24));
        player1score = new JLabel("16",SwingConstants.CENTER);
        player1score.setFont(font.deriveFont(Font.PLAIN,32));

        player2 = new JLabel("Joueur 2",new ImageIcon(getClass().getResource("/player_white.png")),SwingConstants.CENTER);
        player2.setVerticalTextPosition(SwingConstants.BOTTOM);
        player2.setHorizontalTextPosition(SwingConstants.CENTER);
        player2.setFont(font.deriveFont(Font.PLAIN,24));
        player2score = new JLabel("16",SwingConstants.CENTER);
        player2score.setFont(font.deriveFont(Font.PLAIN,32));
    }

    /**
     * Place les composants sur la fenêtre
     */
    private void placeComponents() {
        //Joueur 1
        JPanel p = new JPanel(new GridBagLayout()); {
            JPanel q = new JPanel(new GridLayout(0,1)); {
                q.add(player1);
                q.add(player1score);
            }
            q.setOpaque(false);
            p.add(q, new GridBagConstraints());
        }
        p.setBackground(new Color(130, 204, 221));
        p.setBorder(paddedBorderRight);
        mainFrame.add(p,BorderLayout.WEST);
        //Joueur 2
        p = new JPanel(new GridBagLayout()); {
            JPanel q = new JPanel(new GridLayout(0,1)); {
                q.add(player2);
                q.add(player2score);
            }
            q.setOpaque(false);
            p.add(q, new GridBagConstraints());
        }
        p.setBackground(new Color(130, 204, 221));
        p.setBorder(paddedBorderLeft);
        mainFrame.add(p,BorderLayout.EAST);
        //Plateau de jeu
        p = new BoardContainer(new GridBagLayout()); {
            p.add(board, new GridBagConstraints());
        }
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainFrame.add(p,BorderLayout.CENTER);
    }
    /**
     * Initilise les controlleurs de la fenêtre
     */
    private void createControllers() {
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        game.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                board.refreshModel(game);
            }
        });

        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                OthelloTile viewTile = board.getTile(e.getPoint());

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                OthelloTile viewTile = board.getTile(e.getPoint());
                if(viewTile.getStatus() == TileStatus.EMPTY_PLAYABLE) {
                    System.out.println("ok"); //TODO focus
                }
            }
        });
    }

    /**
     * Méthode principale instanciant l'OthelloGUI
     * @param args Arguments d'entrée de l'application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OthelloGUI().display();
            }
        });
    }

    /**
     * Panel contenant le plateau de jeu
     * Celui-ci permet le coté "responsive" de la fenêtre
     */
    class BoardContainer extends JPanel {

        BoardContainer(LayoutManager layout) {
            super(layout);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                Image img = ImageIO.read(getClass().getResource("/background.jpg"));
                g.drawImage(img,0,0,getWidth(),getHeight(), null);
            } catch(IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
