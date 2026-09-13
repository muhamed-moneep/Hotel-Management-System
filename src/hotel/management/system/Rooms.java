/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author moham
 */
package hotel.management.system;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.JOptionPane;
import java.io.*;

public class Rooms extends javax.swing.JFrame {
    
    private String role;
    private TableRowSorter<DefaultTableModel> sorter;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rooms.class.getName());

    /**
     * Creates new form Rooms
     */
    public Rooms() {
        this(null);
    }

    public Rooms(String role) {
        initComponents();
        this.role = role;
        loadRoomsFromFile();
        setupSearchPlaceholder(jTextField1, "Search Room...");
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        sorter = new TableRowSorter<>(model);
        jTable1.setRowSorter(sorter);
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

private void loadRoomsFromFile() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    model.addRow(data);
                }
            }
        } catch (IOException e) {
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setText("Search Room...");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 230, -1));

        jButton3.setBackground(new java.awt.Color(139, 115, 91));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Edit Room");
        jButton3.addActionListener(this::jButton3ActionPerformed);
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 120, -1));

        jButton2.setBackground(new java.awt.Color(139, 115, 91));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Delete Room");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, -1, -1));

        jTable1.setBackground(new java.awt.Color(139, 115, 91));
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
                "Room", "Type", "Floor", "Capacity", "view", "Price/Night", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 630, 620));

        jButton1.setBackground(new java.awt.Color(139, 115, 91));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add Room");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 120, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/room2.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 0, 990, 750));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack)
                .addContainerGap(582, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnBack)
                .addGap(0, 727, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 660, 750));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private void saveAllToFile() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("rooms.txt"))) {
        for (int i = 0; i < model.getRowCount(); i++) {
            StringBuilder rowData = new StringBuilder();
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object val = model.getValueAt(i, j);
                rowData.append(val != null ? val.toString() : "");
                if (j < model.getColumnCount() - 1) rowData.append(",");
            }
            bw.write(rowData.toString());
            bw.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving rooms data!");
    }
}
    
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jTextField1KeyReleased(null);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    javax.swing.JTextField[] fields = new javax.swing.JTextField[7];
    for (int i = 0; i < 7; i++) fields[i] = new javax.swing.JTextField();

    Object[] message = {
        "Room Number:", fields[0],
        "Type:", fields[1],
        "Floor:", fields[2],
        "Capacity:", fields[3],
        "View:", fields[4],
        "Price/Night:", fields[5],
        "Status (Available/Reserved):", fields[6]
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Add New Room", JOptionPane.OK_CANCEL_OPTION);
    
    if (option == JOptionPane.OK_OPTION) {
        if (!fields[0].getText().matches("\\d+") || !fields[5].getText().matches("\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(this, "Room Number and Price must be numeric!");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new Object[]{
            fields[0].getText(), fields[1].getText(), fields[2].getText(),
            fields[3].getText(), fields[4].getText(), fields[5].getText(), fields[6].getText()
        });
        saveAllToFile();
        JOptionPane.showMessageDialog(this, "Room Added Successfully!");
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = jTable1.convertRowIndexToModel(selectedRow);
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            
            javax.swing.JTextField[] fields = new javax.swing.JTextField[7];
            for(int i=0; i<7; i++) {
                fields[i] = new javax.swing.JTextField(model.getValueAt(modelRow, i).toString());
            }

            Object[] message = {
                "Room Num:", fields[0], "Type:", fields[1], "Floor:", fields[2],
                "Capacity:", fields[3], "View:", fields[4], "Price:", fields[5], "Status:", fields[6]
            };

            if (JOptionPane.showConfirmDialog(null, message, "Edit Room", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                for(int i=0; i<7; i++) model.setValueAt(fields[i].getText(), modelRow, i);
                saveAllToFile();
                JOptionPane.showMessageDialog(this, "Room Updated!");
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
    if ("receptionist".equalsIgnoreCase(role)) {
        new Home1("Receptionist").setVisible(true);
    } else {
        new Home1(role).setVisible(true);
    }
    this.dispose();                              
    }//GEN-LAST:event_btnBackActionPerformed

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = jTable1.convertRowIndexToModel(selectedRow);
            ((DefaultTableModel)jTable1.getModel()).removeRow(modelRow);
            saveAllToFile();
            JOptionPane.showMessageDialog(this, "Room Deleted!");
        } else {
            JOptionPane.showMessageDialog(this, "Select a room first!");
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
        String query = jTextField1.getText().trim();
        if (query.isEmpty() || query.equalsIgnoreCase("Search Room...")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Rooms().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
