package com.example.android.galleryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.android.galleryapp.databinding.ActivityGalleryBinding;
import com.example.android.galleryapp.databinding.ChipColorBinding;
import com.example.android.galleryapp.databinding.ChipLabelBinding;
import com.example.android.galleryapp.databinding.DialogAddImageBinding;
import com.example.android.galleryapp.databinding.ItemCardBinding;
import com.example.android.galleryapp.models.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.List;
import java.util.Set;

public class GalleryActivity extends AppCompatActivity {

    ActivityGalleryBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

//        new ItemHelper()
//                .fetchData(1920, 1080, this, new ItemHelper.OnCompleteListener() {
//                    @Override
//                    public void onFetched(Bitmap image, Set<Integer> colors, List<String> labels) {
//                        b.loader.setVisibility(View.GONE);
//                        b.imageView.setImageBitmap(image);
//                        inflateColorChips(colors);
//                        inflateLabelChips(labels);
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        b.loader.setVisibility(View.GONE);
//                        new MaterialAlertDialogBuilder(GalleryActivity.this)
//                                .setTitle("Error")
//                                .setMessage(error)
//                                .show();
//                    }
//                });
        // testColorChips();
        //testlabelChips();
//        testDailog();

        //showSimpleDialog();
        //showCustomViewDialog();
        //showCustomThemeDialog();
//        loadImage();
    }



    //Label Chips

//    private void testlabelChips() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wow);
//
//        InputImage image = InputImage.fromBitmap(bitmap, 0);
//        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
//        labeler.process(image)
//                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
//                    @Override
//                    public void onSuccess(List<ImageLabel> labels) {
//                        // Task completed successfully
//                        inflateLabelChips(labels);
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Task failed with an exception
//                        new MaterialAlertDialogBuilder(MainActivity.this)
//                                .setTitle("Error")
//                                .setMessage(e.toString())
//                                .show();
//                    }
//                });
//    }

//    private void inflateLabelChips(List<String> labels) {
//        for (String label : labels) {
//            ChipLabelBinding binding = ChipLabelBinding.inflate(getLayoutInflater());
//            binding.getRoot().setText(label);
//            b.labelChips.addView(binding.getRoot());
//        }
//    }


    //Color Chips

//    private void testColorChips() {
//        /*Drawable folder m jo img hai usko bitmap m convert krne ka method*/
//       Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wow);
//
//            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                public void onGenerated(Palette p) {
//                    inflateColorChips(p);
//                }
//            });
//    }

//    private void inflateColorChips(Set<Integer> colors) {
//        for (int color : colors) {
//            ChipColorBinding binding = ChipColorBinding.inflate(getLayoutInflater());
//            binding.getRoot().setChipBackgroundColor(ColorStateList.valueOf(color));
//            b.colorChips.addView(binding.getRoot());
//        }
//    }

    /*This method will give set of integer(colors)*/

//    private Set<Integer> getColorsFromPalette(Palette p) {
//        Set<Integer> colors = new HashSet<>();
//
//        colors.add(p.getVibrantColor(0));
//        colors.add(p.getLightVibrantColor(0));
//        colors.add(p.getDarkVibrantColor(0));
//
//        colors.add(p.getMutedColor(0));
//        colors.add(p.getLightMutedColor(0));
//        colors.add(p.getDarkMutedColor(0));
//
//        colors.add(p.getVibrantColor(0));
//
//        colors.remove(0);
//
//        return colors;
//    }


    private void testDailog() {
        DialogAddImageBinding binding = DialogAddImageBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(binding.getRoot())
                .show();

        binding.fetchImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.enterDimenRoot.setVisibility(View.GONE);
                binding.linearProgressIndicatorRoot.setVisibility(View.VISIBLE);

                //Use Handler for delay in Android
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.linearProgressIndicatorRoot.setVisibility(View.GONE);
                                binding.mainRoot.setVisibility(View.VISIBLE);
                            }
                        }, 2000);
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    //Actions Menu methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_iamge) {
            showAddImageDialog();
            return true;
        }
        return false;
    }

    private void showAddImageDialog() {
        new AddImageDialog()
                .show(this, new AddImageDialog.OnCompleteListener() {
                    @Override
                    public void onImageAdded(Item item) {
                        inflateViewForItem(item);
                    }

                    @Override
                    public void onError(String error) {
                        new MaterialAlertDialogBuilder(GalleryActivity.this)
                                .setTitle("Error")
                                .setMessage(error)
                                .show();
                    }
                });
    }

    private void inflateViewForItem(Item item) {
        //Inflate Layout
        ItemCardBinding binding = ItemCardBinding.inflate(getLayoutInflater());

        //Bind Data
        binding.imageView.setImageBitmap(item.image);
        binding.title.setText(item.label);
        binding.title.setBackgroundColor(item.color);

        //Add it to the list
        b.list.addView(binding.getRoot());
    }

    private void refresh() {
        /*This method failed coz Glide apni cache maintain kr hi rha hai hr activity k instance pr */

//        b.loader.setVisibility(View.VISIBLE);
//        b.subtitle.setText(R.string.loading_image);
//        b.imageView.setImageDrawable(null);
//
//        loadImage();

        /*Solution---> Restart the activity so that no cache is there*/

        finish();
        startActivity(new Intent(this, GalleryActivity.class));
    }

//    private void loadImage() {
//        Glide.with(this)
//                .asBitmap()
//                .load("https://picsum.photos/1920/1080")
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)       //Coz Glide memory m b store krk rkta h so we need to skip it
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        b.loader.setVisibility(View.GONE);
//                        b.subtitle.setText(getString(R.string.image_failed,e.toString()));
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        b.loader.setVisibility(View.GONE);
//                        b.subtitle.setText(R.string.image_loaded);
//                        b.imageView.setImageBitmap(resource);
//
////                        labelImage(resource);
//                        createPaletteAsync(resource);
//                        return true;
//                    }
//                })
//                .into(b.imageView);
//    }

    private void labelImage(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        // Task completed successfully
                        new MaterialAlertDialogBuilder(GalleryActivity.this)
                                .setTitle("Labels fetched")
                                .setMessage(labels.toString())
                                .show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        new MaterialAlertDialogBuilder(GalleryActivity.this)
                                .setTitle("Error")
                                .setMessage(e.toString())
                                .show();
                    }
                });
    }


    // Generate palette asynchronously and use it on a different
// thread using onGenerated()
    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                // Use generated instance
                new MaterialAlertDialogBuilder(GalleryActivity.this)
                        .setTitle("Palette fetched")
                        .setMessage(p.getSwatches().toString())
                        .show();
            }
        });
    }


    private void showCustomThemeDialog() {
        new MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme)
                .setTitle("Congratulations!")
                .setCancelable(false)
                .setMessage("You won a lottery of Rs. 1 Crore.")
                .show();
    }

//    private void showCustomViewDialog() {
//        DialogAddImageBinding binding = DialogAddImageBinding.inflate(getLayoutInflater());
//
//        new MaterialAlertDialogBuilder(this)
//                .setView(binding.getRoot());
//                .show();
//
//        //Use binding reference to bind data, set event handlers
//    }

    private void showSimpleDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Congratulations!")
                .setCancelable(false)
                .setMessage("You won a lottery of Rs. 1 Crore.")
                .setPositiveButton("REDEEM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(GalleryActivity.this, "You redeemed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("DISAGREE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(GalleryActivity.this, "You disagreed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("PROVE IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(GalleryActivity.this, "You asked for a proof!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}