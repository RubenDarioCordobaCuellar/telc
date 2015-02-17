/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccc.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "permiso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permiso.findAll", query = "SELECT p FROM Permiso p"),
    @NamedQuery(name = "Permiso.findByCodigoPermiso", query = "SELECT p FROM Permiso p WHERE p.codigoPermiso = :codigoPermiso"),
    @NamedQuery(name = "Permiso.findByNombrePermiso", query = "SELECT p FROM Permiso p WHERE p.nombrePermiso = :nombrePermiso")})
public class Permiso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoPermiso")
    private Integer codigoPermiso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombrePermiso")
    private String nombrePermiso;
    @JoinTable(name = "rol_has_permiso", joinColumns = {
        @JoinColumn(name = "permiso_codigoPermiso", referencedColumnName = "codigoPermiso")}, inverseJoinColumns = {
        @JoinColumn(name = "rol_codigoRol", referencedColumnName = "codigoRol")})
    @ManyToMany
    private List<Rol> rolList;

    public Permiso() {
    }

    public Permiso(Integer codigoPermiso) {
        this.codigoPermiso = codigoPermiso;
    }

    public Permiso(Integer codigoPermiso, String nombrePermiso) {
        this.codigoPermiso = codigoPermiso;
        this.nombrePermiso = nombrePermiso;
    }

    public Integer getCodigoPermiso() {
        return codigoPermiso;
    }

    public void setCodigoPermiso(Integer codigoPermiso) {
        this.codigoPermiso = codigoPermiso;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    @XmlTransient
    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoPermiso != null ? codigoPermiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permiso)) {
            return false;
        }
        Permiso other = (Permiso) object;
        if ((this.codigoPermiso == null && other.codigoPermiso != null) || (this.codigoPermiso != null && !this.codigoPermiso.equals(other.codigoPermiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Permiso[ codigoPermiso=" + codigoPermiso + " ]";
    }
    
}
