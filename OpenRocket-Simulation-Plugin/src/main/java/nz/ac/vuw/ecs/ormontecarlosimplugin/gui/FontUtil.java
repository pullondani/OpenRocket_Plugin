package nz.ac.vuw.ecs.ormontecarlosimplugin.gui;

import javax.swing.JLabel;
import java.awt.Font;

/**
 * Utility class for getting fonts.
 * @author Thomas Page
 */
public class FontUtil {
    private static final String defaultFontFamily = new JLabel().getFont().getFamily();

    /**
     * Text font.
     *
     * @return font
     */
    public static Font getDefaultFont() {
        return new Font(defaultFontFamily, Font.PLAIN, 11);
    }

    /**
     * For headings.
     *
     * @return font
     */
    public static Font getHeadingFont() {
        return new Font(defaultFontFamily, Font.BOLD, 12);
    }
}
