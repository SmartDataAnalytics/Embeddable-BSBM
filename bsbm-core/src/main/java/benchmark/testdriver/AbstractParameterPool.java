package benchmark.testdriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import benchmark.common.TestDriverParams;
import benchmark.generator.Generator;
import benchmark.generator.RandomBucket;
import benchmark.generator.ValueGenerator;
import benchmark.model.ProductType;

public abstract class AbstractParameterPool {
    protected ValueGenerator valueGen;
    protected ValueGenerator valueGen2;
    protected RandomBucket countryGen;
    protected Calendar currentDate;
    protected ProductType[] productTypeLeaves;
    protected Map<String,Integer> wordHash;
    protected Integer[] producerOfProduct;
    protected Integer[] vendorOfOffer;
    protected Integer[] ratingsiteOfReview;
    protected Integer productCount;
    protected Integer reviewCount;
    protected Integer offerCount;
    protected int productTypeCount;
    protected List<Integer> maxProductTypePerLevel;

    protected Integer scalefactor;
    protected String currentDateString;
    protected String[] wordList;

    public abstract Object[] getParametersForQuery(Query query);

    public Integer getScalefactor() {
        return scalefactor;
    }

    protected void init(TestDriverParams params, long seed) {

        Random seedGen = new Random(seed);
        valueGen = new ValueGenerator(seedGen.nextLong());

        countryGen = Generator.createCountryGenerator(seedGen.nextLong());

        valueGen2 = new ValueGenerator(seedGen.nextLong());

        currentDate = params.getToday();
        productTypeLeaves = params.getProductTypeLeaves().toArray(new ProductType[0]);
        wordHash = params.getWordList();
        producerOfProduct = params.getProducerOfProduct().toArray(new Integer[0]);
        vendorOfOffer = params.getVendorOfOffer().toArray(new Integer[0]);
        ratingsiteOfReview = params.getRatingsiteOfReview().toArray(new Integer[0]);
        productCount = params.getProductCount();
        reviewCount = params.getReviewCount();
        offerCount = params.getOfferCount();
        productTypeCount = params.getProductCount();
        maxProductTypePerLevel = params.getMaxProductTypeNrPerLevel();

        currentDateString = formatDateString(params.getToday());
        wordList = params.getWordList().keySet().toArray(new String[0]);
        scalefactor = params.getProducerOfProduct().get(producerOfProduct.length - 1);
    }

    protected void init(File resourceDir, long seed) {

        TestDriverParams params = new TestDriverParams();

        //Read in the Product Type hierarchy from resourceDir/pth.dat
        readProductTypeHierarchy(params, resourceDir);

        //Product-Producer Relationships from resourceDir/pp.dat
        File pp = readProductProducerData(params, resourceDir);

        //Offer-Vendor Relationships from resourceDir/vo.dat
        readOfferAndVendorData(params, resourceDir, pp);

        //Review-Rating Site Relationships from resourceDir/rr.dat
        readReviewSiteData(params, resourceDir);

        //Current date and words of Product labels from resourceDir/cdlw.dat
        readDateAndLabelWords(params, resourceDir);

        init(params, seed);
    }

