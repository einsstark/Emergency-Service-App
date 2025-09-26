import java.util.*;
import java.io.*;

public class AppLauncher {
    private static final String DATA_FILE = "calls.txt";
    private static final Scanner SC = new Scanner(System.in);
    private static RescueCallManager manager;

    public static void main(String[] args) {
        manager = new RescueCallManager(DATA_FILE);
        manager.loadCalls();

        while (true) {
            showMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    handleAdd();
                    break;
                case 2:
                    handleSearchById();
                    break;
                case 3:
                    handleUpdate();
                    break;
                case 4:
                    handleDelete();
                    break;
                case 5:
                    handleListAll();
                    break;
                case 6:
                    handleSearchByName();
                    break;
                case 7:
                    handleSearchByPhone();
                    break;
                case 8:
                    manager.saveCalls();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
            System.out.println();
        }
    }

    private static void showMenu() {
        System.out.println("==== Emergency Service App ====");
        System.out.println("1) Add a new call");
        System.out.println("2) Search a call by ID");
        System.out.println("3) Update a call by ID");
        System.out.println("4) Delete a call by ID");
        System.out.println("5) List all calls");
        System.out.println("6) Search calls by name");
        System.out.println("7) Search calls by phone");
        System.out.println("8) Save & Exit");
    }

    private static void handleAdd() {
        String name = readNonEmpty("Caller name: ");
        String phone = readPhone("Contact number (digits only): ");
        String description = readNonEmpty("Description (what happened?): ");
        String services = readNonEmpty("Required services (e.g., fire, police, ambulance): ");

        RescueCallRecord rec = manager.createAndAdd(name, phone, description, services);
        System.out.println("\nSaved:\n" + rec.toDisplayString());
    }

    private static void handleSearchById() {
        int id = readInt("Enter call ID: ");
        RescueCallRecord rec = manager.getById(id);
        if (rec == null) {
            System.out.println("No record with ID " + id);
        } else {
            System.out.println(rec.toDisplayString());
        }
    }

    private static void handleUpdate() {
        int id = readInt("Enter call ID to update: ");
        RescueCallRecord rec = manager.getById(id);
        if (rec == null) {
            System.out.println("No record with ID " + id);
            return;
        }
        System.out.println("Leave a field empty to keep the current value.");
        String name = readMaybe("New name [" + rec.getCallerName() + "]: ");
        String phone = readMaybe("New phone [" + rec.getContactNumber() + "]: ");
        String description = readMaybe("New description [" + rec.getDescription() + "]: ");
        String services = readMaybe("New required services [" + rec.getRequiredServices() + "]: ");
        String statusStr = readMaybe("New status (NEW/IN_PROGRESS/RESOLVED) [" + rec.getStatus() + "]: ");

        if (name != null && !name.trim().isEmpty()) rec.setCallerName(name.trim());
        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("\\d{3,}")) {
                System.out.println("Phone must be digits only (min 3). Keeping old value.");
            } else {
                rec.setContactNumber(phone.trim());
            }
        }
        if (description != null && !description.trim().isEmpty()) rec.setDescription(description.trim());
        if (services != null && !services.trim().isEmpty()) rec.setRequiredServices(services.trim());
        if (statusStr != null && !statusStr.trim().isEmpty()) {
            try {
                rec.setStatus(RescueCallRecord.Status.valueOf(statusStr.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Keeping old value.");
            }
        }

        manager.saveCalls();
        System.out.println("Updated:\n" + rec.toDisplayString());
    }

    private static void handleDelete() {
        int id = readInt("Enter call ID to delete: ");
        boolean ok = manager.deleteById(id);
        if (ok) {
            System.out.println("Deleted record " + id);
        } else {
            System.out.println("No record with ID " + id);
        }
    }

    private static void handleListAll() {
        List<RescueCallRecord> list = manager.getAll();
        if (list.isEmpty()) {
            System.out.println("No emergency calls recorded.");
            return;
        }
        for (RescueCallRecord r : list) {
            System.out.println(r.toDisplayString());
            System.out.println("---------------------------");
        }
        System.out.println("Total: " + list.size());
    }

    private static void handleSearchByName() {
        String name = readNonEmpty("Name contains: ");
        List<RescueCallRecord> out = manager.searchByName(name);
        if (out.isEmpty()) {
            System.out.println("No matches for name containing \"" + name + "\"");
        } else {
            for (RescueCallRecord r : out) {
                System.out.println(r.toDisplayString());
                System.out.println("---------------------------");
            }
            System.out.println("Matches: " + out.size());
        }
    }

    private static void handleSearchByPhone() {
        String phone = readNonEmpty("Phone contains: ");
        List<RescueCallRecord> out = manager.searchByPhone(phone);
        if (out.isEmpty()) {
            System.out.println("No matches for phone containing \"" + phone + "\"");
        } else {
            for (RescueCallRecord r : out) {
                System.out.println(r.toDisplayString());
                System.out.println("---------------------------");
            }
            System.out.println("Matches: " + out.size());
        }
    }

    // --- input helpers ---
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SC.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SC.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Value cannot be empty.");
        }
    }

    private static String readPhone(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SC.nextLine();
            if (s != null && s.trim().matches("\\d{3,}")) return s.trim();
            System.out.println("Phone must be digits only (min 3).");
        }
    }

    // returns null if user pressed enter (keep old value)
    private static String readMaybe(String prompt) {
        System.out.print(prompt);
        String s = SC.nextLine();
        return s;
    }
}
