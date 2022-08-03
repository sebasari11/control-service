package proyecto.ucuenca.control.service;

import proyecto.ucuenca.control.entity.ControlMeasure;

import java.util.List;

public interface ControlService {
    public List<ControlMeasure> findAll();
    public ControlMeasure createControlMeasure(ControlMeasure controlMeasure);
    public ControlMeasure updateControlMeasure(ControlMeasure controlMeasure);
    public ControlMeasure deleteControlMeasure(ControlMeasure controlMeasure);
    public ControlMeasure getControlMeasure(Long id);
    public List<ControlMeasure> findLastControlMeasures();
}
