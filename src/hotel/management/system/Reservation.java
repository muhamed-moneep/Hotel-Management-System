package hotel.management.system;

import java.awt.Color;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Reservation extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(Reservation.class.getName());

    // ── role field (used by Back button) ──────────────────────────────────
    private String role;

    // reservations.txt columns:
    // 0=GuestName, 1=Room, 2=CheckIn, 3=CheckOut, 4=Services, 5=Total, 6=Status
    public static final String FILE = "reservations.txt";
    // Add this method so NewBooking can access the table
public javax.swing.JTable getTable() {
    return jTable1;
}

    // ── Constructors ──────────────────────────────────────────────────────
    public Reservation() {
        initComponents();
        this.setLocationRelativeTo(null);
        setupButtons();
        loadReservationsFromFile();
        setupSearchPlaceholder(jTextField1, "Search Room...");
    }

    public Reservation(String role) {
        this.role = role;
        initComponents();
        this.setLocationRelativeTo(null);
        setupButtons();
        loadReservationsFromFile();
        setupSearchPlaceholder(jTextField1, "Search Room...");
    }



    /** Clears placeholder text on mouse enter, restores it on mouse exit. */
    private void setupSearchPlaceholder(javax.swing.JTextField field, String placeholder) {
        field.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(java.awt.Color.BLACK);
                }
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setForeground(java.awt.Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    /** Wire up all 6 buttons + search field + combobox filter */
    private void setupButtons() {

        // ── jButton4 = New Booking ───────────────────────────────────────
        jButton4.addActionListener(e -> {
            new NewBooking(this, -1, null).setVisible(true);
        });

        // ── jButton6 = Edit ──────────────────────────────────────────────
        jButton6.addActionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation to edit.");
                return;
            }
            // Collect current row data into a String array
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            String[] rowData = new String[7];
            for (int i = 0; i < 7; i++) {
                Object val = model.getValueAt(row, i);
                rowData[i] = val == null ? "" : val.toString();
            }
            new NewBooking(this, row, rowData).setVisible(true);
        });

        // ── jButton1 = Check-IN ──────────────────────────────────────────
        jButton1.addActionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation first.");
                return;
            }
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setValueAt("Check-In", row, 6);   // update Status column
            saveAllToFile();
            JOptionPane.showMessageDialog(this, "Guest checked in successfully!");
        });

        // ── jButton5 = Check-Out ─────────────────────────────────────────
        jButton5.addActionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation first.");
                return;
            }
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            String roomNum = model.getValueAt(row, 1) == null ? "" :
                             model.getValueAt(row, 1).toString();
            model.setValueAt("Check-Out", row, 6);   // update Status
            saveAllToFile();

            // Mark room as Available in rooms.txt
            updateRoomStatus(roomNum, "Available");

            JOptionPane.showMessageDialog(this, "Guest checked out. Room " + roomNum + " is now available.");
        });

        // ── jButton3 = Cancel reservation ────────────────────────────────
        jButton3.addActionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel this reservation?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            String roomNum = model.getValueAt(row, 1) == null ? "" :
                             model.getValueAt(row, 1).toString();
            model.setValueAt("Canceled", row, 6);
            saveAllToFile();
            updateRoomStatus(roomNum, "Available");
            JOptionPane.showMessageDialog(this, "Reservation canceled.");
        });

        // ── jButton2 = Bill ──────────────────────────────────────────────
        jButton2.addActionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation to view bill.");
                return;
            }
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            String guest    = str(model.getValueAt(row, 0));
            String room     = str(model.getValueAt(row, 1));
            String checkIn  = str(model.getValueAt(row, 2));
            String checkOut = str(model.getValueAt(row, 3));
            String services = str(model.getValueAt(row, 4));
            String total    = str(model.getValueAt(row, 5));
            String status   = str(model.getValueAt(row, 6));

            String bill =
                "════════════════════════\n" +
                "        HNU HOTEL BILL\n" +
                "════════════════════════\n" +
                "Guest   : " + guest    + "\n" +
                "Room    : " + room     + "\n" +
                "Check-In: " + checkIn  + "\n" +
                "Check-Out: " + checkOut + "\n" +
                "Services: " + services + "\n" +
                "────────────────────────\n" +
                "Total   : " + total + " L.E\n" +
                "Status  : " + status   + "\n" +
                "════════════════════════";

            JOptionPane.showMessageDialog(this, bill, "Bill", JOptionPane.INFORMATION_MESSAGE);
        });

      
    }

    // ── Load reservations from file into the table ────────────────────────
    public void loadReservationsFromFile() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);   // clear existing rows
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                // Pad to 7 columns if needed
                String[] row = new String[7];
                for (int i = 0; i < 7; i++) {
                    row[i] = (i < parts.length) ? parts[i].trim() : "";
                }
                model.addRow(row);
            }
        } catch (IOException e) {
            // File doesn't exist yet — that's fine
        }
    }

    // ── Save all table rows back to file ──────────────────────────────────
    public void saveAllToFile() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                // Skip completely empty rows
                if (model.getValueAt(i, 0) == null ||
                    model.getValueAt(i, 0).toString().trim().isEmpty()) continue;

                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 7; j++) {
                    if (j > 0) sb.append(",");
                    Object val = model.getValueAt(i, j);
                    sb.append(val == null ? "" : val.toString().trim());
                }
                pw.println(sb.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // ── Filter table by search text AND combobox status ───────────────────
   private void filterTable() {
    String search = jTextField1.getText().trim();
    String status  = jComboBox1.getSelectedItem().toString();

    // ignore the placeholder text
    if (search.equalsIgnoreCase("Search Room...")) search = "";

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    javax.swing.table.TableRowSorter<DefaultTableModel> sorter =
        new javax.swing.table.TableRowSorter<>(model);
    jTable1.setRowSorter(sorter);

    java.util.List<javax.swing.RowFilter<Object, Object>> filters = new ArrayList<>();

    if (!search.isEmpty()) {
        try {
            filters.add(javax.swing.RowFilter.regexFilter("(?i).*" + search + ".*"));
        } catch (java.util.regex.PatternSyntaxException ignored) {}
    }

    if (!status.equals("All")) {
        filters.add(javax.swing.RowFilter.regexFilter(
            "(?i).*" + status + ".*", 6));
    }

    sorter.setRowFilter(filters.isEmpty() ? null :
        javax.swing.RowFilter.andFilter(filters));
}
    // ── Update a room's status in rooms.txt ───────────────────────────────
    private void updateRoomStatus(String roomNumber, String newStatus) {
        File file = new File("rooms.txt");
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[0].trim().equals(roomNumber.trim())) {
                    parts[6] = newStatus;
                    line = String.join(",", parts);
                }
                lines.add(line);
            }
        } catch (IOException e) { return; }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (String l : lines) pw.println(l);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ── Helper: null-safe toString ────────────────────────────────────────
    private String str(Object o) { return o == null ? "" : o.toString(); }

    // ═════════════════════════════════════════════════════════════════════
    //  ORIGINAL NETBEANS GENERATED CODE — DO NOT MODIFY
    // ═════════════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setForeground(new java.awt.Color(139, 115, 91));
        jTextField1.setText("Search Room...");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 450, 40));

        jButton1.setBackground(new java.awt.Color(139, 115, 91));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Check-IN");
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 630, 130, 45));

        jButton2.setBackground(new java.awt.Color(139, 115, 91));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Bill");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 630, 130, 45));

        jButton3.setBackground(new java.awt.Color(139, 115, 91));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Cancel");
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 630, 130, 45));

        jTable1.setBackground(new java.awt.Color(250, 249, 246));
        jTable1.setFont(new java.awt.Font("Rockwell", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Guest Name", "Room", "Check-IN", "Check-Out", "Services", "Total (L.E)", "Statues"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jTable1.setSelectionBackground(new java.awt.Color(250, 249, 246));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 1330, 440));

        jButton4.setBackground(new java.awt.Color(139, 115, 91));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("New Booking");
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 630, 130, 45));

        jButton5.setBackground(new java.awt.Color(139, 115, 91));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Check-Out");
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 630, 130, 45));

        jButton6.setBackground(new java.awt.Color(139, 115, 91));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Edit");
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 630, 130, 45));

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
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/reserv inter 4.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 0, -1, 180));

        jComboBox1.setBackground(new java.awt.Color(139, 115, 91));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Confirmed", "Check-In", "Check-Out", "Canceled" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 110, -1));

        jButton7.setBackground(new java.awt.Color(139, 115, 91));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Delete Room");
        jButton7.addActionListener(this::jButton7ActionPerformed);
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 60, -1, -1));

        jPanel2.setBackground(new java.awt.Color(208, 166, 106));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1424, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 705, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1424, 705));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        filterTable();
// TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
       filterTable();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        if ("receptionist".equalsIgnoreCase(role)) {
            new Home1("Receptionist").setVisible(true);
        } else {
            new Home1(role).setVisible(true);
        }
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseExited
        btnBack.setBackground(new java.awt.Color(139,115,91));
        btnBack.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_btnBackMouseExited

    private void btnBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseEntered
        btnBack.setBackground(new java.awt.Color(204,204,204));
        btnBack.setForeground(new java.awt.Color(0,0,0));
    }//GEN-LAST:event_btnBackMouseEntered

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = jTable1.convertRowIndexToModel(selectedRow);
            ((DefaultTableModel)jTable1.getModel()).removeRow(modelRow);
            saveAllToFile();
            JOptionPane.showMessageDialog(this, "Reservation Deleted!");
        } else {
            JOptionPane.showMessageDialog(this, "Select the reservation first!");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Reservation().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
