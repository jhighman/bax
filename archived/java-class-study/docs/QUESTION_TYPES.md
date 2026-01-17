# Question Types Guide

This guide documents all supported question types in the exam system, including their YAML formats, required fields, and best practices.

## Table of Contents
1. [Multiple Choice Questions](#multiple-choice-questions)
2. [True/False Questions](#trueFalse-questions)
3. [Fill in the Blank Questions](#fill-in-the-blank-questions)
4. [Matching Questions](#matching-questions)
5. [Ordering Questions](#ordering-questions)
6. [Multiple Select Questions](#multiple-select-questions)
7. [Code Questions](#code-questions)
8. [Code Completion Questions](#code-completion-questions)
9. [Tracing Questions](#tracing-questions)
10. [Analysis Questions](#analysis-questions)

## Common Fields
All question types support these common fields:
- `type`: (Required) The type of question (e.g., "MultipleChoice", "CodeCompletion")
- `question`: (Required) The question text
- `explanation`: (Optional) Explanation shown after answering
- `codeBlock`: (Optional for most types, required for Code, CodeCompletion, Tracing)
- `imagePath`: (Optional) Path to an image to display with the question

## Code Questions
Used for questions that require analyzing, determining the output, or identifying missing parts of code.

### Example (Output Question)
```yaml
- type: Code
  question: "What is the output of the following code?"
  codeBlock: |
    public class Main {
        public static void main(String[] args) {
            int x = 5;
            System.out.println(x * 2);
        }
    }
  correctAnswer: "10"
  explanation: "The code multiplies 5 by 2 and prints the result, which is 10."
```

### Example (Missing Code Question)
```yaml
- type: Code
  question: "What keyword is missing in the following code to make it compile?"
  codeBlock: |
    public class Test {
        public static void main(String[] args) {
            _____ x = 10;
            System.out.println(x);
        }
    }
  correctAnswer: "int"
  explanation: "The variable x needs a type declaration, and int is appropriate for the value 10."
```

### Required Fields
- `type`: Must be "Code"
- `question`: The question text
- `codeBlock`: The code to display
- `correctAnswer`: The expected answer (case-sensitive)

### Best Practices
1. Code Formatting:
   - Use consistent indentation (4 spaces)
   - Include necessary imports
   - Comment complex logic
   - Use meaningful variable names

2. Question Clarity:
   - Specify expected answer format
   - Indicate if whitespace/case matters
   - Use clear placeholders (e.g., _____)

3. Answer Validation:
   - Allow equivalent answers where appropriate
   - Be explicit about case sensitivity
   - Handle whitespace consistently

## Code Completion Questions
Used for questions where students complete a partially written function or code block.

### Example
```yaml
- type: CodeCompletion
  question: "Complete the function to deallocate memory for a book_t struct array."
  codeBlock: |
    typedef struct {
        char ** sentences;
        int numSentences;
        char * title;
        char * author;
    } book_t;
    void cleanUp(book_t * lib, int numBooks) {
        // Your code here
    }
  correctAnswer: |
    for (int i = 0; i < numBooks; i++) {
        for (int j = 0; j < lib[i].numSentences; j++) {
            free(lib[i].sentences[j]);
        }
        free(lib[i].sentences);
        free(lib[i].title);
        free(lib[i].author);
    }
    free(lib);
  explanation: "The function must free each sentence string, the sentences array, title, author, and the lib array."
  imagePath: "diagrams/book_t_memory.png"
```

### Required Fields
- `type`: Must be "CodeCompletion"
- `question`: The question text
- `codeBlock`: The partial code with placeholder
- `correctAnswer`: The expected code fragment

### Best Practices
1. Code Setup:
   - Provide complete function signatures
   - Clearly mark placeholders
   - Ensure code is valid except for missing part

2. Answer Expectations:
   - Specify if exact syntax is required
   - Document allowed variations
   - Normalize whitespace in validation

## Tracing Questions
Used for questions where students trace algorithm execution states.

### Example
```yaml
- type: Tracing
  question: "Trace the stack contents after each indicated point (A, B, C) in the given code."
  codeBlock: |
    void followStack(stack_t * myStack) {
        int x;
        push(myStack, 12);
        push(myStack, 5);
        push(myStack, -8);
        // A
        x = pop(myStack);
        push(myStack, 23);
        x = pop(myStack);
        // B
        push(myStack, 17);
        push(myStack, -3);
        x = pop(myStack);
        push(myStack, 9);
        push(myStack, 6);
        push(myStack, -14);
        x = pop(myStack);
        x = pop(myStack);
        push(myStack, 34);
        // C
    }
  tracePoints:
    - label: "A"
      correctAnswer: "[-8, 5, 12]"
    - label: "B"
      correctAnswer: "[5, 12]"
    - label: "C"
      correctAnswer: "[34, 9, 17, 5, 12]"
  explanation: "The stack grows with pushes and shrinks with pops, maintaining LIFO order."
```

### Required Fields
- `type`: Must be "Tracing"
- `question`: The question text
- `codeBlock`: The code to trace
- `tracePoints`: List of points with labels and correct answers

### Best Practices
1. Code and Points:
   - Clearly mark trace points
   - Limit to 3-5 points
   - Ensure code is executable

2. Answer Format:
   - Specify expected format
   - Allow normalized input
   - Document format conventions

## Analysis Questions
Used for mathematical reasoning, complexity analysis, and step-by-step solutions.

### Example
```yaml
- type: Analysis
  question: "Solve the recurrence T(1)=1, T(n)=2T(n-1)+5 for n≥2 using iteration."
  steps:
    - label: "T(2)"
      correctAnswer: "7"
    - label: "T(3)"
      correctAnswer: "19"
    - label: "T(4)"
      correctAnswer: "43"
    - label: "Closed form"
      correctAnswer: "T(n)=6*2^n-5"
  explanation: "The recurrence is solved by iterating and identifying the pattern."
```

### Required Fields
- `type`: Must be "Analysis"
- `question`: The question text
- `steps`: List of steps with labels and correct answers

### Best Practices
1. Steps:
   - Include 3-5 steps
   - Clearly label each step
   - Show progression of solution

2. Answer Format:
   - Specify format requirements
   - Allow equivalent expressions
   - Document accepted notations

## Answer Format Conventions
- Multiple Choice: Letters ("A", "B", "C", "D")
- True/False: "A" for True, "B" for False
- Fill in the Blank: Exact text match
- Multiple Select: List of letters ["A", "B", "D"]
- Matching: Map of left items to right items
- Ordering: List in correct order
- Code: Exact text match
- Code Completion: Multi-line code fragment
- Tracing: List of states
- Analysis: List of numerical or expression answers

## General Best Practices
1. Question Text:
   - Be clear and concise
   - Provide necessary context
   - Use proper grammar

2. Explanations:
   - Explain correct and incorrect answers
   - Include relevant examples
   - Reference documentation

3. Code Blocks:
   - Use consistent formatting
   - Include only relevant code
   - Ensure syntax is correct

4. Validation:
   - Handle whitespace consistently
   - Allow equivalent answers
   - Provide clear feedback 