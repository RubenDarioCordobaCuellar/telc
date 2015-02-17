/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccc.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "vereda_barrio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VeredaBarrio.findAll", query = "SELECT v FROM VeredaBarrio v"),
    @NamedQuery(name = "VeredaBarrio.findByCodigoVereda", query = "SELECT v FROM VeredaBarrio v WHERE v.codigoVereda = :codigoVereda"),
    @NamedQuery(name = "VeredaBarrio.findByNombreVereda", query = "SELECT v FROM VeredaBarrio v WHERE v.nombreVereda = :nombreVereda")})
public class VeredaBarrio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoVereda")
    private Integer codigoVereda;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreVereda")
    private String nombreVereda;
    @JoinColumn(name = "corregimiento_codigoMunicipio", referencedColumnName = "codigoMunicipio")
    @ManyToOne(optional = false)
    private Corregimiento corregimientocodigoMunicipio;

    public VeredaBarrio() {
    }

    public VeredaBarrio(Integer codigoVereda) {
        this.codigoVereda = codigoVereda;
    }

    public VeredaBarrio(Integer codigoVereda, String nombreVereda) {
        this.codigoVereda = codigoVereda;
        this.nombreVereda = nombreVereda;
    }

    public Integer getCodigoVereda() {
        return codigoVereda;
    }

    public void setCodigoVereda(Integer codigoVereda) {
        this.codigoVereda = codigoVereda;
    }

    public String getNombreVereda() {
        return nombreVereda;
    }

    public void setNombreVereda(String nombreVereda) {
        this.nombreVereda = nombreVereda;
    }

    public Corregimiento getCorregimientocodigoMunicipio() {
        return corregimientocodigoMunicipio;
    }

    public void setCorregimientocodigoMunicipio(Corregimiento corregimientocodigoMunicipio) {
        this.corregimientocodigoMunicipio = corregimientocodigoMunicipio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoVereda != null ? codigoVereda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VeredaBarrio)) {
            return false;
        }
        VeredaBarrio other = (VeredaBarrio) object;
        if ((this.codigoVereda == null && other.codigoVereda != null) || (this.codigoVereda != null && !this.codigoVereda.equals(other.codigoVereda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.VeredaBarrio[ codigoVereda=" + codigoVereda + " ]";
    }
    
}
