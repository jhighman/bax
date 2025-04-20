# Exam Writing Guide

This guide explains how to write exam questions in YAML format for the Java Exam Application.

## General Format

Questions are written in YAML format. Each exam file can either be:
- A list of questions directly
- A map with a `questions` key containing the list of questions

Example:
```yaml
questions:
  - type: MultipleChoice
    question: "Your question here"
    # ... other fields
```

or directly:

```yaml
- type: MultipleChoice
  question: "Your question here"
  # ... other fields
```

## Question Types

### 1. Multiple Choice Questions
Used for questions with multiple options where only one answer is correct.

```yaml
- type: MultipleChoice
  question: "Which interface is at the top of the Java Collections hierarchy?"
  choices:
    - "List"
    - "Collection"
    - "Iterable"
    - "ArrayList"
  correctAnswer: "C"  # Letter corresponding to the correct choice (A, B, C, D)
  explanation: "The Iterable interface is at the top of the Collections hierarchy..."
```

### 2. True/False Questions
Used for statements that are either true or false.

```yaml
- type: TrueFalse
  question: "HashMap allows null keys and multiple null values."
  correctAnswer: "A"  # "A" for True, "B" for False
  explanation: "HashMap allows one null key and multiple null values..."
```

### 3. Fill in the Blank Questions
Used for questions where the student needs to provide a specific word or phrase.

```yaml
- type: FillInTheBlank
  question: "The _______ interface extends Collection and ensures that duplicate elements are not allowed."
  correctAnswer: "Set"
  explanation: "The Set interface extends Collection and adds the constraint..."
```

### 4. Multiple Select Questions
Used for questions where multiple answers can be correct.

```yaml
- type: MultipleSelect
  question: "Which of the following are synchronized Collection implementations? (Select all that apply)"
  choices:
    - "Vector"
    - "Hashtable"
    - "ArrayList"
    - "Collections.synchronizedList()"
  correctAnswers: ["A", "B", "D"]  # Letters corresponding to correct choices
  explanation: "Vector and Hashtable are legacy synchronized collections..."
```

### 5. Matching Questions
Used for questions where students need to match items from two lists.

```yaml
- type: Matching
  question: "Match the Collection type with its primary characteristic:"
  leftItems:
    - "HashSet"
    - "TreeSet"
    - "LinkedList"
  rightItems:
    - "Unique elements with no ordering"
    - "Sorted elements in natural order"
    - "Double-ended queue operations"
  correctMatches:
    "HashSet": "Unique elements with no ordering"
    "TreeSet": "Sorted elements in natural order"
    "LinkedList": "Double-ended queue operations"
  explanation: "Each collection type has specific characteristics..."
```

### 6. Ordering Questions
Used for questions where students need to arrange items in the correct order.

```yaml
- type: Ordering
  question: "Order the following operations from most to least efficient for ArrayList:"
  items:
    - "Get element by index"
    - "Add element at end"
    - "Add element at beginning"
  correctOrder:
    - "Get element by index"
    - "Add element at end"
    - "Add element at beginning"
  explanation: "ArrayList operations have different efficiencies..."
```

### 7. Code Questions
Used for questions that require analyzing, completing, or determining the output of code.

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

Code questions can be used in several ways:
1. **Output Questions**: Ask what code will print
2. **Missing Code**: Ask for missing keywords or statements
3. **Code Analysis**: Ask about the behavior or result of code

