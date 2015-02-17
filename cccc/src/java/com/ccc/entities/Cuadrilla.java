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
@Table(name = "cuadrilla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuadrilla.findAll", query = "SELECT c FROM Cuadrilla c"),
    @NamedQuery(name = "Cuadrilla.findByCodigoCuadrilla", query = "SELECT c FROM Cuadrilla c WHERE c.codigoCuadrilla = :codigoCuadrilla"),
    @NamedQuery(name = "Cuadrilla.findByNombreCuadrilla", query = "SELECT c FROM Cuadrilla c WHERE c.nombreCuadrilla = :nombreCuadrilla")})
public class Cuadrilla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoCuadrilla")
    private Integer codigoCuadrilla;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombreCuadrilla")
    private String nombreCuadrilla;
    @JoinColumn(name = "Vehiculo_codigoVehiculo", referencedColumnName = "codigoVehiculo")
    @ManyToOne(optional = false)
    private Vehiculo vehiculocodigoVehiculo;

    public Cuadrilla() {
    }

    public Cuadrilla(Integer codigoCuadrilla) {
        this.codigoCuadrilla = codigoCuadrilla;
    }

    public Cuadrilla(Integer codigoCuadrilla, String nombreCuadrilla) {
        this.codigoCuadrilla = codigoCuadrilla;
        this.nombreCuadrilla = nombreCuadrilla;
    }

    public Integer getCodigoCuadrilla() {
        return codigoCuadrilla;
    }

    public void setCodigoCuadrilla(Integer codigoCuadrilla) {
        this.codigoCuadrilla = codigoCuadrilla;
    }

    public String getNombreCuadrilla() {
        return nombreCuadrilla;
    }

    public void setNombreCuadrilla(String nombreCuadrilla) {
        this.nombreCuadrilla = nombreCuadrilla;
    }

    public Vehiculo getVehiculocodigoVehiculo() {
        return vehiculocodigoVehiculo;
    }

    public void setVehiculocodigoVehiculo(Vehiculo vehiculocodigoVehiculo) {
        this.vehiculocodigoVehiculo = vehiculocodigoVehiculo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoCuadrilla != null ? codigoCuadrilla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuadrilla)) {
            return false;
        }
        Cuadrilla other = (Cuadrilla) object;
        if ((this.codigoCuadrilla == null && other.codigoCuadrilla != null) || (this.codigoCuadrilla != null && !this.codigoCuadrilla.equals(other.codigoCuadrilla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccc.entities.Cuadrilla[ codigoCuadrilla=" + codigoCuadrilla + " ]";
    }
    
}
