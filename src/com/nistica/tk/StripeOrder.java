package com.nistica.tk;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

public class StripeOrder {
	private Map<String, Object> chargeMap = new HashMap<String, Object>(); 
	private Map<String, Object> cardMap = new HashMap<String, Object>();
	private Map<String, Object> customerMap = new HashMap<String, Object>();
	boolean cardSet = false;

	public StripeOrder(){
		Stripe.apiKey = "sk_test_4aGesuqZhhrj4LhMeV9d0EiM";
	}
	
	
	public boolean setCreditCard(String cardNumber, String personName, int expMonth, int expYear){
		cardMap.put("number", cardNumber);
		cardMap.put("name", personName);
		cardMap.put("exp_month", expMonth);
		cardMap.put("exp_year", expYear);
		cardMap.put("cvc", 233333);
		
		chargeMap.put("card", cardMap);
		cardSet = true;
		
		customerMap.put("card", cardMap);
		customerMap.put("description", personName);
		try {
			Customer customer = Customer.create(customerMap);
			System.out.println("Customer id: " + customer.getId());
		} catch (AuthenticationException e) {
			System.out.println("AUTH EXCEPTION ON CUSTOMER");
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			System.out.println("CARD EXCEPTION");
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	//remove static later
	public boolean sendPayment(int amount, String desc){
		
		
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
