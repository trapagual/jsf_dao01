
package es.trapasoft.jsf.dao;


/**
 * Esta clase representa una excepción en la configuración DAO que no puede resolverse en 
 * tiempo de ejecución, como un fichero que no se encuentra, una propiedad inexistente, etc.
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2008/07/dao-tutorial-data-layer.html
 */
public class DAOConfigurationException extends RuntimeException {
 
    // Constantes ----------------------------------------------------------------------------------
 
    private static final long serialVersionUID = 1L;
 
    // Constructores -------------------------------------------------------------------------------
 
    /**
     * Construye un  DAOConfigurationException con el mensaje de error dado.
     * @param message The detail message of the DAOConfigurationException.
     */
    public DAOConfigurationException(String message) {
        super(message);
    }
 
    /**
     * Constructs a DAOConfigurationException with the given root cause.
     * @param cause The root cause of the DAOConfigurationException.
     */
    public DAOConfigurationException(Throwable cause) {
        super(cause);
    }
 
    /**
     * Constructs a DAOConfigurationException with the given detail message and root cause.
     * @param message The detail message of the DAOConfigurationException.
     * @param cause The root cause of the DAOConfigurationException.
     */
    public DAOConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
 
}
