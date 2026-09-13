

/*
 * FileManager.java — updated to add incrementServiceUsage()
 * All original binary save/load methods are kept exactly as they were.
 */

import hotel.management.system.Rooms;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileManager {

  

    public static void saveRooms(ArrayList<Rooms> rooms) {
        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream("rooms.dat"));
            out.writeObject(rooms);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Rooms> loadRooms() {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream("rooms.dat"));
            return (ArrayList<Rooms>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    

    /**
     * Increments the usage count of a service in services.txt by 1.
     * services.txt format per line: name,category,type,price,available,usageCount
     *
     * @param serviceName the name of the service (must match column 0, case-insensitive)
     */
    public static void incrementServiceUsage(String serviceName) {
        File file = new File("services.txt");
        java.util.List<String> lines = new ArrayList<>();

        // Step 1: Read all lines
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Step 2: Find the matching service and increment its count
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(serviceName.trim())) {
                // Ensure the line has at least 6 fields
                // Pad with empty commas if needed
                while (parts.length < 6) {
                    String[] bigger = new String[parts.length + 1];
                    System.arraycopy(parts, 0, bigger, 0, parts.length);
                    bigger[parts.length] = "0";
                    parts = bigger;
                }

                // Increment field[5] (usageCount)
                int count = 0;
                try { count = Integer.parseInt(parts[5].trim()); }
                catch (NumberFormatException ignored) {}
                parts[5] = String.valueOf(count + 1);

                // Rebuild the line
                lines.set(i, String.join(",", parts));
                found = true;
                break;
            }
        }

        // If service name wasn't found, add it as a new line with count = 1
        if (!found) {
            lines.add(serviceName + ",General,Hotel Service,0,Yes,1");
        }

        // Step 3: Write all lines back to the file
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                pw.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
