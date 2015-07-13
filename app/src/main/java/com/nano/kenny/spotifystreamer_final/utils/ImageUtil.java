package com.nano.kenny.spotifystreamer_final.utils;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Kenny on 7/11/2015.
 */
public class ImageUtil {
    public static final int MIN_IMAGE_SEARCH_WIDTH = 200;
    public static final int MIN_IMAGE_SEARCH_HEIGHT = 200;
    public static final int MAX_IMAGE_SEARCH_WIDTH = 500;
    public static final int MAX_IMAGE_SEARCH_HEIGHT = 500;

    // retrieve first image url unless there is a smaller size
    // reason is image sizes, determined by the url strings in list,
    // start from largest available size to smallest
    public static String searchForImageURLGivenSizeConstraints(List<Image> images,
                                                         int minWidth, int minHeight,
                                                         int maxWidth, int maxHeight) {
        String imageURL = images.get(0).url;

        for (Image image : images) {
            boolean imgGreaterThanEqualMinSize = image.width >= minWidth
                    && image.height >= minHeight;
            boolean imgLessThanEqualMaxSize = image.width <= maxWidth
                    && image.height <= maxHeight;

            if (imgGreaterThanEqualMinSize && imgLessThanEqualMaxSize) {
                imageURL = image.url;
            }
        }

        return imageURL;
    }
}
