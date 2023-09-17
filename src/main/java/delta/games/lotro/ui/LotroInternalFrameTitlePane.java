package delta.games.lotro.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.SoftCache;
import com.formdev.flatlaf.util.UIScale;

/**
 * LotroInternalFrameTitlePane used inside {@link delta.games.lotro.ui.LotroInternalFrameUI LotroInternalFrameUI}.
 * @author MaxThlon
 */
public class LotroInternalFrameTitlePane extends BasicInternalFrameTitlePane {
  private static final SoftCache<String, BufferedImage> imageCache = new SoftCache<>();

  private JLabel titleLabel;
  private int titlePaneHeight;

  public LotroInternalFrameTitlePane(JInternalFrame f) {
    super(f);
  }

  @Override
  protected void installDefaults() {
    super.installDefaults(); 
    
    BufferedImage bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/titlebar.tga"));
    if (bufferedImage != null) {
      imageCache.put("titlebar", bufferedImage);
      titlePaneHeight = bufferedImage.getHeight(); // UIManager.getInt("InternalFrame.titlePaneHeight");
      
      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/titlebar_left.tga"));
      if (bufferedImage != null) imageCache.put("titlebar_left", bufferedImage);
      
      bufferedImage = LotroImageUtils.loadImageWithAlpha(new File(System.getProperty("user.home") + "/Documents/The Lord of the Rings Online/UI/skins/JRR AzureGlass/box/titlebar_right.tga"));
      if (bufferedImage != null) imageCache.put("titlebar_right", bufferedImage);
    } else titlePaneHeight = 20;
  }

  @Override
  protected PropertyChangeListener createPropertyChangeListener() {
    return new LotroPropertyChangeHandler();
  }

  @Override
  public Dimension getPreferredSize() {
    return getMinimumSize();
  }

  @Override
  public Dimension getMinimumSize() {
    Dimension d = new Dimension(super.getMinimumSize());
    d.height = titlePaneHeight + 2;

    /*
     * XPStyle xp = XPStyle.getXP(); if (xp != null) { // Note: Don't know how to
     * calculate height on XP, // the captionbarheight is 25 but native caption is
     * 30 (maximized 26) if (frame.isMaximum()) { d.height -= 1; } else { d.height
     * += 3; } }
     */
    return d;
  }

  @Override
  protected LayoutManager createLayout() {
    return new GridBagLayout();
  }

  @Override
  protected void createButtons() {
  }
  
  @Override
  protected void addSubComponents() {
    titleLabel = new JLabel( frame.getTitle() );
    titleLabel.setFont( FlatUIUtils.nonUIResource( getFont() ) );
    titleLabel.setMinimumSize( new Dimension( UIScale.scale( 32 ), 1 ) );
    
    add(titleLabel);
    updateColors();
  }
  
  protected void updateColors() {
    //Color background = FlatUIUtils.nonUIResource( frame.isSelected() ? selectedTitleColor : notSelectedTitleColor );
    Color foreground = FlatUIUtils.nonUIResource( frame.isSelected() ? selectedTextColor : notSelectedTextColor );

    titleLabel.setForeground( foreground );
  }
  
  /**
   * Does nothing because FlatLaf internal frames do not have system menus.
   */
  @Override
  protected void assembleSystemMenu() {
  }

  /**
   * Does nothing because FlatLaf internal frames do not have system menus.
   */
  @Override
  protected void showSystemMenu() {
  }
  
  @Override
  public void paintComponent( Graphics g ) {
    paintTitleBackground( g );
  }
  
  @Override
  protected void paintTitleBackground(Graphics g) {
    BufferedImage image = imageCache.get("titlebar");

    if (image != null) {
      int middle = getWidth() / 2;
      int titlebarMiddle = image.getWidth() / 2;
      
      g.drawImage(image, middle - titlebarMiddle, 0, null);
      
      image = imageCache.get("titlebar_left");
      if (image != null) g.drawImage(image, middle - titlebarMiddle - image.getWidth(), 0, null);
      
      image = imageCache.get("titlebar_right");
      if (image != null) g.drawImage(image, middle + titlebarMiddle, 0, null);
    } else {
      super.paintTitleBackground(g);
    }
  }

  //---- class FlatPropertyChangeHandler ------------------------------------
  protected class LotroPropertyChangeHandler
    extends PropertyChangeHandler
  {
    @Override
    public void propertyChange( PropertyChangeEvent e ) {
      switch( e.getPropertyName() ) {
        case JInternalFrame.TITLE_PROPERTY:
          titleLabel.setText( frame.getTitle() );
          break;

        case JInternalFrame.IS_SELECTED_PROPERTY:
          updateColors();
          break;

        case "componentOrientation":
          applyComponentOrientation( frame.getComponentOrientation() );
          break;

        case "opaque":
          // Do not invoke super.propertyChange() here because it always
          // invokes repaint(), which would cause endless repainting.
          // The opaque flag is temporary changed in FlatUIUtils.hasOpaqueBeenExplicitlySet(),
          // invoked from FlatInternalFrameUI.update().
          return;
      }

      super.propertyChange( e );
    }
  }
}
