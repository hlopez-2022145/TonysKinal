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
import org.hernanlopez.bean.Producto;
import org.hernanlopez.bean.Productos_has_Platos;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;


public class Productos_has_PlatosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos_has_Platos> listaProHasPla;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Producto> listaProducto;
    
    @FXML private TextField txtCodigoProductoYPlato;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TableView tblProductoHas;
    @FXML private TableColumn colProductoYPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
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
        cmbCodigoProducto.setItems(getProducto());
    }
    
    public void cargarDatos(){
        tblProductoHas.setItems(getProductoHas());
        colProductoYPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer> ("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("codigoProducto"));
    }
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
            if(tblProductoHas.getSelectionModel().getSelectedItems() != null){
                txtCodigoProductoYPlato.setText(String.valueOf(((Productos_has_Platos)tblProductoHas.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
                cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Productos_has_Platos)tblProductoHas.getSelectionModel().getSelectedItem()).getCodigoPlato())); 
                cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Productos_has_Platos)tblProductoHas.getSelectionModel().getSelectedItem()).getCodigoProducto())); 
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
    
    public Producto buscarProducto (int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimineto.setInt(1, codigoProducto);
            ResultSet registro = procedimineto.executeQuery();
            while(registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"),
                                     registro.getString("nombreProducto"),
                                     registro.getInt("cantidadProducto"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         return resultado;       
    }
    
    public ObservableList<Productos_has_Platos> getProductoHas(){
        ArrayList<Productos_has_Platos> lista = new ArrayList<Productos_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductosHasPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos_has_Platos(resultado.getInt("Productos_codigoProducto"),
                                        resultado.getInt("codigoPlato"),
                                        resultado.getInt("codigoProducto")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProHasPla =  FXCollections.observableArrayList(lista); 
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
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new Producto(resultado.getInt("codigoProducto"),
                                     resultado.getString("nombreProducto"),
                                     resultado.getInt("cantidadProducto")));
             }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista); 
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
                if(txtCodigoProductoYPlato.getText().isEmpty()||cmbCodigoPlato.getSelectionModel().getSelectedItem()  == null||cmbCodigoProducto.getSelectionModel().getSelectedItem() == null){
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
        Productos_has_Platos registro = new Productos_has_Platos();
        registro.setProductos_codigoProducto(Integer.parseInt(txtCodigoProductoYPlato.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());

        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProductoHasPlato(?,?,?)");
            procedimiento.setInt(1, registro.getProductos_codigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
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
        txtCodigoProductoYPlato.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }
    public void activarControles(){
        txtCodigoProductoYPlato.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoProductoYPlato.clear();
        cmbCodigoPlato.setValue("");
        cmbCodigoProducto.setValue("");
        tblProductoHas.getSelectionModel().clearSelection();
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
