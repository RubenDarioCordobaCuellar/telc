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
@Table(name = "corregimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Corregimiento.findAll", query = "SELECT c FROM Corregimiento c"),
    @NamedQuery(name = "Corregimiento.findByCodigoMunicipio", query = "SELECT c FROM Corregimiento c WHERE c.codigoMunicipio = :codigoMunicipio"),
    @NamedQuery(name = "Corregimiento.findByNombreMunicipio", query = "SELECT c FROM Corregimiento c WHERE c.nombreMunicipio = :nombreMunicipio")})
public class Corregimiento implements Serializable {
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
    @JoinColumn(name = "Municipio_Ciudad_codigoMunicipio", referencedColumnName = "codigoMunicipio")
    @ManyToOne(optional = false)
    private MunicipioCiudad municipioCiudadcodigoMunicipio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corregimientocodigoMunicipio")
    private List<VeredaBarrio> veredaBarrioList;

    public Corregimiento() {
    }

    public Corregimiento(Integer codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public Corregimiento(Integer codigoMunicipio, String nombreMunicipio) {
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

    public MunicipioCiudad getMunicipioCiudadcodigoMunicipio() {
        return municipioCiudadcodigoMunicipio;
    }

    public void setMunicipioCiudadcodigoMunicipio(MunicipioCiudad municipioCiudadcodigoMunicipio) {
        this.municipioCiudadcodigoMunicipio = municipioCiudadcodigoMunicipio;
    }

    @XmlTransient
    public List<VeredaBarrio> getVeredaBarrioList() {
        return veredaBarrioList;
    }

    public void setVeredaBarrioList(List<VeredaBarrio> veredaBarrioList) {
        this.veredaBarrioList = veredaBarrioList;
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
        if (!(object instanceof Corregimiento)) {
            return false;
        }
        Corregimiento other = (Corregimiento) object;
        if ((this.codigoMunicipio == null && other.codigoMunicipio != null) || (this.codigoMunicipio != null && !this.codigoMunicipio.equals(other.codigoMunicipio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Corregimiento[ codigoMunicipio=" + codigoMunicipio + " ]";
    }
    
}
