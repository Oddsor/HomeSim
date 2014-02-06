
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Odd
 */
public class SimulationDisplay extends JPanel{
    
    private Point startPoint;
    private Image image;
    private SQLiteConnection db;
    private ArrayList<Person> people;

    public SimulationDisplay(String mapName, Point startPoint, SQLiteConnection db) {
        this.startPoint = startPoint;
        this.db = db;
        image = new ImageIcon(getClass().getResource(mapName)).getImage();
        Dimension size = new Dimension(image.getWidth(this), image.getHeight(this));
        setPreferredSize(size);
        people = null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);
        if(people != null){
            for(Person person: people){
                g.drawImage(person.avatarImg, person.currentLocation().x - (person.avatarImg.getWidth(this) / 2), 
                    person.currentLocation().y - (person.avatarImg.getHeight(this) / 2), 
                    person.avatarImg.getWidth(this), person.avatarImg.getHeight(this), this);
            }
        }
    }
    
    public void update(ArrayList<Person> people){
        this.people = people;
        repaint();
    }
}
