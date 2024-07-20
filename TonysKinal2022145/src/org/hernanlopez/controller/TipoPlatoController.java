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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.hernanlopez.bean.TipoPlato;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;


public class TipoPlatoController implements Initializable {
 
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipoPlato;
    @FXML private TableView tblTipoPlatos;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipoPlato;
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
    }
    
    public void cargarDatos(){
        tblTipoPlatos.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("codigoTipoPlato"));
        colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,String>("descripcionTipo"));
    }
    
    public ObservableList<TipoPlato>getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlatos");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                    resultado.getString("descripcionTipo")));
             }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoPlato = FXCollections.observableArrayList(lista); 
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
                if(txtDescripcionTipoPlato.getText().isEmpty()){
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
                    break;
                }
        }
    }
    
     public void guardar(){
        TipoPlato registro = new TipoPlato();
        registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
       
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
            procedimiento.setString(1, registro.getDescripcionTipo());
            procedimiento.execute();
            listaTipoPlato.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
       
    }
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){   
            if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){ 
                txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
                txtDescripcionTipoPlato.setText(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcionTipo());
            }else{
                JOptionPane.showMessageDialog(null,"Fila no seleccionada..!!");
            }
        }else{
            tblTipoPlatos.getSelectionModel().clearSelection();
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
            default:
                if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro? ","Eliminar Tipo Plato",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION ){ 
                        try{
                            int codigoTipoPlato = ((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato();
                             
                            PreparedStatement buscar = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlatoTipo(?)");
                            buscar.setInt(1, codigoTipoPlato);
                            ResultSet resultado = buscar.executeQuery();
                            
                            if(resultado.next()){
                                JOptionPane.showMessageDialog(null, " No se puede eliminar: Está siendo usado por otro registro\n Elimine todos los registros que puedan estar utilizando esté dato e inténtelo de nuevo.");
                                limpiarControles();
                                tblTipoPlatos.getSelectionModel().clearSelection();
                            }else{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                                procedimiento.setInt(1, codigoTipoPlato);
                                procedimiento.execute();

                                listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        tblTipoPlatos.getSelectionModel().clearSelection();
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila para poder Eliminar");
                }
            break;    
        }
    }
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/hernanlopez/image/editar.png"));
                    imgReporte.setImage(new Image("/org/hernanlopez/image/cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;   
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila para poder Editar");
                }
                break;
            case ACTUALIZAR:
                if(txtDescripcionTipoPlato.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null," No se puede actualizar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    imgEditar.setImage(new Image("/org/hernanlopez/image/editar.png"));
                    imgReporte.setImage(new Image("/org/hernanlopez/image/Reporte.png"));
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
        }
    }
    
    public void actualizar(){
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?,?)");
           TipoPlato registro = (TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem();
           registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
           procedimiento.setInt(1, registro.getCodigoTipoPlato());
           procedimiento.setString(2, registro.getDescripcionTipo());
           procedimiento.execute();
         }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            default:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/hernanlopez/image/editar.png"));
                imgReporte.setImage(new Image("/org/hernanlopez/image/Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                tblTipoPlatos.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(true);   
    }
    
    public void limpiarControles(){
       txtCodigoTipoPlato.clear();
       txtDescripcionTipoPlato.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
}
