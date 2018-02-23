package br.com.caelum.fj91.performance.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.caelum.fj91.performance.daos.TransacaoDao;
import br.com.caelum.fj91.performance.models.Transacao;
import br.com.caelum.fj91.performance.services.LogService;

@Component
public class TransacaoListener {
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private TransacaoDao transacaoDao;
	
	@CacheEvict(allEntries=true, value="transacoes")
	@JmsListener(destination="fila.processamento.de.transacoes")
	public void processa(Transacao transacao) throws InterruptedException {
		logService.info("Salvando alguma coisa");
		
		Thread.sleep(3000);
		
		transacaoDao.save(transacao);
	}
	
}
