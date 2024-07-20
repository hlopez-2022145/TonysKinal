package org.hernanlopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.hernanlopez.bean.Plato;
import org.hernanlopez.bean.Servicio;
import org.hernanlopez.bean.Servicios_has_Platos;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;


public class Servicios_has_PlatosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicios_has_Platos> listaSerHasPla;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Servicio> listaServicio;
    
    @FXML private TextField txtCodigoServicioYPlato;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private TableView tblServicioHas;
    @FXML private TableColumn colServicioYPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoServicio.setItems(getServicio());
    }
    
    public void cargarDatos(){
        tblServicioHas.setItems(getServicioHas());
        colServicioYPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer> ("Servicios_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("codigoServicio"));
    }
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
            if(tblServicioHas.getSelectionModel().getSelectedItems() != null){
                txtCodigoServicioYPlato.setText(String.valueOf(((Servicios_has_Platos)tblServicioHas.getSelectionModel().getSelectedItem()).getServicios_codigoProducto()));
                cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicios_has_Platos)tblServicioHas.getSelectionModel().getSelectedItem()).getCodigoPlato())); 
                cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Platos)tblServicioHas.getSelectionModel().getSelectedItem()).getCodigoServicio())); 
            }else{
              JOptionPane.showMessageDialog(null,"Fila no seleccionada..!!");
            }
        }else{
          limpiarControles();  
        }
    }
    
     public Plato buscarPlato (int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato(?)");
            procedimineto.setInt(1, codigoPlato);
            ResultSet registro = procedimineto.executeQuery();
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),
                                     registro.getInt("cantidad"),
                                     registro.getString("nombrePlato"),
                                     registro.getString("descripcionPlato"),
                                     registro.getDouble("precioPlato"),
                                     registro.getInt("codigoTipoPlato"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         return resultado;       
    }
    
    public Servicio buscarServicio (int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimineto.setInt(1, codigoServicio);
            ResultSet registro = procedimineto.executeQuery();
            while(registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
                                     registro.getDate("fechaServicio"),
                                     registro.getString("tipoServicio"),
                                     registro.getTime("horaServicio"),
                                     registro.getString("lugarServicio"),
                                     registro.getString("telefonoContacto"),
                                     registro.getInt("codigoEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         return resultado;       
    }
    
    public ObservableList<Servicios_has_Platos> getServicioHas(){
        ArrayList<Servicios_has_Platos> lista = new ArrayList<Servicios_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServiciosHasPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_has_Platos(resultado.getInt("Servicios_codigoProducto"),
                                        resultado.getInt("codigoPlato"),
                                        resultado.getInt("codigoServicio")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaSerHasPla =  FXCollections.observableArrayList(lista); 
    }

    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new Plato(resultado.getInt("codigoPlato"),
                                     resultado.getInt("cantidad"),
                                     resultado.getString("nombrePlato"),
                                     resultado.getString("descripcionPlato"),
                                     resultado.getDouble("precioPlato"),
                                     resultado.getInt("codigoTipoPlato")));
             }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista); 
    }
    
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                     resultado.getDate("fechaServicio"),
                                     resultado.getString("tipoServicio"),
                                     resultado.getTime("horaServicio"),
                                     resultado.getString("lugarServicio"),
                                     resultado.getString("telefonoContacto"),
                                     resultado.getInt("codigoEmpresa")));
             }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista); 
    }
    
     public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/hernanlopez/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/hernanlopez/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(txtCodigoServicioYPlato.getText().isEmpty()||cmbCodigoPlato.getSelectionModel().getSelectedItem()  == null||cmbCodigoServicio.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null," No se puede guardar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                       guardar();
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/hernanlopez/image/agregar.png"));
                        imgEliminar.setImage(new Image("/org/hernanlopez/image/eliminar.png"));
                        tipoDeOperacion = operaciones.NINGUNO;
                        cargarDatos();
                }
            break;
        }
    }
    
     public void guardar(){
        Servicios_has_Platos registro = new Servicios_has_Platos();
        registro.setServicios_codigoProducto(Integer.parseInt(txtCodigoServicioYPlato.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());

        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicioHasPlato(?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
     public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/hernanlopez/image/agregar.png"));
                imgEliminar.setImage(new Image("/org/hernanlopez/image/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
     }
     
    public void desactivarControles(){
        txtCodigoServicioYPlato.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicio.setDisable(true);
    }
    public void activarControles(){
        txtCodigoServicioYPlato.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoServicioYPlato.clear();
        cmbCodigoPlato.setValue("");
        cmbCodigoServicio.setValue("");
        tblServicioHas.getSelectionModel().clearSelection();
    }
    
    public void getEscenarioPrincipal(){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
