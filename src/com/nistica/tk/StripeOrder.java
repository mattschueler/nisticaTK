package com.nistica.tk;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public class StripeOrder {
	private Map<String, Object> chargeMap = new HashMap<String, Object>(); 
	private Map<String, Object> cardMap = new HashMap<String, Object>();
	boolean cardSet = false;
	
	
	public void setCreditCard(String cardNumber, String personName, int expMonth, int expYear){
		cardMap.put("number", cardNumber);
		cardMap.put("name", personName);
		cardMap.put("exp_month", expMonth);
		cardMap.put("exp_year", expYear);
		
		
		chargeMap.put("card", cardMap);
		cardSet = true;
	}
	
	//remove static later
	public boolean sendPayment(int amount, String desc){
		Stripe.apiKey = "sk_test_4aGesuqZhhrj4LhMeV9d0EiM";
		
		if(!cardSet)
		{
			System.err.println("Credit card not set");
			return false;
		}
		
		chargeMap.put("amount", amount);//amount in cents
		chargeMap.put("currency", "usd");
		chargeMap.put("description", desc);
		
		try{
			Charge charge = Charge.create(chargeMap);
			System.out.println(charge);
		} catch(StripeException e){
			e.printStackTrace();
		}
		
		return true;
	}

}
