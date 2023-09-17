package delta.games.lotro.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

import org.apache.log4j.Logger;

import delta.common.ui.ImageUtils;

/**
 * Image-related utilities.
 * @author MaxThlon
 */
public class LotroImageUtils extends ImageUtils
{
  static {
    IIORegistry registry = IIORegistry.getDefaultInstance();
    registry.registerServiceProvider(new com.realityinteractive.imageio.tga.TGAImageReaderSpi());
  }

  private static final Logger _logger=Logger.getLogger(LotroImageUtils.class);

  public static BufferedImage loadImageWithAlpha(File inputFile) {
    BufferedImage image = null;
    
    /*
    try {
      ImageInputStream input = ImageIO.createImageInputStream(inputFile);
      
      Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
      
      if (!readers.hasNext()) {
        throw new IllegalArgumentException("No reader for: " + inputFile);
      }
      
      ImageReader reader = readers.next();
      try {
        reader.setInput(input);
        Iterator<ImageTypeSpecifier> types = reader.getImageTypes(0);
        ImageReadParam param = reader.getDefaultReadParam();
        param.setDestinationType(reader.getRawImageType(0));
        //param.setDestinationType(ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB));
        
        image = reader. read(0, param);
      
      } finally {
        reader.dispose();
      }
    } catch(IOException ioe) {
      _logger.error("Cannot load image file: "+inputFile,ioe);
    }
    
    */
    
    
    try {
      
      image = ImageIO.read(inputFile);
    } catch (IOException ioe) {
      _logger.error("Cannot load image file: "+inputFile, ioe);
    }
    return image;
  }
}
