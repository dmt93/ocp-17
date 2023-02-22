package records;

/* Notes: Chapter 7; Encapsulating Data with Records

  POJO: Plain Old Java Object
    - used to pass data around
    - few or no complex data

  JavaBean:
    - is a POJO
    - with some additional rules
 */

import java.util.Objects;

public class App {

  /**
   * record -> keyword
   * (int numberEggs, String name) -> List of fields surrounded by parentheses {}
   * { } -> declare optional constructors, methods, and constants
   *
   * - equals(), hashCode(), toString() will be generated
   * - compiler generates constructors with the
   * - params in the same order of the declaration
   * - generates getters without a "get"-prefix
   * - legal to declare record without any fields
   * - every field is final
   */
  public record Crane(int numberEggs, String name) { }
  protected record Empty() { }

  // every record is implicitly final
  // you cannot extend or inherit a record
  public final record FinalRecord() {}

  private interface Bird {}
  // record can implement regular or sealed interface
  record Implementer() implements Bird {}

  public static void main(String[] args) {
    var crane = new Crane(10, "Peter");
    System.out.println(crane.toString());
    var empty = new Empty();
    System.out.println(empty.toString());

    // declaring constructors
    // long constructor = constructor created by the compiler
    // compiler will not insert own constructor if you define your own
    record OwnConstructor(int a, int b) {
      // you have to set the fields here
      public OwnConstructor(int a, int b) {
        if (a < b) throw new IllegalStateException("a is less than b");
        this.a = a;
        this.b = b;
      }
    }

    // compact constructor
    record CompactConstructor(int a, int b) {
      CompactConstructor{
        a = a + 100; // you don't modify "this.a" BUT the provided param "a"
      } // long constructor is called at the end
    }

    // transforming parameters
    record TransformingParam(int a, int b) {
      TransformingParam {
        if (a > 100) throw new IllegalStateException("a is greater than 100");
        a = a + 1000;
      }
    }

    // overloaded constructors
    // - can take a completely different list of params
    // - after the first line, all fields will already be assigned, and the object is immutable
    record Overloaded(int a, int b) {

      Overloaded(int c, int d, String lol) {
        // first line must be an explicit call to another constructor
        // you can only transform data on the first line
        // no other constructors? you must call the long constructor
        this(d, c);
        // writing code here will lead to a compiler error
      }
    }

    var over = new Overloaded(1, 2, "String");

    // Customizing records
//    - members that a record can contain
//      * overloaded and compact constructors
//      * instance methods including  overriding any provided methods (accessors, equals(), hashCode(), toString())
//      * nested classes, interfaces, annotations, enums, records

    record Custom(String name) {

      public static final int foo = 10;
      public static int bar = 20;

      // public int size; -> compile error
      // private boolean friendly -> compile error
      // records also do not support instance initializers
      // all fields must happen in a constructor

      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }
        Custom custom = (Custom) o;
        return Objects.equals(name, custom.name);
      }

      @Override
      public int hashCode() {
        return Objects.hash(name);
      }

      @Override
      public String toString() {
        return "Custom{" +
            "name='" + name + '\'' +
            '}';
      }
    }

  }


}