package org.hernanlopez.bean;


public class Empleado {
    private int codigoEmpleado;
    private int numeroEmpleado;
    private String nombresEmpleados;
    private String apellidosEmpleado;
    private String direccionEmpleado;
    private String telefonoContacto;
    private String gradoCocinero;
    private int codigoTipoEmpleado;
 
    public Empleado(){
        
    }

    public Empleado(int codigoEmpleado, int numeroEmpleado, String nombresEmpleados, String apellidosEmpleado, String direccionEmpleado, String telefonoContacto, String gradoCocinero, int codigoTipoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
        this.numeroEmpleado = numeroEmpleado;
        this.nombresEmpleados = nombresEmpleados;
        this.apellidosEmpleado = apellidosEmpleado;
        this.direccionEmpleado = direccionEmpleado;
        this.telefonoContacto = telefonoContacto;
        this.gradoCocinero = gradoCocinero;
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    
    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getNombresEmpleados() {
        return nombresEmpleados;
    }

    public void setNombresEmpleados(String nombresEmpleados) {
        this.nombresEmpleados = nombresEmpleados;
    }

    public String getApellidosEmpleado() {
        return apellidosEmpleado;
    }

    public void setApellidosEmpleado(String apellidosEmpleado) {
        this.apellidosEmpleado = apellidosEmpleado;
    }

    public String getDireccionEmpleado() {
        return direccionEmpleado;
    }

    public void setDireccionEmpleado(String direccionEmpleado) {
        this.direccionEmpleado = direccionEmpleado;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getGradoCocinero() {
        return gradoCocinero;
    }

    public void setGradoCocinero(String gradoCocinero) {
        this.gradoCocinero = gradoCocinero;
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    @Override
    public String toString() {
        return codigoEmpleado  + " || " + nombresEmpleados;
    }
    
    
}
