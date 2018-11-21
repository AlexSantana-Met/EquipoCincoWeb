/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import controller.ContactoFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alejandro
 */
@Named(value = "contactoBean")
@RequestScoped
public class ContactoBean {

    private String mensaje;
    private String correo;
    private String nombre;

    /**
     * Creates a new instance of ContactoBean
     */
    public ContactoBean() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void limpiar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        this.correo = "";
        this.mensaje = "";
        this.nombre = "";
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
    }

    public void registrarContacto() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (this.mensaje.isEmpty() || this.mensaje.length() < 30 || this.mensaje.length() > 200) {
            if (this.mensaje.length() < 10 || this.mensaje.length() > 200) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El mensaje debe ser de mínimo 30 caracteres y de máximo 200.", null));
            } else {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor, ingrese un mensaje para saber su opinión o sugerencia.", null));
            }
        } else {
            if (this.correo.isEmpty()) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor, ingrese su correo para responderle.", null));
            } else {
                ContactoFacade cf = new ContactoFacade();
                String result = cf.nuevoContacto(this);
                switch (result) {
                    case "EXITO":
                        limpiar();
                        fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Gracias por su opinión.", null));
                        break;
                    case "ERROR":
                        fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error con el envío de su mensaje.", null));
                        break;
                }
            }
        }
    }

}
