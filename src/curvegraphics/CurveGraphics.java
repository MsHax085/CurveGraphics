package curvegraphics;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Richard Dahlgren
 * @since 2013-dec-05
 * @version 1.0
 */
public class CurveGraphics implements Runnable, ActionListener, ChangeListener {

    private final JFrame graphicsFrame;
    private final JFrame optionsFrame;
    private final Thread thread;
    
    private final SpinnerNumberModel xDisplacementSpinnerModel;
    private final SpinnerNumberModel yDisplacementSpinnerModel;
    private final SpinnerNumberModel xDisplacementMultiplierSpinnerModel;
    private final SpinnerNumberModel yDisplacementMultiplierSpinnerModel;
    private final SpinnerNumberModel dotSpacingSpinnerModel;
    
    private final JSpinner xDisplacementSpinner;
    private final JSpinner yDisplacementSpinner;
    private final JSpinner xDisplacementMultiplierSpinner;
    private final JSpinner yDisplacementMultiplierSpinner;
    private final JSpinner dotSpacingSpinner;
    
    private final JLabel xDisplacementLabel;
    private final JLabel yDisplacementLabel;
    private final JLabel xDisplacementMultiplierLabel;
    private final JLabel yDisplacementMultiplierLabel;
    private final JLabel dotSpacingLabel;
    
    final DecimalFormat decimal = new DecimalFormat("#.##");
    private final Font font;
    
    private int x_displacement = 50;
    private int y_displacement = 50;
    private int x_displacement_multiplier = 10;
    private int y_displacement_multiplier = 8;
    private double dot_spacing = 0.1;
    
