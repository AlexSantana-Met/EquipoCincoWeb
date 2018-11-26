/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import controller.CitasFacade;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;

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
    private List<String> horasDisponibles;
    private List<SelectItem> listaReturn;

    /**
     * Creates a new instance of CitaBean
     */
    public CitaBean() {
        this.listaReturn = new ArrayList<>();
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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
        this.listaReturn = new ArrayList<>();
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
        return (String) hora;
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
        listaCitas = cf.obtenerCitas(idCl);
        if (listaCitas == null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No existen registros de citas realizadas.", null));
            return "Consultas.xhtml";
        } else {
            for (CitaBean listaCita : listaCitas) {
                listaCita.setFecha(formateaFecha(listaCita));
            }
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

    public void fechaRes() {
        if (date1 != null) {
            System.out.println(date1.toString());
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
        CitasFacade cf = new CitasFacade();
        if (date1 != null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
            if (date1.compareTo(new java.util.Date()) <= 0) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecciona una fecha posterior a la actual.", null));
            } else {
                List<String> ocupadas = new ArrayList<>();
                ocupadas = cf.obtenerHoras(date1);
                mostrarDisponibles();
                if (ocupadas != null && !ocupadas.isEmpty()) {
                    for (String ocupada : ocupadas) {
                        horasDisponibles.remove(ocupada);
                    }
                }
            }
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecciona una fecha por favor.", null));
        }
    }

    private Date date1;
//

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public List<String> getHorasDisponibles() {
        return horasDisponibles;
    }

    public void setHorasDisponibles(List<String> horasDisponibles) {
        this.horasDisponibles = horasDisponibles;
    }

    public List<String> mostrarDisponibles() {
        horasDisponibles = new ArrayList<>();
        horasDisponibles.add("09:00");
        horasDisponibles.add("10:00");
        horasDisponibles.add("11:00");
        horasDisponibles.add("12:00");
        horasDisponibles.add("15:00");
        horasDisponibles.add("16:00");
        horasDisponibles.add("17:00");
        horasDisponibles.add("18:00");
        return horasDisponibles;
    }

    public List<SelectItem> getHorasDispCitas() {
        listaReturn.clear();
        SelectItem first = new SelectItem("", "Selecciona una opción", "", false, true, true);
        if (horasDisponibles == null) {
            listaReturn.add(first);
        } else {
            listaReturn.add(first);
//            for (int i = 0; i < horasDisponibles.size(); i++) {
//                listaReturn.add(new SelectItem(i + 1, horasDisponibles.get(i).toString()));
//            }
            for (String horasDisponible : horasDisponibles) {
                listaReturn.add(new SelectItem((String) horasDisponible, horasDisponible));
            }
        }
        return listaReturn;
    }

    public List<String> getHrsDip() {
        return horasDisponibles;
    }

    public void agendarCita() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (this.descripcion.isEmpty() || date1 == null || hora.isEmpty()) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Por favor, ingrese todos lo campos que se solicitan.", null));
        } else {
            ExternalContext ex = fc.getExternalContext();
            HttpSession session = (HttpSession) ex.getSession(false);
            this.idCliente = ((ClienteBean) session.getAttribute("cliente")).getIdCliente();
            CitasFacade cf = new CitasFacade();
            String result = cf.registrarCita(this);
            switch (result) {
                case "EXITO":
                    limpiar();
                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Ha agendado la cita exitosamente.", null));
                    break;
                case "ERROR":
                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error en su registro.", null));
                    break;
            }
        }
    }

    public void limpiar() {
        this.anio = 0;
        this.dia = 0;
        this.mes = 0;
        this.date1 = null;
        this.descripcion = "";
        this.fecha = null;
        this.horasDisponibles = null;
        this.respuesta = null;
        this.hora = "";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
    }

    public List<SelectItem> getListaReturn() {
        return listaReturn;
    }

    public void setListaReturn(List<SelectItem> listaReturn) {
        this.listaReturn = listaReturn;
    }

    public void checa() {
        if (hora.isEmpty()) {
            System.out.println("Nada");
        } else {
            System.out.println("Algo" + hora.toString());
        }
    }

}
