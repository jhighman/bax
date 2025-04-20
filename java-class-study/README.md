# Java Exam Application

A Java Swing application for taking interactive exams on various Java topics. The application supports multiple question types including multiple choice, true/false, fill in the blank, matching, ordering, and multiple select questions.

## Prerequisites

Before you begin, make sure you have:
- Java JDK 17 or later installed
- Maven installed
- A basic understanding of Java programming

## Project Structure

```
java-class-study/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── highman/
│                   ├── ExamAppSwing.java    # Main GUI application
│                   ├── ExamReader.java      # Reads exam questions from YAML files
│                   └── exams/               # Question type implementations
└── exams/                                   # Directory containing exam YAML files
```

## Setting Up Your Development Environment

### 1. Install Java JDK
- Download and install Java JDK 17 from [Oracle's website](https://www.oracle.com/java/technologies/downloads/#java17)
- Set the JAVA_HOME environment variable to point to your JDK installation
- Add Java's bin directory to your PATH

### 2. Install Maven
- Download Maven from [Apache Maven website](https://maven.apache.org/download.cgi)
- Extract the downloaded file to a directory of your choice
- Add Maven's bin directory to your PATH
- Verify installation by running `mvn -version` in your terminal

### 3. Set Up the Project
1. Clone this repository:
   ```bash
   git clone <repository-url>
   cd java-class-study
   ```

2. The project uses Maven for dependency management. Maven will automatically download all required dependencies when you build the project.

## Running the Application

### Using Maven
1. Open a terminal in the `java-class-study` directory
2. Run the application:
   ```bash
   mvn clean compile exec:java
   ```

This command does three things:
- `clean`: Removes any previously compiled files
- `compile`: Compiles the Java source code
- `exec:java`: Runs the main application

### Understanding the Maven Command
- `mvn`: The Maven command-line tool
- `clean`: Cleans the project (removes compiled files)
- `compile`: Compiles the source code
- `exec:java`: Executes the Java application

## Creating Your Own Exams

Exams are stored in YAML files in the `exams` directory. Each exam file contains a list of questions with the following structure:

```yaml
- type: MultipleChoice
  question: "What is polymorphism in Java?"
  choices:
    - "A feature that allows a class to have multiple constructors"
    - "The ability of an object to take many forms"
    - "A way to hide implementation details"
    - "The process of creating multiple instances"
  correctAnswer: "B"
  explanation: "Polymorphism allows objects to be treated as instances of their parent class..."
```

### Supported Question Types
1. **MultipleChoice**: Single correct answer from multiple options
2. **TrueFalse**: True or false questions
3. **FillInTheBlank**: Questions requiring text input
4. **Matching**: Matching items from two lists
5. **Ordering**: Arranging items in the correct order
6. **MultipleSelect**: Selecting multiple correct answers

## Troubleshooting

### Common Issues

1. **Maven not found**
   - Make sure Maven is installed and in your PATH
   - Try running `mvn -version` to verify installation

2. **Java not found**
   - Ensure Java JDK 17 is installed
   - Check JAVA_HOME environment variable
   - Run `java -version` to verify installation

3. **Dependencies not downloading**
   - Check your internet connection
   - Try running `mvn clean install` to force dependency download

4. **Application won't start**
   - Make sure you're in the correct directory
   - Check for any error messages in the console
   - Verify that exam YAML files exist in the exams directory

## Getting Help

If you encounter any issues:
1. Check the error messages in your terminal
2. Verify your Java and Maven installations
3. Ensure you're in the correct directory when running commands
4. Check that the exams directory contains valid YAML files

## Contributing

Feel free to:
- Create new exam files
- Add new question types
- Improve the user interface
- Report bugs or suggest improvements

## License

This project is open source and available under the MIT License. 