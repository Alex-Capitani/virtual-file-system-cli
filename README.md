# Virtual File System — Java CLI

A command-line application developed in Java that provides directory navigation, file listing, text file reading, and file metadata inspection using Java IO and NIO2 APIs.

The application allows controlled navigation within a defined root directory through terminal-style commands, ensuring safe filesystem interaction and consistent command execution.

---

## Features

The application supports the following commands:

| Command | Description |
|----------|------------|
| `LIST` | Lists files and directories in the current path |
| `OPEN <directory>` | Opens a directory |
| `BACK` | Returns to the parent directory |
| `SHOW <file.txt>` | Displays contents of a text file |
| `DETAIL <file or directory>` | Displays filesystem metadata |
| `EXIT` | Terminates the application |

---

## Technologies Used

- Java 24
- Maven
- IntelliJ IDEA
- Java IO API
- Java NIO2 (`Files`, `Path`, `BasicFileAttributeView`)
- Command Line Interface (CLI)

---

## Technical Highlights

- Filesystem navigation control
- File and directory manipulation
- CLI command parsing and execution
- Enum-based command execution strategy
- Exception handling for safe operations
- Modular command architecture
- Resource management using try-with-resources

---

## Project Structure

src/
└── main/java
├── Application.java
├── Command.java
├── FileReader.java
└── UnsupportedOperationException.java


---

## How to Run

1. Place a folder named `hd` in the project root directory, or configure the environment variable `VFS_ROOT` pointing to a directory of your choice.

2. Run the application:

Application.main()


3. Execute commands in the terminal:

LIST
OPEN documents
SHOW file.txt
DETAIL file.txt
BACK
EXIT

---

## Example Output

$> LIST
documents/
music/
notes.txt

$> SHOW notes.txt
File content displayed here...

## Author

**Alex Capitani** 
Java backend developer
## Project Structure

