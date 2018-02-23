package br.com.caelum.fj91.performance.services;

 
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class LogService {

	
	private final Logger logger = Logger.getGlobal();
	
	public void info(String info) {
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		logger.info(info);
	}
	
	
}
