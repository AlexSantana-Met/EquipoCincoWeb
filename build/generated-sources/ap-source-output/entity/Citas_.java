package entity;

import entity.Clientes;
import entity.Empleados;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-22T14:13:22")
@StaticMetamodel(Citas.class)
public class Citas_ { 

    public static volatile SingularAttribute<Citas, Integer> idCita;
    public static volatile SingularAttribute<Citas, String> descripcion;
    public static volatile SingularAttribute<Citas, String> estado;
    public static volatile SingularAttribute<Citas, Empleados> empleadosId;
    public static volatile SingularAttribute<Citas, Date> hora;
    public static volatile SingularAttribute<Citas, Clientes> clientesId;
    public static volatile SingularAttribute<Citas, Integer> mes;
    public static volatile SingularAttribute<Citas, String> respuesta;
    public static volatile SingularAttribute<Citas, Integer> dia;
    public static volatile SingularAttribute<Citas, Integer> anio;

}