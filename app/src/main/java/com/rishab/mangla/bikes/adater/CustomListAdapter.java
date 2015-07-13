package com.rishab.mangla.bikes.adater;

import com.rishab.mangla.bikes.R;
import com.rishab.mangla.bikes.app.AppController;
import com.rishab.mangla.bikes.model.Bike;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Bike> bikeItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Bike> bikeItems) {
		this.activity = activity;
		this.bikeItems = bikeItems;
	}

	@Override
	public int getCount() {
		return bikeItems.size();
	}

	@Override
	public Object getItem(int location) {
		return bikeItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView capacity = (TextView) convertView.findViewById(R.id.capacity);
		TextView year = (TextView) convertView.findViewById(R.id.price);

		// getting bike data for the row
		Bike m = bikeItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		
		// title
		title.setText(m.getTitle());
		
		// capacity
		capacity.setText(m.getCapacity());

		// price starts
		year.setText("Rs. " + m.getPrice() + " onwards");

		return convertView;
	}

}