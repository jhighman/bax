# Module 5: Descriptors (Composite Attributes)

In this module, we enrich our Student model with structured data like Address and PersonName. These are **Descriptors** (IE terminology) or Value Objects (DDD terminology) - data that describes an entity but has no identity of its own.

## Learning Objectives

By the end of this module, you will be able to:

1. Understand the difference between Entities and Descriptors
2. Implement composite attributes in Rails
3. Create helper methods for formatted display
4. Apply the DRY principle through refactoring
5. Design rich domain models

## DDD Concept: Descriptors (Value Objects)

A **Descriptor** is defined by its attributes, not an identity:

- Two addresses with the same street, city, state, zip are equal
- An address doesn't have a lifecycle separate from its student
- Changing an address means replacing it, not updating it

```ruby
# These are the SAME address (equality by value)
address1 = Address.new(street: "123 Main", city: "Orlando", state: "FL", zip: "32816")
address2 = Address.new(street: "123 Main", city: "Orlando", state: "FL", zip: "32816")
address1 == address2  # true (if implemented correctly)
```

## Java/C Bridge: Embedded Values

**Java (JPA Embeddable):**
```java
@Embeddable
public class Address {
    private String street;
    private String city;
    private String state;
    private String zip;
}

@Entity
public class Student {
    @Embedded
    private Address address;
}
```

**C (Struct composition):**
```c
typedef struct {
    char street[100];
    char city[50];
    char state[3];
    char zip[10];
} Address;

typedef struct {
    int id;
    Address address;  // Embedded struct
} Student;
```

**Rails:**
```ruby
class Student < ApplicationRecord
  # Address stored as columns: street, city, state, zip
  # Grouped logically through methods

  def address
    {
      street: street,
      city: city,
      state: state,
      zip: zip
    }
  end

  def formatted_address
    "#{street}, #{city}, #{state} #{zip}"
  end
end
```

## Key Concepts

### When to Use Descriptors

Use a Descriptor when:
- The data has no identity separate from its parent
- Two instances with same values are interchangeable
- The concept is defined by its attributes

Examples: Address, Money, DateRange, PhoneNumber, PersonName

### Implementation Options

1. **Column groups** - Store as separate columns, group with methods
2. **Serialized hash** - Store as JSON in single column
3. **Separate table** - When descriptor might be shared (rare)

## App State After This Module

- Student profile with full address
- PersonName with first, middle, last, suffix
- Formatted display helpers
- Bootstrap profile cards
- Refactored views using partials

## Development Practice: Refactoring

- Extract common patterns into helpers
- Use partials for repeated view code
- Keep models focused on domain logic
- Don't repeat display formatting

---

*To be expanded with full instructions and working app.*
