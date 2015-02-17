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
@Table(name = "municipio_ciudad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MunicipioCiudad.findAll", query = "SELECT m FROM MunicipioCiudad m"),
    @NamedQuery(name = "MunicipioCiudad.findByCodigoMunicipio", query = "SELECT m FROM MunicipioCiudad m WHERE m.codigoMunicipio = :codigoMunicipio"),
    @NamedQuery(name = "MunicipioCiudad.findByNombreMunicipio", query = "SELECT m FROM MunicipioCiudad m WHERE m.nombreMunicipio = :nombreMunicipio")})
public class MunicipioCiudad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoMunicipio")
    private Integer codigoMunicipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreMunicipio")
    private String nombreMunicipio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipioCiudadcodigoMunicipio")
    private List<Corregimiento> corregimientoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipioCiudadcodigoMunicipio")
    private List<Localidades> localidadesList;

    public MunicipioCiudad() {
    }

    public MunicipioCiudad(Integer codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public MunicipioCiudad(Integer codigoMunicipio, String nombreMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
        this.nombreMunicipio = nombreMunicipio;
    }

    public Integer getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Integer codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    @XmlTransient
    public List<Corregimiento> getCorregimientoList() {
        return corregimientoList;
    }

    public void setCorregimientoList(List<Corregimiento> corregimientoList) {
        this.corregimientoList = corregimientoList;
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
        hash += (codigoMunicipio != null ? codigoMunicipio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MunicipioCiudad)) {
            return false;
        }
        MunicipioCiudad other = (MunicipioCiudad) object;
        if ((this.codigoMunicipio == null && other.codigoMunicipio != null) || (this.codigoMunicipio != null && !this.codigoMunicipio.equals(other.codigoMunicipio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.MunicipioCiudad[ codigoMunicipio=" + codigoMunicipio + " ]";
    }
    
}
