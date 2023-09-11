package delta.games.lotro.ui.flatlaf;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLaf;

@SuppressWarnings("serial")
public class FlatLotroLaf extends FlatLaf
{
  public static final String NAME = "FlatLaf Lotro";

  /**
   * Sets the application look and feel to this LaF
   * using {@link UIManager#setLookAndFeel(javax.swing.LookAndFeel)}.
   *
   * @since 1.2
   */
  public static boolean setup() {
    return setup( new FlatLotroLaf() );
  }

  /**
   * Adds this look and feel to the set of available look and feels.
   * <p>
   * Useful if your application uses {@link UIManager#getInstalledLookAndFeels()}
   * to query available LaFs and display them to the user in a combobox.
   */
  public static void installLafInfo() {
    installLafInfo( NAME, FlatLotroLaf.class );
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDescription() {
    return "FlatLaf Lotro Look and Feel";
  }

  @Override
  public boolean isDark() {
    return true;
  }

}
