package delta.games.lotro.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;

import com.formdev.flatlaf.ui.FlatInternalFrameUI;
import com.formdev.flatlaf.ui.FlatStylingSupport.StyleableBorder;
import com.formdev.flatlaf.util.SoftCache;

public class LotroInternalFrameUI extends FlatInternalFrameUI {
  //private static final SoftCache<String, BufferedImage> imageCache = new SoftCache<>();
  
  public static ComponentUI createUI( JComponent c ) {
    return new LotroInternalFrameUI( (JInternalFrame) c );
  }

  public LotroInternalFrameUI(JInternalFrame b) {
    super(b);
  }
  
  @Override
  protected JComponent createNorthPane( JInternalFrame w ) {
    return new LotroInternalFrameTitlePane( w );
  }
  
  @Override
  public void installUI( JComponent c ) {
    super.installUI( c );
  }
  
  @SuppressWarnings("serial")
  public static class LotroInternalFrameBorder extends FlatInternalFrameBorder {
    private static final SoftCache<String, BufferedImage> imageCache = new SoftCache<>();
    
    public LotroInternalFrameBorder() {
      super();
      
      BufferedImage bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/box_topleft.tga"));
      if (bufferedImage != null) {
        imageCache.put("topleft", bufferedImage);
      }
      
      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/box_topright.tga"));
      if (bufferedImage != null) {
        imageCache.put("topright", bufferedImage);
      }
      
      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/box_bottomleft.tga"));
      if (bufferedImage != null) {
        imageCache.put("bottomleft", bufferedImage);
      }
      
      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/box_bottomright.tga"));
      if (bufferedImage != null) {
        imageCache.put("bottomright", bufferedImage);
      }
      
      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/box_top.tga"));
      if (bufferedImage != null) {
        imageCache.put("top", bufferedImage);
      }
      
      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/box_left.tga"));
      if (bufferedImage != null) {
        imageCache.put("left", bufferedImage);
      }

      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/box_right.tga"));
      if (bufferedImage != null) {
        imageCache.put("right", bufferedImage);
      }
      
      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/box_bottom.tga"));
      if (bufferedImage != null) {
        imageCache.put("bottom", bufferedImage);
      }
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      JInternalFrame jInternalFrame = (JInternalFrame) c;
      JComponent lotroInternalFrameTitlePane = ((LotroInternalFrameUI)jInternalFrame.getUI()).getNorthPane();
      Graphics2D g2 = (Graphics2D)g;
      Insets insets = getBorderInsets(c);

      BufferedImage image = imageCache.get("topleft");
      if (image != null ) {
        //new TexturePaint(bufferedImage,  new Rectangle(bufferedImage.getWidth(),bufferedImage.getHeight()
        

        int insetLeft = image.getWidth();
        int insetRight = image.getWidth();
        int insetTop = lotroInternalFrameTitlePane.getHeight() + (image.getHeight() / 2);
        int insetBottom = image.getHeight() / 2;
        
        g.drawImage(image, 0, lotroInternalFrameTitlePane.getHeight(), null);
        image = imageCache.get("topright");
        if (image != null ) g.drawImage(image, width - image.getWidth(), lotroInternalFrameTitlePane.getHeight(), null);
        image = imageCache.get("bottomleft");
        if (image != null ) g.drawImage(image, 0, height - image.getHeight() + insets.bottom, null);
        image = imageCache.get("bottomright");
        if (image != null ) g.drawImage(image, width - image.getWidth(), height - image.getHeight() + insets.top, null);
        
        image = imageCache.get("left");
        if (image != null ) {
          TexturePaint texturePaint = new TexturePaint(image, new Rectangle(0, insetTop, image.getWidth(), height - insetTop - insetBottom));
          
          g2.setPaint(texturePaint);
          g2.fillRect(0, insetTop, image.getWidth(), height - insetTop - insetBottom);
        }
        
        image = imageCache.get("right");
        if (image != null ) {
          TexturePaint texturePaint = new TexturePaint(image, new Rectangle(width - image.getWidth(), insetTop, image.getWidth(), height - insetTop - insetBottom));
          g2.setPaint(texturePaint);
          g2.fillRect(width - image.getWidth(), insetTop, image.getWidth(), height - insetTop - insetBottom);
        }
        
        image = imageCache.get("top");
        if (image != null ) {
          TexturePaint texturePaint = new TexturePaint(image, new Rectangle(insetLeft, lotroInternalFrameTitlePane.getHeight(), width - insetLeft - insetRight, image.getHeight()));
          g2.setPaint(texturePaint);
          g2.fillRect(insetLeft, lotroInternalFrameTitlePane.getHeight(), width - insetLeft - insetRight, image.getHeight());
        }
        
        image = imageCache.get("bottom");
        if (image != null ) {
          TexturePaint texturePaint = new TexturePaint(image, new Rectangle(insetLeft, height - image.getHeight() + insets.top, width - insetLeft - insetRight, image.getHeight()));
          g2.setPaint(texturePaint);
          g2.fillRect(insetLeft, height - image.getHeight() + insets.top, width - insetLeft - insetRight, image.getHeight());
        }
      } else super.paintBorder(c, g, x, y, width, height);
      
          
      /*
      Color oldColor = g.getColor();
      g.translate(x, y);

      int tileW = tileIcon.getIconWidth();
      int tileH = tileIcon.getIconHeight();
      paintEdge(c, g, 0, 0, width - insets.right, insets.top, tileW, tileH);
      paintEdge(c, g, 0, insets.top, insets.left, height - insets.top, tileW, tileH);
      paintEdge(c, g, insets.left, height - insets.bottom, width - insets.left, insets.bottom, tileW, tileH);
      paintEdge(c, g, width - insets.right, 0, insets.right, height - insets.bottom, tileW, tileH);

      g.translate(-x, -y);
      g.setColor(oldColor);
      */
    }
    
    private void paintEdge(Component c, Graphics g, int x, int y, int width, int height, int tileW, int tileH) {
      /*g = g.create(x, y, width, height);
      int sY = -(y % tileH);
      for (x = -(x % tileW); x < width; x += tileW) {
          for (y = sY; y < height; y += tileH) {
              this.tileIcon.paintIcon(c, g, x, y);
          }
      }
      g.dispose();*/
    }
  }
}
