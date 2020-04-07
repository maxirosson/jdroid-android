package com.jdroid.android.google.inappbilling.ui;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.google.inappbilling.R;
import com.jdroid.android.google.inappbilling.client.Product;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.utils.LocalizationUtils;

public abstract class ProductViewType extends RecyclerViewType<Product, ProductViewType.ProductHolder> {

	@Override
	protected Integer getLayoutResourceId() {
		return R.layout.jdroid_product_item;
	}

	@Override
	protected Class<Product> getItemClass() {
		return Product.class;
	}

	@Override
	public RecyclerView.ViewHolder createViewHolderFromView(View view) {
		ProductHolder holder = new ProductHolder(view);
		holder.icon = findView(view, R.id.itemIcon);
		holder.title = findView(view, R.id.itemTitle);
		holder.description = findView(view, R.id.itemDescription);
		holder.price = findView(view, R.id.itemPrice);
		return holder;
	}

	@Override
	public void fillHolderFromItem(final Product product, ProductHolder holder) {
		if (product.getProductType().getIconId() != null) {
			holder.icon.setImageResource(product.getProductType().getIconId());
			holder.icon.setVisibility(View.VISIBLE);
		} else {
			holder.icon.setVisibility(View.GONE);
		}
		holder.title.setText(product.getTitle());
		holder.description.setText(product.getDescription());
		holder.price.setText(product.isAvailableToPurchase() ? product.getFormattedPrice() : LocalizationUtils.INSTANCE.getString(
			R.string.jdroid_purchased));
		holder.price.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onPriceClick(product);
			}
		});
		holder.price.setFocusable(true);
		holder.price.setEnabled(product.isAvailableToPurchase());
	}

	protected abstract void onPriceClick(Product product);

	public static class ProductHolder extends RecyclerView.ViewHolder {

		protected ImageView icon;
		protected TextView title;
		protected TextView description;
		protected TextView price;

		public ProductHolder(View itemView) {
			super(itemView);
		}
	}

}