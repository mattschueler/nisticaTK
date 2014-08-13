package com.nistica.tk;

public class StripeTester {
	public static void main(String[] args){
		StripeOrder stripeOrder = new StripeOrder();
		stripeOrder.setCreditCard("4242424242424242", "Person Name", 12, 2020);
		stripeOrder.sendPayment(100, "<name> is ordering <food>");
		}
}
