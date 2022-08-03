package proyecto.ucuenca.control.client;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import proyecto.ucuenca.control.model.Measure;

@FeignClient(name="measure-service", path="/measures")
public interface MeasureClient {
    @RequestLine("GET /lastMeasure")
    public Measure findLastMeasure();

    @RequestLine("GET /{id}")
    public Measure getMeasure(@Param Long id);
}