    public static void main(final String[] args) {
        final CurveGraphics cg = new CurveGraphics();
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    public CurveGraphics() {
        graphicsFrame = new JFrame("CurveGraphics");
        optionsFrame = new JFrame("Options");
        
        graphicsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        graphicsFrame.setSize(1000, 500);
        optionsFrame.setSize(200, 240);
        
        graphicsFrame.setLocationRelativeTo(null);
        optionsFrame.setLocationRelativeTo(null);
        
        graphicsFrame.setResizable(false);
        optionsFrame.setResizable(false);
        
        final Container container = optionsFrame.getContentPane();
        final SpringLayout layout = new SpringLayout();
        {
            container.setLayout(layout);
            container.setBackground(new Color(83, 83, 83));
            
            // Objects
            xDisplacementSpinnerModel = new SpinnerNumberModel(x_displacement, -1000, 1000, 1);// Default, Min, Max, Step
            xDisplacementSpinner = new JSpinner(xDisplacementSpinnerModel);
            xDisplacementLabel = new JLabel("X-Axis Displacement");
            
            yDisplacementSpinnerModel = new SpinnerNumberModel(y_displacement, -1000, 1000, 1);
            yDisplacementSpinner = new JSpinner(yDisplacementSpinnerModel);
            yDisplacementLabel = new JLabel("Y-Axis Displacement");
            
            xDisplacementMultiplierSpinnerModel = new SpinnerNumberModel(x_displacement_multiplier, 0, 50, 1);
            xDisplacementMultiplierSpinner = new JSpinner(xDisplacementMultiplierSpinnerModel);
            xDisplacementMultiplierLabel = new JLabel("X-Axis Multiplier");
            
            yDisplacementMultiplierSpinnerModel = new SpinnerNumberModel(y_displacement_multiplier, 0, 50, 1);
            yDisplacementMultiplierSpinner = new JSpinner(yDisplacementMultiplierSpinnerModel);
            yDisplacementMultiplierLabel = new JLabel("Y-Axis Multiplier");
            
            dotSpacingSpinnerModel = new SpinnerNumberModel(dot_spacing, 0, 10, 0.001);
            dotSpacingSpinner = new JSpinner(dotSpacingSpinnerModel);
            dotSpacingLabel = new JLabel("Dot Spacing");

            // Object dimension
            xDisplacementSpinner.setPreferredSize(new Dimension(40, 25));
            yDisplacementSpinner.setPreferredSize(new Dimension(40, 25));
            
            xDisplacementMultiplierSpinner.setPreferredSize(new Dimension(40, 25));
            yDisplacementMultiplierSpinner.setPreferredSize(new Dimension(40, 25));
            
            dotSpacingSpinner.setPreferredSize(new Dimension(55, 25));
            
            // Object settings
            xDisplacementLabel.setForeground(Color.WHITE);
            yDisplacementLabel.setForeground(Color.WHITE);
            
            xDisplacementMultiplierLabel.setForeground(Color.WHITE);
            yDisplacementMultiplierLabel.setForeground(Color.WHITE);
            
            dotSpacingLabel.setForeground(Color.WHITE);
            
            // Object event listeners
            xDisplacementSpinner.addChangeListener(this);
            yDisplacementSpinner.addChangeListener(this);
            
            xDisplacementMultiplierSpinner.addChangeListener(this);
            yDisplacementMultiplierSpinner.addChangeListener(this);
            
            dotSpacingSpinner.addChangeListener(this);
            
            // Add objects
            container.add(xDisplacementSpinner);
            container.add(xDisplacementLabel);
            container.add(yDisplacementSpinner);
            container.add(yDisplacementLabel);
            
            container.add(xDisplacementMultiplierSpinner);
            container.add(xDisplacementMultiplierLabel);
            container.add(yDisplacementMultiplierSpinner);
            container.add(yDisplacementMultiplierLabel);
            
            container.add(dotSpacingSpinner);
            container.add(dotSpacingLabel);
            
            // Layout
            layout.putConstraint(SpringLayout.NORTH, xDisplacementSpinner, 5, SpringLayout.NORTH, container);
            layout.putConstraint(SpringLayout.EAST, xDisplacementSpinner, -7, SpringLayout.EAST, container);
            layout.putConstraint(SpringLayout.NORTH, xDisplacementLabel, 4, SpringLayout.NORTH, xDisplacementSpinner);
            layout.putConstraint(SpringLayout.WEST, xDisplacementLabel, 10, SpringLayout.WEST, container);
            
            layout.putConstraint(SpringLayout.NORTH, yDisplacementSpinner, 35, SpringLayout.NORTH, xDisplacementSpinner);
            layout.putConstraint(SpringLayout.EAST, yDisplacementSpinner, 0, SpringLayout.EAST, xDisplacementSpinner);
            layout.putConstraint(SpringLayout.NORTH, yDisplacementLabel, 4, SpringLayout.NORTH, yDisplacementSpinner);
            layout.putConstraint(SpringLayout.WEST, yDisplacementLabel, 10, SpringLayout.WEST, container);
            
            layout.putConstraint(SpringLayout.NORTH, xDisplacementMultiplierSpinner, 35, SpringLayout.NORTH, yDisplacementSpinner);
            layout.putConstraint(SpringLayout.EAST, xDisplacementMultiplierSpinner, 0, SpringLayout.EAST, yDisplacementSpinner);
            layout.putConstraint(SpringLayout.NORTH, xDisplacementMultiplierLabel, 4, SpringLayout.NORTH, xDisplacementMultiplierSpinner);
            layout.putConstraint(SpringLayout.WEST, xDisplacementMultiplierLabel, 10, SpringLayout.WEST, container);
            
            layout.putConstraint(SpringLayout.NORTH, yDisplacementMultiplierSpinner, 35, SpringLayout.NORTH, xDisplacementMultiplierSpinner);
            layout.putConstraint(SpringLayout.EAST, yDisplacementMultiplierSpinner, 0, SpringLayout.EAST, xDisplacementMultiplierSpinner);
            layout.putConstraint(SpringLayout.NORTH, yDisplacementMultiplierLabel, 4, SpringLayout.NORTH, yDisplacementMultiplierSpinner);
            layout.putConstraint(SpringLayout.WEST, yDisplacementMultiplierLabel, 10, SpringLayout.WEST, container);
            
            layout.putConstraint(SpringLayout.NORTH, dotSpacingSpinner, 35, SpringLayout.NORTH, yDisplacementMultiplierSpinner);
            layout.putConstraint(SpringLayout.EAST, dotSpacingSpinner, 0, SpringLayout.EAST, yDisplacementMultiplierSpinner);
            layout.putConstraint(SpringLayout.NORTH, dotSpacingLabel, 4, SpringLayout.NORTH, dotSpacingSpinner);
            layout.putConstraint(SpringLayout.WEST, dotSpacingLabel, 10, SpringLayout.WEST, container);
        }
        
        graphicsFrame.setVisible(true);
        optionsFrame.setVisible(true);
        
        graphicsFrame.createBufferStrategy(2);
        
        font = new Font("Arial", Font.PLAIN, 24);
        
        thread = new Thread(this);
        thread.start();
    }
    
    public void close() {
        graphicsFrame.setVisible(false);
        optionsFrame.setVisible(false);
        
        thread.interrupt();
        System.exit(0);
    }
    
    private void drawGraphics() {
        final BufferStrategy bf = graphicsFrame.getBufferStrategy();
        Graphics2D g = null;
        
        try {
            g = (Graphics2D) bf.getDrawGraphics();
            g.setFont(font);
            
            final FontMetrics fontMetrics = g.getFontMetrics();
            
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHints(rh);
            
            // Draw background
            g.setColor(new Color(38, 38, 38));
            g.fillRect(0, 0, 1000, 500);
            
            // Print author name
            final String by = "Richard Dahlgren";
            g.setColor(new Color(53, 53, 53));
            g.drawString(by, 990 - fontMetrics.stringWidth(by), 480);
            
            final int origo_x = x_displacement * x_displacement_multiplier;
            final int origo_y = y_displacement * y_displacement_multiplier;
            
            g.setColor(Color.DARK_GRAY);
            g.drawLine(0, origo_y, 1000, origo_y);// X-Axis
            g.drawLine(origo_x, 0, origo_x, 500);// Y-Axis
            
            g.setColor(Color.WHITE);
            
            for (double x = -50; x <= 50; x += dot_spacing) {
                /*// Polynom
                double y = (Math.pow(x, 2) - 1) * (Math.pow(x, 2) - 4) * (Math.pow(x, 2) - 9);
                y /= 36;
                
                // Ellips
                y = (Math.sqrt(16 - Math.pow(x, 2))) / 2;
                
                // Hyperbel
                y = Math.sqrt((Math.pow(x, 2) / 4) - 1);
                
                // Superellips
                y = Math.pow((1 - 0.6415 * Math.pow(x, 2.5)) / 0.203063, 1/2.3);
                
                // Tredjegradskruva
                y = Math.sqrt(Math.pow(x, 3) + Math.pow(x, 2));
                
                // Kissoid
                y = -(Math.pow(x, 3/2) / Math.sqrt(2 - x));
                
                y *= -1;// Translate Y-Axis
               
                // Final coordinates on screen
                double realX = (x + x_displacement) * x_displacement_multiplier;
                double realY = (y + y_displacement) * y_displacement_multiplier;
                
                g.fillOval((int) realX, (int) realY, 1, 1);*/
                
                //drawPolynom(g, x);
                //drawEllipse(g, x);
                //drawHyperbola(g, x);
                //drawSuperEllipse(g, x);
                //drawCubicCurve(g, x);
                //drawCissoid(g, x);
                //drawPascalSnail(g, x);
                //drawChainLine(g, x);
                drawBooths(g, x);
                
            }
            
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
        
        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }
    
    private void drawPolynom(Graphics2D g, double x) {// 1 Solution
        double y = ((Math.pow(x, 2) - 1) * (Math.pow(x, 2) - 4) * (Math.pow(x, 2) - 9)) / 36;
        draw(g, x, y);
    }
    
    private void drawEllipse(Graphics2D g, double x) {// 2 Solutions
        double y1 = Math.sqrt(16 - Math.pow(x, 2)) / 2;
        double y2 = -y1;
        
        draw(g, x, y1);
        draw(g, x, y2);
    }
    
    private void drawHyperbola(Graphics2D g, double x) {// 2 Solutions
        double y1 = Math.sqrt(Math.pow(x, 2) - 4) / 2;
        double y2 = -y1;
        
        draw(g, x, y1);
        draw(g, x, y2);
    }
    
    private void drawSuperEllipse(Graphics2D g, double x) {// 4 Solutions
        double y1 = 2 * Math.pow(1 - 0.06415 * Math.pow(x, 2.5), 0.4);
        double y2 = -y1;
        double y3 = 2 * Math.pow(1 - 0.06415 * Math.pow(-x, 2.5), 0.4);
        double y4 = -y3;
        
        draw(g, x, y1);
        draw(g, x, y2);
        draw(g, x, y3);
        draw(g, x, y4);
    }
    
    private void drawCubicCurve(Graphics2D g, double x) {// 2 Solutions
        double y1 = x * Math.sqrt(x + 1);
        double y2 = -y1;
        
        draw(g, x, y1);
        draw(g, x, y2);
    }
    
    private void drawCissoid(Graphics2D g, double x) {// 2 Solutions, Approximate
        double y1 = -(Math.pow(x, 3/2) / Math.sqrt(2 - x));
        double y2 = -y1;
        
        draw(g, x, y1);
        draw(g, x, y2);
    }
    
    private void drawPascalSnail(Graphics2D g, double x) {// 4 Solutions
        double y1 = Math.sqrt(-2 * Math.pow(x, 2) + 4 * x - Math.sqrt(8 * x + 1) + 1) / Math.sqrt(2);
        double y2 = Math.sqrt(-2 * Math.pow(x, 2) + 4 * x + Math.sqrt(8 * x + 1) + 1) / Math.sqrt(2);
        double y3 = -y1;
        double y4 = -y2;
        
        draw(g, x, y1);
        draw(g, x, y2);
        draw(g, x, y3);
        draw(g, x, y4);
    }
    
    private void drawChainLine(Graphics2D g, double x) {// 1 Solution
        double y = 0.4 * Math.cosh(x);
        draw(g, x, y);
    }
    
    private void drawBooths(Graphics2D g, double x) {// 4 Solution
        double y1 = Math.sqrt(-2 * Math.pow(x, 2) - Math.sqrt(12 * Math.pow(x, 2) + 1) + 1) / Math.sqrt(2);
        double y2 = Math.sqrt(-2 * Math.pow(x, 2) + Math.sqrt(12 * Math.pow(x, 2) + 1) + 1) / Math.sqrt(2);
        double y3 = -y1;
        double y4 = -y2;
        
        draw(g, x, y1);
        draw(g, x, y2);
        draw(g, x, y3);
        draw(g, x, y4);
    }
    
    private void draw(Graphics2D g, double x, double y) {
        double realX = (x + x_displacement) * x_displacement_multiplier;
        double realY = (-y + y_displacement) * y_displacement_multiplier;

        g.fillOval((int) realX, (int) realY, 1, 1);
    }

    @Override
    public void run() {
        
        while (!thread.isInterrupted()) {
            
            drawGraphics();
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(CurveGraphics.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        final Object obj = e.getSource();
        if (obj instanceof JComboBox) {
            
            final JComboBox selected = (JComboBox) obj;
            final String curve = (String) selected.getSelectedItem();
            
            switch (curve) {
                case "Sine Curve":
                {
                    trigFunction = "SIN";
                    break;
                }
                case "Cosine Curve":
                {
                    trigFunction = "COS";
                    break;
                }
                case "Tangent Curve":
                {
                    trigFunction = "TAN";
                    break;
                }
            }
            displacement = 0;
            return;
        }
        
        if (obj instanceof JButton) {
            
            final JButton selected = (JButton) obj;
            
            if (selected.getText().equals("Start")) {
                selected.setText("Pause");
                isPaused = false;
            } else {
                selected.setText("Start");
                isPaused = true;
            }
        }*/
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        
        final Object obj = e.getSource();
        
        if (obj == xDisplacementSpinner) {
            x_displacement = (int) xDisplacementSpinner.getValue();
            
        } else if (obj == yDisplacementSpinner) {
            y_displacement = (int) yDisplacementSpinner.getValue();
            
        } else if (obj == xDisplacementMultiplierSpinner) {
            x_displacement_multiplier = (int) xDisplacementMultiplierSpinner.getValue();
            
        } else if (obj == yDisplacementMultiplierSpinner) {
            y_displacement_multiplier = (int) yDisplacementMultiplierSpinner.getValue();
        
        } else if (obj == dotSpacingSpinner) {
            dot_spacing = (double) dotSpacingSpinner.getValue();
        }
    }
}