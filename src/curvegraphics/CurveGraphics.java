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
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    
    final DecimalFormat decimal = new DecimalFormat("#.##");
    private final Font font;
    
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
            
            g.setColor(new Color(38, 38, 38));
            g.fillRect(0, 0, 1000, 500);
            
            final String by = "Richard Dahlgren";
            g.setColor(new Color(53, 53, 53));
            g.drawString(by, 990 - fontMetrics.stringWidth(by), 480);
            
            g.setColor(Color.WHITE);
            
            for (double x = -50; x <= 50; x += 0.1) {
                
                double y = (Math.pow(x, 2) - 1) * (Math.pow(x, 2) - 4) * (Math.pow(x, 2) - 9);
                y /= 36;
                
                y *= -1;// Translate Y-Axis
                
                x *= 10;
                y *= 10;
                
                int realX = (int) (x + 0);
                int realY = (int) (y + 250);
                
                g.fillOval((int) realX, realY, 1, 1);
                
            }
            
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
        
        bf.show();
        Toolkit.getDefaultToolkit().sync();
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
        /*
        final Object obj = e.getSource();
        
        if (obj == amplitudeSpinner) {
            amplitude = (int) amplitudeSpinner.getValue();
            
        } else if (obj == frequencySpinner) {
            frequency = (int) frequencySpinner.getValue();
            
        } else if (obj == speedSpinner) {
            speed = (int) speedSpinner.getValue();
            
        } else if (obj == dotSpaceSpinner) {
            spacing = (int) dotSpaceSpinner.getValue();
        }*/
    }
}