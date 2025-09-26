import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public class RescueCallManager {
    private final String dataFilePath;
    private final Map<Integer, RescueCallRecord> byId = new LinkedHashMap<>();

    public RescueCallManager(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public List<RescueCallRecord> getAll() {
        return new ArrayList<>(byId.values());
    }

    public RescueCallRecord getById(int id) {
        return byId.get(id);
    }

    public RescueCallRecord createAndAdd(String name, String phone, String description, String requiredServices) {
        int nextId = nextId();
        RescueCallRecord rec = new RescueCallRecord(nextId, name, phone, description, requiredServices);
        byId.put(nextId, rec);
        saveCalls();
        return rec;
    }

    public boolean deleteById(int id) {
        RescueCallRecord removed = byId.remove(id);
        if (removed != null) {
            saveCalls();
            return true;
        }
        return false;
    }

    public List<RescueCallRecord> searchByName(String needle) {
        String q = needle.toLowerCase();
        return byId.values().stream()
                .filter(r -> r.getCallerName() != null && r.getCallerName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<RescueCallRecord> searchByPhone(String needle) {
        String q = needle.toLowerCase();
        return byId.values().stream()
                .filter(r -> r.getContactNumber() != null && r.getContactNumber().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    private int nextId() {
        int max = 0;
        for (Integer id : byId.keySet()) max = Math.max(max, id);
        return max + 1;
    }

    public void loadCalls() {
        byId.clear();
        Path p = Paths.get(dataFilePath);
        if (!Files.exists(p)) {
            // create empty file
            try {
                Files.createFile(p);
            } catch (IOException ignored) {}
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    RescueCallRecord rec = RescueCallRecord.fromCsvLine(line);
                    byId.put(rec.getCallId(), rec);
                } catch (Exception e) {
                    System.err.println("Skipping bad line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read " + dataFilePath + ": " + e.getMessage());
        }
    }

    public void saveCalls() {
        Path p = Paths.get(dataFilePath);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            for (RescueCallRecord r : byId.values()) {
                bw.write(r.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write " + dataFilePath + ": " + e.getMessage());
        }
    }
}
