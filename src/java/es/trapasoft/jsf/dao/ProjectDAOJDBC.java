/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.trapasoft.jsf.dao;

import static es.trapasoft.jsf.dao.DAOUtil.prepareStatement;
import static es.trapasoft.jsf.dao.DAOUtil.toSqlDate;
import es.trapasoft.jsf.models.Project;
import es.trapasoft.jsf.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alejandro
 */
public class ProjectDAOJDBC implements ProjectDAO {

    private static final String SQL_FIND_BY_ID
            = "select id, name, description, startDate, dueDate, estimatedHours, createdAt, updatedAt from projects where id=?";
    private static final String SQL_FIND_USERS_BY_PROJECT_ID
            = "select u.* from users u, projects_users pu where u.id = pu.user_id and pu.project_id = ?";
    private static final String SQL_FIND_BY_NAME
            = "select id, name, description, startDate, dueDate, estimatedHours, createdAt, updatedAt from projects where upper(name) like ?";
    private static final String SQL_FIND_PARENT
            = "select id, name, description, startDate, dueDate, estimatedHours, createdAt, updatedAt from projects where "
            + " id = (select parent_id from projects where id = ?)";
    private static final String SQL_FIND_CHILDREN
            = "select * from projects where parent_id = ? ";

    private static final String SQL_INSERT
            = "insert into projects (name, description, startdate, duedate, estimatedhours, parent_id) values (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE
            = "update projects set name=?, description=?, startdate=?, duedate=?, estimatedhours=?, parent_id=? where id = ?";
    private static final String SQL_DELETE
            = "delete from projects where id = ? ";

    private static final String SQL_ADD_USER_TO_PROJECT
            = "insert into projects_users (user_id, project_id) values (?, ?)";
    private static final String SQL_DEL_USER_FROM_PROJECT
            = " delete from projects_users where user_id = ? and project_id = ?";
    
    
    private DAOFactory daoFactory;

    /**
     * Construct a Project DAO for the given DAOFactory. Package private so that
     * it can be constructed inside the DAO package only.
     *
     * @param daoFactory The DAOFactory to construct this User DAO for.
     */
    ProjectDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Project find(Long id) throws DAOException {
        Project project = null;
        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_BY_ID, false, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                project = map(resultSet);
                project.setUsers(findUsersByProjectId(id));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return project;
    }

    @Override
    public List<Project> find(String name) throws DAOException {
        List<Project> projects = new ArrayList<Project>();
        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement p = prepareStatement(connection, SQL_FIND_BY_NAME, false, '%' + name.toUpperCase() + '%');
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Project project = map(rs);
                project.setUsers(findUsersByProjectId(project.getId()));
                projects.add(project);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAOJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException(ex);
        }
        return projects;
    }

    @Override
    public Project findParent(Long id) throws DAOException {

        Project project = null;
        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PARENT, false, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                project = map(resultSet);
                project.setUsers(findUsersByProjectId(project.getId()));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return project;
    }

    @Override
    public List<Project> findChildren(Long id) throws DAOException {
        List<Project> projects = new ArrayList<Project>();
        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_CHILDREN, false, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Project project = map(resultSet);
                project.setUsers(findUsersByProjectId(project.getId()));
                projects.add(project);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return projects;
    }


    /**
     * Carga el objeto Project con los datos del ResultSet
     *
     * @param rs
     * @return un objeto Project relleno
     * @throws SQLException
     */
    private static Project map(ResultSet rs) throws SQLException {
        /*
        id            
        name          
        description   
        startDate     
        dueDate       
        estimatedHours
        createdAt     
        updatedAt     
        parent_id 
         */
        Project p = new Project();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setStartDate(rs.getDate("startDate"));
        p.setDueDate(rs.getDate("dueDate"));
        p.setEstimatedHours(rs.getLong("estimatedHours"));
        p.setCreatedAt(rs.getTimestamp("createdAt"));
        p.setUpdatedAt(rs.getTimestamp("updatedAt"));
        p.setParentId(rs.getLong("parent_id"));

        // FIXME faltan los usuarios
        // p.setUsers(users);
        return p;
    }

    @Override
    public List<User> findUsersByProjectId(Long id) {
        List<User> users = new ArrayList<User>();
        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement p = prepareStatement(connection, SQL_FIND_USERS_BY_PROJECT_ID, false, id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setFirstname(rs.getString("firstname"));
                u.setLastname(rs.getString("lastname"));
                u.setEmail(rs.getString("email"));
                u.setBirthdate(rs.getDate("birthdate"));
                users.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAOJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException(ex);
        }
        return users;

    }

    @Override
    public void create(Project project) throws IllegalArgumentException, DAOException {
        if (project.getId() != null) {
            throw new IllegalArgumentException("Proyecto ya existe, el ID no es nulo");
        }

        Object[] values = {
            project.getName(),
            project.getDescription(),
            toSqlDate(project.getStartDate()),
            toSqlDate(project.getDueDate()),
            project.getEstimatedHours(),
            project.getParentId()
        };

        try {
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Error al crear proyecto: no hay filas insertadas.");
            }

            try {
                ResultSet generatedKeys = statement.getGeneratedKeys() ;
                if (generatedKeys.next()) {
                    project.setId(generatedKeys.getLong(1));
                } else {
                    throw new DAOException("Error al crear proyecto, no se obtiene identificador.");
                }
            } catch (Exception e) {}
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Project project) throws IllegalArgumentException, DAOException {
        if (project.getId() == null) {
            throw new IllegalArgumentException("El ID del proyecto es nulo no se puede actualizar.");
        }

        //"update project set name=?, description=?, startdate=?, duedate=?, estimatedhours=?, parent_id=? where id = ?";
        Object[] values = {
            project.getName(),
            project.getDescription(),
            toSqlDate(project.getStartDate()),
            toSqlDate(project.getDueDate()),
            project.getEstimatedHours(),
            project.getParentId(),
            project.getId()
        };
        try {
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Error al actualizar proyecto. No se actualiza ninguna fila.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Project project) throws DAOException {
        Object[] values = {
            project.getId()
        };

        try {
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Error al borrar proyecto. No se actualiza ninguna fila.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    @Override
    public void addUserToProject(Long userId, Long projectId) throws DAOException {
        Object[] values = {
            userId,
            projectId
        };

        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_ADD_USER_TO_PROJECT, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Error al añadir usuario al proyecto. No se actualiza ninguna fila.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    @Override
    public void delUserFromProject(Long userId, Long projectId) throws DAOException {
            Object[] values = {
            userId,
            projectId
        };

        try {
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DEL_USER_FROM_PROJECT, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Error al eliminar usuario del proyecto. No se actualiza ninguna fila.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }



}
