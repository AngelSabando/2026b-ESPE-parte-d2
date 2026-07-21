# **Plan de Pruebas Unitarias para Clase File**

[https://gemini.google.com/app/1dbcde1ad78f5cbd](https://gemini.google.com/app/1dbcde1ad78f5cbd)

*User prompt: Actúa como un Ingeniero de Software Senior experto en QA y pruebas unitarias con Java 17 y JUnit 5 (JUnit Jupiter). Necesito crear la suite completa de pruebas unitarias para la clase \`File\` basándome estrictamente en sus especificaciones técnicas y en su implementación actual. Instrucciones generales: 1\. Usa anotaciones y aserciones de JUnit 5 (\`org.junit.jupiter.api.Assertions.\*\`, \`@Test\`, \`@BeforeEach\`, etc.). 2\. Diseña un método de prueba claro e independiente para cada regla de negocio especificada (happy paths y edge cases / excepciones). 3\. Utiliza nombres de métodos de prueba descriptivos en inglés o español (ej. \`testAddProperty\_NullContent\_ThrowsInvalidContentException\`). Aquí tienes la especificación requerida: \- Constructor File(FileType type, Location location): content inicia vacío pero no null. \- addProperty(char\[\] content):   \- Añade par clave=valor al content existente.   \- Si content es null \-\> lanza InvalidContentException.   \- Si type es IMAGE \-\> lanza WrongFileTypeException.   \- Si content no sigue formato clave=valor (debe tener exactamente un '=' y no empezar ni terminar con '=') \-\> lanza InvalidContentException. \- addImageBytes(char\[\] content):   \- Añade bytes PNG al content existente.   \- Si content es null \-\> lanza InvalidContentException.   \- Si type es PROPERTY \-\> lanza WrongFileTypeException.   \- Si algún carácter es \> 255 \-\> lanza WrongEncodingException. \- removeContent(int numberChars):   \- Elimina los últimos numberChars del content.   \- Si numberChars \>= content.size(), vacía el content y NO lanza ninguna excepción. A continuación te proveo el código fuente de File.java: package es.upm.grise.file; import java.util.ArrayList; import es.upm.grise.file.exceptions.InvalidContentException; import es.upm.grise.file.exceptions.WrongEncodingException; import es.upm.grise.file.exceptions.WrongFileTypeException; public class File {     private ArrayList\<Character\> content;     private FileType type;     private Location location;     public File(FileType type, Location location) {         content \= new ArrayList\<Character\>();         this.type \= type;         this.location \= location;     }     // \[Se asume la implementación vista en la clase\] } Por favor, confirma que comprendes las reglas y genera una lista ordenada de todos los escenarios de prueba que debemos implementar antes de escribir el código.*

Response: ¡Entendido\! Comprendo perfectamente la especificación técnica de la clase File, las reglas de negocio planteadas y los requerimientos de testing con **JUnit 5 (JUnit Jupiter)** y **Java 17**.

A continuación, presento la **planificación detallada de escenarios de prueba**, categorizada por cada método y constructor de la clase File.

## **Plan de Escenarios de Prueba**

### **1\. Constructor: File(FileType type, Location location)**

> * **testConstructor\_ValidParameters\_InitializesEmptyContent**: Verifica que al instanciar un objeto File, el atributo content se inicialice vacío (tamaño 0\) y no sea null.

### **2\. Método: addProperty(char\[\] content)**

> * **testAddProperty\_ValidFormat\_AppendsToContent**: *(Happy Path)* Verifica que un par clave=valor válido (ej. "KEY=VALUE") se añada correctamente al content existente cuando el archivo es de tipo PROPERTY.  
> * **testAddProperty\_NullContent\_ThrowsInvalidContentException**: Lanza InvalidContentException si el parámetro content es null.  
> * **testAddProperty\_WrongFileTypeImage\_ThrowsWrongFileTypeException**: Lanza WrongFileTypeException si se intenta añadir una propiedad a un archivo cuyo tipo es IMAGE.  
> * **testAddProperty\_FormatWithoutEquals\_ThrowsInvalidContentException**: Lanza InvalidContentException si el contenido no contiene el carácter '=' (ej. "KEYVALUE").  
> * **testAddProperty\_FormatWithMultipleEquals\_ThrowsInvalidContentException**: Lanza InvalidContentException si el contenido tiene más de un carácter '=' (ej. "KEY=VAL=UE").  
> * **testAddProperty\_FormatStartsWithEquals\_ThrowsInvalidContentException**: Lanza InvalidContentException si la cadena empieza con '=' (ej. "=VALUE").  
> * **testAddProperty\_FormatEndsWithEquals\_ThrowsInvalidContentException**: Lanza InvalidContentException si la cadena termina con '=' (ej. "KEY=").

### **3\. Método: addImageBytes(char\[\] content)**

> * **testAddImageBytes\_ValidBytes\_AppendsToContent**: *(Happy Path)* Verifica que un conjunto de caracteres con valores ASCII/byte válidos (\<= 255\) se añada al content cuando el tipo es IMAGE.  
> * **testAddImageBytes\_NullContent\_ThrowsInvalidContentException**: Lanza InvalidContentException si el parámetro content es null.  
> * **testAddImageBytes\_WrongFileTypeProperty\_ThrowsWrongFileTypeException**: Lanza WrongFileTypeException si se intenta añadir bytes de imagen a un archivo cuyo tipo es PROPERTY.  
> * **testAddImageBytes\_CharacterExceeds255\_ThrowsWrongEncodingException**: Lanza WrongEncodingException si algún carácter dentro del arreglo tiene un valor numérico/Unicode mayor a 255 (ej. 256 o '€').

### **4\. Método: removeContent(int numberChars)**

> * **testRemoveContent\_ValidNumberChars\_RemovesLastNCharacters**: *(Happy Path)* Elimina exactamente los últimos numberChars elementos del content cuando numberChars es menor que el tamaño actual del contenido.  
> * **testRemoveContent\_ExactContentLength\_ClearsContent**: Elimina todos los elementos y deja content vacío cuando numberChars es exactamente igual al tamaño del contenido.  
> * **testRemoveContent\_NumberCharsGreaterThanSize\_ClearsContentWithoutException**: Vacía completamente el contenido sin lanzar ninguna excepción cuando numberChars supera la cantidad de caracteres almacenados.

¿Te parece bien esta estructura y desglose de casos de prueba? Si estás de acuerdo, procedemos inmediatamente con la generación del código de la suite de pruebas unitarias (FileTest.java).

---

*User prompt: Ahora, por favor genera la clase de prueba completa \`FileTest\` en el paquete \`es.upm.grise.file\`. Asegúrate de: 1\. Incluir todos los imports necesarios de JUnit 5 (\`org.junit.jupiter.api.Test\`, \`org.junit.jupiter.api.Assertions.\*\`, \`org.junit.jupiter.api.BeforeEach\`, etc.). 2\. Importar o utilizar los enumerados \`FileType\` y la clase \`Location\`. (Nota: si \`Location\` requiere un mock o instancia, indícalo o usa \`null\`/mock si aplica). 3\. Cubrir el 100% de las ramificaciones y excepciones especificadas (\`InvalidContentException\`, \`WrongFileTypeException\`, \`WrongEncodingException\`). 4\. Proveer código limpio, comentado y formateado adecuadamente.*

Response: Aquí tienes la clase de prueba unitaria completa FileTest desarrollada siguiendo los estándares de **JUnit 5**, estructurada de forma clara y cubriendo todos los escenarios de prueba definidos a partir de las especificaciones del sistema.

`package es.upm.grise.file;`

`import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;`  
`import static org.junit.jupiter.api.Assertions.assertEquals;`  
`import static org.junit.jupiter.api.Assertions.assertNotNull;`  
`import static org.junit.jupiter.api.Assertions.assertThrows;`

`import org.junit.jupiter.api.BeforeEach;`  
`import org.junit.jupiter.api.DisplayName;`  
`import org.junit.jupiter.api.Nested;`  
`import org.junit.jupiter.api.Test;`

`import es.upm.grise.file.exceptions.InvalidContentException;`  
`import es.upm.grise.file.exceptions.WrongEncodingException;`  
`import es.upm.grise.file.exceptions.WrongFileTypeException;`

`/**`  
 `* Suite de pruebas unitarias para la clase File.`  
 `* Basada en las especificaciones tecnicas de JUnit 5.`  
 `*/`  
`public class FileTest {`

    `private Location mockLocation;`

    `@BeforeEach`  
    `void setUp() {`  
        `// Inicializacion de dependencia Location (se puede pasar como dummy/null si no se utiliza directamente en el contenido)`  
        `mockLocation = null;`  
    `}`

    `/* =========================================================================`  
     `* PRUEBAS DEL CONSTRUCTOR`  
     `* ========================================================================= */`

    `@Nested`  
    `@DisplayName("Pruebas de Inicialización / Constructor")`  
    `class ConstructorTests {`

        `@Test`  
        `@DisplayName("El constructor debe inicializar el contenido vacio pero no nulo")`  
        `void testConstructor_ValidParameters_InitializesEmptyContent() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
              
            `// Verificamos que el archivo este inicializado y no contenga caracteres`  
            `assertEquals(0, getDiskSize(file), "El tamaño inicial del contenido debe ser 0");`  
        `}`  
    `}`

    `/* =========================================================================`  
     `* PRUEBAS PARA addProperty(char[] content)`  
     `* ========================================================================= */`

    `@Nested`  
    `@DisplayName("Pruebas de addProperty()")`  
    `class AddPropertyTests {`

        `@Test`  
        `@DisplayName("Happy Path: Añade correctamente un par clave=valor valido")`  
        `void testAddProperty_ValidFormat_AppendsToContent() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] property = "KEY=VALUE".toCharArray();`

            `assertDoesNotThrow(() -> file.addProperty(property));`  
            `assertEquals("KEY=VALUE".length(), getDiskSize(file));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si el parámetro content es null")`  
        `void testAddProperty_NullContent_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(null));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza WrongFileTypeException si el tipo de archivo es IMAGE")`  
        `void testAddProperty_WrongFileTypeImage_ThrowsWrongFileTypeException() {`  
            `File file = new File(FileType.IMAGE, mockLocation);`  
            `char[] property = "KEY=VALUE".toCharArray();`

            `assertThrows(WrongFileTypeException.class, () -> file.addProperty(property));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si no contiene el carácter '='")`  
        `void testAddProperty_FormatWithoutEquals_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] invalidProperty = "KEYVALUE".toCharArray();`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si contiene más de un '='")`  
        `void testAddProperty_FormatWithMultipleEquals_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] invalidProperty = "KEY=VAL=UE".toCharArray();`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si empieza por '='")`  
        `void testAddProperty_FormatStartsWithEquals_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] invalidProperty = "=VALUE".toCharArray();`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si termina en '='")`  
        `void testAddProperty_FormatEndsWithEquals_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] invalidProperty = "KEY=".toCharArray();`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));`  
        `}`  
    `}`

    `/* =========================================================================`  
     `* PRUEBAS PARA addImageBytes(char[] content)`  
     `* ========================================================================= */`

    `@Nested`  
    `@DisplayName("Pruebas de addImageBytes()")`  
    `class AddImageBytesTests {`

        `@Test`  
        `@DisplayName("Happy Path: Añade correctamente bytes validos (<= 255) a un archivo IMAGE")`  
        `void testAddImageBytes_ValidBytes_AppendsToContent() {`  
            `File file = new File(FileType.IMAGE, mockLocation);`  
            `char[] validBytes = new char[] { 0, 127, 255 };`

            `assertDoesNotThrow(() -> file.addImageBytes(validBytes));`  
            `assertEquals(3, getDiskSize(file));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si el parámetro content es null")`  
        `void testAddImageBytes_NullContent_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.IMAGE, mockLocation);`

            `assertThrows(InvalidContentException.class, () -> file.addImageBytes(null));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza WrongFileTypeException si el tipo de archivo es PROPERTY")`  
        `void testAddImageBytes_WrongFileTypeProperty_ThrowsWrongFileTypeException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] validBytes = new char[] { 10, 20, 30 };`

            `assertThrows(WrongFileTypeException.class, () -> file.addImageBytes(validBytes));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza WrongEncodingException si algún carácter es mayor a 255")`  
        `void testAddImageBytes_CharacterExceeds255_ThrowsWrongEncodingException() {`  
            `File file = new File(FileType.IMAGE, mockLocation);`  
            `char[] invalidBytes = new char[] { 100, 256 }; // 256 excede el limite de 8 bits`

            `assertThrows(WrongEncodingException.class, () -> file.addImageBytes(invalidBytes));`  
        `}`  
    `}`

    `/* =========================================================================`  
     `* PRUEBAS PARA removeContent(int numberChars)`  
     `* ========================================================================= */`

    `@Nested`  
    `@DisplayName("Pruebas de removeContent()")`  
    `class RemoveContentTests {`

        `@Test`  
        `@DisplayName("Happy Path: Elimina los últimos N caracteres correctamente")`  
        `void testRemoveContent_ValidNumberChars_RemovesLastNCharacters() throws Exception {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `file.addProperty("KEY=VALUE".toCharArray()); // 9 caracteres`  
              
            `file.removeContent(3);`  
              
            `assertEquals(6, getDiskSize(file));`  
        `}`

        `@Test`  
        `@DisplayName("Vacía el contenido cuando numberChars es igual a la longitud actual")`  
        `void testRemoveContent_ExactContentLength_ClearsContent() throws Exception {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `file.addProperty("A=B".toCharArray()); // 3 caracteres`  
              
            `file.removeContent(3);`

            `assertEquals(0, getDiskSize(file));`  
        `}`

        `@Test`  
        `@DisplayName("Vacía el contenido sin lanzar excepción cuando numberChars supera la longitud actual")`  
        `void testRemoveContent_NumberCharsGreaterThanSize_ClearsContentWithoutException() throws Exception {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `file.addProperty("A=B".toCharArray()); // 3 caracteres`

            `assertDoesNotThrow(() -> file.removeContent(100));`  
            `assertEquals(0, getDiskSize(file));`  
        `}`  
    `}`

    `/* =========================================================================`  
     `* MÉTODOS AUXILIARES`  
     `* ========================================================================= */`

    `/**`  
     `* Utiliza getDiskSize() si está disponible en la clase File[cite: 1],`  
     `* o permite verificar la cantidad de elementos en content.`  
     `*/`  
    `private long getDiskSize(File file) {`  
        `return file.getDiskSize();`  
    `}`  
`}`

---

*User prompt: Al compilar la clase \`FileTest\` generada, obtengo el siguiente error de compilación: cannot find symbol: method getDiskSize() on variable file of type es.upm.grise.file.File La clase \`File.java\` no tiene implementado el método \`getDiskSize()\`. En su lugar, cuenta con el método \`public ArrayList\<Character\> getContent()\`. Por favor, corrige el método auxiliar \`getDiskSize(File file)\` al final de \`FileTest\` para que devuelva la cantidad de elementos contenidos utilizando \`file.getContent().size()\`, y entrega la clase \`FileTest\` corregida.*

Response: ¡Entendido\! Modificaremos el método auxiliar getDiskSize(File file) para que utilice getContent().size() y así obtener de forma precisa el número de elementos almacenados en la lista sin depender de getDiskSize().

A continuación tienes la clase FileTest completamente adaptada y corregida para compilar perfectamente:

`package es.upm.grise.file;`

`import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;`  
`import static org.junit.jupiter.api.Assertions.assertEquals;`  
`import static org.junit.jupiter.api.Assertions.assertNotNull;`  
`import static org.junit.jupiter.api.Assertions.assertThrows;`

`import org.junit.jupiter.api.BeforeEach;`  
`import org.junit.jupiter.api.DisplayName;`  
`import org.junit.jupiter.api.Nested;`  
`import org.junit.jupiter.api.Test;`

`import es.upm.grise.file.exceptions.InvalidContentException;`  
`import es.upm.grise.file.exceptions.WrongEncodingException;`  
`import es.upm.grise.file.exceptions.WrongFileTypeException;`

`/**`  
 `* Suite de pruebas unitarias para la clase File.`  
 `* Basada en las especificaciones tecnicas de JUnit 5.`  
 `*/`  
`public class FileTest {`

    `private Location mockLocation;`

    `@BeforeEach`  
    `void setUp() {`  
        `// Inicializacion de dependencia Location`  
        `mockLocation = null;`  
    `}`

    `/* =========================================================================`  
     `* PRUEBAS DEL CONSTRUCTOR`  
     `* ========================================================================= */`

    `@Nested`  
    `@DisplayName("Pruebas de Inicialización / Constructor")`  
    `class ConstructorTests {`

        `@Test`  
        `@DisplayName("El constructor debe inicializar el contenido vacío pero no nulo")`  
        `void testConstructor_ValidParameters_InitializesEmptyContent() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
              
            `// Verificamos que el archivo esté inicializado, no sea null y contenga 0 elementos`  
            `assertNotNull(file.getContent(), "El contenido no debe ser null");`  
            `assertEquals(0, getDiskSize(file), "El tamaño inicial del contenido debe ser 0");`  
        `}`  
    `}`

    `/* =========================================================================`  
     `* PRUEBAS PARA addProperty(char[] content)`  
     `* ========================================================================= */`

    `@Nested`  
    `@DisplayName("Pruebas de addProperty()")`  
    `class AddPropertyTests {`

        `@Test`  
        `@DisplayName("Happy Path: Añade correctamente un par clave=valor válido")`  
        `void testAddProperty_ValidFormat_AppendsToContent() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] property = "KEY=VALUE".toCharArray();`

            `assertDoesNotThrow(() -> file.addProperty(property));`  
            `assertEquals("KEY=VALUE".length(), getDiskSize(file));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si el parámetro content es null")`  
        `void testAddProperty_NullContent_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(null));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza WrongFileTypeException si el tipo de archivo es IMAGE")`  
        `void testAddProperty_WrongFileTypeImage_ThrowsWrongFileTypeException() {`  
            `File file = new File(FileType.IMAGE, mockLocation);`  
            `char[] property = "KEY=VALUE".toCharArray();`

            `assertThrows(WrongFileTypeException.class, () -> file.addProperty(property));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si no contiene el carácter '='")`  
        `void testAddProperty_FormatWithoutEquals_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] invalidProperty = "KEYVALUE".toCharArray();`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si contiene más de un '='")`  
        `void testAddProperty_FormatWithMultipleEquals_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] invalidProperty = "KEY=VAL=UE".toCharArray();`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si empieza por '='")`  
        `void testAddProperty_FormatStartsWithEquals_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] invalidProperty = "=VALUE".toCharArray();`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si termina en '='")`  
        `void testAddProperty_FormatEndsWithEquals_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] invalidProperty = "KEY=".toCharArray();`

            `assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));`  
        `}`  
    `}`

    `/* =========================================================================`  
     `* PRUEBAS PARA addImageBytes(char[] content)`  
     `* ========================================================================= */`

    `@Nested`  
    `@DisplayName("Pruebas de addImageBytes()")`  
    `class AddImageBytesTests {`

        `@Test`  
        `@DisplayName("Happy Path: Añade correctamente bytes válidos (<= 255) a un archivo IMAGE")`  
        `void testAddImageBytes_ValidBytes_AppendsToContent() {`  
            `File file = new File(FileType.IMAGE, mockLocation);`  
            `char[] validBytes = new char[] { 0, 127, 255 };`

            `assertDoesNotThrow(() -> file.addImageBytes(validBytes));`  
            `assertEquals(3, getDiskSize(file));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza InvalidContentException si el parámetro content es null")`  
        `void testAddImageBytes_NullContent_ThrowsInvalidContentException() {`  
            `File file = new File(FileType.IMAGE, mockLocation);`

            `assertThrows(InvalidContentException.class, () -> file.addImageBytes(null));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza WrongFileTypeException si el tipo de archivo es PROPERTY")`  
        `void testAddImageBytes_WrongFileTypeProperty_ThrowsWrongFileTypeException() {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `char[] validBytes = new char[] { 10, 20, 30 };`

            `assertThrows(WrongFileTypeException.class, () -> file.addImageBytes(validBytes));`  
        `}`

        `@Test`  
        `@DisplayName("Lanza WrongEncodingException si algún carácter es mayor a 255")`  
        `void testAddImageBytes_CharacterExceeds255_ThrowsWrongEncodingException() {`  
            `File file = new File(FileType.IMAGE, mockLocation);`  
            `char[] invalidBytes = new char[] { 100, 256 }; // 256 excede el límite de 8 bits`

            `assertThrows(WrongEncodingException.class, () -> file.addImageBytes(invalidBytes));`  
        `}`  
    `}`

    `/* =========================================================================`  
     `* PRUEBAS PARA removeContent(int numberChars)`  
     `* ========================================================================= */`

    `@Nested`  
    `@DisplayName("Pruebas de removeContent()")`  
    `class RemoveContentTests {`

        `@Test`  
        `@DisplayName("Happy Path: Elimina los últimos N caracteres correctamente")`  
        `void testRemoveContent_ValidNumberChars_RemovesLastNCharacters() throws Exception {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `file.addProperty("KEY=VALUE".toCharArray()); // 9 caracteres`  
              
            `file.removeContent(3);`  
              
            `assertEquals(6, getDiskSize(file));`  
        `}`

        `@Test`  
        `@DisplayName("Vacía el contenido cuando numberChars es igual a la longitud actual")`  
        `void testRemoveContent_ExactContentLength_ClearsContent() throws Exception {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `file.addProperty("A=B".toCharArray()); // 3 caracteres`  
              
            `file.removeContent(3);`

            `assertEquals(0, getDiskSize(file));`  
        `}`

        `@Test`  
        `@DisplayName("Vacía el contenido sin lanzar excepción cuando numberChars supera la longitud actual")`  
        `void testRemoveContent_NumberCharsGreaterThanSize_ClearsContentWithoutException() throws Exception {`  
            `File file = new File(FileType.PROPERTY, mockLocation);`  
            `file.addProperty("A=B".toCharArray()); // 3 caracteres`

            `assertDoesNotThrow(() -> file.removeContent(100));`  
            `assertEquals(0, getDiskSize(file));`  
        `}`  
    `}`

    `/* =========================================================================`  
     `* MÉTODOS AUXILIARES`  
     `* ========================================================================= */`

    `/**`  
     `* Devuelve el tamaño actual de la lista de caracteres obtenida de File.`  
     `*/`  
    `private int getDiskSize(File file) {`  
        `return file.getContent().size();`  
    `}`  
`}`

---

