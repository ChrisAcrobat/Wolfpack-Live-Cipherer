package se.olofsson.wolfpack.livecipherer;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

/**
 * Created on 2016-06-05.
 *
 * @author Christoffer Olofsson
 */
public class LiveCipher extends JFrame {
    private final String DEFAULT_TITLE = "Wolfpack: Live Cipherer";

    private JPanel pnlRoot;
    private JTextArea txtUpper;
    private JTextArea txtLower;
    private JSpinner spnRollerLeft;
    private JSpinner spnRollerMiddle;
    private JSpinner spnRollerRight;
    private JCheckBox cbxPrivateKey;
    private JMenuBar jMenuBar;
    public static final String REGEX_UNSUPPORTED_CHARS = "[^A-Z0-9]";
    public static final String REGEX_UNSUPPORTED_CIPHER_CHARS = "[^A-Z]";

    public LiveCipher() {
        setContentPane(pnlRoot);
        setTitle(DEFAULT_TITLE);
        setIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("submarine-icon.png")).getImage());

        // Add menu-bar
        jMenuBar = new JMenuBar();
        {
            jMenuBar.setVisible(false);
            this.setJMenuBar(jMenuBar);

            // Add GitHub-button
            JMenu gitHub = new JMenu("GitHub");
            {
                gitHub.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                try {
                                    Desktop.getDesktop().browse(new URI("https://github.com/ChrisAcrobat/Wolfpack-Live-Cipherer/"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });
                jMenuBar.add(gitHub);
            }

            // Add GitHub-button
            JMenu submarineIcon = new JMenu("Submarine-icon by Elegantthemes");
            {
                submarineIcon.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                try {
                                    Desktop.getDesktop().browse(new URI("http://www.iconarchive.com/show/beautiful-flat-one-color-icons-by-elegantthemes/submarine-icon.html"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });
                jMenuBar.add(submarineIcon);
            }
        }

        // Check for new release.
        new UpdateChecker(this).start();

        // Initiate rollers
        Roller roller3 = new Roller(spnRollerLeft, null, 3);
        Roller roller2 = new Roller(spnRollerMiddle, roller3, 2);
        Roller roller1 = new Roller(spnRollerRight, roller2, 1);

        Roller[] rollerList = new Roller[]{roller1, roller2, roller3};
        primeWheels(rollerList);
        new TextFormatter(roller1, cbxPrivateKey, txtUpper, txtLower).start();

        class UppercaseDocumentFilter extends DocumentFilter {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                fb.insertString(offset, text.toUpperCase().replaceAll(REGEX_UNSUPPORTED_CHARS, " "), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                fb.replace(offset, length, text.toUpperCase().replaceAll(REGEX_UNSUPPORTED_CHARS, " "), attrs);
            }
        }

        UppercaseDocumentFilter uppercaseDocumentFilter = new UppercaseDocumentFilter();

        AbstractDocument txtUpperDocument = (AbstractDocument) txtUpper.getDocument();
        txtUpperDocument.setDocumentFilter(uppercaseDocumentFilter);

        AbstractDocument txtLowerDocument = (AbstractDocument) txtLower.getDocument();
        txtLowerDocument.setDocumentFilter(uppercaseDocumentFilter);

        // Finalize frame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add event handlers
        Toolkit.getDefaultToolkit().addAWTEventListener(this::mouseEvent, AWTEvent.MOUSE_EVENT_MASK);
    }

    private void primeWheels(Roller[] rollerList) {
        int[] primeList = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397};
        for (int index = 0; index < primeList.length; index++) {
            rollerList[index % 3].addPrime(primeList[index]);
        }
        for (Roller roller : rollerList) {
            if (!roller.validate()) {
                System.err.println(roller + ": NOT VALID!");
            }
        }
    }

    private void mouseEvent(AWTEvent event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;

            if (SwingUtilities.isRightMouseButton(mouseEvent) && mouseEvent.isPopupTrigger()) {
                toggleMenuBar();
            }
        }
    }

    private void toggleMenuBar() {
        jMenuBar.setVisible(!jMenuBar.isVisible());
        setTitle(DEFAULT_TITLE + (jMenuBar.isVisible() ? " (" + Main.CURRENT_VERSION + ")" : ""));
    }

    public void addLinkToNewRelease(JMenu jMenu) {
        jMenuBar.add(Box.createHorizontalGlue());
        jMenuBar.add(jMenu);
        toggleMenuBar();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        pnlRoot = new JPanel();
        pnlRoot.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        pnlRoot.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        spnRollerLeft = new JSpinner();
        panel1.add(spnRollerLeft, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spnRollerMiddle = new JSpinner();
        panel1.add(spnRollerMiddle, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spnRollerRight = new JSpinner();
        panel1.add(spnRollerRight, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbxPrivateKey = new JCheckBox();
        cbxPrivateKey.setEnabled(true);
        cbxPrivateKey.setText("Use private key");
        cbxPrivateKey.setToolTipText("Use first three characters as new private key.");
        cbxPrivateKey.setVisible(true);
        panel1.add(cbxPrivateKey, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        pnlRoot.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(400, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtUpper = new JTextArea();
        Font txtUpperFont = this.$$$getFont$$$("Consolas", -1, -1, txtUpper.getFont());
        if (txtUpperFont != null) txtUpper.setFont(txtUpperFont);
        txtUpper.setLineWrap(true);
        txtUpper.setWrapStyleWord(true);
        scrollPane1.setViewportView(txtUpper);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtLower = new JTextArea();
        Font txtLowerFont = this.$$$getFont$$$("Consolas", -1, -1, txtLower.getFont());
        if (txtLowerFont != null) txtLower.setFont(txtLowerFont);
        txtLower.setLineWrap(true);
        txtLower.setWrapStyleWord(true);
        scrollPane2.setViewportView(txtLower);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return pnlRoot;
    }
}
