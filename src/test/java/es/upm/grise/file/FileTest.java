package es.upm.grise.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.grise.file.exceptions.InvalidContentException;
import es.upm.grise.file.exceptions.WrongEncodingException;
import es.upm.grise.file.exceptions.WrongFileTypeException;

/**
 * Suite de pruebas unitarias para la clase File.
 * Basada en las especificaciones tecnicas de JUnit 5.
 */
public class FileTest {

    private Location mockLocation;

    @BeforeEach
    void setUp() {
        // Inicializacion de dependencia Location
        mockLocation = null;
    }

    /* =========================================================================
     * PRUEBAS DEL CONSTRUCTOR
     * ========================================================================= */

    @Nested
    @DisplayName("Pruebas de Inicialización / Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("El constructor debe inicializar el contenido vacío pero no nulo")
        void testConstructor_ValidParameters_InitializesEmptyContent() {
            File file = new File(FileType.PROPERTY, mockLocation);
            
            // Verificamos que el archivo esté inicializado, no sea null y contenga 0 elementos
            assertNotNull(file.getContent(), "El contenido no debe ser null");
            assertEquals(0, getDiskSize(file), "El tamaño inicial del contenido debe ser 0");
        }
    }

    /* =========================================================================
     * PRUEBAS PARA addProperty(char[] content)
     * ========================================================================= */

    @Nested
    @DisplayName("Pruebas de addProperty()")
    class AddPropertyTests {

        @Test
        @DisplayName("Happy Path: Añade correctamente un par clave=valor válido")
        void testAddProperty_ValidFormat_AppendsToContent() {
            File file = new File(FileType.PROPERTY, mockLocation);
            char[] property = "KEY=VALUE".toCharArray();

            assertDoesNotThrow(() -> file.addProperty(property));
            assertEquals("KEY=VALUE".length(), getDiskSize(file));
        }

        @Test
        @DisplayName("Lanza InvalidContentException si el parámetro content es null")
        void testAddProperty_NullContent_ThrowsInvalidContentException() {
            File file = new File(FileType.PROPERTY, mockLocation);

            assertThrows(InvalidContentException.class, () -> file.addProperty(null));
        }

        @Test
        @DisplayName("Lanza WrongFileTypeException si el tipo de archivo es IMAGE")
        void testAddProperty_WrongFileTypeImage_ThrowsWrongFileTypeException() {
            File file = new File(FileType.IMAGE, mockLocation);
            char[] property = "KEY=VALUE".toCharArray();

            assertThrows(WrongFileTypeException.class, () -> file.addProperty(property));
        }

        @Test
        @DisplayName("Lanza InvalidContentException si no contiene el carácter '='")
        void testAddProperty_FormatWithoutEquals_ThrowsInvalidContentException() {
            File file = new File(FileType.PROPERTY, mockLocation);
            char[] invalidProperty = "KEYVALUE".toCharArray();

            assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));
        }

        @Test
        @DisplayName("Lanza InvalidContentException si contiene más de un '='")
        void testAddProperty_FormatWithMultipleEquals_ThrowsInvalidContentException() {
            File file = new File(FileType.PROPERTY, mockLocation);
            char[] invalidProperty = "KEY=VAL=UE".toCharArray();

            assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));
        }

        @Test
        @DisplayName("Lanza InvalidContentException si empieza por '='")
        void testAddProperty_FormatStartsWithEquals_ThrowsInvalidContentException() {
            File file = new File(FileType.PROPERTY, mockLocation);
            char[] invalidProperty = "=VALUE".toCharArray();

            assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));
        }

        @Test
        @DisplayName("Lanza InvalidContentException si termina en '='")
        void testAddProperty_FormatEndsWithEquals_ThrowsInvalidContentException() {
            File file = new File(FileType.PROPERTY, mockLocation);
            char[] invalidProperty = "KEY=".toCharArray();

            assertThrows(InvalidContentException.class, () -> file.addProperty(invalidProperty));
        }
    }

    /* =========================================================================
     * PRUEBAS PARA addImageBytes(char[] content)
     * ========================================================================= */

    @Nested
    @DisplayName("Pruebas de addImageBytes()")
    class AddImageBytesTests {

        @Test
        @DisplayName("Happy Path: Añade correctamente bytes válidos (<= 255) a un archivo IMAGE")
        void testAddImageBytes_ValidBytes_AppendsToContent() {
            File file = new File(FileType.IMAGE, mockLocation);
            char[] validBytes = new char[] { 0, 127, 255 };

            assertDoesNotThrow(() -> file.addImageBytes(validBytes));
            assertEquals(3, getDiskSize(file));
        }

        @Test
        @DisplayName("Lanza InvalidContentException si el parámetro content es null")
        void testAddImageBytes_NullContent_ThrowsInvalidContentException() {
            File file = new File(FileType.IMAGE, mockLocation);

            assertThrows(InvalidContentException.class, () -> file.addImageBytes(null));
        }

        @Test
        @DisplayName("Lanza WrongFileTypeException si el tipo de archivo es PROPERTY")
        void testAddImageBytes_WrongFileTypeProperty_ThrowsWrongFileTypeException() {
            File file = new File(FileType.PROPERTY, mockLocation);
            char[] validBytes = new char[] { 10, 20, 30 };

            assertThrows(WrongFileTypeException.class, () -> file.addImageBytes(validBytes));
        }

        @Test
        @DisplayName("Lanza WrongEncodingException si algún carácter es mayor a 255")
        void testAddImageBytes_CharacterExceeds255_ThrowsWrongEncodingException() {
            File file = new File(FileType.IMAGE, mockLocation);
            char[] invalidBytes = new char[] { 100, 256 }; // 256 excede el límite de 8 bits

            assertThrows(WrongEncodingException.class, () -> file.addImageBytes(invalidBytes));
        }
    }

    /* =========================================================================
     * PRUEBAS PARA removeContent(int numberChars)
     * ========================================================================= */

    @Nested
    @DisplayName("Pruebas de removeContent()")
    class RemoveContentTests {

        @Test
        @DisplayName("Happy Path: Elimina los últimos N caracteres correctamente")
        void testRemoveContent_ValidNumberChars_RemovesLastNCharacters() throws Exception {
            File file = new File(FileType.PROPERTY, mockLocation);
            file.addProperty("KEY=VALUE".toCharArray()); // 9 caracteres
            
            file.removeContent(3);
            
            assertEquals(6, getDiskSize(file));
        }

        @Test
        @DisplayName("Vacía el contenido cuando numberChars es igual a la longitud actual")
        void testRemoveContent_ExactContentLength_ClearsContent() throws Exception {
            File file = new File(FileType.PROPERTY, mockLocation);
            file.addProperty("A=B".toCharArray()); // 3 caracteres
            
            file.removeContent(3);

            assertEquals(0, getDiskSize(file));
        }

        @Test
        @DisplayName("Vacía el contenido sin lanzar excepción cuando numberChars supera la longitud actual")
        void testRemoveContent_NumberCharsGreaterThanSize_ClearsContentWithoutException() throws Exception {
            File file = new File(FileType.PROPERTY, mockLocation);
            file.addProperty("A=B".toCharArray()); // 3 caracteres

            assertDoesNotThrow(() -> file.removeContent(100));
            assertEquals(0, getDiskSize(file));
        }
    }

    /* =========================================================================
     * MÉTODOS AUXILIARES
     * ========================================================================= */

    /**
     * Devuelve el tamaño actual de la lista de caracteres obtenida de File.
     */
    private int getDiskSize(File file) {
        return file.getContent().size();
    }
}