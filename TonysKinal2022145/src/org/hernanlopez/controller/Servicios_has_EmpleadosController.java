package org.hernanlopez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.hernanlopez.bean.Empleado;
import org.hernanlopez.bean.Servicio;
import org.hernanlopez.bean.Servicios_has_Empleados;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;


public class Servicios_has_EmpleadosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicios_has_Empleados> listaSerHasEmp;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    
    private DatePicker fecha;
    @FXML private TextField txtCodigoServicioYEmp;
    @FXML private GridPane grpFechaEmp;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private TableView tblServiciosHasEmpl;
    @FXML private TableColumn colCodigoServicioHasEm;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colCodigoServico;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnReporteGenral;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/hernanlopez/resource/TonysKinal.css");
        grpFechaEmp.add(fecha, 3, 0);
        cmbCodigoEmpleado.setItems(getEmpleado());
        cmbCodigoServicio.setItems(getServicio());
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblServiciosHasEmpl.setItems(getEmpleadoHas());
        colCodigoServicioHasEm.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer> ("Servicios_codigoServicio"));
        colCodigoServico.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Time>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, String>("lugarEvento"));
    }
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
            if(tblServiciosHasEmpl.getSelectionModel().getSelectedItems() != null){
                txtCodigoServicioYEmp.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpl.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_has_Empleados)tblServiciosHasEmpl.getSelectionModel().getSelectedItem()).getCodigoEmpleado())); 
                cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Empleados)tblServiciosHasEmpl.getSelectionModel().getSelectedItem()).getCodigoServicio())); 
                fecha.selectedDateProperty().set(((Servicios_has_Empleados)tblServiciosHasEmpl.getSelectionModel().getSelectedItem()).getFechaEvento());
                txtHoraEvento.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpl.getSelectionModel().getSelectedItem()).getHoraEvento()));
                txtLugarEvento.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpl.getSelectionModel().getSelectedItem()).getLugarEvento()));
            }else{
              JOptionPane.showMessageDialog(null,"Fila no seleccionada..!!");
            }
        }else{
          limpiarControles();  
        }
    }
    
     public Empleado buscarEmpleado (int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleado(?)");
            procedimineto.setInt(1, codigoEmpleado);
            ResultSet registro = procedimineto.executeQuery();
            while(registro.next()){
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                                    registro.getInt("numeroEmpleado"),
                                    registro.getString("nombresEmpleados"),
                                    registro.getString("apellidosEmpleado"),
                                    registro.getString("direccionEmpleado"),
                                    registro.getString("telefonoContacto"),
                                    registro.getString("gradoCocinero"),
                                    registro.getInt("codigoTipoEmpleado"));
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
    
    public ObservableList<Servicios_has_Empleados> getEmpleadoHas(){
        ArrayList<Servicios_has_Empleados> lista = new ArrayList<Servicios_has_Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServiciosHasEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_has_Empleados(resultado.getInt("Servicios_codigoServicio"),
                                        resultado.getInt("codigoEmpleado"),
                                        resultado.getInt("codigoServicio"),
                                        resultado.getDate("fechaEvento"),
                                        resultado.getTime("horaEvento"),
                                        resultado.getString("lugarEvento")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaSerHasEmp =  FXCollections.observableArrayList(lista); 
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
                if(txtCodigoServicioYEmp.getText().isEmpty()||cmbCodigoEmpleado.getSelectionModel().getSelectedItem()  == null||cmbCodigoServicio.getSelectionModel().getSelectedItem() == null||
                        fecha.selectedDateProperty().getValue() == null|| txtHoraEvento.getText().isEmpty()|| txtLugarEvento.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null," No se puede guardar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    try{
                        txtHoraEvento.getText();
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
                            JOptionPane.showMessageDialog(null, "Por Favor Escribir la hora HH:MM:SS ");
                    }
                }
            break;
        }
    }
    
     public void guardar(){
        Servicios_has_Empleados registro = new Servicios_has_Empleados();
        registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoServicioYEmp.getText()));
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(Time.valueOf(txtHoraEvento.getText()));
        registro.setLugarEvento(txtLugarEvento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicioHasEmpleado(?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoEmpleado());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setTime(5, new java.sql.Time(registro.getHoraEvento().getTime()));
            procedimiento.setString(6, registro.getLugarEvento());
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
           default:
                if(tblServiciosHasEmpl.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Eliminar Servicio has Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION ){ 
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicioHasEmpleado(?)");
                            procedimiento.setInt(1,(( Servicios_has_Empleados)tblServiciosHasEmpl.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute();
                            listaSerHasEmp.remove(tblServiciosHasEmpl.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                            tblServiciosHasEmpl.getSelectionModel().clearSelection();
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
                if(tblServiciosHasEmpl.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/hernanlopez/image/editar.png"));
                    imgReporte.setImage(new Image("/org/hernanlopez/image/cancelar.png"));
                    activarControles();
                    cmbCodigoEmpleado.setDisable(true);
                    cmbCodigoServicio.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;   
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila para poder Editar");
                }
                break;
            case ACTUALIZAR:
                 if(txtCodigoServicioYEmp.getText().isEmpty()||cmbCodigoEmpleado.getSelectionModel().getSelectedItem()  == null||cmbCodigoServicio.getSelectionModel().getSelectedItem() == null||
                        fecha.selectedDateProperty().getValue() == null|| txtHoraEvento.getText().isEmpty()|| txtLugarEvento.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null," No se puede actualizar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    try{
                        txtHoraEvento.getText();
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
                            JOptionPane.showMessageDialog(null, "Por Favor Escribir la hora HH:MM:SS ");
                    }
               }
                break;
        }
    }
    
    public void actualizar(){
         try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicioHasEmpleado(?,?,?,?)");
           java.sql.Time horaEvento = java.sql.Time.valueOf(txtHoraEvento.getText());
           Servicios_has_Empleados registro = (Servicios_has_Empleados)tblServiciosHasEmpl.getSelectionModel().getSelectedItem();
           registro.setFechaEvento(fecha.getSelectedDate());
           registro.setFechaEvento(horaEvento);
           registro.setLugarEvento(txtLugarEvento.getText());
           procedimiento.setInt(1, registro.getServicios_codigoServicio());
           procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
           procedimiento.setTime(3, new java.sql.Time(registro.getHoraEvento().getTime()));
           procedimiento.setString(4, registro.getLugarEvento());
           procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void reporte(){
        switch(tipoDeOperacion){
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
        txtCodigoServicioYEmp.setEditable(false);
        cmbCodigoEmpleado.setDisable(true);
        cmbCodigoServicio.setDisable(true);
        fecha.setDisable(true);
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
    }
    public void activarControles(){
        txtCodigoServicioYEmp.setEditable(true);
        cmbCodigoEmpleado.setDisable(false);
        cmbCodigoServicio.setDisable(false);
        fecha.setDisable(false);
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoServicioYEmp.clear();
        cmbCodigoEmpleado.setValue("");
        cmbCodigoServicio.setValue("");
        fecha.selectedDateProperty().set(null);
        txtHoraEvento.clear();
        txtLugarEvento.clear();
        tblServiciosHasEmpl.getSelectionModel().clearSelection();
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

