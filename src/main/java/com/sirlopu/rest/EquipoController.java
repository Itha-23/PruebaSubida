package com.sirlopu.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.sirlopu.converter.EquipoConverter;
import com.sirlopu.dto.EquipoDto;
import com.sirlopu.entity.Equipo;
import com.sirlopu.service.EquipoService;
import com.sirlopu.service.PdfService;
import com.sirlopu.util.WrapperResponse;

@RestController
@RequestMapping("/v1/equipos")
public class EquipoController {

    @Autowired
    private EquipoService service;

    @Autowired
    private EquipoConverter converter;

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public ResponseEntity<List<EquipoDto>> findAll(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        List<EquipoDto> equipos = converter.fromEntity(service.findAll(page));

        return new WrapperResponse(true, "success", equipos).createResponse(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EquipoDto> create(@RequestBody EquipoDto equipo) {
        Equipo equipoEntity = converter.fromDTO(equipo);
        EquipoDto registro = converter.fromEntity(service.save(equipoEntity));
        return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoDto> update(@PathVariable("id") int id, @RequestBody EquipoDto equipo) {
        Equipo equipoEntity = converter.fromDTO(equipo);
        equipoEntity.setId(id);
        EquipoDto registro = converter.fromEntity(service.save(equipoEntity));
        return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        service.delete(id);
        return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoDto> findById(@PathVariable("id") int id) {
        EquipoDto registro = converter.fromEntity(service.findById(id));
        return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateReport() {
        List<EquipoDto> equipos = converter.fromEntity(service.findAll());

        LocalDateTime fecha = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHora = fecha.format(formato);

        Context context = new Context();
        context.setVariable("registros", equipos);
        context.setVariable("fecha", fechaHora);

        byte[] pdfBytes = pdfService.createPdf("equipoReport", context);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_equipos.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}