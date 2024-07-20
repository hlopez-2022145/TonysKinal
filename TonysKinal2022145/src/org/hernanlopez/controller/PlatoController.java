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
import org.hernanlopez.bean.TipoPlato;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;


public class PlatoController implements Initializable {
    private Principal escenarioPrincipal;
     private enum operaciones{GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Plato> listaPlato;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecioPlato;
    @FXML private ComboBox cmbCodigoTipoPlato;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colCodigoTipoPlato;
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
        cmbCodigoTipoPlato.setItems(getTipoPlato());
        desactivarControles();
    }
    
     public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer> ("codigoPlato"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String> ("nombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato, String> ("descripcionPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer> ("cantidad"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Double> ("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer> ("codigoTipoPlato"));
    }
    
    public void seleccionarElemento(){
         if(tipoDeOperacion != operaciones.GUARDAR){   
            if(tblPlatos.getSelectionModel().getSelectedItem() != null){ 
                txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                txtNombrePlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato()));
                txtDescripcion.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato()));
                txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
                txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
                cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            }else{
                JOptionPane.showMessageDialog(null,"Fila no seleccionada..!!");
            }
        
        }else{
            //JOptionPane.showMessageDialog(null, "No se pueden guardar datos iguales....");
            tblPlatos.getSelectionModel().clearSelection();
        }
    }
   
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?)");
            procedimineto.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimineto.executeQuery();
            while(registro.next()){
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                     registro.getString("descripcionTipo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
        return listaPlato =  FXCollections.observableArrayList(lista); 
    }
     
    public ObservableList<TipoPlato> getTipoPlato(){
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
                if(txtNombrePlato.getText().isEmpty() || txtDescripcion.getText().isEmpty() || txtCantidad.getText().isEmpty()||
                        txtPrecioPlato.getText().isEmpty()||cmbCodigoTipoPlato.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null," No se puede guardar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    try{
                        Integer.parseInt(txtCantidad.getText());
                        try{
                            Double.parseDouble(txtPrecioPlato.getText());
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
                        }catch(Exception e){
                             JOptionPane.showMessageDialog(null, "No se aceptan letras en Precio solo números");
                        }
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null, "No se aceptan letras en Cantidad solo números");
                    }
                }
            break;
        }
    }
    
     public void guardar(){
        Plato registro = new Plato();
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcion.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPlato(?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlato.add(registro);
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
            default:
                if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Eliminar Plato",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION ){ 
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlato(?)");
                            procedimiento.setInt(1,((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlato.remove(tblPlatos.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
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
                if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/hernanlopez/image/editar.png"));
                    imgReporte.setImage(new Image("/org/hernanlopez/image/cancelar.png"));
                    activarControles();
                    cmbCodigoTipoPlato.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;   
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila para poder Editar");
                }
                break;
            case ACTUALIZAR:
                 if(txtNombrePlato.getText().isEmpty() || txtDescripcion.getText().isEmpty() || txtCantidad.getText().isEmpty()||
                        txtPrecioPlato.getText().isEmpty()||cmbCodigoTipoPlato.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null," No se puede actualizar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    try{
                        Integer.parseInt(txtCantidad.getText());
                        try{
                            Double.parseDouble(txtPrecioPlato.getText());
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
                        }catch(Exception e){
                            JOptionPane.showMessageDialog(null, "No se aceptan letras en Precio solo números");
                        }
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null, "No se aceptan letras en Cantidad solo números");
                    }
               }
                break;
        }
    }
    
    public void actualizar(){
         try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPlato(?,?,?,?,?)");
           Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
           registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
           registro.setNombrePlato(txtNombrePlato.getText());
           registro.setDescripcionPlato(txtDescripcion.getText());
           registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
           procedimiento.setInt(1, registro.getCodigoPlato());
           procedimiento.setInt(2, registro.getCantidad());
           procedimiento.setString(3, registro.getNombrePlato());
           procedimiento.setString(4, registro.getDescripcionPlato());
           procedimiento.setDouble(5, registro.getPrecioPlato());
           procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void reporte(){
        switch(tipoDeOperacion){
           case NINGUNO:
              if(tblPlatos.getSelectionModel().getSelectedItem() != null){
//                    imprimirReporte();
//                    limpiarControles();
//       
              }else{
                   JOptionPane.showMessageDialog(null, "Por favor seleccione una fila para realizar el Reporte"); 
               }
           break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/hernanlopez/image/editar.png"));
                imgReporte.setImage(new Image("/org/hernanlopez/image/Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                break;
        }
    }
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcion.setEditable(false);
        txtCantidad.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(false);
    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcion.setEditable(true);
        txtCantidad.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }
    
    public void limpiarControles(){
       txtCodigoPlato.clear();
        txtNombrePlato.clear();
        txtDescripcion.clear();
        txtCantidad.clear();
        txtPrecioPlato.clear();
        cmbCodigoTipoPlato.setValue("");
        tblPlatos.getSelectionModel().clearSelection();
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
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
}
