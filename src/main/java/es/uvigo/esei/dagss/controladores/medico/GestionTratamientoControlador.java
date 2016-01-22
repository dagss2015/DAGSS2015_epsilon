/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.dominio.daos.GenericoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.daos.TratamientoDAO;
import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Deni
 */

@Named(value = "gestionTratamientoControlador")
@SessionScoped

public class GestionTratamientoControlador implements Serializable{
    @EJB 
    TratamientoDAO tratamientoDao;
    
    @EJB
    MedicoDAO medicoDAO;
    @EJB
    PacienteDAO pacienteDAO;
    @EJB
    MedicamentoDAO medicamentoDAO;
    @EJB
    RecetaDAO recetaDAO;
            
    List<Tratamiento> tratamientos;
    Tratamiento tratamientoActual;
    
    Medico medicoActual;
    Paciente pacienteActual;
    Medicamento medicamentoActual;
    
    private int dosis;
    private String indicaciones;
    Prescripcion prescripcionActual;    
    
    List<Receta> recetas;
    
    GestionTratamientoControlador(){
    }
    
    @PostConstruct
    public void inicializar() {
        tratamientos = tratamientoDao.buscarTodos();
    }
    
    public List<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public Tratamiento getTratamientoActual() {
        return tratamientoActual;
    }

    public void setTratamientoActual(Tratamiento tratamientoActual) {
        this.tratamientoActual = tratamientoActual;
    }
    
    public List<Paciente> getListadoPacientes() {
        return pacienteDAO.buscarTodos();
    }
        
    public List<Medico> getListadoMedicos() {
        return medicoDAO.buscarTodos();
    }
    
    public Medico getMedicoActual(){
        return medicoActual;
    }
    
    public Paciente getPacienteActual(){
        return pacienteActual;
    }
    
    public void doEliminar() {
        tratamientoDao.eliminar(tratamientoActual);
        tratamientos = tratamientoDao.buscarTodos(); // Actualizar lista de centros
    }

    public void doNuevo() {
        tratamientoActual = new Tratamiento();
    }

    public void doEditar(Tratamiento tratamiento) {
        tratamientoActual = tratamiento;   // Otra alternativa: volver a refrescarlos desde el DAO
    }

    public void doGuardarNuevo(Medico medico, Paciente paciente){ 
        tratamientoActual.setMedico(medico);
        tratamientoActual.setPaciente(paciente);
        tratamientoActual = tratamientoDao.crear(tratamientoActual);       
        tratamientos = tratamientoDao.buscarTodos();
    }

    public void doGuardarEditado() {
        tratamientoActual = tratamientoDao.actualizar(tratamientoActual);
        tratamientos = tratamientoDao.buscarTodos();
    }
    
    public List<Tratamiento> tratamientoPorPaciente(Long medicoId, Long pacienteId){
        return tratamientoDao.buscarTratamientoPorPacienteMedico(medicoId, pacienteId);
    }
    
    public String doShowDetalleTratamiento(){
        return "verTratamiento";
    }
    
    public Medicamento getMedicamentoActual() {
        return medicamentoActual;
    }

    public void setMedicamentoActual(Medicamento medicamentoActual) {
        this.medicamentoActual = medicamentoActual;
    }
    
    public int getDosis(){
        return dosis;
    }
    
    public void setDosis(int dosis){
        this.dosis = dosis;
    }
    
    public String getIndicaciones(){
        return indicaciones;
    }
    
    public void setIndicaciones(String indicaciones){
        this.indicaciones = indicaciones;
    }
    
    public Prescripcion getPrescripcionActual(){
        return prescripcionActual;
    }
    
    public void setPrescripcionActual(Prescripcion prescripcionActual){
        this.prescripcionActual = prescripcionActual;
    }
       
    public void doNuevaPrescripcion(Medicamento medicamento){
        Prescripcion p = new Prescripcion(indicaciones, tratamientoActual, medicamento, dosis);
        prescripcionActual=p;
        tratamientoActual.getPrescipciones().add(prescripcionActual);
        List<Prescripcion> prescipciones = tratamientoActual.getPrescipciones();
        tratamientoActual.setPrescipciones(prescipciones);       
        tratamientoActual = tratamientoDao.actualizar(tratamientoActual);
        tratamientos = tratamientoDao.buscarTodos();
    }
    
    
    public List<Prescripcion> doActualizarPrescripciones() {        
        return tratamientoActual.getPrescipciones();
    }
   
    
    public void crearReceta(Prescripcion prescripcion, Integer cantidad, Date inicio, Date fin){
              
        if(prescripcion.getMedicamento().getNumeroDosis() >= prescripcion.getDosis())
        {
            Receta receta = new Receta(prescripcion, cantidad, inicio, fin, EstadoReceta.GENERADA, null);
            recetaDAO.crear(receta);
            prescripcionActual.getRecetas().add(receta);
            
            
        }else{
            int numRecetas = 0;
            numRecetas = (prescripcion.getDosis()/prescripcion.getMedicamento().getNumeroDosis());
            for(int i = 0; i < numRecetas; i++){
                fin = sumarDiasFecha(inicio, prescripcion.getMedicamento().getNumeroDosis());
                Receta receta = new Receta(prescripcion, cantidad, inicio, fin, EstadoReceta.GENERADA, null);
                recetaDAO.crear(receta);
                prescripcionActual.getRecetas().add(receta);                  
                inicio = fin;                
            }
        }
        
    }
    
    public Date sumarDiasFecha(Date fecha, int dias){
      Calendar calendar = Calendar.getInstance();	
      calendar.setTime(fecha); // Configuramos la fecha que se recibe06	
      calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<007	
      return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
	
    }
    
    
}
