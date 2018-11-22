/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import controller.CitasFacade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alejandro
 */
@Named(value = "citaBean")
@RequestScoped
public class CitaBean implements Serializable {

    int idCita;
    int anio;
    int dia;
    int mes;
    int idCliente;
    int idEmpleado;
    String hora;
    String descripcion;
    private String respuesta;
    String estado;
    List<CitaBean> listaCitas;
    private String fecha;

    /**
     * Creates a new instance of CitaBean
     */
    public CitaBean() {
    }

    public CitaBean(int idCita, int anio, int dia, int mes, int idCliente, int idEmpleado, String hora, String descripcion, String estado, String respuesta) {
        this.idCita = idCita;
        this.anio = anio;
        this.dia = dia;
        this.mes = mes;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.hora = hora;
        this.descripcion = descripcion;
        this.estado = estado;
        this.respuesta = respuesta;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
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

    public List<CitaBean> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(List<CitaBean> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String mostrarCitas() {
        FacesContext fc = FacesContext.getCurrentInstance();
        CitasFacade cf = new CitasFacade();
        ExternalContext ec = fc.getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        int idCl = ((ClienteBean) session.getAttribute("cliente")).getIdCliente();
        listaCitas = new ArrayList<>();
        listaCitas = cf.obtenerOrdenes(idCl);
        for (CitaBean listaCita : listaCitas) {
            listaCita.setFecha(formateaFecha(listaCita));
        }
        if (listaCitas.isEmpty()) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No existen registros de ordenes realizadas.", null));
            return null;
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
            return "Consultas.xhtml";
        }
    }

    private String formateaFecha(CitaBean cita) {
        String res = "";

        res = cita.getDia() < 10 ? res.concat("0" + String.valueOf(cita.getDia())) : res.concat(String.valueOf(cita.getDia()));
        switch (cita.getMes()) {
            case 1:
                res = res.concat("/Enero/");
                break;
            case 2:
                res = res.concat("/Febrero/");
                break;
            case 3:
                res = res.concat("/Marzo/");
                break;
            case 4:
                res = res.concat("/Abril/");
                break;
            case 5:
                res = res.concat("/Mayo/");
                break;
            case 6:
                res = res.concat("/Junio/");
                break;
            case 7:
                res = res.concat("/Julio/");
                break;
            case 8:
                res = res.concat("/Agosto/");
                break;
            case 9:
                res = res.concat("/Septiembre/");
                break;
            case 10:
                res = res.concat("/Octubre/");
                break;
            case 11:
                res = res.concat("/Noviembre/");
                break;
            case 12:
                res = res.concat("/Diciembre/");
                break;
        }
        res = res.concat(String.valueOf(cita.getAnio()));
//        if (cita.getDia() < 10) {
//            res = res.concat("0" + String.valueOf(cita.getDia()));
//        } else {
//            res = res.concat(String.valueOf(cita.getDia()));
//        }
//        return String.valueOf(cita.dia) + " - "
//                + String.valueOf(cita.mes) + " - "
//                + String.valueOf(cita.anio);
        return res;
    }

}
