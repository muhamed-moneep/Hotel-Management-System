package hotel.management.system;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author moham
 */
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Dashboard extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dashboard.class.getName());
    private String role;

    // ── NEW fields (my additions) ────────────────────────────────────────────
    // Number labels that sit inside each stat panel
    private JLabel lblTotal, lblAvailable, lblOccupied, lblBooking, lblRevenue;
    // Text area for Near Checkout panel
    private JTextArea checkoutArea;
    // Inner chart panel reference so we can repaint it
    private ServicesChartPanel chartPanel;
    // Timer that ticks every 3 seconds and refreshes everything
    private javax.swing.Timer refreshTimer;
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        this(null);
    }

    public Dashboard(String role) {
        this.role = role;
        initComponents();
        this.setLocationRelativeTo(null);
        setupDashboard(); // ← MY ADDITION: hook in all live panels
    }
 private void setupDashboard() {
        setupStatPanels();
        setupCheckoutPanel();
        setupChartPanel();
        setupRoomGrid();

        // Auto-refresh every 3 seconds
       refreshTimer = new javax.swing.Timer(3000, e -> refreshAll());
        refreshTimer.start();

        // Do one immediate refresh so numbers appear right away
        refreshAll();
    }

    

    private void setupStatPanels() {
        lblTotal   = addNumberToStatPanel(jPanel1);
        lblAvailable = addNumberToStatPanel(jPanel2);
        lblOccupied  = addNumberToStatPanel(jPanel3);
        lblBooking   = addNumberToStatPanel(jPanel4);
        lblRevenue   = addNumberToStatPanel(jPanel5);
    }

    /** Changes a stat panel to 2-row layout and returns the new number label. */
   private javax.swing.JLabel addNumberToStatPanel(javax.swing.JPanel panel) {
        panel.setLayout(new GridLayout(2, 1, 0, 2));
        JLabel numLabel = new JLabel("—", SwingConstants.CENTER);
        numLabel.setFont(new Font("Book Antiqua", Font.BOLD, 26));
        numLabel.setForeground(new Color(255, 255, 255));
        panel.add(numLabel);
        return numLabel;
    }

    /** Reads rooms.txt and updates all 5 stat counters. */
    private void refreshStats() {
        java.util.List<String[]> rooms = readRoomsTxt();
        int total     = rooms.size();
        int available = 0;
        int reserved  = 0;
        double revenue = 0;

        for (String[] r : rooms) {
            // rooms.txt format: roomNumber,type,floors,beds,view,price,status
            if (r.length >= 7) {
                String status = r[6].trim();
                if (status.equalsIgnoreCase("Available")) available++;
                else reserved++;

                // Add room price as simple revenue indicator
                try { revenue += Double.parseDouble(r[5].trim()); }
                catch (NumberFormatException ignored) {}
            }
        }

        lblTotal.setText(String.valueOf(total));
        lblAvailable.setText(String.valueOf(available));
        lblOccupied.setText(String.valueOf(reserved));
        lblBooking.setText(String.valueOf(reserved));           // same as occupied for now
        lblRevenue.setText(String.format("$%.0f", revenue));
    }

    // ── 2. NEAR CHECKOUT PANEL (jPanel6) ─────────────────────────────────────

    private void setupCheckoutPanel() {
    jPanel6.removeAll();
    jPanel6.setLayout(new BorderLayout(4, 4));
    jPanel6.setBackground(new Color(165, 148, 135));
    jPanel6.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(139, 115, 91), 2),
        BorderFactory.createEmptyBorder(6, 6, 6, 6)
    ));

    javax.swing.JLabel title = new javax.swing.JLabel("  Near Checkout", javax.swing.SwingConstants.LEFT); // ← HERE
    title.setFont(new Font("Book Antiqua", Font.BOLD, 14));
    title.setForeground(Color.WHITE);
    jPanel6.add(title, BorderLayout.NORTH);
        checkoutArea = new JTextArea();
        checkoutArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        checkoutArea.setBackground(new Color(190, 175, 155));
        checkoutArea.setForeground(new Color(50, 30, 10));
        checkoutArea.setEditable(false);
        checkoutArea.setMargin(new Insets(6, 6, 6, 6));

        JScrollPane scroll = new JScrollPane(checkoutArea);
        scroll.setBorder(null);
        jPanel6.add(scroll, BorderLayout.CENTER);
    }

    /** Reads reservations.txt and shows guests checking out within 2 days. */
    private void refreshCheckout() {
    java.util.List<String[]> allReservations = new ArrayList<>();

    // Read reservations.txt
    // Expected format: guestName,roomNumber,checkIn,checkOut,services,total,status
    try (BufferedReader br = new BufferedReader(new FileReader("reservations.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) allReservations.add(parts);
            }
        }
    } catch (IOException e) {
        checkoutArea.setText("No reservations file found yet.");
        return;
    }

    if (allReservations.isEmpty()) {
        checkoutArea.setText("No reservations yet.");
        return;
    }

    // Sort by checkout date (field[3]) — nearest first
    allReservations.sort((a, b) -> a[3].trim().compareTo(b[3].trim()));

    StringBuilder sb = new StringBuilder();
    java.time.LocalDate today = java.time.LocalDate.now();

    for (String[] r : allReservations) {
        try {
            java.time.LocalDate checkout = java.time.LocalDate.parse(r[3].trim());
            if (checkout.isBefore(today)) continue; // skip past checkouts

            long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, checkout);

            // CHANGE 5: Only show reservations with 2 days or LESS remaining
            if (daysLeft > 2) continue;

            sb.append("Guest : ").append(r[0].trim()).append("\n");
            sb.append("Room  : ").append(r[1].trim()).append("\n");
            sb.append("Checkout: ").append(r[3].trim()).append("\n");
            if (daysLeft == 0)
                sb.append("⚠ Checking out TODAY!\n");
            else if (daysLeft == 1)
                sb.append("⚠ Checking out TOMORROW!\n");
            else
                sb.append("Days left: ").append(daysLeft).append("\n");
            sb.append("─────────────────\n");
        } catch (Exception ignored) {}
    }

    if (sb.length() == 0) sb.append("No checkouts within the next 2 days.");
    checkoutArea.setText(sb.toString());
    checkoutArea.setCaretPosition(0);
}
    // ── 3. SERVICES CHART PANEL (jPanel7) ────────────────────────────────────

    private void setupChartPanel() {
        jPanel7.removeAll();
        jPanel7.setLayout(new BorderLayout());
        jPanel7.setBackground(new Color(165, 148, 135));
        jPanel7.setBounds(800, 170, 580, 298);
        chartPanel = new ServicesChartPanel();
        jPanel7.add(chartPanel, BorderLayout.CENTER);
    }

    // ── 4. ROOM GRID (jPanel18) ───────────────────────────────────────────────

    /** Clears the static labels from initComponents and adds live colored tiles. */
    private void refreshRoomGrid() {
        jPanel18.removeAll();
        jPanel18.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        jPanel18.setBackground(new Color(139, 115, 91));

        java.util.List<String[]> rooms = readRoomsTxt();

        if (rooms.isEmpty()) {
            JLabel empty = new JLabel("No room data found in rooms.txt");
            empty.setForeground(Color.WHITE);
            jPanel18.add(empty);
        }

        for (String[] r : rooms) {
            if (r.length < 7) continue;

            String roomNum  = r[0].trim();
            String type     = r[1].trim().toUpperCase();
            String status   = r[6].trim();

            boolean isAvailable = status.equalsIgnoreCase("Available");

            // Colored tile panel
            JPanel tile = new JPanel(new GridLayout(3, 1, 0, 2));
            tile.setPreferredSize(new Dimension(100, 90));

            // GREEN for available, RED for occupied/reserved
            Color tileColor = isAvailable
                ? new Color(60, 140, 80)    // green
                : new Color(180, 50, 50);   // red

            tile.setBackground(tileColor);
            tile.setBorder(BorderFactory.createLineBorder(tileColor.darker(), 2));

            JLabel numLbl = new JLabel(roomNum, SwingConstants.CENTER);
            numLbl.setFont(new Font("Book Antiqua", Font.BOLD, 14));
            numLbl.setForeground(Color.WHITE);

            JLabel typeLbl = new JLabel(type, SwingConstants.CENTER);
            typeLbl.setFont(new Font("Book Antiqua", Font.PLAIN, 11));
            typeLbl.setForeground(new Color(230, 230, 230));

            JLabel statusLbl = new JLabel(isAvailable ? "● Free" : "● Taken", SwingConstants.CENTER);
            statusLbl.setFont(new Font("Book Antiqua", Font.PLAIN, 10));
            statusLbl.setForeground(isAvailable ? new Color(180, 255, 180) : new Color(255, 180, 180));

            tile.add(numLbl);
            tile.add(typeLbl);
            tile.add(statusLbl);

            tile.setToolTipText("Room " + roomNum + " | " + type + " | " + status);

            jPanel18.add(tile);
        }

        jPanel18.revalidate();
        jPanel18.repaint();
    }

    // ── REFRESH ALL ───────────────────────────────────────────────────────────

    /** Called by the timer and on startup — refreshes every live section. */
    private void refreshAll() {
        refreshStats();
        refreshCheckout();
        if (chartPanel != null) chartPanel.refresh(); // reread services.txt and repaint
        refreshRoomGrid();
    }

    // ── FILE READING HELPERS ──────────────────────────────────────────────────

    /**
     * Reads rooms.txt and returns each line split by comma.
     * Format: roomNumber,type,floors,beds,view,price,status
     */
    private java.util.List<String[]> readRoomsTxt() {
        java.util.List<String[]> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) result.add(parts);
                }
            }
        } catch (IOException e) {
            // File not found is fine — just return empty list
        }
        return result;
    }

    private void setupRoomGrid() {
    refreshRoomGrid();}

    // ── INNER CLASS: SERVICES BAR CHART ──────────────────────────────────────
    /**
     * A JPanel that draws a bar chart of service usage counts.
     * Reads services.txt every time refresh() is called.
     * services.txt format: name,category,type,price,available,usageCount
     */
    private class ServicesChartPanel extends JPanel {

        // Bar colors — warm hotel palette
        private static final Color[] BAR_COLORS = {
            new Color(180, 140, 80),   // gold
            new Color(100, 150, 120),  // sage
            new Color(160, 110, 90),   // terracotta
            new Color(90, 120, 160),   // slate blue
            new Color(200, 165, 100),  // sand
            new Color(140, 90, 130),   // mauve
        };

        private Map<String, Integer> data = new LinkedHashMap<>();

        public ServicesChartPanel() {
            setBackground(new Color(165, 148, 135));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(139, 115, 91), 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
            refresh();
        }

        /** Re-reads services.txt and repaints the chart. */
        public void refresh() {
            data.clear();
            try (BufferedReader br = new BufferedReader(new FileReader("services.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(",");
                    if (parts.length >= 1) {
                        String name = parts[0].trim();
                        int count = 0;
                        // usageCount is field[5] if it exists
                        if (parts.length >= 6) {
                            try { count = Integer.parseInt(parts[5].trim()); }
                            catch (NumberFormatException ignored) {}
                        }
                        data.put(name, count);
                    }
                }
            } catch (IOException e) {
                // File missing — show empty chart
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int padBottom = 50;  // space for labels
            int padTop    = 30;  // space above bars
            int padSide   = 20;

            // Chart title
            g2.setFont(new Font("Book Antiqua", Font.BOLD, 13));
            g2.setColor(Color.WHITE);
            g2.drawString("Services Usage", padSide, 20);

            if (data.isEmpty()) {
                g2.setFont(new Font("Book Antiqua", Font.ITALIC, 12));
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawString("No services data.", padSide, h / 2);
                return;
            }

            String[] names  = data.keySet().toArray(new String[0]);
            Integer[] counts = data.values().toArray(new Integer[0]);

            int maxVal = Arrays.stream(counts).mapToInt(i -> i).max().orElse(1);
            if (maxVal == 0) maxVal = 1;

            int chartH = h - padBottom - padTop;
            int totalBars = names.length;
            int barW = Math.max(18, (w - 2 * padSide) / (totalBars * 2));
            int gap  = barW;

            // Baseline
            g2.setColor(new Color(200, 185, 155));
            g2.drawLine(padSide, h - padBottom, w - padSide, h - padBottom);

            for (int i = 0; i < totalBars; i++) {
                int barH = (int) ((double) counts[i] / maxVal * chartH);
                int x = padSide + i * (barW + gap);
                int y = h - padBottom - barH;

                // Shadow
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(x + 3, y + 3, barW, barH, 6, 6);

                // Main bar
                Color c = BAR_COLORS[i % BAR_COLORS.length];
                g2.setColor(c);
                g2.fillRoundRect(x, y, barW, barH, 6, 6);

                // Top highlight (3D feel)
                g2.setColor(c.brighter());
                g2.fillRoundRect(x, y, barW, 5, 4, 4);

                // Count on top
                g2.setFont(new Font("Book Antiqua", Font.BOLD, 11));
                g2.setColor(Color.WHITE);
                String cs = String.valueOf(counts[i]);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(cs, x + (barW - fm.stringWidth(cs)) / 2, Math.max(y - 4, padTop));

                // Service name below baseline (rotated 30°)
                g2.setFont(new Font("Book Antiqua", Font.PLAIN, 10));
                Graphics2D g2r = (Graphics2D) g2.create();
                g2r.setColor(new Color(230, 220, 200));
                g2r.translate(x + barW / 2, h - padBottom + 8);
                g2r.rotate(Math.toRadians(30));
                g2r.drawString(names[i], 0, 0);
                g2r.dispose();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(165, 148, 135));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 5, 0, new java.awt.Color(0, 51, 153)));
        jPanel1.setLayout(new java.awt.GridLayout(1, 5, 16, 0));

        jLabel1.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Total Room");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 200, 100));

        jPanel2.setBackground(new java.awt.Color(165, 148, 135));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 5, 0, new java.awt.Color(0, 153, 0)));
        jPanel2.setLayout(new java.awt.GridLayout(1, 5, 16, 0));

        jLabel2.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Available");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel2);

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 200, 100));

        jPanel3.setBackground(new java.awt.Color(165, 148, 135));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 5, 0, new java.awt.Color(204, 0, 0)));
        jPanel3.setLayout(new java.awt.GridLayout(1, 5, 16, 0));

        jLabel3.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Occupied");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel3.add(jLabel3);

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 40, 200, 100));

        jPanel4.setBackground(new java.awt.Color(165, 148, 135));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 5, 0, new java.awt.Color(255, 153, 51)));
        jPanel4.setLayout(new java.awt.GridLayout(1, 5, 16, 0));

        jLabel4.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Booking");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel4.add(jLabel4);

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 40, 200, 100));

        jPanel5.setBackground(new java.awt.Color(165, 148, 135));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 5, 0, new java.awt.Color(255, 255, 40)));
        jPanel5.setLayout(new java.awt.GridLayout(1, 5, 16, 0));

        jLabel5.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Revenue");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel5.add(jLabel5);

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 40, 200, 100));

        jLabel6.setText("Near Checkout");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(358, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 500, 300));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 551, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 170, 580, 298));

        jPanel18.setBackground(new java.awt.Color(139, 115, 91));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Room image2.jpg"))); // NOI18N
        jLabel8.setText("103/ SUI");
        jLabel8.setAutoscrolls(true);
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel8.setIconTextGap(10);
        jLabel8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 6, -1, -1));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/images 2.jpg"))); // NOI18N
        jLabel9.setText("104/ FAM");
        jLabel9.setAutoscrolls(true);
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel9.setIconTextGap(10);
        jLabel9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 6, -1, 91));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Room image2.jpg"))); // NOI18N
        jLabel10.setText("303/ SUI");
        jLabel10.setAutoscrolls(true);
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel10.setIconTextGap(10);
        jLabel10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(921, 0, 109, 109));

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Room image.jpg"))); // NOI18N
        jLabel11.setText("102/DOU");
        jLabel11.setAutoscrolls(true);
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel11.setIconTextGap(10);
        jLabel11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(201, 0, 108, -1));

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/images 2.jpg"))); // NOI18N
        jLabel12.setText("101/ SIN");
        jLabel12.setAutoscrolls(true);
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel12.setIconTextGap(10);
        jLabel12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 106, 112, 101));

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/images 2.jpg"))); // NOI18N
        jLabel13.setText("301/ SIN");
        jLabel13.setAutoscrolls(true);
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel13.setIconTextGap(10);
        jLabel13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(767, 6, 90, 85));

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Room image.jpg"))); // NOI18N
        jLabel14.setText("202/ DOU");
        jLabel14.setAutoscrolls(true);
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel14.setIconTextGap(10);
        jLabel14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(209, 107, -1, 100));

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/images 2.jpg"))); // NOI18N
        jLabel15.setText("204/ FAM");
        jLabel15.setAutoscrolls(true);
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel15.setIconTextGap(10);
        jLabel15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 113, -1, 95));

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Room image.jpg"))); // NOI18N
        jLabel16.setText("302/ FAM");
        jLabel16.setAutoscrolls(true);
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel16.setIconTextGap(10);
        jLabel16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(767, 103, -1, -1));

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/images 2.jpg"))); // NOI18N
        jLabel17.setText("401/ DOU");
        jLabel17.setAutoscrolls(true);
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel17.setIconTextGap(10);
        jLabel17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(921, 115, 109, -1));

        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Room image.jpg"))); // NOI18N
        jLabel18.setText("402/ SUI");
        jLabel18.setAutoscrolls(true);
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel18.setIconTextGap(10);
        jLabel18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1107, 0, -1, -1));

        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Room image2.jpg"))); // NOI18N
        jLabel19.setText("403/ SIN");
        jLabel19.setAutoscrolls(true);
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel19.setIconTextGap(10);
        jLabel19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1107, 107, -1, -1));

        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Room image2.jpg"))); // NOI18N
        jLabel20.setText("203/ SUI");
        jLabel20.setAutoscrolls(true);
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel20.setIconTextGap(10);
        jLabel20.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 107, -1, -1));

        jLabel22.setBackground(new java.awt.Color(139, 115, 91));
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/images 2.jpg"))); // NOI18N
        jLabel22.setText("101/ SIN");
        jLabel22.setAutoscrolls(true);
        jLabel22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel22.setIconTextGap(10);
        jLabel22.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel18.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 10, -1, 91));

        getContentPane().add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 500, 1230, 210));

        btnBack.setBackground(new java.awt.Color(139, 115, 91));
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBackMouseExited(evt);
            }
        });
        btnBack.addActionListener(this::btnBackActionPerformed);
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/blur 2.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1421, 754));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseEntered

        btnBack.setBackground(new java.awt.Color(204,204,204));
       btnBack.setForeground(new java.awt.Color(0,0,0));
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackMouseEntered

    private void btnBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseExited

        btnBack.setBackground(new java.awt.Color(139,115,91));
      btnBack.setForeground(new java.awt.Color(255,255,255));
       
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackMouseExited

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        if ("receptionist".equalsIgnoreCase(role)) {
            new Home1("receptionist").setVisible(true);
        } else {
            new Home1(role).setVisible(true);
        }
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new Dashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables
}
