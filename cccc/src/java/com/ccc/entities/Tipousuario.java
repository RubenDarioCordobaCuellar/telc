/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccc.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "tipousuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipousuario.findAll", query = "SELECT t FROM Tipousuario t"),
    @NamedQuery(name = "Tipousuario.findByCodigoTipoUsuario", query = "SELECT t FROM Tipousuario t WHERE t.codigoTipoUsuario = :codigoTipoUsuario"),
    @NamedQuery(name = "Tipousuario.findByTipoUsuario", query = "SELECT t FROM Tipousuario t WHERE t.tipoUsuario = :tipoUsuario")})
public class Tipousuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoTipoUsuario")
    private Integer codigoTipoUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipoUsuario")
    private String tipoUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoUsuariocodigoTipoUsuario")
    private List<Tecnico> tecnicoList;
    @JoinColumn(name = "persona_Cedula", referencedColumnName = "Cedula")
    @ManyToOne(optional = false)
    private Persona personaCedula;
    @JoinColumn(name = "rol_codigoRol", referencedColumnName = "codigoRol")
    @ManyToOne(optional = false)
    private Rol rolcodigoRol;

    public Tipousuario() {
    }

    public Tipousuario(Integer codigoTipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

    public Tipousuario(Integer codigoTipoUsuario, String tipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getCodigoTipoUsuario() {
        return codigoTipoUsuario;
    }

    public void setCodigoTipoUsuario(Integer codigoTipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @XmlTransient
    public List<Tecnico> getTecnicoList() {
        return tecnicoList;
    }

    public void setTecnicoList(List<Tecnico> tecnicoList) {
        this.tecnicoList = tecnicoList;
    }

    public Persona getPersonaCedula() {
        return personaCedula;
    }

    public void setPersonaCedula(Persona personaCedula) {
        this.personaCedula = personaCedula;
    }

    public Rol getRolcodigoRol() {
        return rolcodigoRol;
    }

    public void setRolcodigoRol(Rol rolcodigoRol) {
        this.rolcodigoRol = rolcodigoRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoTipoUsuario != null ? codigoTipoUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipousuario)) {
            return false;
        }
        Tipousuario other = (Tipousuario) object;
        if ((this.codigoTipoUsuario == null && other.codigoTipoUsuario != null) || (this.codigoTipoUsuario != null && !this.codigoTipoUsuario.equals(other.codigoTipoUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Tipousuario[ codigoTipoUsuario=" + codigoTipoUsuario + " ]";
    }
    
}
