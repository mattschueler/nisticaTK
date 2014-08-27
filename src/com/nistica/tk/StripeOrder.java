package com.nistica.tk;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		Stripe.apiKey = null;
		try {
			
			InputStream in = MenuParser.class.getResourceAsStream("/other/apikey.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			Stripe.apiKey = reader.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public String setCreditCard(String cardNumber, String personName, int expMonth, int expYear, int cvc){
		cardMap.put("number", cardNumber);
		cardMap.put("name", personName);
		cardMap.put("exp_month", expMonth);
		cardMap.put("exp_year", expYear);
		cardMap.put("cvc", cvc);
		
		chargeMap.put("card", cardMap);
		cardSet = true;
		
		customerMap.put("card", cardMap);
		customerMap.put("description", personName);
		try {
			Customer customer = Customer.create(customerMap);
			return "Card Valid";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return "Authentication Error";
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			return "Invalid Request";
		} catch (APIConnectionException e) {
			e.printStackTrace();
			return "API Connection Error: "+e.getMessage();
		} catch (CardException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (APIException e) {
			e.printStackTrace();
		}
		
		return "Unknown error";
		
	}
	
	//remove static later
	@SuppressWarnings("unused")
	public boolean sendPayment(double amount, double tip, String desc){		
		if(!cardSet)
		{
			System.err.println("Credit card not set");
			return false;
		}
		
		amount*=1.07;//Adds 7% sales tax 
		amount+=tip;
		//This finds how much more to charge the customer 
		//so that when stripe takes their cut of 2.9% of $.30 on each transaction,
		//the leftover will still equal what the customer had to pay with sales tax. 
		//To figure out how this works, do the math like I did.
		double transactionOffset = (amount*(.029)+.3)/(.971);
		amount+=transactionOffset;
		//turns it into cents bc stripe only accepts in cents
		amount*=100;
		
		chargeMap.put("amount", Math.round(amount));//amount in cents
		chargeMap.put("currency", "usd");
		chargeMap.put("description", desc);
		
		try{
			Charge charge = Charge.create(chargeMap);
		} catch(StripeException e){
			e.printStackTrace();
		}
		
		return true;
	}

}
