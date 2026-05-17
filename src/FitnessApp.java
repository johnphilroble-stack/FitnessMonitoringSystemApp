import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class FitnessApp extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    private final FitnessManager manager;


    private JTextField nameField, ageField, weightField, heightField;


    private JTextField calcWeightField, calcHeightField, calcBpmField, calcStepsField;
    private JLabel liveBmiLabel, liveHrLabel, liveCalLabel;


    private JLabel hrValLabel, bmiStripeLabel, calStripeLabel;
    private JLabel summaryAct, summaryCal, summarySteps;


    private JPanel screen3RecsPanel, screen4RecsPanel;

    public FitnessApp() {
        manager = new FitnessManager();

        setTitle("Fitness Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(375, 667);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createScreen1Register(), "Screen1");
        mainContainer.add(createScreen2Activity(), "Screen2");
        mainContainer.add(createScreen3Results(), "Screen3");
        mainContainer.add(createScreen4Final(), "Screen4");

        add(mainContainer);
        cardLayout.show(mainContainer, "Screen1");
    }


    private JPanel createScreen1Register() {
        JPanel panel = createMobilePanelBase();
        panel.add(createHeaderBar(false), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(new Color(245, 246, 251));

        JLabel title = new JLabel("Fitness Monitor", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(42, 61, 102));
        content.add(title, BorderLayout.NORTH);

        RoundedPanel whiteCard = new RoundedPanel(24, Color.WHITE);
        whiteCard.setLayout(new GridLayout(4, 1, 0, 16));
        whiteCard.setBorder(new EmptyBorder(22, 18, 22, 18));

        nameField = createStyledTextField("Enter your Name");
        ageField = createStyledTextField("Enter your Age");
        weightField = createStyledTextField("Enter your weight (kg)");
        heightField = createStyledTextField("Enter your height (m)");

        whiteCard.add(createFieldWrapper("Name", nameField));
        whiteCard.add(createFieldWrapper("Age", ageField));
        whiteCard.add(createFieldWrapper("Weight (kg)", weightField));
        whiteCard.add(createFieldWrapper("Height (m)", heightField));
        content.add(whiteCard, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);

        RoundedButton nextBtn = new RoundedButton("Next", new Color(93, 173, 226));
        nextBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manager.processUserRegistration(nameField.getText(), ageField.getText(), weightField.getText(), heightField.getText());

                calcWeightField.setText(weightField.getText());
                calcHeightField.setText(heightField.getText());
                calcBpmField.setText("75");
                calcStepsField.setText("4500");

                executeLiveScreen2Calculation("Resting");
                cardLayout.show(mainContainer, "Screen2");
            }
        });
        panel.add(nextBtn, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createScreen2Activity() {
        JPanel panel = createMobilePanelBase();
        panel.add(createHeaderBar(true), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setBackground(new Color(245, 246, 251));

        JPanel topHeaderWrapper = new JPanel(new BorderLayout(0, 8));
        topHeaderWrapper.setBackground(new Color(245, 246, 251));

        JLabel title = new JLabel("Select Activity", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(42, 61, 102));
        topHeaderWrapper.add(title, BorderLayout.NORTH);

        RoundedPanel multiInputCard = new RoundedPanel(18, Color.WHITE);
        multiInputCard.setLayout(new GridLayout(1, 4, 6, 0));
        multiInputCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        calcWeightField = new JTextField();
        calcWeightField.setHorizontalAlignment(JTextField.CENTER);
        calcWeightField.setFont(new Font("Segoe UI", Font.BOLD, 12));

        calcHeightField = new JTextField();
        calcHeightField.setHorizontalAlignment(JTextField.CENTER);
        calcHeightField.setFont(new Font("Segoe UI", Font.BOLD, 12));

        calcBpmField = new JTextField();
        calcBpmField.setHorizontalAlignment(JTextField.CENTER);
        calcBpmField.setFont(new Font("Segoe UI", Font.BOLD, 12));

        calcStepsField = new JTextField();
        calcStepsField.setHorizontalAlignment(JTextField.CENTER);
        calcStepsField.setFont(new Font("Segoe UI", Font.BOLD, 12));

        multiInputCard.add(createMiniFieldWrapper("Weight", calcWeightField));
        multiInputCard.add(createMiniFieldWrapper("Height", calcHeightField));
        multiInputCard.add(createMiniFieldWrapper("BPM", calcBpmField));
        multiInputCard.add(createMiniFieldWrapper("Steps", calcStepsField));
        topHeaderWrapper.add(multiInputCard, BorderLayout.SOUTH);
        content.add(topHeaderWrapper, BorderLayout.NORTH);

        RoundedPanel listCard = new RoundedPanel(24, Color.WHITE);
        listCard.setLayout(new GridLayout(4, 1, 0, 10));
        listCard.setBorder(new EmptyBorder(12, 14, 12, 14));

        listCard.add(createActivityRow("Resting", new Color(220, 242, 237), "C:/Users/Phil/Downloads/resting.png"));
        listCard.add(createActivityRow("Walking", new Color(220, 242, 237), "C:/Users/Phil/Downloads/walking.png"));
        listCard.add(createActivityRow("Running", new Color(249, 234, 215), "C:/Users/Phil/Downloads/running.png"));
        listCard.add(createActivityRow("Exercise", new Color(247, 214, 217), "C:/Users/Phil/Downloads/exercise.png"));
        content.add(listCard, BorderLayout.CENTER);

        RoundedPanel liveDisplayCard = new RoundedPanel(20, Color.WHITE);
        liveDisplayCard.setLayout(new BoxLayout(liveDisplayCard, BoxLayout.Y_AXIS));
        liveDisplayCard.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel heading = new JLabel("Live Calculator Metrics");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 14));
        heading.setForeground(new Color(42, 61, 102));
        liveDisplayCard.add(heading);
        liveDisplayCard.add(Box.createVerticalStrut(8));

        liveHrLabel = new JLabel("Heart Rate: -- BPM");
        liveHrLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        liveHrLabel.setForeground(new Color(231, 76, 60));

        liveBmiLabel = new JLabel("BMI Metric: --");
        liveBmiLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        liveBmiLabel.setForeground(new Color(29, 143, 131));

        liveCalLabel = new JLabel("Calorie Burn: -- kcal");
        liveCalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        liveCalLabel.setForeground(new Color(146, 92, 52));

        liveDisplayCard.add(createLiveRowWrapper(liveHrLabel, "C:/Users/Phil/Downloads/heartrate.png"));
        liveDisplayCard.add(Box.createVerticalStrut(6));
        liveDisplayCard.add(createLiveRowWrapper(liveBmiLabel, "C:/Users/Phil/Downloads/bmi.png"));
        liveDisplayCard.add(Box.createVerticalStrut(6));
        liveDisplayCard.add(createLiveRowWrapper(liveCalLabel, "C:/Users/Phil/Downloads/calories.png"));

        JPanel bottomDeck = new JPanel(new BorderLayout(0, 8));
        bottomDeck.setBackground(new Color(245, 246, 251));
        bottomDeck.add(liveDisplayCard, BorderLayout.NORTH);

        RoundedButton proceedBtn = new RoundedButton("View Dashboard Summary", new Color(93, 173, 226));
        proceedBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainContainer, "Screen3");
            }
        });
        bottomDeck.add(proceedBtn, BorderLayout.SOUTH);
        content.add(bottomDeck, BorderLayout.SOUTH);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createScreen3Results() {
        JPanel panel = createMobilePanelBase();
        panel.add(createHeaderBar(true), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(new Color(245, 246, 251));

        JLabel title = new JLabel("Fitness Results", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(42, 61, 102));
        content.add(title, BorderLayout.NORTH);

        RoundedPanel metricsCard = new RoundedPanel(24, Color.WHITE);
        metricsCard.setLayout(new BoxLayout(metricsCard, BoxLayout.Y_AXIS));
        metricsCard.setBorder(new EmptyBorder(20, 16, 20, 16));

        JLabel heartLabel = new JLabel();
        heartLabel.setPreferredSize(new Dimension(65, 65));
        heartLabel.setHorizontalAlignment(JLabel.CENTER);
        heartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon heartIcon = new ImageIcon("C:/Users/Phil/Downloads/heartrate.png");
            if (heartIcon.getIconWidth() > 0) {
                heartLabel.setIcon(new ImageIcon(heartIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) { heartLabel.setText("❤️"); }
        metricsCard.add(heartLabel);
        metricsCard.add(Box.createVerticalStrut(4));

        JLabel hrTitle = new JLabel("Heart Rate");
        hrTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hrTitle.setForeground(Color.GRAY);
        hrTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        metricsCard.add(hrTitle);

        hrValLabel = new JLabel("--- BPM", JLabel.CENTER);
        hrValLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        hrValLabel.setForeground(new Color(231, 76, 60));
        hrValLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        metricsCard.add(hrValLabel);
        metricsCard.add(Box.createVerticalStrut(15));

        bmiStripeLabel = new JLabel("BMI: --");
        bmiStripeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bmiStripeLabel.setForeground(new Color(29, 143, 131));
        JPanel bmiStripe = createStripeWrapper(bmiStripeLabel, new Color(213, 243, 233), "C:/Users/Phil/Downloads/bmi.png");

        calStripeLabel = new JLabel("Calories: --");
        calStripeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calStripeLabel.setForeground(new Color(146, 92, 52));
        JPanel calStripe = createStripeWrapper(calStripeLabel, new Color(252, 234, 210), "C:/Users/Phil/Downloads/calories.png");

        metricsCard.add(bmiStripe);
        metricsCard.add(Box.createVerticalStrut(10));
        metricsCard.add(calStripe);
        content.add(metricsCard, BorderLayout.CENTER);

        RoundedPanel bottomContainer = new RoundedPanel(0, new Color(245, 246, 251));
        bottomContainer.setLayout(new BorderLayout(0, 12));

        RoundedPanel recBlock = new RoundedPanel(20, Color.WHITE);
        recBlock.setLayout(new BoxLayout(recBlock, BoxLayout.Y_AXIS));
        recBlock.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel recTitle = new JLabel("Recommendations");
        recTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        recTitle.setForeground(new Color(42, 61, 102));
        recBlock.add(recTitle);
        recBlock.add(Box.createVerticalStrut(8));

        screen3RecsPanel = new JPanel();
        screen3RecsPanel.setLayout(new BoxLayout(screen3RecsPanel, BoxLayout.Y_AXIS));
        screen3RecsPanel.setOpaque(false);
        recBlock.add(screen3RecsPanel);

        bottomContainer.add(recBlock, BorderLayout.NORTH);

        RoundedButton nextBtn = new RoundedButton("Next", new Color(93, 173, 226));
        nextBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainContainer, "Screen4");
            }
        });
        bottomContainer.add(nextBtn, BorderLayout.SOUTH);
        content.add(bottomContainer, BorderLayout.SOUTH);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createScreen4Final() {
        JPanel panel = createMobilePanelBase();
        panel.add(createHeaderBar(true), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(new Color(245, 246, 251));

        JPanel topHeaderArea = new JPanel();
        topHeaderArea.setLayout(new BoxLayout(topHeaderArea, BoxLayout.Y_AXIS));
        topHeaderArea.setBackground(new Color(245, 246, 251));

        JLabel trophyLabel = new JLabel();
        trophyLabel.setPreferredSize(new Dimension(65, 65));
        trophyLabel.setHorizontalAlignment(JLabel.CENTER);
        trophyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon trophyIcon = new ImageIcon("C:/Users/Phil/Downloads/trophy.png");
            if (trophyIcon.getIconWidth() > 0) {
                trophyLabel.setIcon(new ImageIcon(trophyIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) { trophyLabel.setText("🏆"); }
        topHeaderArea.add(trophyLabel);

        JLabel jobDone = new JLabel("Good job today!", JLabel.CENTER);
        jobDone.setFont(new Font("Segoe UI", Font.BOLD, 22));
        jobDone.setForeground(new Color(42, 61, 102));
        jobDone.setAlignmentX(Component.CENTER_ALIGNMENT);
        topHeaderArea.add(Box.createVerticalStrut(4));
        topHeaderArea.add(jobDone);
        content.add(topHeaderArea, BorderLayout.NORTH);

        RoundedPanel gridCard = new RoundedPanel(24, Color.WHITE);
        gridCard.setLayout(new GridLayout(1, 3, 10, 0));
        gridCard.setBorder(new EmptyBorder(14, 10, 14, 10));

        summaryAct = new JLabel("0 mins", JLabel.CENTER);
        summaryCal = new JLabel("0 kcal", JLabel.CENTER);
        summarySteps = new JLabel("0", JLabel.CENTER);

        gridCard.add(createStatColumn("C:/Users/Phil/Downloads/activity.png", summaryAct, "Activity"));
        gridCard.add(createStatColumn("C:/Users/Phil/Downloads/calories.png", summaryCal, "Calories"));
        gridCard.add(createStatColumn("C:/Users/Phil/Downloads/steps.png", summarySteps, "Steps"));
        content.add(gridCard, BorderLayout.CENTER);

        JPanel bottomArea = new JPanel();
        bottomArea.setLayout(new BoxLayout(bottomArea, BoxLayout.Y_AXIS));
        bottomArea.setBackground(new Color(245, 246, 251));

        RoundedPanel recBorderFrame = new RoundedPanel(24, Color.WHITE);
        recBorderFrame.setCustomBorder(new Color(93, 173, 226), 2);
        recBorderFrame.setLayout(new BoxLayout(recBorderFrame, BoxLayout.Y_AXIS));
        recBorderFrame.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel recTitle = new JLabel("Recommendations");
        recTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recTitle.setForeground(new Color(42, 61, 102));
        recBorderFrame.add(recTitle);
        recBorderFrame.add(Box.createVerticalStrut(10));

        screen4RecsPanel = new JPanel();
        screen4RecsPanel.setLayout(new BoxLayout(screen4RecsPanel, BoxLayout.Y_AXIS));
        screen4RecsPanel.setOpaque(false);
        recBorderFrame.add(screen4RecsPanel);
        bottomArea.add(recBorderFrame);
        bottomArea.add(Box.createVerticalStrut(12));

        RoundedPanel pushBanner = new RoundedPanel(14, new Color(220, 242, 237));
        pushBanner.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel pushLbl = new JLabel("🔥  Keep pushing! You're doing great!");
        pushLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pushLbl.setForeground(new Color(28, 140, 120));
        pushBanner.add(pushLbl);
        bottomArea.add(pushBanner);

        content.add(bottomArea, BorderLayout.SOUTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void executeLiveScreen2Calculation(String activityName) {
        manager.calculateFitness(calcWeightField.getText(), calcHeightField.getText(), calcBpmField.getText(), calcStepsField.getText(), activityName);
        UserMetrics data = manager.getMetrics();

        liveHrLabel.setText("Heart Rate: " + data.getHeartRate() + " BPM (" + data.getHeartRateStatus() + ")");
        liveBmiLabel.setText("BMI Metric: " + data.getBmi() + " (" + data.getBmiStatus() + ")");
        liveCalLabel.setText("Calorie Burn: " + data.getCalories() + " kcal");

        hrValLabel.setText(data.getHeartRate() + " BPM (" + data.getHeartRateStatus() + ")");
        bmiStripeLabel.setText("BMI: " + data.getBmi() + " (" + data.getBmiStatus() + ")");
        calStripeLabel.setText("Calories: " + data.getCalories() + " kcal");

        summaryAct.setText(data.getSelectedActivity().equals("Resting") ? "Rest" : "30 mins");
        summaryCal.setText(data.getCalories() + " kcal");
        summarySteps.setText(String.format("%,d", data.getSteps()));

        String[] freshTips = manager.generatePersonalizedAdvisories();
        repopulateAdvisoryPanel(screen3RecsPanel, freshTips);
        repopulateAdvisoryPanel(screen4RecsPanel, freshTips);
    }

    private void repopulateAdvisoryPanel(JPanel targetPanel, String[] tips) {
        targetPanel.removeAll();
        for (String tip : tips) {
            JLabel lbl = new JLabel();
            JPanel formattedRow = createRowWrapperWithBullet(lbl);
            lbl.setText("<html>" + tip + "</html>");
            targetPanel.add(formattedRow);
            targetPanel.add(Box.createVerticalStrut(6));
        }
        targetPanel.revalidate();
        targetPanel.repaint();
    }

    private JPanel createRowWrapperWithBullet(JLabel textLabel) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));

        JPanel bullet = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(213, 243, 233));
                g2.fill(new Ellipse2D.Double(0, 2, 14, 14));
                g2.setColor(new Color(29, 143, 131));
                g2.fill(new Ellipse2D.Double(4, 6, 6, 6));
                g2.dispose();
            }
        };
        bullet.setOpaque(false);
        bullet.setPreferredSize(new Dimension(14, 20));

        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textLabel.setForeground(new Color(92, 98, 135));

        row.add(bullet, BorderLayout.WEST);
        row.add(textLabel, BorderLayout.CENTER);
        return row;
    }

    private JPanel createMobilePanelBase() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(245, 246, 251));
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        return panel;
    }

    private JPanel createHeaderBar(boolean showBack) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(245, 246, 251));
        bar.setPreferredSize(new Dimension(0, 35));

        if (showBack) {
            JLabel back = new JLabel("←");
            back.setFont(new Font("Segoe UI", Font.BOLD, 20));
            back.setForeground(new Color(42, 61, 102));
            back.setCursor(new Cursor(Cursor.HAND_CURSOR));
            back.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { cardLayout.previous(mainContainer); }
            });
            bar.add(back, BorderLayout.WEST);
        }
        JLabel dots = new JLabel("•••");
        dots.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dots.setForeground(Color.LIGHT_GRAY);
        bar.add(dots, BorderLayout.EAST);
        return bar;
    }

    private JPanel createFieldWrapper(String labelText, JTextField field) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 4));
        wrapper.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(110, 120, 140));
        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createMiniFieldWrapper(String labelText, JTextField field) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 2));
        wrapper.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(labelText, JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(110, 120, 140));
        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        tf.setForeground(Color.LIGHT_GRAY);
        tf.setBackground(new Color(240, 242, 248));
        tf.setBorder(new EmptyBorder(5, 8, 5, 8));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(0, 44));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(new Color(42, 61, 102)); }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(Color.LIGHT_GRAY); }
            }
        });
        return tf;
    }

    private JPanel createActivityRow(String name, Color bg, String imagePath) {
        JPanel row = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 56));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftGroup.setOpaque(false);

        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(36, 36));
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getIconWidth() > 0) iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH)));
        } catch (Exception e) { iconLabel.setText("⚪"); }
        leftGroup.add(iconLabel);

        JLabel label = new JLabel(name);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(42, 61, 102));
        leftGroup.add(label);

        row.add(leftGroup, BorderLayout.WEST);
        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                executeLiveScreen2Calculation(name);
                cardLayout.show(mainContainer, "Screen3");
            }
        });
        return row;
    }

    private JPanel createStripeWrapper(JLabel textLabel, Color bg, String imagePath) {
        JPanel stripe = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        stripe.setOpaque(false);
        stripe.setMaximumSize(new Dimension(Short.MAX_VALUE, 42));

        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(24, 24));
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getIconWidth() > 0) iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        } catch (Exception e) { iconLabel.setText("🔸"); }

        stripe.add(iconLabel);
        stripe.add(textLabel);
        return stripe;
    }

    private JPanel createLiveRowWrapper(JLabel textLabel, String imagePath) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(20, 20));
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getIconWidth() > 0) imgLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imgLabel.setText("▫️"); }

        row.add(imgLabel);
        row.add(textLabel);
        return row;
    }

    private JPanel createStatColumn(String imagePath, JLabel valLabel, String title) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(32, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getIconWidth() > 0) iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        } catch (Exception e) { iconLabel.setText("🔹"); }

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tLbl.setForeground(Color.GRAY);
        tLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valLabel.setForeground(new Color(42, 61, 102));
        valLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        col.add(iconLabel);
        col.add(Box.createVerticalStrut(4));
        col.add(tLbl);
        col.add(Box.createVerticalStrut(2));
        col.add(valLabel);
        return col;
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;
        private Color borderCol = null;
        private int borderThickness = 0;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        public void setCustomBorder(Color color, int thickness) {
            this.borderCol = color;
            this.borderThickness = thickness;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            if (borderCol != null) {
                g2.setColor(borderCol);
                g2.setStroke(new BasicStroke(borderThickness));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, radius, radius));
            }
            g2.dispose();
        }
    }

    private static class RoundedButton extends JPanel {
        private final String text;
        private final Color baseColor;

        public RoundedButton(String text, Color baseColor) {
            this.text = text;
            this.baseColor = baseColor;
            setOpaque(false);
            setPreferredSize(new Dimension(0, 44));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(baseColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }
}