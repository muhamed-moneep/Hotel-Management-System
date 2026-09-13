package hotel.management.system;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * NewBooking.java
 * Used for BOTH creating a new reservation AND editing an existing one.
 *
 * How it works:
 *  - New Booking: new NewBooking(parentFrame, -1, null)
 *  - Edit:        new NewBooking(parentFrame, rowIndex, rowData)
 */
public class NewBooking extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(NewBooking.class.getName());

    // Reference to the Reservation frame so we can refresh its table after save
    private Reservation parentFrame;
    private int editRowIndex;   // -1 = new booking, >=0 = editing that row
    private boolean isEditMode;

    //Input fields (added programmatically)
    private javax.swing.JTextField txtGuest, txtRoom, txtCheckIn, txtCheckOut,
                                   txtServices, txtTotal;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton btnSave, btnCancel;

    //Constructors 

    /** For NEW booking */
    public NewBooking(Reservation parent, int rowIndex, String[] rowData) {
        this.parentFrame  = parent;
        this.editRowIndex = rowIndex;
        this.isEditMode   = (rowIndex >= 0 && rowData != null);

        initComponents();   // NetBeans generated (empty layout)
        buildUI();          // My UI built programmatically
        setTitle(isEditMode ? "Edit Reservation" : "New Booking");
        setSize(500, 440);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // If editing, pre-fill all fields with the selected row's data
        if (isEditMode) {
            txtGuest.setText(rowData[0]);
            txtRoom.setText(rowData[1]);
            txtCheckIn.setText(rowData[2]);
            txtCheckOut.setText(rowData[3]);
            txtServices.setText(rowData[4]);
            txtTotal.setText(rowData[5]);
            // Set status combobox
            for (int i = 0; i < cmbStatus.getItemCount(); i++) {
                if (cmbStatus.getItemAt(i).equalsIgnoreCase(rowData[6])) {
                    cmbStatus.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    

    private void buildUI() {
        // Use the existing content pane, set a nice layout
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(208, 166, 106));

        Color brown  = new Color(139, 115, 91);
        Color white  = Color.WHITE;
        Font  lFont  = new Font("Book Antiqua", Font.BOLD, 14);
        Font  fFont  = new Font("Rockwell", Font.PLAIN, 14);

        int labelX = 30, fieldX = 200, y = 30, w = 260, h = 32, gap = 46;

        // ── Row helpers ───────────────────────────────────────────────────
        addLabel("Guest Name :", labelX, y, lFont);
        txtGuest = addField(fieldX, y, w, h, fFont);  y += gap;

        addLabel("Room No :", labelX, y, lFont);
        txtRoom = addField(fieldX, y, w, h, fFont);
        // When room field loses focus: load room price and re-calculate total
        txtRoom.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                recalculateTotal();
            }
        });
        y += gap;

        addLabel("Check-In :", labelX, y, lFont);
        txtCheckIn = addField(fieldX, y, w, h, fFont);
        addHint(txtCheckIn, "yyyy-MM-dd");             y += gap;

        addLabel("Check-Out :", labelX, y, lFont);
        txtCheckOut = addField(fieldX, y, w, h, fFont);
        addHint(txtCheckOut, "yyyy-MM-dd");            y += gap;

        addLabel("Services :", labelX, y, lFont);
        txtServices = addField(fieldX, y, w, h, fFont);
        addHint(txtServices, "e.g. Spa, Breakfast");
        // Re-calculate total whenever services field loses focus
        txtServices.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                recalculateTotal();
            }
        });
        y += gap;

        addLabel("Total (L.E) :", labelX, y, lFont);
        txtTotal = addField(fieldX, y, w, h, fFont);
        txtTotal.setEditable(false);   // auto-calculated — not manually editable
        txtTotal.setBackground(new Color(220, 210, 190));
        y += gap;

        addLabel("Status :", labelX, y, lFont);
        cmbStatus = new javax.swing.JComboBox<>(
            new String[]{"Confirmed", "Check-In", "Check-Out", "Canceled"});
        cmbStatus.setBounds(fieldX, y, w, h);
        cmbStatus.setBackground(brown);
        cmbStatus.setForeground(white);
        cmbStatus.setFont(fFont);
        getContentPane().add(cmbStatus);               y += gap + 10;

        // ── Save button ───────────────────────────────────────────────────
        btnSave = new javax.swing.JButton(isEditMode ? "Update" : "Save");
        btnSave.setBounds(80, y, 130, 40);
        btnSave.setBackground(brown);
        btnSave.setForeground(white);
        btnSave.setFont(new Font("Book Antiqua", Font.BOLD, 14));
        btnSave.addActionListener(e -> saveBooking());
        getContentPane().add(btnSave);

        // ── Cancel button ─────────────────────────────────────────────────
        btnCancel = new javax.swing.JButton("Cancel");
        btnCancel.setBounds(270, y, 130, 40);
        btnCancel.setBackground(new Color(180, 60, 60));
        btnCancel.setForeground(white);
        btnCancel.setFont(new Font("Book Antiqua", Font.BOLD, 14));
        btnCancel.addActionListener(e -> dispose());
        getContentPane().add(btnCancel);
    }

    // ── Auto-calculate total from room price + services ───────────────────
    /**
     * Reads the price of the selected room from rooms.txt,
     * then adds prices of any matching services from services.txt,
     * and sets the result in txtTotal (read-only).
     *
     * rooms.txt format:    roomNumber,type,floors,beds,view,price,status
     * services.txt format: name,category,description,price,available,used
     */
  private void recalculateTotal() {
    String roomNum = txtRoom.getText().trim();
    if (roomNum.isEmpty()) return;

    int days = getDaysBetween();          // checkout - checkin
    int beds = getRoomBeds(roomNum);      // from rooms.txt column 3
    double roomPrice    = getRoomPrice(roomNum);         // price per night
    double servicesPrice = getServicesPrice(beds);       // service price × beds

    double total = (roomPrice * days) + servicesPrice;
    txtTotal.setText(String.format("%.0f", total));
}
   private int getDaysBetween() {
    try {
        String in  = txtCheckIn.getText().trim();
        String out = txtCheckOut.getText().trim();
        if (in.isEmpty() || out.isEmpty() || in.equals("yyyy-MM-dd") || out.equals("yyyy-MM-dd")) return 1;

        java.time.LocalDate checkIn  = java.time.LocalDate.parse(in);
        java.time.LocalDate checkOut = java.time.LocalDate.parse(out);

        long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        return (int) Math.max(days, 1);
    } catch (Exception e) {
        return 1;
    }
}
    /** Reads rooms.txt and returns the price for the given room number. */
   private double getRoomPrice(String roomNum) {
    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("rooms.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 7 && parts[0].trim().equals(roomNum)) {
                try { return Double.parseDouble(parts[5].trim()); }
                catch (NumberFormatException ignored) {}
            }
        }
    } catch (java.io.IOException ignored) {}
    return 0;
}
   private int getRoomBeds(String roomNum) {
    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("rooms.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 7 && parts[0].trim().equals(roomNum)) {
                try { return Integer.parseInt(parts[3].trim()); }
                catch (NumberFormatException ignored) {}
            }
        }
    } catch (java.io.IOException ignored) {}
    return 1;
}
    /**
     * Reads services.txt and sums prices of services that match names
     * entered in the services field (comma-separated).
     */
    private double getServicesPrice(int beds) {
    String servText = txtServices.getText().trim();
    if (servText.isEmpty() || servText.startsWith("e.g.")) return 0;

    java.util.Map<String, Double> priceMap = new java.util.LinkedHashMap<>();
    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("services.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                try {
                    priceMap.put(parts[0].trim().toLowerCase(),
                                 Double.parseDouble(parts[3].trim()));
                } catch (NumberFormatException ignored) {}
            }
        }
    } catch (java.io.IOException ignored) {}

    double total = 0;
    for (String srv : servText.split(",")) {
        String key = srv.trim().toLowerCase();
        if (priceMap.containsKey(key)) total += priceMap.get(key) * beds;
    }
    return total;
}
/**
     * Returns true if the given room number is currently "Reserved" in rooms.txt.
     * Used to block double-booking.
     */
    private boolean isRoomAlreadyBooked(String roomNum) {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("rooms.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[0].trim().equals(roomNum.trim())) {
                    return parts[6].trim().equalsIgnoreCase("Reserved");
                }
            }
        } catch (java.io.IOException ignored) {}
        return false;
    }

    // ── Helpers for building UI ───────────────────────────────────────────

    private void addLabel(String text, int x, int y, Font f) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(text);
        lbl.setBounds(x, y, 165, 30);
        lbl.setFont(f);
        lbl.setForeground(new Color(60, 35, 10));
        getContentPane().add(lbl);
    }

    private javax.swing.JTextField addField(int x, int y, int w, int h, Font f) {
        javax.swing.JTextField tf = new javax.swing.JTextField();
        tf.setBounds(x, y, w, h);
        tf.setFont(f);
        tf.setBackground(new Color(245, 235, 215));
        tf.setForeground(new Color(60, 35, 10));
        getContentPane().add(tf);
        return tf;
    }

    /** Adds grey hint text that disappears on focus */
    private void addHint(javax.swing.JTextField field, String hint) {
        field.setForeground(Color.GRAY);
        field.setText(hint);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(new Color(60, 35, 10));
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(hint);
                }
            }
        });
    }

    //  SAVE LOGIC
    private void saveBooking() {
        // Validate required fields
        String guest    = txtGuest.getText().trim();
        String room     = txtRoom.getText().trim();
        String checkIn  = txtCheckIn.getText().trim();
        String checkOut = txtCheckOut.getText().trim();
        String services = txtServices.getText().trim();
        String status   = cmbStatus.getSelectedItem().toString();

        // Clear hint placeholders if still there
        if (checkIn.equals("yyyy-MM-dd"))  checkIn  = "";
        if (checkOut.equals("yyyy-MM-dd")) checkOut = "";
        if (services.contains("e.g."))     services = "";

        if (guest.isEmpty() || room.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Guest Name and Room are required.");
            return;
        }

        // ─────────────────────────────────────────────────────────────────
        // NEW: Validate check-out is not before check-in
        // ─────────────────────────────────────────────────────────────────
        if (checkIn.isEmpty() || checkOut.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both check-in and check-out dates (format: yyyy-MM-dd).",
                "Missing Dates", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            java.time.LocalDate inDate  = java.time.LocalDate.parse(checkIn);
            java.time.LocalDate outDate = java.time.LocalDate.parse(checkOut);
            if (outDate.isBefore(inDate)) {
                JOptionPane.showMessageDialog(this,
                    "Check-out date cannot be earlier than check-in date!",
                    "Invalid Dates", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Please use valid dates in format: yyyy-MM-dd\nExample: 2025-12-31",
                "Date Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // ─────────────────────────────────────────────────────────────────

        // CHANGE 6: Block booking if room is already reserved (new bookings only)
        if (!isEditMode && isRoomAlreadyBooked(room)) {
            JOptionPane.showMessageDialog(this,
                "The room " + room + " is already booked!\nPlease choose a different room.",
                "Room Not Available", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // CHANGE 4: Auto-calculate total before saving
        recalculateTotal();
        String total = txtTotal.getText().trim();
        if (total.isEmpty()) total = "0";

        String[] newRow = {guest, room, checkIn, checkOut, services, total, status};

        if (parentFrame != null) {
           DefaultTableModel model = (DefaultTableModel) parentFrame.getTable().getModel();

            if (isEditMode) {
                // Update existing row
                for (int col = 0; col < 7; col++) {
                    model.setValueAt(newRow[col], editRowIndex, col);
                }
            } else {
                // Find first empty row and fill it, or add new row
                boolean added = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0) == null ||
                        model.getValueAt(i, 0).toString().trim().isEmpty()) {
                        for (int col = 0; col < 7; col++) {
                            model.setValueAt(newRow[col], i, col);
                        }
                        added = true;
                        break;
                    }
                }
                if (!added) model.addRow(newRow);

                // Mark room as Reserved in rooms.txt
                updateRoomStatus(room, "Reserved");
            }

            // Save everything to file
            parentFrame.saveAllToFile();
            parentFrame.loadReservationsFromFile();  // refresh table display
        }

        JOptionPane.showMessageDialog(this,
            isEditMode ? "Reservation updated!" : "Booking saved successfully!");
        dispose();
    }

    /** Update room status in rooms.txt */
    private void updateRoomStatus(String roomNumber, String newStatus) {
        File file = new File("rooms.txt");
        java.util.List<String> lines = new ArrayList<>();
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

    // ═════════════════════════════════════════════════════════════════════
    //  NETBEANS GENERATED — DO NOT MODIFY
    // ═════════════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new NewBooking(null, -1, null).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
