package proyecto.ucuenca.control.service;

import feign.Feign;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import proyecto.ucuenca.control.client.MeasureClient;
import proyecto.ucuenca.control.client.UserClient;
import proyecto.ucuenca.control.entity.ControlMeasure;
import proyecto.ucuenca.control.model.Measure;
import proyecto.ucuenca.control.model.User;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ControlServiceImp implements ControlService{
    private final MongoOperations mongoOperations;

    MeasureClient measureClient;

    UserClient userClient;

    public ControlServiceImp(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
        measureClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(MeasureClient.class))
                .logLevel(Logger.Level.FULL)
                .target(MeasureClient.class, "https://measure-service.herokuapp.com/measures/");

        userClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(MeasureClient.class))
                .logLevel(Logger.Level.FULL)
                .target(UserClient.class, "http://54.87.30.242:8081/user");



    }

    @Override
    public List<ControlMeasure> findAll() {
        return mongoOperations.findAll(ControlMeasure.class);
    }


    @Override
    public ControlMeasure getControlMeasure(Long id) {
        ControlMeasure controlMeasure = this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), ControlMeasure.class);
        if (controlMeasure != null){
            Measure measure= measureClient.getMeasure(controlMeasure.getMeasureId());
            User user = userClient.getUser(controlMeasure.getUserId());
            controlMeasure.setMeasure(measure);
            controlMeasure.setUser(user);
        }

        return controlMeasure;
    }


    @Override
    public ControlMeasure createControlMeasure(ControlMeasure controlMeasure) {
        ControlMeasure controlMeasureDB= getControlMeasure(controlMeasure.getId());
        if (controlMeasureDB != null) {
            return controlMeasureDB;
        }
        Measure measure= measureClient.findLastMeasure();
        User user = userClient.getUser(measure.getUserId());
        controlMeasure.setMeasure(measure);
        controlMeasure.setUser(user);
        controlMeasure.setMeasureId(measure.getId());
        controlMeasure.setUserId(measure.getUserId());
        Double systolic = measure.getSystolicPressure();
        Double diastolic = measure.getDiastolicPressure();
        Double pam = (systolic+2*diastolic)/3;
        controlMeasure.setPam(pam);
        controlMeasure.setIsExercising(Boolean.FALSE);
        String status = controlMeasure.getStatusBySex(Boolean.FALSE);
        controlMeasure.setStatus(status);
        controlMeasure.setCreate(new Date());
        controlMeasureDB = mongoOperations.save(controlMeasure);
        return controlMeasureDB;


    }

    @Override
    public ControlMeasure updateControlMeasure(ControlMeasure controlMeasure) {
        ControlMeasure controlMeasureDB = getControlMeasure(controlMeasure.getId());
        if (controlMeasureDB == null) {
            return null;
        }
        controlMeasureDB.setUserId(controlMeasure.getUserId());
        controlMeasureDB.setMeasureId(controlMeasure.getMeasureId());
        controlMeasureDB.setCreate(controlMeasure.getCreate());
        controlMeasureDB.setPam(controlMeasure.getPam());
        controlMeasureDB.setIsExercising(controlMeasure.getIsExercising());
        controlMeasureDB.setStatus(controlMeasure.getStatus());


        return this.mongoOperations.save(controlMeasureDB);
    }

    @Override
    public ControlMeasure deleteControlMeasure(ControlMeasure controlMeasure) {
        ControlMeasure controlMeasureDB = getControlMeasure(controlMeasure.getId());
        if (controlMeasureDB == null) {
            return null;
        }
        Long id = controlMeasureDB.getId();
        return this.mongoOperations.findAndRemove(new Query(Criteria.where("Id").is(id)), ControlMeasure.class);
    }


    @Override
    public List<ControlMeasure> getControlMeasuresByUser(Long id) {
        Query query = new Query(Criteria.where("userId").is(id));
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        List<ControlMeasure> controlMeasuresDB = this.mongoOperations.find(query,ControlMeasure.class);
        controlMeasuresDB.forEach((control -> {
            if (control != null){
                User user = userClient.getUser(control.getUserId());
                Measure measure = measureClient.getMeasure(control.getMeasureId());
                control.setUser(user);
                control.setMeasure(measure);
            }
        }));

        return controlMeasuresDB;
    }

    @Override
    public ControlMeasure getLastControlMeasureByUser(Long id) {
        List<ControlMeasure> controlMeasuresDB = this.getControlMeasuresByUser(id);
        ControlMeasure controlMeasure = controlMeasuresDB.get(0);
        if (controlMeasure != null){
            User user = userClient.getUser(id);
            Measure measure = measureClient.getMeasure(controlMeasure.getMeasureId());
            controlMeasure.setUser(user);
            controlMeasure.setMeasure(measure);
        }
        return controlMeasure;
    }


}