package org.broad.igv.ui.color;

import org.jfree.ui.RectangleEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Simple panel for displaying color palettes
 *
 * @author Jim Robinson
 * @date 11/2/11
 */
public class ColorPanel extends JPanel implements Serializable {

    Map<String, java.util.List<Color>> colorPalette;
    List<Palette> paletteList = new ArrayList<Palette>();
    boolean showGrayScale = false;

    public ColorPanel() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                for (Palette p : paletteList) {
                    if (p.bounds.contains(mouseEvent.getPoint())) {
                        for (Swatch s : p.swatches) {
                            if (s.bounds.contains(mouseEvent.getPoint())) {
                                Color c = s.color;
                                Color newColor = JColorChooser.showDialog(ColorPanel.this, "Choose new color", c);
                                if (newColor != null) {
                                    s.color = newColor;
                                    repaint(s.bounds);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Calls the UI delegate's paint method, if the UI delegate
     * is non-<code>null</code>.  We pass the delegate a copy of the
     * <code>Graphics</code> object to protect the rest of the
     * paint code from irrevocable changes
     * (for example, <code>Graphics.translate</code>).
     * <p/>
     * If you override this in a subclass you should not make permanent
     * changes to the passed in <code>Graphics</code>. For example, you
     * should not alter the clip <code>Rectangle</code> or modify the
     * transform. If you need to do these operations you may find it
     * easier to create a new <code>Graphics</code> from the passed in
     * <code>Graphics</code> and manipulate it. Further, if you do not
     * invoker super's implementation you must honor the opaque property,
     * that is
     * if this component is opaque, you must completely fill in the background
     * in a non-opaque color. If you do not honor the opaque property you
     * will likely see visual artifacts.
     * <p/>
     * The passed in <code>Graphics</code> object might
     * have a transform other than the identify transform
     * installed on it.  In this case, you might get
     * unexpected results if you cumulatively apply
     * another transform.
     *
     * @param g the <code>Graphics</code> object to protect
     * @see #paint
     * @see javax.swing.plaf.ComponentUI
     */


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        for (Palette palette : paletteList) {
            g.setColor(Color.black);
            g.drawString(palette.label, palette.x, palette.y);
            for (Swatch swatch : palette.swatches) {
                g.setColor(swatch.color);
                g.fillRect(swatch.bounds.x, swatch.bounds.y, swatch.bounds.width, swatch.bounds.height);

                if (showGrayScale) {
                    BufferedImage image = new BufferedImage(swatch.bounds.width, swatch.bounds.height, BufferedImage.TYPE_BYTE_GRAY);
                    Graphics gi = image.getGraphics();
                    gi.setColor(swatch.color);
                    gi.fillRect(0, 0, swatch.bounds.width, swatch.bounds.height);
                    gi.dispose();

                    g.drawImage(image, swatch.bounds.x + 20, swatch.bounds.y, null);
                }
            }

        }
    }

    @Override
    public void doLayout() {
        if (colorPalette == null) {
            try {
                colorPalette = ColorUtilities.loadPalettes();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        // Lay out palletes in a grid


        int x = 0;
        int ybase = 30;

        paletteList.clear();

        int dx = showGrayScale ? 35 : 65;

        for (Map.Entry<String, List<Color>> entry : colorPalette.entrySet()) {

            x += dx;
            int y = ybase;

            int px0 = x;
            int py0 = y;
            int pw = 35;

            Palette palette = new Palette(entry.getKey(), x, y);
            paletteList.add(palette);

            y += 10;

            for (Color c : entry.getValue()) {
                y += 20;
                Rectangle r = new Rectangle(x, y, 18, 18);
                palette.swatches.add(new Swatch(r, c));
            }

            int ph = y - ybase;
            palette.setBounds(new Rectangle(px0, py0, pw, ph));

            if (x > getWidth() - 2 * dx) {
                x = 0;
                ybase = 250;
            }
        }
    }


    static class Palette {
        int x;
        int y;
        String label;
        List<Swatch> swatches;
        Rectangle bounds;

        Palette(String label, int x, int y) {
            this.label = label;
            this.x = x;
            this.y = y;
            swatches = new ArrayList<Swatch>();
        }

        public void setBounds(Rectangle bounds) {
            this.bounds = bounds;
        }
    }

    static class Swatch {
        Color color;
        Rectangle bounds;

        Swatch(Rectangle bounds, Color color) {
            this.bounds = bounds;
            this.color = color;
        }


    }
}