    private void readDateAndLabelWords(TestDriverParams out, File resourceDir) {
        File cdlw = new File(resourceDir, "cdlw.dat");
        ObjectInputStream currentDateAndLabelWordsInput;
        try {
            currentDateAndLabelWordsInput = new ObjectInputStream(new FileInputStream(cdlw));
            out.setProductCount(currentDateAndLabelWordsInput.readInt());
            out.setReviewCount (currentDateAndLabelWordsInput.readInt());
            out.setOfferCount(currentDateAndLabelWordsInput.readInt());
            out.setToday((Calendar) currentDateAndLabelWordsInput.readObject());


            @SuppressWarnings("unchecked")
            Map<String, Integer> x = (Map<String, Integer>)currentDateAndLabelWordsInput.readObject();
            out.setWordList(x);

        } catch(IOException e) {
            System.err.println("Could not open or process file " + cdlw.getAbsolutePath());
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        catch(ClassNotFoundException e) { System.err.println(e); }
    }

    private void readReviewSiteData(TestDriverParams out, File resourceDir) {
        File rr = new File(resourceDir, "rr.dat");
        ObjectInputStream reviewRatingsiteInput;
        try {
            reviewRatingsiteInput = new ObjectInputStream(new FileInputStream(rr));
            //ratingsiteOfReview = (Integer[]) reviewRatingsiteInput.readObject();
            out.setRatingsiteOfReview(Arrays.asList((Integer[]) reviewRatingsiteInput.readObject()));


        } catch(IOException e) {
            System.err.println("Could not open or process file " + rr.getAbsolutePath());
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        catch(ClassNotFoundException e) { System.err.println(e); }
    }

    private void readOfferAndVendorData(TestDriverParams out, File resourceDir, File pp) {
        File vo = new File(resourceDir, "vo.dat");
        ObjectInputStream offerVendorInput;
        try {
            offerVendorInput = new ObjectInputStream(new FileInputStream(vo));
            out.setVendorOfOffer(Arrays.asList((Integer[]) offerVendorInput.readObject()));
        } catch(IOException e) {
            System.err.println("Could not open or process file " + pp.getAbsolutePath());
            System.err.println(e.getMessage());
            System.exit(-1);
        } catch(ClassNotFoundException e) { System.err.println(e); }
    }

    private File readProductProducerData(TestDriverParams out, File resourceDir) {
        File pp = new File(resourceDir, "pp.dat");
        ObjectInputStream productProducerInput;
        try {
            productProducerInput = new ObjectInputStream(new FileInputStream(pp));
            out.setProducerOfProduct(Arrays.asList((Integer[]) productProducerInput.readObject()));
        } catch(IOException e) {
            System.err.println("Could not open or process file " + pp.getAbsolutePath());
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        catch(ClassNotFoundException e) { System.err.println(e); }
        return pp;
    }

    @SuppressWarnings("unchecked")
    private void readProductTypeHierarchy(TestDriverParams out, File resourceDir) {
        ObjectInputStream productTypeInput;
        File pth = new File(resourceDir, "pth.dat");
        try {
            productTypeInput = new ObjectInputStream(new FileInputStream(pth));
            out.setProductTypeLeaves(Arrays.asList((ProductType[]) productTypeInput.readObject()));
            out.setProductTypeCount(productTypeInput.readInt());
            out.setMaxProductTypeNrPerLevel((List<Integer>) productTypeInput.readObject());
        } catch(IOException e) {
            System.err.println("Could not open or process file " + pth.getAbsolutePath());
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        catch(ClassNotFoundException e) { System.err.println(e); }
    }

    /**
     * Format the date string DBMS dependent
     * @param date The object to transform into a string representation
     * @return formatted String
     */
    abstract protected String formatDateString(Calendar date);

    /*
     * Get a random Product URI
     */
    protected Integer getRandomProductNr() {
        Integer productNr = valueGen.randomInt(1, productCount);

        return productNr;
    }

    /*
     * Returns the ProducerNr of given Product Nr.
     */
    protected Integer getProducerOfProduct(Integer productNr) {
        Integer producerNr = Arrays.binarySearch(producerOfProduct, productNr);
        if(producerNr<0)
            producerNr = - producerNr - 1;

        return producerNr;
    }

    /*
     * Returns the ProducerNr of given Product Nr.
     */
    protected Integer getVendorOfOffer(Integer offerNr) {
        Integer vendorNr = Arrays.binarySearch(vendorOfOffer, offerNr);
        if(vendorNr<0)
            vendorNr = - vendorNr - 1;

        return vendorNr;
    }

    /*
     * Returns the Rating Site Nr of given Review Nr
     */
    protected Integer getRatingsiteOfReviewer(Integer reviewNr) {
        Integer ratingSiteNr = Arrays.binarySearch(ratingsiteOfReview, reviewNr);
        if(ratingSiteNr<0)
            ratingSiteNr = - ratingSiteNr - 1;

        return ratingSiteNr;
    }

    /*
     * Returns a random number between 1-500
     */
    protected Integer getProductPropertyNumeric() {
        return valueGen.randomInt(1, 500);
    }

    /*
     * Get random word from word list
     */
    protected String getRandomWord() {
        Integer index = valueGen.randomInt(0, wordList.length-1);

        return wordList[index];
    }
}
