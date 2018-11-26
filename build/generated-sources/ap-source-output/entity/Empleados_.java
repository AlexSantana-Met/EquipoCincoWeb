package entity;

import entity.Citas;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-26T12:48:42")
@StaticMetamodel(Empleados.class)
public class Empleados_ { 

    public static volatile SingularAttribute<Empleados, String> area;
    public static volatile SingularAttribute<Empleados, Integer> idEmpleado;
    public static volatile SingularAttribute<Empleados, String> apMaterno;
    public static volatile SingularAttribute<Empleados, String> nombre;
    public static volatile ListAttribute<Empleados, Citas> citasList;
    public static volatile SingularAttribute<Empleados, String> apPaterno;

}