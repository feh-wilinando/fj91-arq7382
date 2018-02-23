package br.com.caelum.fj91.performance.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.fj91.performance.daos.TransacaoDao;
import br.com.caelum.fj91.performance.models.Transacao;
import br.com.caelum.fj91.performance.services.LogService;

@RestController
@RequestMapping("/transacoes")
public class TransacoesController {
	
	@Autowired
	private TransacaoDao transacaoDao;
	
	@Autowired
	private LogService logService;
	
	
	@Autowired
	private JmsTemplate jms;
	
	private static int pageSize = 15;

	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, 
	consumes=MediaType.APPLICATION_JSON_VALUE, 
	method=RequestMethod.POST)	
	public ResponseEntity<?> save(@RequestBody Transacao transacao){
		
		
		jms.send("fila.processamento.de.transacoes",
				(session) -> session.createObjectMessage(transacao)); 
		
		return ResponseEntity.accepted().build();
	}
	
	
	
	@Cacheable("transacoes")
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, 
					method=RequestMethod.GET)
	public Page<Transacao> lista(@RequestParam("page") 
									Optional<Integer> optionalPage){
		
		
		logService.info("Listando todas as transações");
		
		return transacaoDao
				.findAll(
						new PageRequest(optionalPage.orElse(0), pageSize));		
	}
	
	
/*
 * curl -X POST 
 * 		-d '{"data":1170900000000,"tipoDeTransacao":"ENTRADA","valor":578.36}' 
 * 		-H "Content-type: application/json"
 * 		-i 
 * 		http://localhost:8080/transacoes 
 * 
 * curl -i http://localhost:8080/transacoes/1009009
 */	
	
	@RequestMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOne(@PathVariable Long id){
		
		logService.info("Procurando por uma transação com o id: " + id);
		
		Optional<Transacao> optionalTranasacao = transacaoDao.findOne(id);
		
		if (optionalTranasacao.isPresent()) {
			return ResponseEntity.ok(optionalTranasacao.get());
		}
		
		return ResponseEntity.noContent().build();
	}
	
}
