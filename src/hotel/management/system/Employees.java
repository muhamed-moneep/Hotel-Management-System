package hotel.management.system;

import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author moham
 */
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;



public class Employees extends javax.swing.JFrame {
    private String role;
    private TableRowSorter<DefaultTableModel> sorter;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Employees.class.getName());

    /**
     * Creates new form Employees
     */
   
    public Employees() {
    initComponents();

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);  

    loadEmployeesFromFile();

    sorter = new TableRowSorter<>(model);
    jTable1.setRowSorter(sorter);
    }

 private void loadEmployeesFromFile() {
    
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    
    model.setRowCount(0); 
    try (BufferedReader br = new BufferedReader(new FileReader("employees.txt"))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            if (data.length == 7) {
                model.addRow(data);
            }
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
   private void saveAllToFile() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    try (BufferedWriter bw = new BufferedWriter(new FileWriter("employees.txt"))) {

        for (int i = 0; i < model.getRowCount(); i++) {

            Object id = model.getValueAt(i, 0);

            if (id == null || id.toString().trim().isEmpty()) continue;

            StringBuilder rowData = new StringBuilder();

            for (int j = 0; j < model.getColumnCount(); j++) {
                Object val = model.getValueAt(i, j);
                rowData.append(val != null ? val.toString().trim() : "");
                if (j < model.getColumnCount() - 1) rowData.append(",");
            }

            bw.write(rowData.toString());
            bw.newLine();
        }
        System.out.println("Saving employees...");

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
    }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/;employee inter2.jpeg"))); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIconTextGap(10);
        jLabel1.setPreferredSize(new java.awt.Dimension(1500, 750));
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 0, 620, 1050));

        jPanel1.setBackground(new java.awt.Color(190, 160, 109));

        jTable1.setBackground(new java.awt.Color(0, 55, 95));
        jTable1.setFont(new java.awt.Font("Rockwell", 0, 18)); // NOI18N
        jTable1.setForeground(new java.awt.Color(255, 255, 255));
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
                "ID", "Name", "Phone", "Email", "Salary(L.E)", "Shift", "Postion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(jTable1);

        jTextField1.setText("search employees...");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jButton3.setBackground(new java.awt.Color(0, 55, 95));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Edit Employee");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton2.setBackground(new java.awt.Color(0, 55, 95));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Delete Employee");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton1.setBackground(new java.awt.Color(0, 55, 95));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add Employee");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        btnBack.setText("Back");
        btnBack.addActionListener(this::btnBackActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 185, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(204, 204, 204)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBack)
                        .addGap(187, 187, 187)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 880, 750));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = jTable1.convertRowIndexToModel(selectedRow);
            ((DefaultTableModel)jTable1.getModel()).removeRow(modelRow);
            saveAllToFile(); 
        }
    }
    
    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
        String query = jTextField1.getText().toLowerCase();
        if (query.equals("search employees...")) query = "";
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   javax.swing.JTextField[] fields = new javax.swing.JTextField[7];
    for (int i = 0; i < 7; i++) fields[i] = new javax.swing.JTextField();
    
    Object[] message = {
        "ID (Numbers only):", fields[0],
        "Name:", fields[1],
        "Phone (Numbers only):", fields[2],
        "Email:", fields[3],
        "Salary (Numbers only):", fields[4],
        "Shift:", fields[5],
        "Position:", fields[6]
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Add New Employee", JOptionPane.OK_CANCEL_OPTION);
    
    if (option == JOptionPane.OK_OPTION) {
      
        if (fields[0].getText().isEmpty() || fields[1].getText().isEmpty() || fields[4].getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID, Name, and Salary are mandatory!");
            return;
        }
        
        if (!isNumeric(fields[0].getText())) {
            JOptionPane.showMessageDialog(this, "ID must be a number!");
            return;
        }
        
        if (!isNumeric(fields[4].getText())) {
            JOptionPane.showMessageDialog(this, "Salary must be a number!");
            return;
        }

        if (!fields[3].getText().contains("@") || !fields[3].getText().contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Email!");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new Object[]{
            fields[0].getText(), fields[1].getText(), fields[2].getText(),
            fields[3].getText(), fields[4].getText(), fields[5].getText(), fields[6].getText()
        });
        saveAllToFile();
        JOptionPane.showMessageDialog(this, "Employee Added Successfully!");
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
            "ID:", fields[0], "Name:", fields[1], "Phone:", fields[2],
            "Email:", fields[3], "Salary:", fields[4], "Shift:", fields[5], "Position:", fields[6]
        };

        if (JOptionPane.showConfirmDialog(null, message, "Edit", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            
            if (!isNumeric(fields[4].getText())) {
                JOptionPane.showMessageDialog(this, "Salary must be a numeric value!");
                return;
            }

            for(int i=0; i<7; i++) model.setValueAt(fields[i].getText(), modelRow, i);
            saveAllToFile();
            JOptionPane.showMessageDialog(this, "Data Updated Successfully!");
        }
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jTextField1KeyReleased(null);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
    new Home1(role).setVisible(true); 
    this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private boolean isNumeric(String str) {
    if (str == null || str.isEmpty()) return false;
    return str.matches("\\d+(\\.\\d+)?"); 
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
        java.awt.EventQueue.invokeLater(() -> new Employees().setVisible(true));
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
