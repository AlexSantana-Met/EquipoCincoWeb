/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.CitaBean;
import ManagedBean.exceptions.RollbackFailureException;
import entity.Citas;
import entity.Clientes;
import entity.Empleados;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public List<CitaBean> obtenerCitas(int idCliente) {
        List<CitaBean> listaRet = new ArrayList<>();
        List<Citas> listaCitas = citasJPA.findByIdCliente(idCliente);
        if (listaCitas == null) {
            return null;
        } else {
            for (Citas cita : listaCitas) {
                String horas = cita.getHora().getHours() < 10 ? "0" + String.valueOf(cita.getHora().getHours()) : String.valueOf(cita.getHora().getHours());

                listaRet.add(new CitaBean(0, cita.getAnio(), cita.getDia(), cita.getMes(),
                        idCliente, cita.getEmpleadosId().getIdEmpleado(), horas + ":00",
                        cita.getDescripcion(), cita.getEstado(), cita.getRespuesta()));
            }
            return listaRet;
        }
    }

    public List<String> obtenerHoras(Date fecha) {
        List<String> horas = new ArrayList<>();
        horas = citasJPA.getHorasCitas(fecha.getDate(), fecha.getMonth() + 1, fecha.getYear() + 1900);
        return horas;
    }

    public String registrarCita(CitaBean cita) {
        int idCita = citasJPA.getMaxId();
        Clientes clientes = new Clientes(cita.getIdCliente());
        Empleados empleados = new Empleados((int)(Math.random() * 3));
        Citas citas = new Citas(idCita, cita.getDia(), cita.getMes(), cita.getAnio(), 
                new java.util.Date(), cita.getDescripcion(), "En proceso", clientes, empleados);
        try {
            citasJPA.create(citas);
            return "Exito";
        } catch (RollbackFailureException ex) {
            return "ERROR";
        } catch (Exception ex) {
            return "ERROR";
        }
    }

}