Required fields for Code questions:
- `type`: Must be "Code"
- `question`: The question text
- `codeBlock`: The code to display (using YAML's `|` for proper formatting)
- `correctAnswer`: The expected answer (case-sensitive)

Optional fields:
- `explanation`: Explanation of the answer
- `imagePath`: Path to any supplementary image

Example variations:

1. Output Question:
```yaml
- type: Code
  question: "What does this code print?"
  codeBlock: |
    int[] arr = {1, 2, 3};
    System.out.println(arr.length);
  correctAnswer: "3"
  explanation: "The length property returns the size of the array, which is 3."
```

2. Missing Code Question:
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

3. Code Analysis Question:
```yaml
- type: Code
  question: "What is the time complexity of this method?"
  codeBlock: |
    public static int findMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
  correctAnswer: "O(n)"
  explanation: "The method performs a single pass through the array, comparing each element exactly once."
```

Best Practices for Code Questions:
1. **Code Formatting**
   - Use consistent indentation
   - Include necessary imports
   - Comment complex logic
   - Use meaningful variable names

2. **Question Clarity**
   - Specify exact format for expected output
   - Indicate if whitespace/formatting matters
   - For missing code, clearly mark the blank (e.g., with _____)

3. **Answer Validation**
   - Consider multiple valid answers (e.g., "0" vs "0.0")
   - Be explicit about case sensitivity
   - Handle whitespace consistently

### 8. Code Completion Questions
Used for questions where students need to complete missing code in a given code block.

```yaml
- type: CodeCompletion
  question: "Complete the function to calculate the factorial of a number."
  codeBlock: |
    int factorial(int n) {
        // Your code here
    }
  correctAnswer: |
    if (n <= 1) return 1;
    return n * factorial(n - 1);
  explanation: "The factorial function uses recursion to multiply n by factorial of (n-1)."
  imagePath: "diagrams/factorial.png"  # Optional
```

Required fields for Code Completion questions:
- `type`: Must be "CodeCompletion"
- `question`: Clear instructions about what code needs to be completed
- `codeBlock`: Template code with placeholder for student's answer
- `correctAnswer`: The expected code solution

Optional fields:
- `explanation`: Detailed explanation of the solution
- `imagePath`: Path to any supplementary diagram or image

Best Practices:
1. Clearly mark where code should be added (e.g., "// Your code here")
2. Provide sufficient context in the template code
3. Include necessary function signatures or class structure
4. Consider multiple valid solutions if applicable

### 9. Tracing Questions
Used for questions where students need to track program state or data structure contents at specific points.

```yaml
- type: Tracing
  question: "Trace the stack contents at points A, B, and C in the code."
  codeBlock: |
    void processStack(Stack* s) {
        push(s, 10);
        push(s, 20);
        // Point A
        pop(s);
        push(s, 30);
        // Point B
        push(s, 40);
        pop(s);
        // Point C
    }
  tracePoints:
    - label: "A"
      correctAnswer: "[20, 10]"
    - label: "B"
      correctAnswer: "[30, 10]"
    - label: "C"
      correctAnswer: "[30, 10]"
  explanation: "The stack changes as elements are pushed and popped, following LIFO order."
```

Required fields for Tracing questions:
- `type`: Must be "Tracing"
- `question`: Clear instructions about what to trace
- `codeBlock`: The code to analyze
- `tracePoints`: List of points to trace, each with:
  - `label`: Identifier for the trace point
  - `correctAnswer`: Expected state at that point

Optional fields:
- `explanation`: Explanation of the program's behavior
- `imagePath`: Helpful diagrams or visualizations

Best Practices:
1. Clearly mark trace points in the code
2. Use consistent format for state representation
3. Include enough trace points to test understanding
4. Provide clear state expectations

### 10. Analysis Questions
Used for questions that require step-by-step analysis or problem-solving.

```yaml
- type: Analysis
  question: "Convert the binary number 1101 to decimal, showing each step."
  steps:
    - label: "Step 1: Identify place values"
      correctAnswer: "8 4 2 1"
    - label: "Step 2: Multiply each digit"
      correctAnswer: "1×8 + 1×4 + 0×2 + 1×1"
    - label: "Step 3: Sum values"
      correctAnswer: "8 + 4 + 0 + 1 = 13"
    - label: "Final answer"
      correctAnswer: "13"
  explanation: "Binary to decimal conversion involves multiplying each digit by its place value (power of 2) and summing."
```

Required fields for Analysis questions:
- `type`: Must be "Analysis"
- `question`: Clear problem statement
- `steps`: List of analysis steps, each with:
  - `label`: Step identifier or description
  - `correctAnswer`: Expected answer for that step

Optional fields:
- `explanation`: Detailed explanation of the solution process
- `imagePath`: Supporting diagrams or illustrations

Best Practices:
1. Break complex problems into clear, logical steps
2. Provide specific expectations for each step
3. Include intermediate calculations or reasoning
4. Use clear formatting for mathematical expressions

## Language-Specific Examples

This guide includes examples for multiple programming languages. See specific example files:
- Java examples: `java_code_examples.yaml`
- C examples: `c_code_examples.yaml`
- Python examples: `python_code_examples.yaml`

Each language file follows the same structure but includes language-appropriate examples and idioms.

## Common Fields

All question types support these common fields:
- `type`: (Required) The type of question
- `question`: (Required) The question text
- `explanation`: (Optional) Explanation shown after answering
- `codeSnippet`: (Optional) Code snippet to display with the question
- `imagePath`: (Optional) Path to an image to display with the question

## Answer Format Conventions

1. **Multiple Choice**: Use letters ("A", "B", "C", "D") for `correctAnswer`
2. **True/False**: Use "A" for True and "B" for False
3. **Fill in the Blank**: Exact text match (case-sensitive)
4. **Multiple Select**: List of letters (["A", "B", "D"])
5. **Matching**: Map of left items to right items
6. **Ordering**: List in correct order

## Best Practices

1. **Question Text**
   - Be clear and concise
   - Use proper grammar and punctuation
   - Avoid ambiguity
   - Include all necessary context

2. **Answer Choices**
   - Make all choices plausible
   - Keep similar length and structure
   - Avoid "all/none of the above" when possible
   - Randomize correct answer positions

3. **Explanations**
   - Explain why the correct answer is right
   - Explain why incorrect answers are wrong
   - Include relevant examples or references
   - Link to additional resources when appropriate

4. **Code Snippets**
   - Format code properly
   - Include only relevant code
   - Use clear variable names
   - Comment complex logic

## Example Exam File

```yaml
questions:
  - type: MultipleChoice
    question: "Which interface is at the top of the Java Collections hierarchy?"
    choices:
      - "List"
      - "Collection"
      - "Iterable"
      - "ArrayList"
    correctAnswer: "C"
    explanation: "The Iterable interface is at the top..."

  - type: TrueFalse
    question: "HashMap allows null keys and multiple null values."
    correctAnswer: "A"
    explanation: "HashMap allows one null key..."

  - type: FillInTheBlank
    question: "The _______ interface extends Collection and ensures uniqueness."
    correctAnswer: "Set"
    explanation: "The Set interface extends Collection..."
```

## Common Issues and Solutions

1. **Question Not Loading**
   - Check YAML syntax
   - Verify all required fields are present
   - Ensure file extension is `.yaml`

2. **Answer Not Recognized**
   - Check case sensitivity
   - Verify answer format matches convention
   - Look for extra whitespace

3. **Formatting Issues**
   - Use proper YAML indentation
   - Use quotes around strings containing special characters
   - Use `>` for multi-line strings

## Testing Questions

Before adding questions to an exam:
1. Verify correct answer works
2. Try incorrect answers
3. Check explanation clarity
4. Test any code snippets
5. Validate YAML syntax 