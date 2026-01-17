# Module [Number]: [Module Title]

## Introduction

[1-2 paragraphs explaining the topic's relevance to database design and SDLC. Connect to previous modules and preview what students will learn.]

This module builds on [previous concepts] and introduces [new concepts]. By the end of this module, you will understand how [key learning objective] relates to database-driven application development and fits into the broader DDD framework.

## Learning Objectives

By the end of this module, students will be able to:
- [Specific, measurable learning objective 1]
- [Specific, measurable learning objective 2]
- [Specific, measurable learning objective 3]

## Key Concepts

### [Concept 1]
- **Definition**: [Clear, concise definition using IE terminology]
- **Database Perspective**: [How this concept relates to ER modeling and SQL]
- **DDD Integration**: [How this fits into domain-driven design principles]

### [Concept 2]
- **Definition**: [Clear, concise definition using IE terminology]
- **Database Perspective**: [How this concept relates to ER modeling and SQL]
- **DDD Integration**: [How this fits into domain-driven design principles]

### [Concept 3]
- **Definition**: [Clear, concise definition using IE terminology]
- **Database Perspective**: [How this concept relates to ER modeling and SQL]
- **DDD Integration**: [How this fits into domain-driven design principles]

## Integration with IE and Fowler

### Information Engineering (IE) Perspective
[Explicitly bridge to James Martin's IE methodology:]
- **Entity Types**: [How concepts map to IE entity types]
- **Descriptors/Attributes**: [How to model attributes and composite descriptors]
- **Normalization**: [Normalization considerations and trade-offs]

### Fowler's Analysis Patterns
[Connect to relevant Martin Fowler patterns:]
- **[Pattern Name]**: [Brief description and application]
- **Implementation**: [How to apply in ER diagrams and Rails]

## Rails Examples

### UCF Course Manager Example

[Provide concrete code examples using the running UCF Course Manager application]

#### Entity-Relationship Diagram
```
[ASCII or description of ERD showing entities, relationships, and attributes]
```

#### Rails Migration
```ruby
class Create[EntityName] < ActiveRecord::Migration[7.0]
  def change
    create_table :[table_name] do |t|
      # Entity attributes
      t.string :[attribute_name], null: false
      
      # Composite descriptor attributes
      t.string :[descriptor_attribute_1]
      t.string :[descriptor_attribute_2]
      
      # Relationships
      t.references :[related_entity], null: false, foreign_key: true
      
      t.timestamps
    end
    
    # Indexes for performance
    add_index :[table_name], :[attribute_name]
  end
end
```

#### Rails Model
```ruby
class [EntityName] < ApplicationRecord
  # Associations (representing ER relationships)
  belongs_to :[related_entity]
  has_many :[related_entities]
  
  # Validations (enforcing domain rules)
  validates :[attribute_name], presence: true, uniqueness: true
  validates :[descriptor_attribute], presence: true
  
  # Composite descriptor methods
  def [descriptor_name]
    "[descriptor_attribute_1], [descriptor_attribute_2]"
  end
  
  # Domain logic
  def [domain_method]
    # Business logic implementation
  end
  
  private
  
  def [validation_method]
    # Custom validation logic
  end
end
```

#### Service Layer (Domain Services)
```ruby
class [DomainService]
  def initialize([parameters])
    @[parameter] = [parameter]
  end
  
  def [service_method]
    # Domain service logic that coordinates multiple entities
    # or handles complex business rules
  end
  
  private
  
  def [helper_method]
    # Private helper methods
  end
end
```

## Activities and Exercises

### Exercise 1: ER Diagram Creation
**Objective**: [What students should accomplish]

**Instructions**:
1. [Step-by-step instructions]
2. [Use IE notation with boxes for entities, lines for relationships]
3. [Include attributes inside entity boxes]

**Deliverable**: [What students should submit]

### Exercise 2: Rails Implementation
**Objective**: [What students should accomplish]

**Instructions**:
1. [Step-by-step coding instructions]
2. [Include migration, model, and basic tests]
3. [Focus on domain logic and database integrity]

**Deliverable**: [What students should submit]

### Exercise 3: Collaborative Domain Modeling
**Objective**: [Group learning objective]

**Instructions**:
1. [Group formation and roles]
2. [Domain modeling session guidelines]
3. [Presentation requirements]

**Deliverable**: [Group presentation or documentation]

## Readings and Resources

### Required Readings
- Evans, Eric. *Domain-Driven Design*. Chapter [X]: [Chapter Title]
- Fowler, Martin. *Analysis Patterns*. Chapter [Y]: [Pattern Name]
- Martin, James. *Information Engineering*. Section [Z]: [Topic]

### Supplementary Resources
- Rails Guides: [Specific guide relevant to module]
- [Additional articles, videos, or documentation]
- [UCF library resources if applicable]

### Code Examples Repository
- [Link to GitHub repository with complete examples]
- [Specific branch or directory for this module]

## Assessment

### Knowledge Check Quiz
[5-10 multiple choice or short answer questions covering key concepts]

1. **Question**: [Question text]
   - a) [Option A]
   - b) [Option B]
   - c) [Option C]
   - d) [Option D]
   
   **Answer**: [Correct answer with brief explanation]

### Discussion Prompts
1. [Thought-provoking question about domain modeling]
2. [Question connecting theory to practical application]
3. [Question encouraging peer collaboration]

### Mini-Assignment
**Title**: [Assignment name]
**Points**: [Point value]
**Due Date**: [Relative to module completion]

**Description**: [What students need to accomplish]

**Rubric**:
- **Domain Understanding (40%)**: [Criteria for domain concept mastery]
- **Technical Implementation (40%)**: [Criteria for Rails/database implementation]
- **Documentation (20%)**: [Criteria for clear explanation and documentation]

## SDLC Integration

### How This Module Fits the Software Development Life Cycle

**Requirements Analysis**: [How concepts apply to gathering and analyzing requirements]

**Design Phase**: [How concepts inform system and database design]

**Implementation**: [How concepts guide coding and database implementation]

**Testing**: [How to test domain logic and database integrity]

**Deployment**: [Considerations for deploying domain-driven applications]

**Maintenance**: [How domain models evolve and are maintained over time]

## Connection to UCF Curriculum

This module builds on concepts from:
- **CIS 3360 (Database Systems)**: [Specific connections to database course content]
- **[Other relevant courses]**: [How this module connects to other CS/IS courses]

## Next Steps

In the next module, we will explore [preview of next module content] and see how [current concepts] integrate with [upcoming concepts] to create more sophisticated domain models.

---

## Module Checklist

Before moving to the next module, ensure you can:
- [ ] [Key competency 1]
- [ ] [Key competency 2]
- [ ] [Key competency 3]
- [ ] Complete the knowledge check quiz with 80% or higher
- [ ] Successfully implement the Rails examples
- [ ] Participate meaningfully in discussion forums

---

*Remember: Domain modeling is an iterative process. Don't expect to get everything perfect on the first try. Focus on understanding the core concepts and how they apply to database-driven application development.*