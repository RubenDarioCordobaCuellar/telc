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
import javax.persistence.ManyToOne;
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
@Table(name = "localidades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localidades.findAll", query = "SELECT l FROM Localidades l"),
    @NamedQuery(name = "Localidades.findByCodigoLocalidad", query = "SELECT l FROM Localidades l WHERE l.codigoLocalidad = :codigoLocalidad"),
    @NamedQuery(name = "Localidades.findByNombreLocalidad", query = "SELECT l FROM Localidades l WHERE l.nombreLocalidad = :nombreLocalidad")})
public class Localidades implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoLocalidad")
    private Integer codigoLocalidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreLocalidad")
    private String nombreLocalidad;
    @JoinTable(name = "ordenes_has_localidades", joinColumns = {
        @JoinColumn(name = "Localidades_codigoLocalidad", referencedColumnName = "codigoLocalidad")}, inverseJoinColumns = {
        @JoinColumn(name = "ordenes_numeroOrden", referencedColumnName = "numeroOrden")})
    @ManyToMany
    private List<Ordenes> ordenesList;
    @JoinColumn(name = "Municipio_Ciudad_codigoMunicipio", referencedColumnName = "codigoMunicipio")
    @ManyToOne(optional = false)
    private MunicipioCiudad municipioCiudadcodigoMunicipio;
    @JoinColumn(name = "Sector_codigoSector", referencedColumnName = "codigoSector")
    @ManyToOne(optional = false)
    private Sector sectorcodigoSector;

    public Localidades() {
    }

    public Localidades(Integer codigoLocalidad) {
        this.codigoLocalidad = codigoLocalidad;
    }

    public Localidades(Integer codigoLocalidad, String nombreLocalidad) {
        this.codigoLocalidad = codigoLocalidad;
        this.nombreLocalidad = nombreLocalidad;
    }

    public Integer getCodigoLocalidad() {
        return codigoLocalidad;
    }

    public void setCodigoLocalidad(Integer codigoLocalidad) {
        this.codigoLocalidad = codigoLocalidad;
    }

    public String getNombreLocalidad() {
        return nombreLocalidad;
    }

    public void setNombreLocalidad(String nombreLocalidad) {
        this.nombreLocalidad = nombreLocalidad;
    }

    @XmlTransient
    public List<Ordenes> getOrdenesList() {
        return ordenesList;
    }

    public void setOrdenesList(List<Ordenes> ordenesList) {
        this.ordenesList = ordenesList;
    }

    public MunicipioCiudad getMunicipioCiudadcodigoMunicipio() {
        return municipioCiudadcodigoMunicipio;
    }

    public void setMunicipioCiudadcodigoMunicipio(MunicipioCiudad municipioCiudadcodigoMunicipio) {
        this.municipioCiudadcodigoMunicipio = municipioCiudadcodigoMunicipio;
    }

    public Sector getSectorcodigoSector() {
        return sectorcodigoSector;
    }

    public void setSectorcodigoSector(Sector sectorcodigoSector) {
        this.sectorcodigoSector = sectorcodigoSector;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoLocalidad != null ? codigoLocalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Localidades)) {
            return false;
        }
        Localidades other = (Localidades) object;
        if ((this.codigoLocalidad == null && other.codigoLocalidad != null) || (this.codigoLocalidad != null && !this.codigoLocalidad.equals(other.codigoLocalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Localidades[ codigoLocalidad=" + codigoLocalidad + " ]";
    }
    
}
