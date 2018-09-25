package com.ezest.microservice.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ezest.microservice.currencyconversionservice.bean.CurrencyConversionBean;
@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	@GetMapping("/currency-converter/{from}/to/{to}/quntity/{quantity}")
	
	public CurrencyConversionBean convertCurrency(@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		Map<String,String> uriVariable = new HashMap<>();
		uriVariable.put("from",from);
		uriVariable.put("to",to);
		ResponseEntity<CurrencyConversionBean> reponseEntity=
		new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/{from}/to/{to}", CurrencyConversionBean.class,
		uriVariable);
		
		CurrencyConversionBean response=reponseEntity.getBody();
		
		return new CurrencyConversionBean(response.getId(),
		from,to,response.getConversionMultiple(),quantity,
		quantity.multiply(response.getConversionMultiple()),
				 response.getPort());
			
	}

	@GetMapping("/currency-converter-feign/{from}/to/{to}/quntity/{quantity}")
	
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
CurrencyConversionBean response =proxy.retrieveExchangeValue(from, to);	
		
		
		return new CurrencyConversionBean(response.getId(),
		from,to,response.getConversionMultiple(),quantity,
		quantity.multiply(response.getConversionMultiple()),
				 response.getPort());
			
	}

}
