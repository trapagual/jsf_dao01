/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package es.trapasoft.jsf.beans;

import es.trapasoft.jsf.dao.DAOFactory;
import es.trapasoft.jsf.dao.UserDAO;
import es.trapasoft.jsf.models.User;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author alejandro
 */
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 8799656478674716638L;
    private DAOFactory javabase;
    private UserDAO userDAO;
    private List<User> users;

    private User selectedUser;

    private static Logger LOG = Logger.getLogger(UserBean.class.getName());

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
        selectedUser = new User();
        LOG.log(Level.INFO, "Init: users tiene " + users.size() + " registros.");

    }

    /* ------------- ACCIONES ------------------- */
    public String newUser() {
        setSelectedUser(new User());
        LOG.log(Level.INFO, "newUser: con el usuario vacio");
        //return "userdetail";
        return null;
    }

    public void newUserDlg() {
        setSelectedUser(new User());
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("resizable", false);
        options.put("modal", true);
                RequestContext.getCurrentInstance().openDialog("userdetaildlg", options, null);
    }

    public String salvarUsuario() throws NoSuchAlgorithmException {
        // como en el formulario no le pido el password, meto aqui 'farola' para que se grabe
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(new String("farola").getBytes());
        selectedUser.setPassword(new String(thedigest));
        userDAO.create(selectedUser);
        // rellenar de nuevo la lista de usuarios
        users = userDAO.list();
        return "user";
    }

    /* ------------ EVENTOS -------------------- */
    public void onRowSelect(SelectEvent event) {
        Long userId = ((User) event.getObject()).getId();
        // rellenar selected user
        setSelectedUser(userDAO.find(userId));
    }

    public void onRowUnselect(UnselectEvent event) {
        setSelectedUser(new User());
    }

    /* ------------- GETTERS / SETTERS ----------------- */
    public List<User> getUsers() {
        return users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User s) {
        selectedUser = s;
        if (selectedUser != null) {
            LOG.log(Level.INFO, "voy a cargar el usuario con id: " + selectedUser.getId());
        } else {
            LOG.log(Level.INFO, "voy a cargar el usuario con id: nulo");
        }

        if (!selectedUser.isEmpty()) {
            selectedUser.setProjects(userDAO.findProjectsByUserId(selectedUser.getId()));
        }
    }

}
