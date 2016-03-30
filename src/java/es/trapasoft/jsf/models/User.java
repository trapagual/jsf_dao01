
package es.trapasoft.jsf.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
/**
 * Esta clase representa el modelo de Usuario. Esta clase del model puede usarse en todas las capas: 
 * la de datos, la de controladores y la de vistas
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2008/07/dao-tutorial-data-layer.html
 */
public class User implements Serializable {
 
    // Constantes ----------------------------------------------------------------------------------
 
    private static final long serialVersionUID = 1L;
 
    // Propiedades---------------------------------------------------------------------------------
 
    private Long id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Date birthdate;
 
    private List<Project> projects;
    
    
    public User() {
        projects = new ArrayList<Project>();
    }
    // Getters/setters ----------------------------------------------------------------------------
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getFirstname() {
        return firstname;
    }
 
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
 
    public String getLastname() {
        return lastname;
    }
 
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
 
    public Date getBirthdate() {
        return birthdate;
    }
 
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
 
    // Object overrides ---------------------------------------------------------------------------
 
    /**
     * El ID es único. Así que sólo tenemos que comparar el ID de los usuarios.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof User) && (id != null)
             ? id.equals(((User) other).id)
             : (other == this);
    }
 
    /**
     * Como el ID es único para cada Usuario, usuarios con el mismo ID han de devolver el mismo hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null)
             ? (this.getClass().hashCode() + id.hashCode())
             : super.hashCode();
    }
 
    /**
     * Devuelve la cadena que representa a este usuario. No obligatorio, es bueno para leer los logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("User[id=%d,email=%s,firstname=%s,lastname=%s,birthdate=%s]",
            id, email, firstname, lastname, birthdate);
    }
 
}