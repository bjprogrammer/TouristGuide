package bjasuja.syr.edu.touristguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import bjasuja.syr.edu.touristguide.util.Constants;
import bjasuja.syr.edu.touristguide.util.IabHelper;
import bjasuja.syr.edu.touristguide.util.IabResult;
import bjasuja.syr.edu.touristguide.util.Inventory;
import bjasuja.syr.edu.touristguide.util.Purchase;

public class MyBillin {
    static final String TAG = "GatePuzzle";
    static final String SKU_REMOVE_ADS = "android.test.purchased";
    static final int RC_REQUEST = 10111;

    Activity activity;
    IabHelper mHelper;

    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnIjNm93cFGqMSIB/OgKoG5OMRk3LlqMskAScqkKYQDjTyqDPP8p209JypC2rs8R9pUvshV2dcSes4nzfJ61v4KhhTZwh4IzBGlj01ffblWL1SfXoRwEcCkL3v9XR53N//BbST6lt/szqrCYx+TTyPvWD8+WEKnDzsp7HLlVH5Z+AYgWzIRURTPeRhdc0TP22AyY7DNR45np2lr0gH/0SyEVez7LSVG2BNzCWb4/iZgXfjtmiqkk7hWMXQ1K3GAWjCnEtKfetnKVD8F8yiBj1Flyqqz86iNXSqMPyColuo6C225HrJukv+6ac0FtYVUi+EFPuq6wwX6liQxZJDvMlawIDAQAB";
    Boolean isAdsDisabled = false;
    String payload = "ANY_PAYLOAD_STRING";

    public MyBillin(Activity launcher) {
        this.activity = launcher;
    }

    public void onCreate() {

        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(activity, base64EncodedPublicKey);


        mHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {

                    return;
                }


                if (mHelper == null)
                    return;


                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            if (mHelper == null)
                return;

            // Is it a failure?
            if (result.isFailure()) {
                // complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            // Do we have the premium upgrade?
            Purchase removeAdsPurchase = inventory.getPurchase(SKU_REMOVE_ADS);
            Constants.isAdsDisabled = (removeAdsPurchase != null && verifyDeveloperPayload(removeAdsPurchase));
            removeAds();

            // setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // User clicked the "Remove Ads" button.
    public void purchaseRemoveAds() {

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                mHelper.launchPurchaseFlow(activity, SKU_REMOVE_ADS,
                        RC_REQUEST, mPurchaseFinishedListener, payload);

            }
        });
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
                + data);
        if (mHelper == null)
            return true;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            return false;
        } else {

            Log.d(TAG, "onActivityResult handled by IABUtil.");

            return true;
        }

    }


    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();


        return true;
    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: "
                    + purchase);


            if (mHelper == null)
                return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_REMOVE_ADS)) {
                MainActivity.bottomT.setVisibility(View.VISIBLE);
                removeAds();

            }
        }
    };

     void removeAds() {
        isAdsDisabled = true;

    }

    // We're being destroyed. It's important to dispose of the helper here!

    public void onDestroy() {
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(final String message) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                AlertDialog.Builder bld = new AlertDialog.Builder(activity);
                bld.setMessage(message);
                bld.setNeutralButton("OK", null);
                Log.d(TAG, "Showing alert dialog: " + message);
                bld.create().show();
            }
        });
    }

}