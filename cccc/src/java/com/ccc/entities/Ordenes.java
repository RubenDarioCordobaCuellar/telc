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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "ordenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ordenes.findAll", query = "SELECT o FROM Ordenes o"),
    @NamedQuery(name = "Ordenes.findByNumeroOrden", query = "SELECT o FROM Ordenes o WHERE o.numeroOrden = :numeroOrden")})
public class Ordenes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "numeroOrden")
    private Integer numeroOrden;
    @ManyToMany(mappedBy = "ordenesList")
    private List<Localidades> localidadesList;
    @ManyToMany(mappedBy = "ordenesList")
    private List<Numerolote> numeroloteList;
    @JoinColumn(name = "Estado_codigoEstado", referencedColumnName = "codigoEstado")
    @ManyToOne(optional = false)
    private Estado estadocodigoEstado;
    @JoinColumn(name = "Tecnico_codigoTecnico", referencedColumnName = "codigoTecnico")
    @ManyToOne(optional = false)
    private Tecnico tecnicocodigoTecnico;
    @JoinColumn(name = "TipoOrdenes_codigoTipo", referencedColumnName = "codigoTipo")
    @ManyToOne(optional = false)
    private Tipoordenes tipoOrdenescodigoTipo;
    @JoinColumn(name = "clienteEmpresa_codigoNIC", referencedColumnName = "codigoNIC")
    @ManyToOne(optional = false)
    private Clienteempresa clienteEmpresacodigoNIC;

    public Ordenes() {
    }

    public Ordenes(Integer numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Integer getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(Integer numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    @XmlTransient
    public List<Localidades> getLocalidadesList() {
        return localidadesList;
    }

    public void setLocalidadesList(List<Localidades> localidadesList) {
        this.localidadesList = localidadesList;
    }

    @XmlTransient
    public List<Numerolote> getNumeroloteList() {
        return numeroloteList;
    }

    public void setNumeroloteList(List<Numerolote> numeroloteList) {
        this.numeroloteList = numeroloteList;
    }

    public Estado getEstadocodigoEstado() {
        return estadocodigoEstado;
    }

    public void setEstadocodigoEstado(Estado estadocodigoEstado) {
        this.estadocodigoEstado = estadocodigoEstado;
    }

    public Tecnico getTecnicocodigoTecnico() {
        return tecnicocodigoTecnico;
    }

    public void setTecnicocodigoTecnico(Tecnico tecnicocodigoTecnico) {
        this.tecnicocodigoTecnico = tecnicocodigoTecnico;
    }

    public Tipoordenes getTipoOrdenescodigoTipo() {
        return tipoOrdenescodigoTipo;
    }

    public void setTipoOrdenescodigoTipo(Tipoordenes tipoOrdenescodigoTipo) {
        this.tipoOrdenescodigoTipo = tipoOrdenescodigoTipo;
    }

    public Clienteempresa getClienteEmpresacodigoNIC() {
        return clienteEmpresacodigoNIC;
    }

    public void setClienteEmpresacodigoNIC(Clienteempresa clienteEmpresacodigoNIC) {
        this.clienteEmpresacodigoNIC = clienteEmpresacodigoNIC;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroOrden != null ? numeroOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ordenes)) {
            return false;
        }
        Ordenes other = (Ordenes) object;
        if ((this.numeroOrden == null && other.numeroOrden != null) || (this.numeroOrden != null && !this.numeroOrden.equals(other.numeroOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Ordenes[ numeroOrden=" + numeroOrden + " ]";
    }
    
}
