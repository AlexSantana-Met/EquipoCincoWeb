/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.CitaBean;
import ManagedBean.OrdenBean;
import entity.Citas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
public class CitasFacade {
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
    UserTransaction utx = null;
    CitasJpaController citasJPA = new CitasJpaController(utx, emf);
    
    public List<CitaBean> obtenerOrdenes(int idCliente) {
        List<CitaBean> listaRet = new ArrayList<>();
        for (Citas cita : citasJPA.findByIdCliente(idCliente)) {
            listaRet.add(new CitaBean(0, cita.getAnio(), cita.getDia(), cita.getMes(), 
                    idCliente, cita.getEmpleadosId().getIdEmpleado(), cita.getHora().getHours() + ":" + cita.getHora().getMinutes(), 
                    cita.getDescripcion(), cita.getEstado(), cita.getRespuesta()));
        }
        return listaRet;
    }
}
