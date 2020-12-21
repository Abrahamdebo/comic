/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JuCa
 */
@Entity
@Table(name = "Detalle_Entrada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleEntrada.findAll", query = "SELECT d FROM DetalleEntrada d")
    , @NamedQuery(name = "DetalleEntrada.findById", query = "SELECT d FROM DetalleEntrada d WHERE d.id = :id")
    , @NamedQuery(name = "DetalleEntrada.findByActivos", query = "SELECT d FROM DetalleEntrada d WHERE d.status = 1")
    , @NamedQuery(name = "DetalleEntrada.findByInactivos", query = "SELECT d FROM DetalleEntrada d WHERE d.status = 0")
    , @NamedQuery(name = "DetalleEntrada.findByCantidad", query = "SELECT d FROM DetalleEntrada d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "DetalleEntrada.findByPrecioCompra", query = "SELECT d FROM DetalleEntrada d WHERE d.precioCompra = :precioCompra")
    , @NamedQuery(name = "DetalleEntrada.findByStatus", query = "SELECT d FROM DetalleEntrada d WHERE d.status = :status")})
public class DetalleEntrada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_compra")
    private double precioCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @JoinColumn(name = "id_entrada", referencedColumnName = "id")
    @ManyToOne
    private Entradas idEntrada;
    @JoinColumn(name = "id_producto", referencedColumnName = "id")
    @ManyToOne
    private Productos idProducto;

    public DetalleEntrada() {
    }

    public DetalleEntrada(Integer id) {
        this.id = id;
    }

    public DetalleEntrada(Integer id, int cantidad, double precioCompra, int status) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Entradas getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(Entradas idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Productos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Productos idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleEntrada)) {
            return false;
        }
        DetalleEntrada other = (DetalleEntrada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.DetalleEntrada[ id=" + id + " ]";
    }
    
}
