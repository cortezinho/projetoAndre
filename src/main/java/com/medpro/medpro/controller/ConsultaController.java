package com.medpro.medpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medpro.medpro.model.dto.DadosAgendamentoConsulta;
import com.medpro.medpro.model.dto.DadosCancelamentoConsulta;
import com.medpro.medpro.model.dto.DadosListagemConsulta;
import com.medpro.medpro.model.entity.Consulta;
import com.medpro.medpro.repository.ConsultaRepository;
import com.medpro.medpro.service.AgendaDeConsultas;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("consultas")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultas agenda;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity detalhar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemConsulta>> listar(
            @PageableDefault(size = 10, sort = { "data" }) Pageable paginacao) {
        // Passo 1: Busca a página de Entidades (Consulta)
        Page<Consulta> consultas = consultaRepository.findAll(paginacao);

        // Passo 2: Converte para página de DTOs (DadosListagemConsulta)
        Page<DadosListagemConsulta> page = consultas.map(DadosListagemConsulta::new);

        // Passo 3: Retorna a resposta tipada corretamente
        return ResponseEntity.ok(page);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}