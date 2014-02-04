
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Odd
 */
public class SimulationDisplay extends JPanel{
    
    private Simulator sim;
    private Point startPoint;
    private Image image;
    private SQLiteConnection db;

    public SimulationDisplay(Simulator sim, String mapName, Point startPoint, SQLiteConnection db) {
        this.sim = sim;
        this.startPoint = startPoint;
        this.db = db;
        image = new ImageIcon(getClass().getResource(mapName)).getImage();
        Dimension size = new Dimension(image.getWidth(this), image.getHeight(this));
        setPreferredSize(size);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);
    }
}
