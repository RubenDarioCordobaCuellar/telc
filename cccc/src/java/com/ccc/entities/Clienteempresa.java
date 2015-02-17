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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "clienteempresa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clienteempresa.findAll", query = "SELECT c FROM Clienteempresa c"),
    @NamedQuery(name = "Clienteempresa.findByCodigoNIC", query = "SELECT c FROM Clienteempresa c WHERE c.codigoNIC = :codigoNIC")})
public class Clienteempresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoNIC")
    private Integer codigoNIC;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clienteEmpresacodigoNIC")
    private List<Ordenes> ordenesList;

    public Clienteempresa() {
    }

    public Clienteempresa(Integer codigoNIC) {
        this.codigoNIC = codigoNIC;
    }

    public Integer getCodigoNIC() {
        return codigoNIC;
    }

    public void setCodigoNIC(Integer codigoNIC) {
        this.codigoNIC = codigoNIC;
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
        hash += (codigoNIC != null ? codigoNIC.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clienteempresa)) {
            return false;
        }
        Clienteempresa other = (Clienteempresa) object;
        if ((this.codigoNIC == null && other.codigoNIC != null) || (this.codigoNIC != null && !this.codigoNIC.equals(other.codigoNIC))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Clienteempresa[ codigoNIC=" + codigoNIC + " ]";
    }
    
}
