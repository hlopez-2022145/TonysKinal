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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.hernanlopez.bean.Empleado;
import org.hernanlopez.bean.TipoEmpleado;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;
import org.hernanlopez.report.GenerarReporte;


public class EmpleadoController implements Initializable  {
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private final String FondoEmpleado = "/org/hernanlopez/image/PlantillaReporte.jpg";
    
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtApellidoEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colNombreEmpleado;
    @FXML private TableColumn colApellidoEmpleado;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
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
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer> ("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer> ("numeroEmpleado"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("nombresEmpleados"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("apellidosEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }
    
    public void seleccionarElemento(){
         if(tipoDeOperacion != operaciones.GUARDAR){   
            if(tblEmpleados.getSelectionModel().getSelectedItem() != null){ 
                txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
                txtNombreEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleados()));
                txtApellidoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado()));
                txtDireccionEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado()));
                txtTelefonoContacto.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
                txtGradoCocinero.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero()));
                cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            }else{
                JOptionPane.showMessageDialog(null,"Fila no seleccionada..!!");
            }
        
        }else{
            //JOptionPane.showMessageDialog(null, "No se pueden guardar datos iguales....");
            tblEmpleados.getSelectionModel().clearSelection();
        }
    }
   
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimineto.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimineto.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                     registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("nombresEmpleados"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado =  FXCollections.observableArrayList(lista); 
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
                if(txtNumeroEmpleado.getText().isEmpty() || txtNombreEmpleado.getText().isEmpty() || txtApellidoEmpleado.getText().isEmpty()||
                        txtDireccionEmpleado.getText().isEmpty()||txtTelefonoContacto.getText().isEmpty()||txtGradoCocinero.getText().isEmpty()||cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null," No se puede guardar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                     try{   
                        Integer.parseInt(txtNumeroEmpleado.getText());
                        try{
                            Integer.parseInt(txtTelefonoContacto.getText());
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
                          JOptionPane.showMessageDialog(null, "No se aceptan letras en Teléfono solo números");
                        }
                     }catch(Exception e){
                          JOptionPane.showMessageDialog(null, "No se aceptan letras en Número empleado solo números");
                     }
                        
                }
                    
            break;
        }
    }
    
     public void guardar(){
        Empleado registro = new Empleado();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setNombresEmpleados(txtNombreEmpleado.getText());
        registro.setApellidosEmpleado(txtApellidoEmpleado.getText());
        registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleado(?,?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getNombresEmpleados());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
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
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Eliminar Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION ){ 
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1,((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getFocusedIndex());
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
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/hernanlopez/image/editar.png"));
                    imgReporte.setImage(new Image("/org/hernanlopez/image/cancelar.png"));
                    activarControles();
                    cmbCodigoTipoEmpleado.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;   
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila para poder Editar");
                }
                break;
            case ACTUALIZAR:
                if(txtNumeroEmpleado.getText().isEmpty() || txtNombreEmpleado.getText().isEmpty() || txtApellidoEmpleado.getText().isEmpty()||
                    txtDireccionEmpleado.getText().isEmpty()||txtTelefonoContacto.getText().isEmpty()||txtGradoCocinero.getText().isEmpty()||cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null," No se puede actualizar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    try{   
                        Integer.parseInt(txtNumeroEmpleado.getText());
                        try{
                            Integer.parseInt(txtTelefonoContacto.getText());
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
                    }catch(Exception e){
                          JOptionPane.showMessageDialog(null, "No se aceptan letras en Número empleado solo números");
                     }
               }
                break;
        }
    }
    
    public void actualizar(){
         try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleado(?,?,?,?,?,?,?)");
           Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
           registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
           registro.setNombresEmpleados(txtNombreEmpleado.getText());
           registro.setApellidosEmpleado(txtApellidoEmpleado.getText());
           registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
           registro.setTelefonoContacto(txtTelefonoContacto.getText());
           registro.setGradoCocinero(txtGradoCocinero.getText());
           procedimiento.setInt(1, registro.getCodigoEmpleado());
           procedimiento.setInt(2, registro.getNumeroEmpleado());
           procedimiento.setString(3, registro.getNombresEmpleados());
           procedimiento.setString(4, registro.getApellidosEmpleado());
           procedimiento.setString(5, registro.getDireccionEmpleado());
           procedimiento.setString(6, registro.getTelefonoContacto());
           procedimiento.setString(7, registro.getGradoCocinero());
           procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void reporte(){
        switch(tipoDeOperacion){
           case NINGUNO:
               if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                    limpiarControles();
       
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
     
     public void imprimirReporte(){
       Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        parametros.put("codTipoEmpleado", codEmpresa);
        parametros.put("FondoEmpleado", this.getClass().getResourceAsStream(FondoEmpleado));
        GenerarReporte.mostrarReporte("ReporteEmpleado.jasper","Reporte de Empleados",parametros);
    }
    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtTelefonoContacto.clear();
        txtGradoCocinero.clear();
        cmbCodigoTipoEmpleado.setValue("");
        tblEmpleados.getSelectionModel().clearSelection();
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
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
}
