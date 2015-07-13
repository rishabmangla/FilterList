package com.rishab.mangla.bikes.model;

import java.util.ArrayList;

public class Bike {
	private String title, thumbnailUrl;
	private String price;
	private String capacity;

	public Bike() {
	}

	public Bike(String name, String thumbnailUrl, String year, String rating,
				ArrayList<String> genre) {
		this.title = name;
		this.thumbnailUrl = thumbnailUrl;
		this.price = year;
		this.capacity = rating;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

}
