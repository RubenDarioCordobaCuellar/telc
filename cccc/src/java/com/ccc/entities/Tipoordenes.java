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
@Table(name = "tipoordenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoordenes.findAll", query = "SELECT t FROM Tipoordenes t"),
    @NamedQuery(name = "Tipoordenes.findByCodigoTipo", query = "SELECT t FROM Tipoordenes t WHERE t.codigoTipo = :codigoTipo"),
    @NamedQuery(name = "Tipoordenes.findByTipoOrden", query = "SELECT t FROM Tipoordenes t WHERE t.tipoOrden = :tipoOrden")})
public class Tipoordenes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoTipo")
    private Integer codigoTipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "tipoOrden")
    private String tipoOrden;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoOrdenescodigoTipo")
    private List<Ordenes> ordenesList;

    public Tipoordenes() {
    }

    public Tipoordenes(Integer codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

    public Tipoordenes(Integer codigoTipo, String tipoOrden) {
        this.codigoTipo = codigoTipo;
        this.tipoOrden = tipoOrden;
    }

    public Integer getCodigoTipo() {
        return codigoTipo;
    }

    public void setCodigoTipo(Integer codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

    public String getTipoOrden() {
        return tipoOrden;
    }

    public void setTipoOrden(String tipoOrden) {
        this.tipoOrden = tipoOrden;
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
        hash += (codigoTipo != null ? codigoTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoordenes)) {
            return false;
        }
        Tipoordenes other = (Tipoordenes) object;
        if ((this.codigoTipo == null && other.codigoTipo != null) || (this.codigoTipo != null && !this.codigoTipo.equals(other.codigoTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Tipoordenes[ codigoTipo=" + codigoTipo + " ]";
    }
    
}
