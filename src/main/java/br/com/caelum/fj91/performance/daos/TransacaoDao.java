package br.com.caelum.fj91.performance.daos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Async;

import br.com.caelum.fj91.performance.models.Transacao;

public interface TransacaoDao extends Repository<Transacao, Long>{
	
	Page<Transacao> findAll(Pageable pageable);
	
	void save(Transacao transacao);
	
	Optional<Transacao> findOne(Long id);
}
