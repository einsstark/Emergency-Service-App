I built a tiny console app to log emergency calls.  
Add a call, read it, update it, delete it. Records live in a simple text file.

<img width="1357" height="566" alt="image" src="https://github.com/user-attachments/assets/f72e1cd4-ac0b-48e9-8d92-76a9111ce487" />

## Why I built this

I wanted a small, clean way to practice the basics that matter:
- turning user input into data that persists
- keeping UI, logic, and data separate
- handling errors without crashing
- thinking about IDs, validation, and simple storage

No frameworks. Just core Java.

## What it does
- Create a call (name, phone, description, required services)
- Read a call by ID, or list all calls
- Update or delete by ID
- Search by **name** or **phone** (partial match)
- Save to `calls.txt` with simple CSV-style lines
- Each record gets a timestamp and a status: `NEW`, `IN_PROGRESS`, `RESOLVED`

## Run it locally
Java 8+.

```bash
cd EmergencyServiceApp/EmergencyServiceApp
javac *.java
java AppLauncher

