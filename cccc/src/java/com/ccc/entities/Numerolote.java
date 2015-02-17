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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "numerolote")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Numerolote.findAll", query = "SELECT n FROM Numerolote n"),
    @NamedQuery(name = "Numerolote.findByNumeroLote", query = "SELECT n FROM Numerolote n WHERE n.numeroLote = :numeroLote")})
public class Numerolote implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "numeroLote")
    private Integer numeroLote;
    @JoinTable(name = "ordenes_has_numerolote", joinColumns = {
        @JoinColumn(name = "numeroLote_numeroLote", referencedColumnName = "numeroLote")}, inverseJoinColumns = {
        @JoinColumn(name = "ordenes_numeroOrden", referencedColumnName = "numeroOrden")})
    @ManyToMany
    private List<Ordenes> ordenesList;

    public Numerolote() {
    }

    public Numerolote(Integer numeroLote) {
        this.numeroLote = numeroLote;
    }

    public Integer getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(Integer numeroLote) {
        this.numeroLote = numeroLote;
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
        hash += (numeroLote != null ? numeroLote.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Numerolote)) {
            return false;
        }
        Numerolote other = (Numerolote) object;
        if ((this.numeroLote == null && other.numeroLote != null) || (this.numeroLote != null && !this.numeroLote.equals(other.numeroLote))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Numerolote[ numeroLote=" + numeroLote + " ]";
    }
    
}
