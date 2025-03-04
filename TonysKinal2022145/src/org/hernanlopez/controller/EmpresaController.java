package org.hernanlopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.hernanlopez.bean.Empresa;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;
import org.hernanlopez.report.GenerarReporte;


public class EmpresaController implements Initializable{
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empresa> listaEmpresa;
    private final String FondoEmpresa = "/org/hernanlopez/image/PlantillaReporte.jpg";
    
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
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
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,String>("telefono"));
    }
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                                    resultado.getString("nombreEmpresa"),
                                    resultado.getString("direccion"),
                                    resultado.getString("telefono")));
             }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista); 
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
                if(txtNombreEmpresa.getText().isEmpty()|| txtDireccionEmpresa.getText().isEmpty()||txtTelefonoEmpresa.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null," No se puede guardar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    try{
                        Integer.parseInt(txtTelefonoEmpresa.getText());
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
                       JOptionPane.showMessageDialog(null, "No se aceptan letras en Teléfono solo números");                  }
                    }
            break;
        }
    }
    public void validar(){
      
    }
    public void guardar(){
        
        Empresa registro = new Empresa();
        //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());

        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa(?, ?, ?)");
            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());
            procedimiento.execute();
            listaEmpresa.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){ 
            if(tblEmpresas.getSelectionModel().getSelectedItem() != null){ 
                txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
                txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
                txtDireccionEmpresa.setText((((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion()));
                txtTelefonoEmpresa.setText((((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono()));   
            }else{
                JOptionPane.showMessageDialog(null,"Fila no seleccionada..!!");
            }
        }else{
            tblEmpresas.getSelectionModel().clearSelection();
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION ){ 
                        try{
                            int codigoEmpresa = ((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa();
                             
                            PreparedStatement buscar = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPresupuestoEmpresa(?)");
                            buscar.setInt(1, codigoEmpresa);
                            ResultSet resultado = buscar.executeQuery();
                            
                            if(resultado.next()){
                                JOptionPane.showMessageDialog(null, " No se puede eliminar: Está siendo usado por otro registro\n Elimine todos los registros que puedan estar utilizando esté dato e inténtelo de nuevo.");
                                limpiarControles();
                                tblEmpresas.getSelectionModel().clearSelection();
                            }else{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                                procedimiento.setInt(1, codigoEmpresa);
                                procedimiento.execute();

                                listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        tblEmpresas.getSelectionModel().clearSelection();
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
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
                if(txtNombreEmpresa.getText().isEmpty()|| txtDireccionEmpresa.getText().isEmpty()||txtTelefonoEmpresa.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null," No se puede actualizar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    try{
                        Integer.parseInt(txtTelefonoEmpresa.getText());
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
                       JOptionPane.showMessageDialog(null, "No se aceptan letras en Teléfono solo números"); 
                    }
                }
                break;
        }
    }
    
    public void actualizar(){
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?,?,?,?)");
           Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
           registro.setNombreEmpresa(txtNombreEmpresa.getText());
           registro.setDireccion(txtDireccionEmpresa.getText());
           registro.setTelefono(txtTelefonoEmpresa.getText());
           procedimiento.setInt(1, registro.getCodigoEmpresa());
           procedimiento.setString(2, registro.getNombreEmpresa());
           procedimiento.setString(3, registro.getDireccion());
           procedimiento.setString(4, registro.getTelefono());
           procedimiento.execute();
         }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            
            case NINGUNO: 
                imprimirReporte();
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
                tblEmpresas.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("FondoEmpresa", this.getClass().getResourceAsStream(FondoEmpresa));
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }
    
    public void limpiarControles(){
       txtCodigoEmpresa.clear();
       txtNombreEmpresa.clear();
       txtDireccionEmpresa.clear();
       txtTelefonoEmpresa.clear();
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
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
}
