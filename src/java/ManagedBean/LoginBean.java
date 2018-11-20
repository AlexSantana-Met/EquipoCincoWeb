/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import ManagedBean.exceptions.RollbackFailureException;
import controller.LoginFacade;
import controller.LoginJpaController;
import entity.Login;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginBean {

    private String correo;
    private String passw;

    public LoginBean(String correo, String passw) {
        this.correo = correo;
        this.passw = passw;
    }

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public Login registroLogin() {
        //funciona
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
//        UserTransaction utx = null;
//
//        LoginJpaController loginJPA = new LoginJpaController(utx, emf);
//
//        Login login = new Login(correo, passw);
//        try {
//            loginJPA.create(login);
//            return login;            
//        } catch (RollbackFailureException ex) {
//            System.out.println("Error en rollback, login");
//            return null;
//        } catch (Exception ex) {
//            System.out.println("Error en inserción, login");
//            return null;
//        }
        LoginFacade lf = new LoginFacade();
        Login l = lf.nuevoLogin(correo, passw);
        return l;
            
//        LoginFacade lf = new LoginFacade();
//        Login l = lf.nuevoLogin(correo, passw);
//        if (l != null) {
//            FacesContext fc = FacesContext.getCurrentInstance();
//            fc.addMessage("", new FacesMessage("Registro exitoso!"));
//            return null;
//        } else {
//            FacesContext fc = FacesContext.getCurrentInstance();
//            fc.addMessage("", new FacesMessage("Error en registro!"));
//            return null;
//        }
    }

}
