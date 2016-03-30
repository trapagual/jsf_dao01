
package es.trapasoft.jsf.dao;


/**
 * Esta clase representa una excepción DAO genérica. Ha de envolver cualquier excpeción que
 * pueda devolver el código, como las SQLExceptions.
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2008/07/dao-tutorial-data-layer.html
 */
public class DAOException extends RuntimeException {
 
    // Constantes ----------------------------------------------------------------------------------
 
    private static final long serialVersionUID = 1L;
 
    // Constructores -------------------------------------------------------------------------------
 
    /**
     * Construye un DAOException con el mendaje dado
     * @param message El detalle de la excpeción.
     */
    public DAOException(String message) {
        super(message);
    }
 
    /**
     * Construye una DAOException con la causa dada.
     * @param cause La causa de la excpción.
     */
    public DAOException(Throwable cause) {
        super(cause);
    }
 
    /**
     * Construye una DAOException con la causa dada y el mensaje de detalle dado.
     * @param message El detalle de la excpeción.
     * @param cause La causa de la excpción.
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
 
}
