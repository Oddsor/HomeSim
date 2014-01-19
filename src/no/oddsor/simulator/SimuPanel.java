/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class SimuPanel extends JPanel{
    
    private Image image;
    private Image avatar;
    private Point avatarPosition;
    
    public Dimension getDimensions(){
        return new Dimension(image.getWidth(this), image.getHeight(this));
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SimuPanel(String imgFile, String avatarFile, Point startpos)
    {
        image = new ImageIcon(getClass().getResource(imgFile)).getImage();
        avatar = new ImageIcon(getClass().getResource(avatarFile)).getImage();
        
        avatarPosition = startpos;
        
        setSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        
        setBounds(0, 0, image.getWidth(this), image.getHeight(this));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);
        
        g.drawImage(avatar, avatarPosition.x - (avatar.getWidth(this) / 2), 
                avatarPosition.y - (avatar.getHeight(this) / 2), 
                avatar.getWidth(this), avatar.getHeight(this), this);
    }

    public void setPosition(Point pos){
        avatarPosition = pos;
        repaint();
    }
}