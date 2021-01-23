package pw.keller.api.entity.url;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.keller.api.common.util.UrlUtil;
import pw.keller.api.config.CustomDynamicSchedule;
import pw.keller.api.exception.EntityNotFoundException;
import pw.keller.api.implement.DTOConverter;
import pw.keller.api.scheduler.CheckUrlTask;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/url")
@CrossOrigin(origins = "*")
public class UrlController implements DTOConverter<Url, UrlDTO> {

    private final UrlService service;
    private final ModelMapper modelMapper;
    private final CustomDynamicSchedule dynamicSchedule;

    @Autowired
    public UrlController(UrlService service, ModelMapper modelMapper, CustomDynamicSchedule dynamicSchedule) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.dynamicSchedule = dynamicSchedule;
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<UrlDTO> getSingleUrl(@PathVariable long id) {
        Optional<Url> url = service.getById(id);
        if (url.isPresent()) {
            return ResponseEntity.ok(convertToDto(url.get()));
        } else {
            throw new EntityNotFoundException("Url not found");
        }
    }

    @GetMapping(value = "/check")
    public ResponseEntity<UrlDTO> updateInterval() {
        for (Url url : service.getAll()){
            url.setStatusCode(UrlUtil.getUrlStatusCode(url.getUrl()));
            service.update(url);
        }
        return ResponseEntity.status(200).build();
    }

    @GetMapping(value = "/interval/{milliseconds}")
    public ResponseEntity<String> updateInterval(@PathVariable long milliseconds) {
        dynamicSchedule.delay(milliseconds);
        return ResponseEntity.status(200).build();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<UrlDTO> createUrl(@Valid @RequestBody UrlDTO fresh) {
        fresh.setId(-1L);
        Url converted = convertToEntity(fresh);
        converted.setStatusCode(UrlUtil.getUrlStatusCode(converted.getUrl()));
        Url savedUrl = service.save(converted);
        return ResponseEntity.ok().body(convertToDto(savedUrl));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<UrlDTO>> getAllUrls() {
        List<Url> urls = service.getAll();
        List<UrlDTO> urlDTOs = Arrays.asList(modelMapper.map(urls, UrlDTO[].class));
        return ResponseEntity.ok(urlDTOs);
    }

   @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable long id) {
        Optional<Url> employee = service.getById(id);
        employee.ifPresentOrElse(service::delete, () -> {
            throw new EntityNotFoundException("Url not found");
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/get/interval")
    public long getInterval() {
        return dynamicSchedule.delayInterval;
    }

    @Override
    public UrlDTO convertToDto(Url post) {
        return modelMapper.map(post, UrlDTO.class);
    }

    @Override
    public Url convertToEntity(UrlDTO postDto) {
        Url post = modelMapper.map(postDto, Url.class);
        if (postDto.getId() != null && postDto.getId() != -1) {
            Optional<Url> oldPost = service.getById(postDto.getId());
            if (oldPost.isPresent()) {
                Url val = oldPost.get();
                val.setUrl(postDto.getUrl());
                val.setStatusCode(postDto.getStatusCode());
                return val;
            }
        }
        return post;
    }
}
