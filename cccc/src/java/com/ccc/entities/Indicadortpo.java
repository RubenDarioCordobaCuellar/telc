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
@Table(name = "indicadortpo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indicadortpo.findAll", query = "SELECT i FROM Indicadortpo i"),
    @NamedQuery(name = "Indicadortpo.findByCodigoIndicador", query = "SELECT i FROM Indicadortpo i WHERE i.codigoIndicador = :codigoIndicador"),
    @NamedQuery(name = "Indicadortpo.findByNombreIndicador", query = "SELECT i FROM Indicadortpo i WHERE i.nombreIndicador = :nombreIndicador")})
public class Indicadortpo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoIndicador")
    private Integer codigoIndicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreIndicador")
    private String nombreIndicador;
    @ManyToMany(mappedBy = "indicadortpoList")
    private List<Tecnico> tecnicoList;

    public Indicadortpo() {
    }

    public Indicadortpo(Integer codigoIndicador) {
        this.codigoIndicador = codigoIndicador;
    }

    public Indicadortpo(Integer codigoIndicador, String nombreIndicador) {
        this.codigoIndicador = codigoIndicador;
        this.nombreIndicador = nombreIndicador;
    }

    public Integer getCodigoIndicador() {
        return codigoIndicador;
    }

    public void setCodigoIndicador(Integer codigoIndicador) {
        this.codigoIndicador = codigoIndicador;
    }

    public String getNombreIndicador() {
        return nombreIndicador;
    }

    public void setNombreIndicador(String nombreIndicador) {
        this.nombreIndicador = nombreIndicador;
    }

    @XmlTransient
    public List<Tecnico> getTecnicoList() {
        return tecnicoList;
    }

    public void setTecnicoList(List<Tecnico> tecnicoList) {
        this.tecnicoList = tecnicoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoIndicador != null ? codigoIndicador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Indicadortpo)) {
            return false;
        }
        Indicadortpo other = (Indicadortpo) object;
        if ((this.codigoIndicador == null && other.codigoIndicador != null) || (this.codigoIndicador != null && !this.codigoIndicador.equals(other.codigoIndicador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Indicadortpo[ codigoIndicador=" + codigoIndicador + " ]";
    }
    
}
