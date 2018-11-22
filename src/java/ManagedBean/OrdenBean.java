/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import controller.OrdenesFacade;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alejandro
 */
@Named(value = "ordenBean")
@RequestScoped
public class OrdenBean {

    private int idOrden;
    private int idCliente;
    private String tipo;
    private String marca;
    private double precio;
    private String estado;
    private List<OrdenBean> listaOrdenes;

    public OrdenBean() {
    }

    public OrdenBean(int idOrden, int idCliente, String tipo, String marca, double precio, String estado) {
        this.idOrden = idOrden;
        this.idCliente = idCliente;
        this.tipo = tipo;
        this.marca = marca;
        this.precio = precio;
        this.estado = estado;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<OrdenBean> getListaOrdenes() {
        return listaOrdenes;
    }

    public void setListaOrdenes(List<OrdenBean> listaOrdenes) {
        this.listaOrdenes = listaOrdenes;
    }
    
    public String mostrarOrdenes(){
        FacesContext fc = FacesContext.getCurrentInstance();
        OrdenesFacade of = new OrdenesFacade();
        ExternalContext ec = fc.getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        int idCl = ((ClienteBean)session.getAttribute("cliente")).getIdCliente();
        listaOrdenes = new ArrayList<>();
        listaOrdenes = of.obtenerOrdenes(idCl);
        if (listaOrdenes.isEmpty()) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No existen registros de ordenes realizadas.", null));
            return null;
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
            return "ConsultaOrdenes.xhtml";
        }
    }

}
