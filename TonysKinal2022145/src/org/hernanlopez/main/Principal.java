/*
    Hernán Misael López Pérez
    IN5AV
    2022145

    Inicio: 28-03-2023

    Modificaciones: 28-03-2023,11-04-2023,
        12-04-2023,17-04-2023,18-04-2023,19-04-2023,
        23-04-2023,24-04-2023,25-04-2023,30-05-2023
 */
package org.hernanlopez.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hernanlopez.controller.EmpleadoController;
import org.hernanlopez.controller.EmpresaController;
import org.hernanlopez.controller.LoginController;
import org.hernanlopez.controller.MenuPrincipalController;
import org.hernanlopez.controller.PlatoController;
import org.hernanlopez.controller.PresupuestoController;
import org.hernanlopez.controller.ProductoController;
import org.hernanlopez.controller.Productos_has_PlatosController;
import org.hernanlopez.controller.ProgramadorController;
import org.hernanlopez.controller.ServicioController;
import org.hernanlopez.controller.Servicios_has_EmpleadosController;
import org.hernanlopez.controller.Servicios_has_PlatosController;
import org.hernanlopez.controller.TipoEmpleadoController;
import org.hernanlopez.controller.TipoPlatoController;
import org.hernanlopez.controller.UsuarioController;

/**
 *
 * @author hlopez
 */
public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/hernanlopez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
      this. escenarioPrincipal = escenarioPrincipal;
      escenarioPrincipal.setTitle("Tony's Kinal 2023");
      escenarioPrincipal.getIcons().add(new Image("/org/hernanlopez/image/favicon.png"));
      //Parent root = FXMLLoader.load(getClass().getResource("/org/hernanlopez/view/MenuPrincipalView.fxml"));
      //Scene escena = new Scene(root);
      //escenarioPrincipal.setScene(escena);
      ventanaLogin();
      escenarioPrincipal.show();
      
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",340,383);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController progra = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",507,213);
            progra.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaEmpresa(){
        try{
            EmpresaController empresaController = (EmpresaController)cambiarEscena("EmpresaView.fxml",800,398);
            empresaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleadoController = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",612,398);
            tipoEmpleadoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoPlatoController = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml",632,398);
            tipoPlatoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto(){
        try{
            ProductoController productoController = (ProductoController)cambiarEscena("ProductoView.fxml",620,398);
            productoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuestoController = (PresupuestoController) cambiarEscena("PresupuestoView.fxml",800,398);
            presupuestoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
    public void ventanaEmpleado(){
        try{
            EmpleadoController empleadoController = (EmpleadoController) cambiarEscena("EmpleadosView.fxml",1234,398);
            empleadoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try{
            LoginController loginController = (LoginController) cambiarEscena("LoginView.fxml",450,400);
            loginController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuarioController = (UsuarioController) cambiarEscena("UsuarioView.fxml",593,398);
            usuarioController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try{
            PlatoController platoController = (PlatoController) cambiarEscena("PlatoView.fxml",1298,398);
            platoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio(){
        try{
            ServicioController servicioController = (ServicioController) cambiarEscena("ServicioView.fxml",1222,398);
            servicioController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductoHas(){
        try{
            Productos_has_PlatosController productos_has_PlatosController = (Productos_has_PlatosController) cambiarEscena("Productos_has_PlatosView.fxml",691,398);
            productos_has_PlatosController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicioHas(){
        try{
            Servicios_has_PlatosController servicios_has_PlatosController = (Servicios_has_PlatosController) cambiarEscena("Servicios_Has_PlatosView.fxml",696,398);
            servicios_has_PlatosController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleadoHas(){
        try{
            Servicios_has_EmpleadosController servicios_has_EmpleadosController = (Servicios_has_EmpleadosController) cambiarEscena("Servicios_has_Empleados.fxml",1015,398);
            servicios_has_EmpleadosController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml,int ancho,int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        // Casteamos para que sean compatibles.
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
}
