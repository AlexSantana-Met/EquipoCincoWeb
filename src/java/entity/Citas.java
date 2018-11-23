/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alejandro
 */
@Entity
@Table(name = "citas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Citas.findAll", query = "SELECT c FROM Citas c")
    , @NamedQuery(name = "Citas.findByIdCita", query = "SELECT c FROM Citas c WHERE c.idCita = :idCita")
    , @NamedQuery(name = "Citas.findByDia", query = "SELECT c FROM Citas c WHERE c.dia = :dia")
    , @NamedQuery(name = "Citas.findByMes", query = "SELECT c FROM Citas c WHERE c.mes = :mes")
    , @NamedQuery(name = "Citas.findByAnio", query = "SELECT c FROM Citas c WHERE c.anio = :anio")
    , @NamedQuery(name = "Citas.findByHora", query = "SELECT c FROM Citas c WHERE c.hora = :hora")
    , @NamedQuery(name = "Citas.findByDescripcion", query = "SELECT c FROM Citas c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Citas.findByEstado", query = "SELECT c FROM Citas c WHERE c.estado = :estado")
    , @NamedQuery(name = "Citas.findByRespuesta", query = "SELECT c FROM Citas c WHERE c.respuesta = :respuesta")})
public class Citas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CITA")
    private Integer idCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIA")
    private int dia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MES")
    private int mes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANIO")
    private int anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORA")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 250)
    @Column(name = "RESPUESTA")
    private String respuesta;
    @JoinColumn(name = "CLIENTES_ID", referencedColumnName = "ID_CLIENTE")
    @ManyToOne(optional = false)
    private Clientes clientesId;
    @JoinColumn(name = "EMPLEADOS_ID", referencedColumnName = "ID_EMPLEADO")
    @ManyToOne(optional = false)
    private Empleados empleadosId;

    public Citas() {
    }

    public Citas(Integer idCita) {
        this.idCita = idCita;
    }

    public Citas(Integer idCita, int dia, int mes, int anio, Date hora, String descripcion, String estado) {
        this.idCita = idCita;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.hora = hora;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Citas(Integer idCita, int dia, int mes, int anio, Date hora, String descripcion, String estado, Clientes clientesId, Empleados empleadosId) {
        this.idCita = idCita;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.hora = hora;
        this.descripcion = descripcion;
        this.estado = estado;
        this.clientesId = clientesId;
        this.empleadosId = empleadosId;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Clientes getClientesId() {
        return clientesId;
    }

    public void setClientesId(Clientes clientesId) {
        this.clientesId = clientesId;
    }

    public Empleados getEmpleadosId() {
        return empleadosId;
    }

    public void setEmpleadosId(Empleados empleadosId) {
        this.empleadosId = empleadosId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCita != null ? idCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Citas)) {
            return false;
        }
        Citas other = (Citas) object;
        if ((this.idCita == null && other.idCita != null) || (this.idCita != null && !this.idCita.equals(other.idCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Citas[ idCita=" + idCita + " ]";
    }
    
}
