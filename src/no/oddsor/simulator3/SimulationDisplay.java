
package no.oddsor.simulator3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import no.oddsor.simulator3.sensor.Contact;
import no.oddsor.simulator3.sensor.Sensor;
import no.oddsor.simulator3.sensor.SensorArea;

/**
 *
 * @author Odd
 */
public class SimulationDisplay extends JPanel{
    
    private Image image;
    private Collection<Sensor> sensors;
    private Collection<Person> people;

    public SimulationDisplay(String mapName) {
        image = new ImageIcon(getClass().getResource(mapName)).getImage();
        Dimension size = new Dimension(image.getWidth(this), image.getHeight(this));
        setPreferredSize(size);
        people = null;
        sensors = null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);
        if(people != null){
            for(Person person: people){
                g.drawImage(person.avatarImg, person.currentLocation().x - (person.avatarImg.getWidth(this) / 2), 
                    person.currentLocation().y - (person.avatarImg.getHeight(this) / 2), 
                    person.avatarImg.getWidth(this), person.avatarImg.getHeight(this), this);
            }
        }
        if(sensors != null){
            for(Sensor sensor: sensors){
                if(!(sensor instanceof Contact)){
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(sensor.getPosition().x - 1, sensor.getPosition().y - 1, 
                            2, 2);
                    for(SensorArea s: sensor.getSensorAreas()){
                        boolean steppedOn = false;
                        if(people != null){
                            for(Person p: people){
                                if(s.getArea().contains(p.currentLocation())){
                                    steppedOn = true;
                                }
                            }
                        }
                        if(steppedOn) g2d.setColor(new Color(0, 255, 0, 50));
                        else g2d.setColor(new Color(255, 255, 0, 50));
                        g2d.fill(s.getArea());
                        g2d.setColor(Color.BLACK);
                        g2d.draw(s.getArea());
                    }
                }
            }
        }
    }
    
    public void update(Collection<Person> people, Collection<Sensor> sensors){
        this.people = people;
        this.sensors = sensors;
        repaint();
    }
}
