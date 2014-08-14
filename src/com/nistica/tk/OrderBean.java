package com.nistica.tk;

public class OrderBean {

	private String firstName;
	private String lastName;
	private String foodNum;
	private String foodName;
	private String meatType;
	private String spiceNum;
	private String qty;
	private String comments;
	private String price;

	public OrderBean() {
	}

	public OrderBean(final String firstName, final String lastName, final String foodNum, final String foodName, 
			final String meatType, final String spiceNum, final String qty, final String comments, final String price) {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setFoodNum(foodNum);
		this.setFoodName(foodName);
		this.setMeatType(meatType);
		this.setSpiceNum(spiceNum);
		this.setQty(qty);
		this.setComments(comments);
		this.setPrice(price);
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFoodNum() {
		return foodNum;
	}
	public void setFoodNum(String foodNum) {
		this.foodNum = foodNum;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getMeatType() {
		return meatType;
	}
	public void setMeatType(String meatType) {
		this.meatType = meatType;
	}
	public String getSpiceNum() {
		return spiceNum;
	}
	public void setSpiceNum(String spiceNum) {
		this.spiceNum = spiceNum;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return String.format("PersonBean [firstName=%s,lastName=%s,foodnum=%s,foodname=%s,unitprice=%s,meattype=%s,spicenum=%s,qty=%s,comments=%s,totalprice=%s]",
				firstName, lastName, foodNum, foodName, meatType, spiceNum, qty, comments, price);
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof OrderBean)) {
			return false;
		}
		OrderBean other = (OrderBean) obj;
		if(firstName == null) {
			if(other.firstName != null) {
				return false;
			}
		} else if(!firstName.equals(other.firstName)) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		if(foodNum == null) {
			if(other.foodNum != null) {
				return false;
			}
		} else if(!(foodNum == other.foodNum)) {
			return false;
		}
		if(foodName == null) {
			if(other.foodName != null) {
				return false;
			}
		} else if(!foodName.equals(other.foodName)) {
			return false;
		}
		if (meatType == null) {
			if (other.meatType != null) {
				return false;
			}
		} else if (!meatType.equals(other.meatType)) {
			return false;
		}
		if (spiceNum == null) {
			if (other.spiceNum != null) {
				return false;
			}
		} else if (!spiceNum.equals(other.spiceNum)) {
			return false;
		}
		if (qty == null) {
			if (other.qty != null) {
				return false;
			}
		} else if (!qty.equals(other.qty)) {
			return false;
		}
		if (comments == null) {
			if (other.comments != null) {
				return false;
			}
		} else if (!comments.equals(other.comments)) {
			return false;
		}
		if (price == null) {
			if (other.price != null) {
				return false;
			}
		} else if (!price.equals(other.price)) {
			return false;
		}
		return true;
	}
}