package com.example.android.galleryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemHelper {

    private Context context;
    private OnCompleteListener listener;
    private String rectangularImageURL ="https://picsum.photos/%d/%d"
                  , squareImageURL = "https://picsum.photos/%d";

    private Bitmap bitmap;
    private Set<Integer> colors;

    //Triggers------------------------------------------------------------------------------------

    //For Rectangular Image
    void fetchData(int x, int y, Context context, OnCompleteListener listener){
        this.context = context;
        //... fetch here & when done,
        //Call listener.onFetched(image,colors,labels);
        this.listener = listener;
        fetchImage(
                String.format(rectangularImageURL,x,y)
        );
    }

    //For Square Image
    void fetchData(int x, Context context, OnCompleteListener listener){
        this.context = context;
        //... fetch here & when done,
        //Call listener.onFetched(image,colors,labels);
        this.listener = listener;
        fetchImage(
                String.format(squareImageURL,x)
        );
    }

    //ImageFetcher------------------------------------------------------------------------------------

    void fetchImage(String url){
        Glide.with(context)
                .asBitmap()
                .load(url)
                  .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap = resource;
                        extractpaletteFromBtimap();
                    }

                      @Override
                      public void onLoadFailed(@Nullable Drawable errorDrawable) {
                          super.onLoadFailed(errorDrawable);
                          listener.onError("Image Load Failed!");
                      }

                      @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                  });
    }

    //PaletteHelper------------------------------------------------------------------------------------

    private void extractpaletteFromBtimap() {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                // Use generated instance
               colors = getColorsFromPalette(p);

               labelImage();
            }
        });

    }

    //LabelHelper------------------------------------------------------------------------------------

    private void labelImage() {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        // Task completed successfully
                        List<String> strings = new ArrayList<>();
                        for(ImageLabel label : labels) {
                            strings.add(label.getText());
                        }
                            listener.onFetched(bitmap,colors,strings);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                       listener.onError(e.toString());
                    }
                });
    }

    private Set<Integer> getColorsFromPalette(Palette p) {
        Set<Integer> colors = new HashSet<>();

        colors.add(p.getVibrantColor(0));
        colors.add(p.getLightVibrantColor(0));
        colors.add(p.getDarkVibrantColor(0));

        colors.add(p.getMutedColor(0));
        colors.add(p.getLightMutedColor(0));
        colors.add(p.getDarkMutedColor(0));

        colors.add(p.getVibrantColor(0));

        colors.remove(0);

        return colors;
    }

    //Listeners------------------------------------------------------------------------------------

    interface OnCompleteListener{
        void onFetched(Bitmap image, Set<Integer> colors, List<String> labels);
        void onError(String error);
        }

}
