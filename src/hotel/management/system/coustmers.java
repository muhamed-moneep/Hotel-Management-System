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
import javax.swing.JOptionPane;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.io.*;

public class coustmers extends javax.swing.JFrame {
    private String role;
    private TableRowSorter<DefaultTableModel> sorter;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(coustmers.class.getName());
    
    public coustmers() {
        initComponents();
    loadCustomersFromFile();
    
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    sorter = new TableRowSorter<>(model);
    jTable1.setRowSorter(sorter);
    } 
    
   
    private void loadCustomersFromFile() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); 
    try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length == 6) { 
                model.addRow(data);
            }
        }
    } catch (IOException e) {
    }
}
    private void saveAllToFile() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("customers.txt"))) {
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
        JOptionPane.showMessageDialog(this, "Error saving to file!");
    }
}
    
    
  private boolean isNumeric(String str) {
    return str != null && str.matches("\\d+");
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(0, 0, 95));
        jTable1.setForeground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Phone", "Email", "Nationality", "Date-Of-Birth", "National ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 750, 550));

        jTextField1.setBackground(new java.awt.Color(0, 0, 95));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("Search Customer...");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 370, -1));

        jButton3.setBackground(new java.awt.Color(0, 0, 95));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Edit Customer");
        jButton3.addActionListener(this::jButton3ActionPerformed);
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 120, -1));

        jButton2.setBackground(new java.awt.Color(0, 0, 95));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Delete Customer");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, -1, -1));

        jButton1.setBackground(new java.awt.Color(0, 0, 95));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add Customer");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, 120, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/coustomers2.jpeg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 0, 970, 1090));

        jPanel1.setBackground(new java.awt.Color(190, 160, 109));

        btnBack.setText("Back");
        btnBack.addActionListener(this::btnBackActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack)
                .addContainerGap(702, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack)
                .addContainerGap(741, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 770));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
    String query = jTextField1.getText().trim();
    if (query.isEmpty() || query.equalsIgnoreCase("Search Customer...")) {
        sorter.setRowFilter(null);
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }
}
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jTextField1KeyReleased(null);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   javax.swing.JTextField[] fields = new javax.swing.JTextField[6];
    for (int i = 0; i < 6; i++) fields[i] = new javax.swing.JTextField();

    Object[] message = {
        "Name:", fields[0], "Phone:", fields[1], "Email:", fields[2],
        "Nationality:", fields[3], "Date of Birth:", fields[4], "National ID:", fields[5]
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Add New Customer", JOptionPane.OK_CANCEL_OPTION);
    if (option == JOptionPane.OK_OPTION) {
        // Validation
        if (fields[0].getText().isEmpty() || fields[5].getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and National ID are required!");
            return;
        }
        if (!isNumeric(fields[1].getText()) || !isNumeric(fields[5].getText())) {
            JOptionPane.showMessageDialog(this, "Phone and National ID must be numbers!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new Object[]{
            fields[0].getText(), fields[1].getText(), fields[2].getText(),
            fields[3].getText(), fields[4].getText(), fields[5].getText()
        });
        saveAllToFile();
        JOptionPane.showMessageDialog(this, "Customer Added Successfully!");
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
     int selectedRow = jTable1.getSelectedRow();
    if (selectedRow != -1) {
        int modelRow = jTable1.convertRowIndexToModel(selectedRow);
        ((DefaultTableModel)jTable1.getModel()).removeRow(modelRow);
        saveAllToFile();
        JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
    } else {
        JOptionPane.showMessageDialog(this, "Please select a customer first!");
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow != -1) {
        int modelRow = jTable1.convertRowIndexToModel(selectedRow);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        javax.swing.JTextField[] fields = new javax.swing.JTextField[6];
        for(int i=0; i<6; i++) {
            fields[i] = new javax.swing.JTextField(model.getValueAt(modelRow, i).toString());
        }

        Object[] message = {
            "Name:", fields[0], "Phone:", fields[1], "Email:", fields[2],
            "Nationality:", fields[3], "Date of Birth:", fields[4], "National ID:", fields[5]
        };

        if (JOptionPane.showConfirmDialog(null, message, "Edit Customer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            for(int i=0; i<6; i++) model.setValueAt(fields[i].getText(), modelRow, i);
            saveAllToFile();
            JOptionPane.showMessageDialog(this, "Updated Successfully!");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Select a customer to edit!");
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
    new Home1(role).setVisible(true); 
    this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new coustmers().setVisible(true));
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
