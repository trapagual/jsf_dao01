package es.trapasoft.jsf.dao;

import static es.trapasoft.jsf.dao.DAOUtil.*;
import es.trapasoft.jsf.models.Project;
import es.trapasoft.jsf.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a concrete JDBC implementation of the {@link UserDAO}
 * interface.
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2008/07/dao-tutorial-data-layer.html
 */
public class UserDAOJDBC implements UserDAO {

    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID
            = "SELECT id, email, firstname, lastname, birthdate FROM users WHERE id = ?";
    private static final String SQL_FIND_BY_EMAIL_AND_PASSWORD
            = "SELECT id, email, firstname, lastname, birthdate FROM users WHERE email = ? AND password = MD5(?)";
    private static final String SQL_FIND_PROJECT_BY_USER_ID
            = "select p.id, p.name, p.description, p.startDate, p.dueDate, p.estimatedHours from projects p, projects_users pu where p.id=pu.project_id and pu.user_id = ?";
    private static final String SQL_LIST_ORDER_BY_ID
            = "SELECT id, email, firstname, lastname, birthdate FROM users ORDER BY id";
    private static final String SQL_INSERT
            = "INSERT INTO users (email, password, firstname, lastname, birthdate) VALUES (?, MD5(?), ?, ?, ?)";
    private static final String SQL_UPDATE
            = "UPDATE users SET email = ?, firstname = ?, lastname = ?, birthdate = ? WHERE id = ?";
    private static final String SQL_DELETE
            = "DELETE FROM users WHERE id = ?";
    private static final String SQL_EXIST_EMAIL
            = "SELECT id FROM users WHERE email = ?";
    private static final String SQL_CHANGE_PASSWORD
            = "UPDATE users SET password = MD5(?) WHERE id = ?";
    private static final String SQL_ADD_PROJECT_TO_USER
            = "insert into projects_users (user_id, project_id) values (?, ?)";
    private static final String SQL_DEL_PROJECT_FROM_USER
            = " delete from projects_users where user_id = ? and project_id = ?";

    // Vars ---------------------------------------------------------------------------------------
    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------
    /**
     * Construct an User DAO for the given DAOFactory. Package private so that
     * it can be constructed inside the DAO package only.
     *
     * @param daoFactory The DAOFactory to construct this User DAO for.
     */
    UserDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public User find(Long id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }

    @Override
    public User find(String email, String password) throws DAOException {
        return find(SQL_FIND_BY_EMAIL_AND_PASSWORD, email, password);
    }

    /**
     * Returns the user from the database matching the given SQL query with the
     * given values.
     *
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The user from the database matching the given SQL query with the
     * given values.
     * @throws DAOException If something fails at database level.
     */
    private User find(String sql, Object... values) throws DAOException {
        User user = null;

        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return user;
    }

    @Override
    public List list() throws DAOException {
        List<User> users = new ArrayList<User>();

        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return users;
    }

    @Override
    public void create(User user) throws IllegalArgumentException, DAOException {
        if (user.getId() != null) {
            throw new IllegalArgumentException("User is already created, the user ID is not null.");
        }

        Object[] values = {
            user.getEmail(),
            user.getPassword(),
            user.getFirstname(),
            user.getLastname(),
            toSqlDate(user.getBirthdate())
        };

        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            } else {
                throw new DAOException("Creating user failed, no generated key obtained.");
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(User user) throws DAOException {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User is not created yet, the user ID is null.");
        }

        Object[] values = {
            user.getEmail(),
            user.getFirstname(),
            user.getLastname(),
            toSqlDate(user.getBirthdate()),
            user.getId()
        };

        try {
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(User user) throws DAOException {
        Object[] values = {
            user.getId()
        };

        try {
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting user failed, no rows affected.");
            } else {
                user.setId(null);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean existEmail(String email) throws DAOException {
        Object[] values = {
            email
        };

        boolean exist = false;

        try {
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_EXIST_EMAIL, false, values);
                ResultSet resultSet = statement.executeQuery();
            exist = resultSet.next();
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return exist;
    }

    @Override
    public void changePassword(User user) throws DAOException {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User is not created yet, the user ID is null.");
        }

        Object[] values = {
            user.getPassword(),
            user.getId()
        };

        try {
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_CHANGE_PASSWORD, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Changing password failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    // Helpers ------------------------------------------------------------------------------------
    /**
     * Map the current row of the given ResultSet to an User.
     *
     * @param resultSet The ResultSet of which the current row is to be mapped
     * to an User.
     * @return The mapped User from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        user.setBirthdate(resultSet.getDate("birthdate"));
        return user;
    }

    @Override
    public List<Project> findProjectsByUserId(Long id) throws DAOException {
        List<Project> projects = new ArrayList<Project>();
        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement ps = prepareStatement(connection, SQL_FIND_PROJECT_BY_USER_ID, false, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Project p = new Project();
                p.setId(rs.getLong("p.id"));
                p.setName(rs.getString("p.name"));
                p.setDescription(rs.getString("p.description"));
                p.setStartDate(rs.getDate("p.startDate"));
                p.setDueDate(rs.getDate("p.dueDate"));
                p.setEstimatedHours(rs.getLong("p.estimatedHours"));
                ProjectDAOJDBC pdao = new ProjectDAOJDBC(daoFactory);
                p.setUsers(pdao.findUsersByProjectId(p.getId()));
                projects.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAOJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException(ex);
        }
        return projects;
    }

    @Override
    public void addProjectToUser(Long userId, Long projectId) throws DAOException {
        Object[] values = {
            userId,
            projectId
        };

        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_ADD_PROJECT_TO_USER, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Error al añadir usuario al proyecto. No se actualiza ninguna fila.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void delProjectFromUser(Long userId, Long projectId) throws DAOException {
        Object[] values = {
            userId,
            projectId
        };

        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DEL_PROJECT_FROM_USER, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Error al eliminar usuario del proyecto. No se actualiza ninguna fila.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

}
