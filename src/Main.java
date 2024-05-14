import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int CARD_WIDTH = 72;
    private static final int CARD_HEIGHT = 96;
    private static final int ANIMATION_DELAY = 50;
    private static final int SHUFFLE_ITERATIONS = 20;
    private static final int ROWS = 4;
    private static final int COLUMNS = 13;

    private List<String> deck;
    private List<JLabel> cardLabels;
    private JPanel cardPanel;
    private Timer timer;
    private int iteration;
    private long startTime;

    public Main() {
        setTitle("Card Shuffler");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        deck = createDeck();
        cardLabels = new ArrayList<>();
        cardPanel = new JPanel(new GridLayout(ROWS, COLUMNS, 10, 10));
        cardPanel.setBackground(new Color(39, 119, 20));
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.setFont(new Font("Arial", Font.BOLD, 20));
        shuffleButton.setForeground(Color.WHITE);
        shuffleButton.setBackground(new Color(237, 85, 59));
        shuffleButton.setFocusPainted(false);
        shuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startShuffle();
            }
        });
        add(shuffleButton, BorderLayout.SOUTH);

        iteration = 0;
        timer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iteration < SHUFFLE_ITERATIONS) {
                    shuffleDeck();
                    iteration++;
                } else {
                    stopShuffle();
                }
            }
        });
    }

    private List<String> createDeck() {
        List<String> suits = List.of("Hearts", "Diamonds", "Clubs", "Spades");
        List<String> ranks = List.of("2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace");
        List<String> deck = new ArrayList<>();
        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(rank + " of " + suit);
            }
        }
        return deck;
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
        updateCardLabels();
    }

    private void updateCardLabels() {
        cardPanel.removeAll();
        cardLabels.clear();
        for (String card : deck) {
            JPanel cardWrapper = new JPanel(new GridBagLayout());
            cardWrapper.setBackground(new Color(39, 119, 20));
            cardWrapper.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

            JLabel label = new JLabel(card);
            label.setForeground(Color.WHITE);
            label.setFont(getFittedFont(card, CARD_WIDTH, CARD_HEIGHT));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER;
            cardWrapper.add(label, gbc);
            cardLabels.add(label);
            cardPanel.add(cardWrapper);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private Font getFittedFont(String text, int width, int height) {
        Font font = new Font("Arial", Font.PLAIN, 1);
        FontMetrics fm;
        int stringWidth = 0;
        int stringHeight = 0;

        do {
            font = new Font("Arial", Font.PLAIN, font.getSize() + 1);
            fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
            stringWidth = fm.stringWidth(text);
            stringHeight = fm.getHeight();
        } while (stringWidth < width && stringHeight < height);

        return font;
    }
    private void startShuffle() {
        iteration = 0;
        startTime = System.currentTimeMillis();
        timer.start();
    }

    private void stopShuffle() {
        timer.stop();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        double shuffleRate = (double) deck.size() / (elapsedTime / 1000.0);
        JOptionPane.showMessageDialog(this, "Shuffled " + deck.size() + " cards in: " +
                Math.ceil((double) elapsedTime / 1000) + " sec at " + shuffleRate + " cards/sec");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main gui = new Main();
                gui.setVisible(true);
            }
        });
    }
}
