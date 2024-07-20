package org.hernanlopez.bean;


public class Servicios_has_Platos {
    private int Servicios_codigoProducto;
    private int codigoPlato;
    private int codigoServicio;

    public Servicios_has_Platos() {
    }

    public Servicios_has_Platos(int Servicios_codigoProducto, int codigoPlato, int codigoServicio) {
        this.Servicios_codigoProducto = Servicios_codigoProducto;
        this.codigoPlato = codigoPlato;
        this.codigoServicio = codigoServicio;
    }

    public int getServicios_codigoProducto() {
        return Servicios_codigoProducto;
    }

    public void setServicios_codigoProducto(int Servicios_codigoProducto) {
        this.Servicios_codigoProducto = Servicios_codigoProducto;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

}
