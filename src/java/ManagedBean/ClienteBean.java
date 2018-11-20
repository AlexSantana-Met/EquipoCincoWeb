/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import ManagedBean.exceptions.RollbackFailureException;
import controller.ClientesFacade;
import entity.Clientes;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alejandro
 */
@Named(value = "clienteBean")
@RequestScoped
public class ClienteBean implements Serializable {

    private int idCliente;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String correo;
    private Date fechaNac;
    private String direccion;
    private String ciudad;
    private String auxP;

    /**
     * Creates a new instance of OrdenBean
     */
    public ClienteBean() {
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

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
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
//            EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
////            EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-PrograWebPU");
//            UserTransaction utx = null;
//
//            ClientesJpaController clientesJPA = new ClientesJpaController(utx, emf);
//            int id = clientesJPA.getMaxId() + 1;
//            LoginBean nuevoLoginBean = new LoginBean(correo, auxP);
//            Login nuevoLogin = nuevoLoginBean.registroLogin();
            ClientesFacade cf = new ClientesFacade();
            String result = cf.nuevoCliente(this);

            switch (result) {
                case "EXITO":
                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha registrado exitosamente.", null));
                    break;
                case "ERROR":
                    limpiar();
                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error en su registro.", null));
                    break;
                case "CORREO":
                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo que usted ingresó ya ha sido registrado anteriormente.", null));
                    break;

            }
            return null;

//            if (nuevoLogin != null) {
//                try {
//                    c = new Clientes(id, nombre, apPaterno, apMaterno, nuevoLogin);
//                    clientesJPA.create(c);
//                    limpiar();
//                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha registrado exitosamente.", null));
//                    return null;
//                } catch (RollbackFailureException ex) {
//                    System.out.println("Error en rollback, cliente " + ex);
//                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error en su registro.", null));
//                    return null;
//                } catch (Exception ex) {
//                    System.out.println("Error en general, cliente " + ex);
//                    fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error en su registro.", null));
//                    return null;
//                }
//            } else {
//                System.out.println("Fuckin' Error cliente");
//                fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo que usted ingresó ya ha sido registrado anteriormente.", null));
//                return null;
//            }
        }
    }
}
