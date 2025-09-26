# EmergencyServiceApp (Java Console)

A tiny console app to record, search, update, and delete **emergency call** records (who called, what happened, and which services are needed).  
Data is saved to a local `calls.txt` file so your records persist between runs.

<img width="1400" height="619" alt="image" src="https://github.com/user-attachments/assets/a92e6157-b97a-47cf-8ea4-eb941a2752e9" />


## What it does
- Add a new call (name, number, description, required services)
- Update an existing call by ID
- Delete a call by ID
- Search a call by ID
- List all calls
- Saves/loads from `EmergencyServiceApp/calls.txt`

> Source lives in `EmergencyServiceApp/` (the inner folder).

---

## Run it locally (Java 8+)

1) Open a terminal in the inner folder:
```bash
cd EmergencyServiceApp/EmergencyServiceApp
```

2) Compile the sources:
```bash
javac *.java
```

3) Run the app:
```bash
java AppLauncher
```

You’ll see a menu with options to add/search/update/delete/list calls.

> Records are saved in `calls.txt` in the same folder. You can pre‑seed it with lines like:
> ```
> 2,Deepak,23,dying,fire
> 4,Deepak Kumar,0123456789,Car accident on Main Street,fire and police
> ```

---

## Project structure
```
EmergencyServiceApp/
└── EmergencyServiceApp/        # source folder
    ├── AppLauncher.java        # entry point & menu
    ├── RescueCallManager.java  # CRUD + file persistence
    ├── RescueCallRecord.java   # data model
    ├── calls.txt               # saved records
    └── (generated) *.class     # compiled outputs
```

---

## Notes
- This is a simple file‑based app (no database).
- If you get a “file not found”, create an empty `calls.txt` beside the `.java` files.
- On Windows, use PowerShell or CMD; commands are the same.
- To clean compiled classes:
```bash
del *.class        # Windows
# or
rm *.class         # macOS/Linux
```
