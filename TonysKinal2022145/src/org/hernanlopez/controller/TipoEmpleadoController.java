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
import org.hernanlopez.bean.TipoEmpleado;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;
import org.hernanlopez.report.GenerarReporte;


public class TipoEmpleadoController implements Initializable{
   
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private final String FondoTipoEmpleado = "/org/hernanlopez/image/PlantillaReporte.jpg";
    
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleados;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcionTipoEmpresa;
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
        tblTipoEmpleados.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("codigoTipoEmpleado"));
        colDescripcionTipoEmpresa.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,String>("descripcion"));
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleados");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                    resultado.getString("descripcion")));
             }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoEmpleado = FXCollections.observableArrayList(lista); 
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
                if(txtDescripcion.getText().isEmpty()){
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
        TipoEmpleado registro = new TipoEmpleado();
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
            if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){ 
                txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
                txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
            }else{
                JOptionPane.showMessageDialog(null,"Fila no seleccionada..!!");
            }
        }else{
            tblTipoEmpleados.getSelectionModel().clearSelection();
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
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro? ","Eliminar Tipo Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION ){ 
                        try{
                            int codigoTipoEmpleado = ((TipoEmpleado) tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado();
                             
                            PreparedStatement buscar = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleadoTipo(?)");
                            buscar.setInt(1, codigoTipoEmpleado);
                            ResultSet resultado = buscar.executeQuery();
                            
                            if(resultado.next()){
                                JOptionPane.showMessageDialog(null, " No se puede eliminar: Está siendo usado por otro registro\n Elimine todos los registros que puedan estar utilizando esté dato e inténtelo de nuevo.");
                                limpiarControles();
                                tblTipoEmpleados.getSelectionModel().clearSelection();
                            }else{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                                procedimiento.setInt(1, codigoTipoEmpleado);
                                procedimiento.execute();

                                listaTipoEmpleado.remove(tblTipoEmpleados.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        tblTipoEmpleados.getSelectionModel().clearSelection();
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila para poder Eliminar");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
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
                if(txtDescripcion.getText().isEmpty()){
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
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?,?)");
           TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem();
           registro.setDescripcion(txtDescripcion.getText());
           procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
           procedimiento.setString(2, registro.getDescripcion());
           procedimiento.execute();
         }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                     imprimirReporte();
                     limpiarControles();
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
    
     public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("FondoTipoEmpleado", this.getClass().getResourceAsStream(FondoTipoEmpleado));
        GenerarReporte.mostrarReporte("ReporteTipoEmp.jasper", "Reporte Tipos de Empleados", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(true);   
    }
    
    public void limpiarControles(){
       txtCodigoTipoEmpleado.clear();
       txtDescripcion.clear();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
}
