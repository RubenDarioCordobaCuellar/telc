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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "tecnico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tecnico.findAll", query = "SELECT t FROM Tecnico t"),
    @NamedQuery(name = "Tecnico.findByCodigoTecnico", query = "SELECT t FROM Tecnico t WHERE t.codigoTecnico = :codigoTecnico")})
public class Tecnico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoTecnico")
    private Integer codigoTecnico;
    @JoinTable(name = "tecnico_has_vehiculo", joinColumns = {
        @JoinColumn(name = "Tecnico_codigoTecnico", referencedColumnName = "codigoTecnico")}, inverseJoinColumns = {
        @JoinColumn(name = "Vehiculo_codigoVehiculo", referencedColumnName = "codigoVehiculo")})
    @ManyToMany
    private List<Vehiculo> vehiculoList;
    @JoinTable(name = "indicadortpo_has_tecnico", joinColumns = {
        @JoinColumn(name = "Tecnico_codigoTecnico", referencedColumnName = "codigoTecnico")}, inverseJoinColumns = {
        @JoinColumn(name = "indicadorTPO_codigoIndicador", referencedColumnName = "codigoIndicador")})
    @ManyToMany
    private List<Indicadortpo> indicadortpoList;
    @JoinColumn(name = "tipoUsuario_codigoTipoUsuario", referencedColumnName = "codigoTipoUsuario")
    @ManyToOne(optional = false)
    private Tipousuario tipoUsuariocodigoTipoUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tecnicocodigoTecnico")
    private List<Ordenes> ordenesList;

    public Tecnico() {
    }

    public Tecnico(Integer codigoTecnico) {
        this.codigoTecnico = codigoTecnico;
    }

    public Integer getCodigoTecnico() {
        return codigoTecnico;
    }

    public void setCodigoTecnico(Integer codigoTecnico) {
        this.codigoTecnico = codigoTecnico;
    }

    @XmlTransient
    public List<Vehiculo> getVehiculoList() {
        return vehiculoList;
    }

    public void setVehiculoList(List<Vehiculo> vehiculoList) {
        this.vehiculoList = vehiculoList;
    }

    @XmlTransient
    public List<Indicadortpo> getIndicadortpoList() {
        return indicadortpoList;
    }

    public void setIndicadortpoList(List<Indicadortpo> indicadortpoList) {
        this.indicadortpoList = indicadortpoList;
    }

    public Tipousuario getTipoUsuariocodigoTipoUsuario() {
        return tipoUsuariocodigoTipoUsuario;
    }

    public void setTipoUsuariocodigoTipoUsuario(Tipousuario tipoUsuariocodigoTipoUsuario) {
        this.tipoUsuariocodigoTipoUsuario = tipoUsuariocodigoTipoUsuario;
    }

    @XmlTransient
    public List<Ordenes> getOrdenesList() {
        return ordenesList;
    }

    public void setOrdenesList(List<Ordenes> ordenesList) {
        this.ordenesList = ordenesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoTecnico != null ? codigoTecnico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tecnico)) {
            return false;
        }
        Tecnico other = (Tecnico) object;
        if ((this.codigoTecnico == null && other.codigoTecnico != null) || (this.codigoTecnico != null && !this.codigoTecnico.equals(other.codigoTecnico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Tecnico[ codigoTecnico=" + codigoTecnico + " ]";
    }
    
}
