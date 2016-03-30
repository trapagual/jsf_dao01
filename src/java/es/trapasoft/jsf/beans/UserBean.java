/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package es.trapasoft.jsf.beans;


import es.trapasoft.jsf.dao.DAOFactory;
import es.trapasoft.jsf.dao.ProjectDAO;
import es.trapasoft.jsf.dao.UserDAO;
import es.trapasoft.jsf.models.Project;
import es.trapasoft.jsf.models.User;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author alejandro
 */
@ManagedBean
@ViewScoped
public class UserBean {

    private DAOFactory javabase;
    private UserDAO userDAO;
    private List<User> users;
    
    private User selectedUser;
    private ProjectDAO projectDAO;
    
    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }
    
    @PostConstruct
    public void init() {
        javabase = DAOFactory.getInstance("javabase.jdbc");
        userDAO = javabase.getUserDAO();
        users = userDAO.list();
    }
    
    /* ------------ EVENTOS -------------------- */
    
    public void onRowSelect(SelectEvent event) {
        Long userId = ((User) event.getObject()).getId();
        // rellenar selected user
        setSelectedUser(userDAO.find(userId));
    }
    
    public void onRowUnselect(UnselectEvent event) {
        setSelectedUser(null);
    }
    
    /* ------------- GETTERS / SETTERS ----------------- */

    public List<User> getUsers() {
        return users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }
    
    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
        if (selectedUser != null) {
            projectDAO = javabase.getProjectDAO();
            // MAL AQUI HAY QUE RELLENAR LOS PROYECTOS DEL USUARIO   projects = projectDAO.findUsersByProjectId(selectedUser.getId());
            selectedUser.setProjects(userDAO.findProjectsByUserId(selectedUser.getId()));
        }
    }




    
}
