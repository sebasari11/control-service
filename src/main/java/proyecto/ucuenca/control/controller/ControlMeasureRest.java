package proyecto.ucuenca.control.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import proyecto.ucuenca.control.entity.ControlMeasure;
import proyecto.ucuenca.control.service.ControlService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/control")
public class ControlMeasureRest {

    @Autowired
    ControlService controlMeasureService;

    // -------------------Retrieve All ControlMeasures--------------------------------------------
    @GetMapping
    public ResponseEntity<List<ControlMeasure>> listAllControlMeasures(){
        List<ControlMeasure> controls= new ArrayList<>();
        controls = controlMeasureService.findAll();
        if (controls.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(controls);
    }

    // -------------------Retrieve Single Control Measure------------------------------------------
    @GetMapping(value = "/{id}")
    public ResponseEntity<ControlMeasure> getControlMeasure(@PathVariable("id") long id) {
        log.info("Fetching Measure with id {}", id);
        ControlMeasure control = controlMeasureService.getControlMeasure(id);
        if (  null == control) {
            log.error("Measure with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(control);
    }

    // -------------------Create a ControlMeasure--------------------------------------------

    @PostMapping
    public ResponseEntity<ControlMeasure> createControlMeasure(@Valid @RequestBody ControlMeasure controlMeasure, BindingResult result){
        log.info("Creating Control Measure: {}", controlMeasure);
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));

        }
        ControlMeasure controlMeasureBD = controlMeasureService.createControlMeasure(controlMeasure);
        return ResponseEntity.status(HttpStatus.CREATED).body(controlMeasureBD);
    }

    // ------------------- Update a ControlMeasure ------------------------------------------------
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateControlMeasure(@PathVariable("id") long id, @RequestBody ControlMeasure controlMeasure) {
        log.info("Updating Measure with id {}", id);

        ControlMeasure currentControlMeasure = controlMeasureService.getControlMeasure(id);

        if ( null == currentControlMeasure ) {
            log.error("Unable to update. Measure with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        controlMeasure.setId(id);
        currentControlMeasure = controlMeasureService.updateControlMeasure(controlMeasure);
        return  ResponseEntity.ok(currentControlMeasure);
    }

    // ------------------- Delete a Control-----------------------------------------
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ControlMeasure> deleteControlMeasure(@PathVariable("id") long id) {
        log.info("Fetching & Deleting ControlMeasure with id {}", id);

        ControlMeasure controlMeasure = controlMeasureService.getControlMeasure(id);
        if ( null == controlMeasure ) {
            log.error("Unable to delete. Measure with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        controlMeasure = controlMeasureService.deleteControlMeasure(controlMeasure);
        return  ResponseEntity.ok(controlMeasure);
    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
