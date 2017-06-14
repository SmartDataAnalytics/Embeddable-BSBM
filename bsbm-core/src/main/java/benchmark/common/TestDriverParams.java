package benchmark.common;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import benchmark.model.ProductType;

public class TestDriverParams {
    protected Calendar today;

    protected int productCount;
    protected int offerCount;
    protected int reviewCount;
    protected int productTypeCount;
    //static List<Integer> maxProductTypeNrPerLevel;

    protected List<Integer> maxProductTypeNrPerLevel;

    protected List<ProductType> productTypeLeaves;
    protected List<ProductType> productTypeNodes;
    protected List<Integer> producerOfProduct;//saves producer-product relationship
    protected List<Integer> vendorOfOffer;//saves vendor-offer relationship
    protected List<Integer> ratingsiteOfReview;//saves review-ratingSite relationship
    protected Map<String,Integer> wordList;//Word list for the Test driver

    public Calendar getToday() {
        return today;
    }

    public TestDriverParams setToday(Calendar today) {
        this.today = today;
        return this;
    }

    public int getProductCount() {
        return productCount;
    }

    public TestDriverParams setProductCount(int productCount) {
        this.productCount = productCount;
        return this;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public TestDriverParams setOfferCount(int offerCount) {
        this.offerCount = offerCount;
        return this;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public TestDriverParams setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
        return this;
    }

    public int getProductTypeCount() {
        return productTypeCount;
    }

    public TestDriverParams setProductTypeCount(int productTypeCount) {
        this.productTypeCount = productTypeCount;
        return this;
    }

    public List<Integer> getMaxProductTypeNrPerLevel() {
        return maxProductTypeNrPerLevel;
    }

    public TestDriverParams setMaxProductTypeNrPerLevel(List<Integer> maxProductTypeNrPerLevel) {
        this.maxProductTypeNrPerLevel = maxProductTypeNrPerLevel;
        return this;
    }

    public List<ProductType> getProductTypeLeaves() {
        return productTypeLeaves;
    }

    public TestDriverParams setProductTypeLeaves(List<ProductType> productTypeLeaves) {
        this.productTypeLeaves = productTypeLeaves;
        return this;
    }

    public List<ProductType> getProductTypeNodes() {
        return productTypeNodes;
    }

    public TestDriverParams setProductTypeNodes(List<ProductType> productTypeNodes) {
        this.productTypeNodes = productTypeNodes;
        return this;
    }

    public List<Integer> getProducerOfProduct() {
        return producerOfProduct;
    }

    public TestDriverParams setProducerOfProduct(List<Integer> producerOfProduct) {
        this.producerOfProduct = producerOfProduct;
        return this;
    }

    public List<Integer> getVendorOfOffer() {
        return vendorOfOffer;
    }

    public TestDriverParams setVendorOfOffer(List<Integer> vendorOfOffer) {
        this.vendorOfOffer = vendorOfOffer;
        return this;
    }

    public List<Integer> getRatingsiteOfReview() {
        return ratingsiteOfReview;
    }

    public TestDriverParams setRatingsiteOfReview(List<Integer> ratingsiteOfReview) {
        this.ratingsiteOfReview = ratingsiteOfReview;
        return this;
    }

    public Map<String, Integer> getWordList() {
        return wordList;
    }

    public TestDriverParams setWordList(Map<String, Integer> wordList) {
        this.wordList = wordList;
        return this;
    }


}
