import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RescueCallRecord {
    public enum Status { NEW, IN_PROGRESS, RESOLVED }

    private int callId;
    private String callerName;
    private String contactNumber;
    private String description;
    private String requiredServices;
    private LocalDateTime createdAt;
    private Status status;

    public RescueCallRecord(int callId, String callerName, String contactNumber, String description, String requiredServices) {
        this.callId = callId;
        this.callerName = callerName;
        this.contactNumber = contactNumber;
        this.description = description;
        this.requiredServices = requiredServices;
        this.createdAt = LocalDateTime.now();
        this.status = Status.NEW;
    }

    public RescueCallRecord(int callId, String callerName, String contactNumber, String description, String requiredServices, LocalDateTime createdAt, Status status) {
        this.callId = callId;
        this.callerName = callerName;
        this.contactNumber = contactNumber;
        this.description = description;
        this.requiredServices = requiredServices;
        this.createdAt = createdAt;
        this.status = status;
    }

    public int getCallId() { return callId; }
    public String getCallerName() { return callerName; }
    public String getContactNumber() { return contactNumber; }
    public String getDescription() { return description; }
    public String getRequiredServices() { return requiredServices; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Status getStatus() { return status; }

    public void setCallerName(String v) { this.callerName = v; }
    public void setContactNumber(String v) { this.contactNumber = v; }
    public void setDescription(String v) { this.description = v; }
    public void setRequiredServices(String v) { this.requiredServices = v; }
    public void setStatus(Status s) { this.status = s; }

    public String toDisplayString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Call ID: " + callId + "\n" +
               "Caller Name: " + callerName + "\n" +
               "Contact Number: " + contactNumber + "\n" +
               "Description: " + description + "\n" +
               "Required Services: " + requiredServices + "\n" +
               "Status: " + status + "\n" +
               "Created At: " + createdAt.format(fmt);
    }

    // --- CSV helpers (simple escaping of comma and backslash) ---
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace(",", "\\,");
    }
    private static String unesc(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (esc) { out.append(c); esc = false; }
            else if (c == '\\') { esc = true; }
            else { out.append(c); }
        }
        return out.toString();
    }

    public String toCsvLine() {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return callId + "," +
               esc(callerName) + "," +
               esc(contactNumber) + "," +
               esc(description) + "," +
               esc(requiredServices) + "," +
               esc(createdAt.format(fmt)) + "," +
               esc(status.name());
    }

    public static RescueCallRecord fromCsvLine(String line) {
        // Split by commas, honoring backslash escapes
        java.util.List<String> parts = new java.util.ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (esc) { cur.append(c); esc = false; }
            else if (c == '\\') { esc = true; }
            else if (c == ',') { parts.add(cur.toString()); cur.setLength(0); }
            else { cur.append(c); }
        }
        parts.add(cur.toString());

        // Expected fields: 7
        if (parts.size() < 5) {
            throw new IllegalArgumentException("Invalid CSV line: " + line);
        }
        int id = Integer.parseInt(parts.get(0));
        String name = unesc(parts.get(1));
        String phone = unesc(parts.get(2));
        String desc = unesc(parts.get(3));
        String req = unesc(parts.get(4));

        LocalDateTime ts = LocalDateTime.now();
        Status st = Status.NEW;
        if (parts.size() >= 6) {
            try { ts = LocalDateTime.parse(unesc(parts.get(5))); } catch (Exception ignored) {}
        }
        if (parts.size() >= 7) {
            try { st = Status.valueOf(unesc(parts.get(6))); } catch (Exception ignored) {}
        }
        return new RescueCallRecord(id, name, phone, desc, req, ts, st);
    }
}
