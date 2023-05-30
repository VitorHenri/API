package com.Ambitus.AmbitusAPI.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.Ambitus.AmbitusAPI.DTOs.DadosEvento;
import com.Ambitus.AmbitusAPI.entities.Evento;
import com.Ambitus.AmbitusAPI.entities.Usuario;
import com.Ambitus.AmbitusAPI.repositories.EventoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/eventos")
public class EventoController {
	
	@Autowired
	private EventoRepository eventoRepositoy;

	@PostMapping("/cadastro")
	private ResponseEntity<Void> cadastrarEvento(@RequestBody @Valid DadosEvento dados, UriComponentsBuilder uriBuilder) {
		Usuario usuario = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Evento evento = new Evento(dados,usuario);
		eventoRepositoy.save(evento);
		URI uri = URI.create(uriBuilder.path("/evento/{id}").build(evento.getId()).toASCIIString());
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping
	private ResponseEntity<Page<Evento>> listarEventos(Pageable paginacao) {
		Page<Evento> eventos =  eventoRepositoy.findAll(paginacao);
		return ResponseEntity.ok(eventos);
	}
}
