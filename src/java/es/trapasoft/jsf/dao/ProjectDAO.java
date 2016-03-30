package es.trapasoft.jsf.dao;

import es.trapasoft.jsf.models.Project;
import es.trapasoft.jsf.models.User;
import java.util.List;

public interface ProjectDAO {

    /**
     * Devuelve un proyecto por su id
     *
     * @param id
     * @return
     * @throws DAOException
     */
    public Project find(Long id) throws DAOException;

    /**
     * Devuelve una lista de proyectos cuyo nombre contiene 'name'
     *
     * @param name
     * @return List<Project>
     * @throws DAOException
     */
    public List<Project> find(String name) throws DAOException;

    /**
     * Devuelve el proyecto padre de un proyecto
     *
     * @param id
     * @return
     * @throws DAOException
     */
    public Project findParent(Long id) throws DAOException;

    /**
     * Devuelve la lista de hijos de un proyecto
     *
     * @param id
     * @return
     * @throws DAOException
     */
    public List<Project> findChildren(Long id) throws DAOException;


    /**
     * Crea el usuario dado en la bd. El ID de usuario ha de ser null, si no
     * lanza un IllegalArgumentException. Tras la creacion, se cargara el ID del
     * usuario con el ID obtenido de la b.d.
     *
     * @param project
     * @return
     */
    public void create(Project project) throws IllegalArgumentException, DAOException;

    /**
     * Actualiza el proyecto dado en la bd. El ID del proyecto NO puede ser
     * nulo. Si lo es lanza un IllegalArgumentException. NO se actualizan
     * usuarios. Usar addUserToProject o delUserFromProject
     *
     * @param project El proyecto a modificar
     * @throws IllegalArgumentException Si el ID del proyecto es nulo
     * @throws DAOException Si hay fallos a nivel de base de datos
     */
    public void update(Project project) throws IllegalArgumentException, DAOException;

    /**
     * Borra el proyecto de la base de datos.
     * Tiene que borrar también la asignación de usuarios.
     * Tras borrar, el DAO pone el ID del proyecto recibido a null
     *
     * @param project El proyecto a borrar de la bd
     * @throws DAOException Si falla algo en la bd.
     */
    public void delete(Project project) throws DAOException;
    
    /**
     * Añade el usuario con ID dado al proyecto con el ID dado
     * @param userId
     * @param projectId
     * @throws DAOException 
     */
    public void addUserToProject(Long userId, Long projectId) throws DAOException;
    
    /**
     * Elimina el usuario con ID dado del proyecto con el ID dado
     * @param userId
     * @param projectId
     * @throws DAOException 
     */
    public void delUserFromProject(Long userId, Long projectId) throws DAOException;
    
    /**
     * Devuelve la lista de usuarios que participan en este proyecto
     * @return
     * @throws DAOException 
     */
    public List<User> findUsersByProjectId(Long id) throws DAOException;
}
