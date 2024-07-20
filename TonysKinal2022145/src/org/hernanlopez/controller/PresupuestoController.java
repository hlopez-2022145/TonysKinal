package org.hernanlopez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.hernanlopez.bean.Empresa;
import org.hernanlopez.bean.Presupuesto;
import org.hernanlopez.db.Conexion;
import org.hernanlopez.main.Principal;
import org.hernanlopez.report.GenerarReporte;


public class PresupuestoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa; 
    private final String FondoGeneral = "/org/hernanlopez/image/PlantillaReporte.jpg";
    
    private DatePicker fecha;
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
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
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer> ("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }
    
    public void seleccionaElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){   
            if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){ 
                txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
                fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
                txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            }else{
                JOptionPane.showMessageDialog(null,"Fila no seleccionada..!!");
            }
        
        }else{
            tblPresupuestos.getSelectionModel().clearSelection();
        }
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimineto.setInt(1, codigoEmpresa);
            ResultSet registro = procedimineto.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                     registro.getString("nombreEmpresa"),
                                     registro.getString("direccion"),
                                     registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuestos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                        resultado.getDate("FechaSolicitud"),
                                        resultado.getDouble("cantidadPresupuesto"),
                                        resultado.getInt("codigoEmpresa")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPresupuesto =  FXCollections.observableArrayList(lista); 
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
                if(txtCantidadPresupuesto.getText().isEmpty() || fecha.selectedDateProperty().getValue() == null || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null," No se puede guardar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                   try{
                        Double.parseDouble(txtCantidadPresupuesto.getText());
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
                   }catch(Exception e){
                       JOptionPane.showMessageDialog(null, "No se aceptan letras en Cantidad Presupuesto solo números");
                   }
                }
        }
    }
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPresupuesto(?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
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
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Eliminar Presupuesto",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION ){ 
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1,((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                            tblPresupuestos.getSelectionModel().clearSelection();
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
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/hernanlopez/image/editar.png"));
                    imgReporte.setImage(new Image("/org/hernanlopez/image/cancelar.png"));
                    activarControles();
                    cmbCodigoEmpresa.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;   
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila para poder Editar");
                }
                break;
            case ACTUALIZAR:
               if(txtCantidadPresupuesto.getText().isEmpty() || fecha.selectedDateProperty().getValue() == null || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null," No se puede actualizar si hay algún campo vacío\n Por favor verificar bien e inténtelo de nuevo.");
                }else{
                    try{
                        Double.parseDouble(txtCantidadPresupuesto.getText());
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
                       JOptionPane.showMessageDialog(null, "No se aceptan letras en Cantidad Presupuesto solo números");
                   }
               }
                break;
        }
    }
    
    public void actualizar(){
         try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPresupuesto(?,?,?)");
           Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
           registro.setFechaSolicitud(fecha.getSelectedDate());
           registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
           procedimiento.setInt(1, registro.getCodigoPresupuesto());
           procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
           procedimiento.setDouble(3, registro.getCantidadPresupuesto());
           procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void reporte(){
        switch(tipoDeOperacion){
//            case NINGUNO:
//               if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
//                    imprimirReporte();
//                    limpiarControles();
//       
//                }else{
//                    JOptionPane.showMessageDialog(null, "Por favor seleccione una fila para realizar el Reporte"); 
//                }
//                break;
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
                tblPresupuestos.getSelectionModel().clearSelection();
                break;
        }
    }
     public void reporteGenral(){
        if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
            imprimirReporteGeneral();
            limpiarControles();
        }else{
            JOptionPane.showMessageDialog(null, "Por favor seleccione una fila para realizar el Reporte");  
        } 
    }
    public void imprimirReporteGeneral(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("FondoGeneral", this.getClass().getResourceAsStream(FondoGeneral));
        GenerarReporte.mostrarReporte("ReporteGeneral.jasper","Reporte de General",parametros);
    }
     
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper","Reporte de Presupuesto",parametros);
    }
     
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        fecha.setDisable(true);
        txtCantidadPresupuesto.setEditable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        fecha.setDisable(false);
        txtCantidadPresupuesto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPresupuesto.clear();
        fecha.selectedDateProperty().set(null);
        txtCantidadPresupuesto.clear();
        cmbCodigoEmpresa.setValue("");
        tblPresupuestos.getSelectionModel().clearSelection();
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
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
}
