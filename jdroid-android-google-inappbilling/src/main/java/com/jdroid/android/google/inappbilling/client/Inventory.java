/*
 * Copyright (c) 2012 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.jdroid.android.google.inappbilling.client;

import com.jdroid.android.google.inappbilling.InAppBillingAppModule;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;

/**
 * Represents a block of information about in-app items.
 */
public class Inventory {
	
	private Map<String, Product> productsMap = Maps.newLinkedHashMap();
	
	public List<Product> getProducts() {
		return Lists.newArrayList(productsMap.values());
	}
	
	public void addProduct(Product product) {
		productsMap.put(product.getId(), product);
	}
	
	public List<Product> getProductsWaitingToConsume() {
		List<Product> productsToConsume = Lists.newArrayList();
		for (Product product : getProducts()) {
			if (product.isWaitingToConsume()) {
				productsToConsume.add(product);
			}
		}
		return productsToConsume;
	}
	
	public List<Product> getSupportedPurchasedProducts() {
		List<ProductType> supportedProductTypes = Lists.newArrayList();
		supportedProductTypes.addAll(InAppBillingAppModule.get().getInAppBillingContext().getManagedProductTypes());
		supportedProductTypes.addAll(InAppBillingAppModule.get().getInAppBillingContext().getSubscriptionsProductTypes());
		List<Product> purchasedProducts = Lists.newArrayList();
		for (Product each : productsMap.values()) {
			if (each.hasVerifiedPurchase() && supportedProductTypes.contains(each.getProductType())) {
				purchasedProducts.add(each);
			}
		}
		return purchasedProducts;
	}
	
	public Product getProduct(String productId) {
		if (InAppBillingAppModule.get().getInAppBillingContext().isStaticResponsesEnabled()) {
			return getProductByTestProductId(productId);
		} else {
			return productsMap.get(productId);
		}
	}
	
	private Product getProductByTestProductId(String testProductId) {
		for (Product each : productsMap.values()) {
			if (testProductId.equals(each.getProductType().getTestProductId())){
				return each;
			}
		}
		return null;
 	}
 	
 	public List<String> getAllProductIds() {
		List<String> productsIds = Lists.newArrayList();
		for (Product each : productsMap.values()) {
			String productId = InAppBillingAppModule.get().getInAppBillingContext().isStaticResponsesEnabled() ? each.getProductType().getTestProductId() : each.getProductType().getProductId();
			if (!productsIds.contains(productId)) {
				productsIds.add(productId);
			}
		}
		return productsIds;
	}
	
}
