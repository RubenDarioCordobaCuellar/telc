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
@Table(name = "sector")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sector.findAll", query = "SELECT s FROM Sector s"),
    @NamedQuery(name = "Sector.findByCodigoSector", query = "SELECT s FROM Sector s WHERE s.codigoSector = :codigoSector"),
    @NamedQuery(name = "Sector.findByNombreSector", query = "SELECT s FROM Sector s WHERE s.nombreSector = :nombreSector")})
public class Sector implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoSector")
    private Integer codigoSector;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreSector")
    private String nombreSector;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sectorcodigoSector")
    private List<Localidades> localidadesList;

    public Sector() {
    }

    public Sector(Integer codigoSector) {
        this.codigoSector = codigoSector;
    }

    public Sector(Integer codigoSector, String nombreSector) {
        this.codigoSector = codigoSector;
        this.nombreSector = nombreSector;
    }

    public Integer getCodigoSector() {
        return codigoSector;
    }

    public void setCodigoSector(Integer codigoSector) {
        this.codigoSector = codigoSector;
    }

    public String getNombreSector() {
        return nombreSector;
    }

    public void setNombreSector(String nombreSector) {
        this.nombreSector = nombreSector;
    }

    @XmlTransient
    public List<Localidades> getLocalidadesList() {
        return localidadesList;
    }

    public void setLocalidadesList(List<Localidades> localidadesList) {
        this.localidadesList = localidadesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoSector != null ? codigoSector.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sector)) {
            return false;
        }
        Sector other = (Sector) object;
        if ((this.codigoSector == null && other.codigoSector != null) || (this.codigoSector != null && !this.codigoSector.equals(other.codigoSector))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Sector[ codigoSector=" + codigoSector + " ]";
    }
    
}
