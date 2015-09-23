/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triquisocketsservidor;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author jedisson.tapias
 */
public class Salir2 extends javax.swing.JPanel {
    int x, y;
    ImageIcon Img;

    public Salir2(JPanel jPanel1) {
        this.x = jPanel1.getWidth();
        this.y = jPanel1.getHeight();
        this.setSize(x, y);
    }

    @Override
    public void paint(Graphics g) {
        Img = new ImageIcon(getClass().getResource("/Resources/salir2.jpg"));
        g.drawImage(Img.getImage(), 0, 0, x, y, null);
    }    

}
