/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.trapasoft.jsf.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class Project implements Serializable {

    /*
        +----------------+--------------+------+-----+---------------------+-----------------------------+ 
        | Field          | Type         | Null | Key | Default             | Extra                       | 
        +----------------+--------------+------+-----+---------------------+-----------------------------+ 
        | id             | int(11)      | NO   | PRI | NULL                | auto_increment              | 
        | name           | varchar(150) | NO   |     | NULL                |                             | 
        | description    | text         | YES  |     | NULL                |                             | 
        | startDate      | date         | YES  |     | NULL                |                             | 
        | dueDate        | date         | YES  |     | NULL                |                             | 
        | estimatedHours | double       | YES  |     | NULL                |                             | 
        | createdAt      | timestamp    | NO   |     | CURRENT_TIMESTAMP   | on update CURRENT_TIMESTAMP | 
        | updatedAt      | timestamp    | NO   |     | 0000-00-00 00:00:00 |                             | 
        | parent_id      | int(11)      | YES  |     | NULL                |                             | 
        +----------------+--------------+------+-----+---------------------+-----------------------------+ 
     */
    private Long id, parentId;
    private String name, description;
    private Date startDate, dueDate, createdAt, updatedAt;
    private double estimatedHours;

    private List<User> users;

    public Project() {
        users = new ArrayList<User>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * El ID es único. Así que sólo tenemos que comparar el ID de los proyectos.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Project) && (id != null)
                ? id.equals(((Project) other).id)
                : (other == this);
    }

    /**
     *
     * Como el ID es único para cada Proyecto, proyectos con el mismo ID han de
     * devolver el mismo hashcode.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null)
                ? (this.getClass().hashCode() + id.hashCode())
                : super.hashCode();
    }

    /**
     * Devuelve la cadena que representa a este usuario. No obligatorio, es
     * bueno para leer los logs.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Project[id=%d, name=%s, startDate=%s, dueDate=%s]",
                id, name, startDate, dueDate);
    }
}
