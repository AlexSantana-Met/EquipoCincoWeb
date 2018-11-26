/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import controller.ClientesFacade;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alejandro
 */
@Named(value = "clienteBean")
@RequestScoped
//@SessionScoped
public class ClienteBean implements Serializable {

    private int idCliente;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String correo;
    private String fechaNac;
    private String direccion;
    private String ciudad;
    private String auxP;

    /**
     * Creates a new instance of OrdenBean
     */
    public ClienteBean() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        ClienteBean cl = (ClienteBean) session.getAttribute("cliente");
        if (cl != null) {
            this.idCliente = cl.getIdCliente();
            this.nombre = cl.getNombre();
            this.apPaterno = cl.getApPaterno();
            this.apMaterno = cl.getApMaterno();
            this.ciudad = cl.getCiudad();
            this.direccion = cl.getDireccion();
            this.fechaNac = cl.getFechaNac();
            this.correo = (String) session.getAttribute("user");
        } else {

        }
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getAuxP() {
        return auxP;
    }

    public void setAuxP(String auxP) {
        this.auxP = auxP;
    }

    public ClienteBean(int id, String nombre, String apPaterno, String apMaterno, String fechaNac, String direccion, String ciudad) {
        this.idCliente = id;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.fechaNac = fechaNac;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public void limpiar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        this.apMaterno = "";
        this.apPaterno = "";
        this.nombre = "";
        this.correo = "";
        this.auxP = "";
//        fc.getMessages().remove();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", null));
    }

    public String registrarCliente() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (this.nombre.isEmpty() || this.apPaterno.isEmpty() || this.apMaterno.isEmpty() || this.correo.isEmpty() || this.auxP.isEmpty()) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Por favor, ingrese todos lo campos que se solicitan.", null));
            System.out.println("Campos vacíos");
            return null;
        } else {
            ClientesFacade cf = new ClientesFacade();
            String result = cf.nuevoCliente(this);

            switch (result) {
                case "EXITO":
                    limpiar();
                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha registrado exitosamente.", null));
                    break;
                case "ERROR":
                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error en su registro.", null));
                    break;
                case "CORREO":
                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo que usted ingresó ya ha sido registrado anteriormente.", null));
                    break;

            }
            return null;
        }
    }

    public String muestraPerfil() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            ClienteBean cl = (ClienteBean) session.getAttribute("cliente");

            ec.redirect(ec.getRequestContextPath() + "/faces/perfil.xhtml");
            return "EXITO";
        } catch (IOException ex) {
            return "ERROR";
        }
    }

    public void cerrarSesion() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        session.setAttribute("cliente", null);
        session.setAttribute("pass", null);
        session.setAttribute("correo", null);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
        } catch (IOException ex) {

        }
    }

    public String editarPerfil() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ClienteBean cliente = (ClienteBean) session.getAttribute("cliente");
        cliente.setCorreo((String) session.getAttribute("user"));
        ClientesFacade cf = new ClientesFacade();
        ClienteBean aux = cf.editarClienteFacade(this);
        if (aux != null) {
            session.setAttribute("cliente", aux);
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se han guardado sus datos exitosamente.", null));
        } else {
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar.", null));
        }
//        switch (result) {
//            case "EXITO":
//
//                break;
//            case "ERROR":
//
//                break;
//        }
        return null;
    }

    public String muestraPerfilEditar() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            ClienteBean cl = (ClienteBean) session.getAttribute("cliente");
            ec.redirect(ec.getRequestContextPath() + "/faces/Editar_perfil.xhtml");
            return "EXITO";
        } catch (IOException ex) {
            return "ERROR";
        }
    }
}
