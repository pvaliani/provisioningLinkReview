/**
 *
 *
 *
 * SOLUTION/RESPONSE:
 *
 * The expectation is that we should have exactly one product provisioning user link OR we have null. In the case
 * where there are many links we can identift that there is a problem with the original offer object which has
 * been passed into the getProductProvisioningLink() method. In terms of how well this is met, the MCS links provisioning key
 * has to be associated with the provisioning link otherwise the method will return another collection which will not be
 * the correct corresponding link. The fact that we have null objects to deal with may suggest that code at a higher level has not been
 * cleaned up sufficiently, though this is just speculation.
 *
 *
 */
/**
 * Gets the product provisioning link from the offer object
 *
 * @param offer offer whose provisioning link should be returned
 * @return the offer's provisioning link or null if one was not found
 */

@SuppressWarnings({
        "squid:S134" // allow deeply nested "if"s; requiring sub-methods here reduces clarity
})

public static String getProductProvisioningLink(AOSOffer offer) { // the method retrieves the product provisioning link via the offer object which is of type AOSoffer

    String link = null; //  Declare a null product provisioning link of type String

    if (offer != null) { //  if the offer object is not null then create a linkCollection hashMap of type Links which in turn is a List of type String
        Links linkCollections = OfferToProductUtils.getLinks(offer); // get the links associated with the offer object and store them in linkCollections

        if (linkCollections != null) { //if linkCollections contains links retrieved from the offer object
                                        // provisioned user links are extracted from linkCollections by the hashMap MCS Provisioning Key and stored in a
                                        // List of type Link i.e they are ordered
            List<Link> provisionedUserLinks = linkCollections.get(MCS_LINKS_PROVISIONING_KEY);

            if (CollectionUtils.isNotEmpty(provisionedUserLinks)) { // check if provisionedUserLinks is not empty without being concerned about a null list
                if (provisionedUserLinks.size() == 1) { // if we have exactly one provisionedUserLink step through (iterator) and this is the foundLink
                    Link foundLink = provisionedUserLinks.iterator().next();
                    if (foundLink != null) { // if the foundLink is not a null then this is the link to be returned
                        link = foundLink.getHref();
                    }

                    if (link == null) { // if the link is null warn that the key has no corresponding link
                        LOG.warn("Product " + MCS_LINKS_PROVISIONING_KEY + " link has no href sub-element");
                    }
                }

                else { // otherwise, in the case many links are present return a msg stating that this product has the incorrect number of links, returning the offerId and the number
                        //of links that have been found for that product. I.e there is perhaps an issue with the offer object passed in
                    LOG.warn(
                            "Product has the wrong number of " + MCS_LINKS_PROVISIONING_KEY
                                    + " links; offerId=%s count=%d",
                            offer.getOfferId(), provisionedUserLinks.size());
                }
            }
        }
    }
    return link; // return the provisioning link which has been retrieved for the product
}


