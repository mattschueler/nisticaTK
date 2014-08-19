package com.nistica.panels;

import java.awt.*;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        Color color1 = new Color(57,183,250);
        Color color2 = color1.darker();
        int w = getWidth();
        int h = getHeight(); 
        GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
